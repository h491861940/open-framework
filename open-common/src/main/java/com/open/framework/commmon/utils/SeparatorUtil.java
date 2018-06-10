package com.open.framework.commmon.utils;

/**
 * The type Separator utils.
 * 分隔符工具类，在拼接一个 本地路径时<br/>
 * 如： String path = "/nfs" + "/" + "user" + "/img.png"
 * 经常要注意 "/" 的左右字符，工具类是帮你自动分析分隔符
 *
 */
public class SeparatorUtil {

    /**
     * 组装一个路径地址，解析分隔符
     * <p>
     * <pre>
     * SeparatorUtil.build(c:/nfs/mg, user/2014/07/09) = c:/nfs/mg/user/2014/07/09
     * SeparatorUtil.build(c:/nfs/mg, /user/2014/07/09) = c:/nfs/mg/user/2014/07/09
     * SeparatorUtil.build(/nfs/mg, /user/2014/07/09) = /nfs/mg/user/2014/07/09
     * SeparatorUtil.build(/nfs/mg/, user/2014/07/09/) = /nfs/mg/user/2014/07/09/
     * </pre>
     *
     * @param dirs
     * @return
     */
    public static String build(String... dirs) {
        StringBuilder sb = new StringBuilder();

        if (dirs != null && dirs.length > 0) {
            for (int i = 0; i < dirs.length; i++) {
                String tmp = dirs[i];
                if (tmp.startsWith("/") && i != 0) {
                    tmp = tmp.substring(1);
                }
                if (i == dirs.length - 1) {
                    sb.append(tmp);
                    continue;
                }
                if (tmp.endsWith("/")) {
                    sb.append(tmp);
                } else {
                    sb.append(tmp).append("/");
                }
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(build("/nfs/mg", "2014/07/09"));
    }

}
