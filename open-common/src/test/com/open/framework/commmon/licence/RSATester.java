package com.open.framework.commmon.licence;

public class RSATester {

    static String publicKey;
    static String privateKey;

    static {
        try {
            RSAUtils.genKeyPair();
            publicKey = RSAUtils.getPublicKey();
            privateKey = RSAUtils.getPrivateKey();
            System.err.println("公钥: \n\r" + publicKey);
            System.err.println("私钥： \n\r" + privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws Exception {
        test1();
    }
    static void test() throws Exception {
        String source = "hello啊啊啊";
        System.out.println("\r加密前文字：\r\n" + source);
        byte[] data = source.getBytes();
        byte[] encodedData = RSAUtils.encryptByPublicKey(data);
        System.out.println("加密后文字：\r\n" + new String(encodedData));
        byte[] decodedData = RSAUtils.decryptByPrivateKey(encodedData);
        String target = new String(decodedData);
        System.out.println("解密后文字: \r\n" + target);
    }
    
    static void test1() throws Exception {
        String source = "hello啊啊啊111";
        RSAUtils.createUserKey(source);
        byte[] userKey =RSAUtils.getUserKey();
        byte[] decodedData = RSAUtils.decryptByPrivateKey(userKey);
        String target = new String(decodedData);
        System.out.println("解密后文字: \r\n" + target);
    }
    
}