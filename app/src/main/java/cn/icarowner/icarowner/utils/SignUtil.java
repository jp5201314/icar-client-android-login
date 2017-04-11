package cn.icarowner.icarowner.utils;


import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.List;

import cn.icarowner.icarowner.net.Pair;

public class SignUtil {

    public static String signWithMd5(String signKey, List<Pair> params) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getFirst());
            sb.append('=');
            sb.append(params.get(i).getSecond());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(signKey);
        return MD5.getMessageDigest(sb.toString().getBytes());
    }

    public static String signWithSHA512(String signKey, List<Pair> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("key=");
        sb.append(signKey);
        sb.append("&");
        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getFirst());
            sb.append('=');
            sb.append(params.get(i).getSecond());
            if (i < params.size() - 1) {
                sb.append('&');
            }
        }

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(sb.toString().getBytes());
            StringBuffer buf = new StringBuffer();
            byte[] bits = md.digest();
            for (int i = 0; i < bits.length; i++) {
                int a = bits[i];
                if (a < 0)
                    a += 256;
                if (a < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(a));
            }
            return buf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    private static final String ALGORITHM = "RSA";


    private static final String SIGN_ALGORITHMS = "SHA1WithRSA";


    private static final String DEFAULT_CHARSET = "UTF-8";


    public static String sign(String content, String privateKey) {

        try {

            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(

                    Base64.decode(privateKey));

            KeyFactory keyf = KeyFactory.getInstance(ALGORITHM);

            PrivateKey priKey = keyf.generatePrivate(priPKCS8);


            java.security.Signature signature = java.security.Signature

                    .getInstance(SIGN_ALGORITHMS);


            signature.initSign(priKey);

            signature.update(content.getBytes(DEFAULT_CHARSET));


            byte[] signed = signature.sign();


            return Base64.encode(signed);

        } catch (Exception e) {

            e.printStackTrace();

        }


        return null;

    }
}
