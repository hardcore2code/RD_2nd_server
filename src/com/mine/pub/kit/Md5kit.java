package com.mine.pub.kit;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.misc.BASE64Encoder;

public class Md5kit {
	// 全局数组
    private final static String[] strDigits = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
    
    /**Determine encrypt algorithm MD5*/  
    private static final String ALGORITHM_MD5 = "MD5";  
    /**UTF-8 Encoding*/  
    private static final String UTF_8 = "UTF-8";  
	/**
	 * @author woody
	 * @date 20160706
	 * @md5 加密
	 * @如果返回-1 则表示转换错误
	 */
	public static String toMd5(String str , int type) {
		MessageDigest md;
		String md5 = "";
		try {
			md = MessageDigest.getInstance("MD5");
			md5 = byteToString(md.digest(str.getBytes()));
			if(type == 16)
			{
				md5 = md5.toString().substring(8, 24);
			}else if(type == 32){
				md5 = md5.toString();
			}
		} catch (NoSuchAlgorithmException e) {
			md5 = "-1";
			e.printStackTrace();
		}
		return md5;
	}
	// 转换字节数组为16进制字串
    private static String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }
    // 返回形式为数字跟字符串
    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        // System.out.println("iRet="+iRet);
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }
    /** 
     * MD5 16bit Encrypt Methods. 
     * @param readyEncryptStr ready encrypt string 
     * @return String encrypt result string 
     * @throws NoSuchAlgorithmException  
     * @throws UnsupportedEncodingException  
     * */  
    public static final String MD5_64bit(String readyEncryptStr) throws NoSuchAlgorithmException, UnsupportedEncodingException{  
        MessageDigest md = MessageDigest.getInstance(ALGORITHM_MD5);  
        BASE64Encoder base64Encoder = new BASE64Encoder();  
        return base64Encoder.encode(md.digest(readyEncryptStr.getBytes(UTF_8)));  
    }  
    public static void main(String[] args){
    	String md5 = toMd5("000000", 32);
    	System.out.println(md5);
    	String md5_64;
		try {
			md5_64 = MD5_64bit("000000");
			System.out.println(md5_64);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    	
    }
}
