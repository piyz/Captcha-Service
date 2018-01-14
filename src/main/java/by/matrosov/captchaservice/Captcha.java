package by.matrosov.captchaservice;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class Captcha {
    public static String generateCaptcha1(){
        Random random = new Random();
        return Integer.toHexString(random.nextInt());
    }

    public static String generateCaptcha2(int value){
        String s = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuffer sb = new StringBuffer();
        while (sb.length() < value){
            int index = (int) (Math.random() * s.length());
            sb.append(s.substring(index, index + 1));
        }
        return sb.toString();
    }

    public static String generateCaptcha3(int value){
        Random random = new Random();
        return (Long.toString(Math.abs(random.nextLong()), 36)).substring(0, value);
    }

    public static String generateCaptcha4(){
        Random random = new Random();
        int length = 7 + (Math.abs(random.nextInt()) % 3);

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int baseCharNumber = Math.abs(random.nextInt()) % 62;
            int charNumber = 0;
            if (baseCharNumber < 26) {
                charNumber = 65 + baseCharNumber;
            }
            else if (baseCharNumber < 52){
                charNumber = 97 + (baseCharNumber - 26);
            }
            else {
                charNumber = 48 + (baseCharNumber - 52);
            }
            sb.append((char)charNumber);
        }

        return sb.toString();
    }

    public static String generateCaptcha5(){
        SecureRandom random = new SecureRandom();

        // randomly generated BigInteger
        BigInteger bigInteger = new BigInteger(35, random);

        // String representation of this BigInteger in the given radix.
        return bigInteger.toString(32);

    }
}
