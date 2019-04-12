package com.open.framework.commmon.utils;

import org.apache.commons.io.IOUtils;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpClientUtil implements Serializable {

    public static RestTemplate template;

    static {
        SimpleClientHttpRequestFactory httpRequestFactory = new SimpleClientHttpRequestFactory();
        httpRequestFactory.setReadTimeout(15000);
        httpRequestFactory.setConnectTimeout(5000);
        template = new RestTemplate(httpRequestFactory);
    }

    /**
     * @param url       访问地址
     * @param body      提交参数体
     * @param clazz     返回值类型(自动转换)
     * @param useToken  是否使用token
     * @param retry     是否重新申请token
     * @param isEncrypt 是否加密参数体/是否解密返回体
     * @return <T>类型的实体类
     * @throws Exception
     * @Description POST调用接口
     */
    public static <T> T post(String url, Object body, Class<T> clazz)
            throws Exception {

        try {
            Map<String, String> headers = new HashMap<String, String>();
            // 转成json格式
            if (body != null && !(body instanceof String)) {
                body = JsonUtil.toJSONString(body);
            }

            // 得到返回值
            String responseBody = post(new URL(url), headers, ((body == null) ? null : body.toString()));
            T t = JsonUtil.strToBean(responseBody, clazz);
            return t;
        } catch (Exception e) {
            if (e.getMessage().contains("connect")) {
                throw new Exception("请求超时");
            } else {
                throw new Exception(e);
            }
        }
    }

    /* ------------------------- 以下内容使用者不用关注 ----------------------- */

    public static String get(URL url, Map<String, String> headers)
            throws Exception {

        HttpURLConnection httpUrlConn = null;
        InputStream inputStream = null;
        try {
            httpUrlConn = (HttpURLConnection) url.openConnection();
            httpUrlConn.setUseCaches(false);
            httpUrlConn.setRequestMethod("GET");

            for (Entry<String, String> entry : headers.entrySet()) {
                httpUrlConn.setRequestProperty((String) entry.getKey(),
                        (String) entry.getValue());
            }

            inputStream = httpUrlConn.getInputStream();
            String reqmsg = IOUtils.toString(inputStream, "UTF-8");
            return reqmsg;
        } finally {
            if (inputStream != null) {
                inputStream.close();
                inputStream = null;
            }

            if (httpUrlConn != null)
                httpUrlConn.disconnect();
        }
    }

    public static String post(URL url, Map<String, String> headers, String body)
            throws Exception {

        HttpURLConnection httpUrlConn = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            httpUrlConn = (HttpURLConnection) url.openConnection();
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            httpUrlConn.setRequestMethod("POST");

            for (Entry<String, String> entry : headers.entrySet()) {
                httpUrlConn.setRequestProperty((String) entry.getKey(),
                        (String) entry.getValue());
            }

            if (body != null) {
                byte[] b = body.getBytes("UTF-8");
                httpUrlConn.setRequestProperty("Charset", "UTF-8");
                httpUrlConn.setRequestProperty("Content-Type", "application/json");
                httpUrlConn.setRequestProperty("Content-length", String.valueOf(b.length));

                outputStream = httpUrlConn.getOutputStream();
                outputStream.write(b);
                outputStream.close();
            }

            inputStream = httpUrlConn.getInputStream();
            String reqmsg = IOUtils.toString(inputStream, "UTF-8");
            return reqmsg;
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }

            if (inputStream != null) {
                inputStream.close();
            }

            if (httpUrlConn != null)
                httpUrlConn.disconnect();
        }
    }

    /**
     * 上传文件
     *
     * @param url      文件路径
     * @param paramMap
     * @param fileMap  文件信息
     * @return
     * @throws Exception
     */
    public static String upload(String url, Map<String, String> paramMap, Map<String, String> fileMap) throws
			Exception {
        long stime = System.currentTimeMillis();
        String res = "";
        HttpURLConnection conn = null;
        String BOUNDARY = "---------------------------123821742118716"; // boundary就是request头和上传文件内容的分隔符
        try {
            URL uri = new URL(url);
            conn = (HttpURLConnection) uri.openConnection();
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

            OutputStream out = new DataOutputStream(conn.getOutputStream());

            // text
            if (paramMap != null) {
                StringBuffer strBuf = new StringBuffer();
                Iterator<Entry<String, String>> iter = paramMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Entry<String, String> entry = (Entry<String, String>) iter.next();
                    String inputName = (String) entry.getKey();
                    String inputValue = (String) entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
                    strBuf.append(inputValue);
                }
                out.write(strBuf.toString().getBytes());
            }

            // file
            if (fileMap != null) {
                Iterator<Entry<String, String>> iter = fileMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Entry<String, String> entry = (Entry<String, String>) iter.next();
                    String inputName = (String) entry.getKey();
                    String inputValue = (String) entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    File file = new File(inputValue);
                    String filename = file.getName();
                    //String contentType = new MimetypesFileTypeMap().getContentType(file);
                    //if (filename.endsWith(".png")) {
                    String contentType = "image/png";
                    //}
                    //if (contentType == null || contentType.equals("")) {
                    //	contentType = "application/octet-stream";
                    //}
                    StringBuffer strBuf = new StringBuffer();
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + filename
                            + "\"\r\n");
                    strBuf.append("Content-Type:" + contentType + "\r\n\r\n");

                    out.write(strBuf.toString().getBytes());

                    DataInputStream in = new DataInputStream(new FileInputStream(file));
                    int bytes = 0;
                    byte[] bufferOut = new byte[1024];
                    while ((bytes = in.read(bufferOut)) != -1) {
                        out.write(bufferOut, 0, bytes);
                    }
                    in.close();
                }
            }

            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            out.write(endData);
            out.flush();
            out.close();

            // 读取返回数据
            StringBuffer strBuf = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                strBuf.append(line).append("\n");
            }
            res = strBuf.toString();
            reader.close();
            reader = null;
            long etime = System.currentTimeMillis();
        } catch (Exception e) {
            if (e.getMessage().contains("connect")) {
                throw new Exception("上传文件失败");
            } else {
                throw new Exception(e);
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        return res;
    }

    /**
     * 下载文件
     *
     * @param url  下载的文件路径
     * @param path 保存文件的地址
     * @return
     * @throws Exception
     */
    public static boolean download(String url, String path) throws Exception {
        long stime = System.currentTimeMillis();
        HttpURLConnection conn = null;
        InputStream inStream = null;
        FileOutputStream outStream = null;
        try {
            URL uri = new URL(url);
            conn = (HttpURLConnection) uri.openConnection();
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("GET");

            if (conn.getInputStream() != null) {
                File file = new File(path);
                file.createNewFile();

                outStream = new FileOutputStream(file);
                inStream = conn.getInputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = inStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }
                outStream.flush();
                long etime = System.currentTimeMillis();
                return true;
            }
        } catch (Exception e) {
            if (e.getMessage().contains("connect")) {
                throw new Exception("请求超时");
            } else {
                throw new Exception(e);
            }
        } finally {
            if (inStream != null)
                try {
                    inStream.close();
                } catch (IOException localIOException2) {
                }
            if (outStream != null)
                try {
                    outStream.close();
                } catch (IOException localIOException3) {
                }
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        return false;
    }

    /**
     * 上传文件（二进制）
     *
     * @param url      文件路径
     * @param filename 文件名(含后缀)
     * @param fileMap  文件信息
     * @return
     * @throws Exception
     */
    public static String fileUpload(String url, String filename, byte[] fileByte) throws Exception {
        long stime = System.currentTimeMillis();
        String res = "";
        HttpURLConnection conn = null;
        String BOUNDARY = "---------------------------123821742118716"; // boundary就是request头和上传文件内容的分隔符
        try {
            URL uri = new URL(url);
            conn = (HttpURLConnection) uri.openConnection();
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            conn.setRequestProperty("Content-Length", String.valueOf(fileByte.length));

            OutputStream out = new DataOutputStream(conn.getOutputStream());

            // file
            StringBuffer strBuf1 = new StringBuffer();
            strBuf1.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
            strBuf1.append("Content-Disposition: form-data; name=\"" + filename.substring(0, filename.lastIndexOf("" +
					".")) + "\"; filename=\"" + filename
                    + "\"\r\n");
            strBuf1.append("Content-Type:\r\n\r\n");
            out.write(strBuf1.toString().getBytes());
            out.write(fileByte);

            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            out.write(endData);
            out.flush();
            out.close();

            // 读取返回数据
            StringBuffer strBuf = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                strBuf.append(line).append("\n");
            }
            res = strBuf.toString();
            reader.close();
            reader = null;
            long etime = System.currentTimeMillis();
        } catch (Exception e) {
            if (e.getMessage().contains("connect")) {
                throw new Exception("请求超时");
            } else {
                throw new Exception(e);
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        return res;
    }

    /**
     * 下载文件
     *
     * @param url 下载的文件路径
     * @return
     * @throws Exception
     */
    public static byte[] fileDownload(String url) throws Exception {
        long stime = System.currentTimeMillis();
        HttpURLConnection conn = null;
        InputStream inStream = null;
        FileOutputStream outStream = null;
        byte[] buffer = null;
        try {
            URL uri = new URL(url);
            conn = (HttpURLConnection) uri.openConnection();
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("GET");

            if (conn.getInputStream() != null) {
                inStream = conn.getInputStream();
                buffer = inputStreamTOByte(inStream);

                long etime = System.currentTimeMillis();
                return buffer;
            }
        } catch (Exception e) {
            if (e.getMessage().contains("connect")) {
                throw new Exception("请求超时");
            } else {
                throw new Exception(e);
            }
        } finally {
            if (inStream != null)
                try {
                    inStream.close();
                } catch (IOException localIOException2) {
                }
            if (outStream != null)
                try {
                    outStream.close();
                } catch (IOException localIOException3) {
                }
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        return buffer;
    }

    /**
     * 获取文件名
     *
     * @param Contentdisposition
     * @return
     */
    public static String getFilename(String Contentdisposition) {
        Pattern p = Pattern.compile("filename=\"(.*)\"");
        Matcher m = p.matcher(Contentdisposition);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }

    /**
     * 将InputStream转换成byte数组
     *
     * @param in InputStream
     * @return byte[]
     * @throws IOException
     */
    public static byte[] inputStreamTOByte(InputStream in) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = in.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        return outStream.toByteArray();
    }

    /**
     * 将byte数组转换成InputStream
     *
     * @param in
     * @return
     * @throws Exception
     */
    public static InputStream byteTOInputStream(byte[] in) throws Exception {
        ByteArrayInputStream is = new ByteArrayInputStream(in);
        return is;
    }

    /**
     * @param response
     * @param postUrl  请求地址
     * @param paramMap
     * @return void 返回类型
     * @Description: 后台进行POST请求
     */
    public static void doBgPostReq(HttpServletResponse response, String postUrl, Map<String, ?> paramMap)
            throws IOException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.println("<form name='postSubmit' method='post' action='" + postUrl + "' >");
        if (paramMap != null && !paramMap.isEmpty()) {
            for (String key : paramMap.keySet()) {
                out.println("<input type='hidden' name='" + key + "' value='" + paramMap.get(key) + "'>");
            }
        }
        out.println("</form>");
        out.println("<script>");
        out.println("  document.postSubmit.submit()");
        out.println("</script>");
    }

    public static void main(String[] args) throws Exception {
/*		String url = "http://192.168.1.35:8080/FileManager/upload?method=upload";
		Map<String, String> fileMap = new HashMap<String, String>();
		fileMap.put("icon_main.png",  "C:/Users/Administrator/Desktop/icon_main.png");
		String response = HttpClientUtil.upload(url, null, fileMap);
		System.out.println("response=" + response);*/
		
		
		/*HttpClientUtil.download(
				"http://113.57.175.26:10080/group1/M00/00/02/wKiKZFthJ7uEfVahAAAAAAAAAAA206.jpg",
				"E:/222.png");*/
        //通过Jackson JSON processing library直接将返回值绑定到对象
        String quoteString = template.getForObject("http://gturnquist-quoters.cfapps.io/api/random", String.class);
        System.out.println(quoteString);
    }
}