package com.kob.util;

import cn.dev33.satoken.secure.SaBase64Util;
import cn.dev33.satoken.secure.SaSecureUtil;

import java.util.Map;

/**
 * @Author peelsannaw
 * @create 06/01/2023 15:41
 */
public class RSAUtil {

   private static Map getPair() throws Exception {
       return SaSecureUtil.rsaGenerateKeyPair();
   }

   public static String encode(String password) throws Exception {
       Map pair = getPair();
       String aPrivate = pair.get("private").toString();
       String aPublic = pair.get("public").toString();
       String after = SaSecureUtil.rsaEncryptByPublic(aPublic, password);
       System.out.println(after);
       System.out.println(SaSecureUtil.rsaDecryptByPublic(aPublic,after));
       return after;
   }

    public static void main(String[] args) {
        String encode = SaBase64Util.encode("123" + "abc");
        SaBase64Util.decode(encode);
    }


}
