package com.mine.rd.controllers.syncUpload;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import sun.misc.BASE64Decoder;


public class EncriptUtil {
	

	
	 static {
	        Security.addProvider(new BouncyCastleProvider());
	     }
	
	 /**
	  * 去读配置文件获得相应的行政区划代码
	  * @param key
	  * @return
	  * @throws IOException
	  */
	 public static String getXzqydm(String key) throws IOException{
		String qyxzdm = "";
		try {
			Properties props = new Properties();
			InputStream in = EncriptUtil.class
					.getResourceAsStream("epa_encript.properties");
			props.load(in);
			qyxzdm = props.getProperty(key);
		} catch (IOException e) {
			throw new IOException("55");
		}
		return qyxzdm;
	 }
	 
	 public static String encrypt(String encData, String key) throws Exception {
        try {
        	String data = encData;
            String iv = "1234567812345678";
 
            Cipher cipher = Cipher.getInstance("AES/CBC/ZeroBytePadding", "BC");
            byte[] dataBytes = data.getBytes("UTF-8");
 
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes("UTF-8"));
 
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(dataBytes);
 
            return new sun.misc.BASE64Encoder().encode(encrypted);
 
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	 
	 public static String desEncrypt(String desData, String key) throws Exception {
        try{
        	String data = desData;
            String iv = "1234567812345678";
 
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(data);
 
            Cipher cipher = Cipher.getInstance("AES/CBC/ZeroBytePadding", "BC");
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes("UTF-8"));
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original,"UTF-8");
            return originalString;
        } catch (Exception e) {
           throw e;
        }
    }
	 
	public static void main(String[] args) throws Exception {
		String data = "{\"qyid\":\"\",\"lsbh\":\"64585385427000196383\",\"qybh\":\"58538542701\",\"zzjgdm\":\"585385427\",\"dwmc\":\"宁夏锦河能源科技有限公司\",\"dwszqx\":\"640601\",\"dwdz\":\"宁夏银川市宁东能源化工基地新材料园区\",\"jd\":\"104-17-12\",\"wd\":\"35-14-57\",\"yzbm\":\"750411\",\"hyfl1\":\"N\",\"hyfl2\":\"N80\",\"hyfl3\":\"N802\",\"hyfl4\":\"N8024\",\"fddbr\":\"李夏富\",\"fddbrdh\":\"0591\",\"lxr\":\"胡志伟\",\"lxrsj\":\"13099585164\",\"gxsj\":\"2018-10-31 05:53:01\",\"sfwxcsy\":\"1\",\"sfybcsy\":\"0\",\"sfylcsy\":\"0\",\"sfwfjy\":\"1\",\"sfhmwfjy\":\"0\",\"hmjyzl\":\"\",\"store\":[{\"ssmc\":\"危废暂存库\",\"sssl\":\"1\",\"zcssmj\":\"0\",\"zdzcnl\":\"80.000000\"}],\"cerfacility\":[{\"ssmc\":\"5万吨/年废矿物油加氢装置III\",\"lyczfs\":\"R9\",\"hzgm\":\"50000\",\"tmcrl\":\"0\",\"ytmrl\":\"0\"},{\"ssmc\":\"1万吨/年塔底油处理装置\",\"lyczfs\":\"R9\",\"hzgm\":\"6040\",\"tmcrl\":\"0\",\"ytmrl\":\"0\"},{\"ssmc\":\"含油包装物处理装置\",\"lyczfs\":\"R4\",\"hzgm\":\"18500\",\"tmcrl\":\"0\",\"ytmrl\":\"0\"},{\"ssmc\":\"原料废油贮存罐体\",\"lyczfs\":\"S\",\"hzgm\":\"50000\",\"tmcrl\":\"0\",\"ytmrl\":\"0\"},{\"ssmc\":\"废乳化液贮存罐\",\"lyczfs\":\"S\",\"hzgm\":\"6040\",\"tmcrl\":\"0\",\"ytmrl\":\"0\"},{\"ssmc\":\"含油包装物贮存库\",\"lyczfs\":\"S\",\"hzgm\":\"18500\",\"tmcrl\":\"0\",\"ytmrl\":\"0\"}]}";
		String jmData = encrypt(data,"5a792afc833d5716");
		System.out.println(jmData);
		System.out.println(desEncrypt(jmData,"5a792afc833d5716"));
	}
	
	/**
	 * 将行政区划代码加密成16位字符串
	 * @param xzqhdm
	 * @return
	 */
	public static List<String> encXzqhdm(List<String> xzqhdm){
		
		/*Set<String> set = new HashSet<String>();
		for (String string : xzqhdm) {
			set.add(encrypt16(string) + "=" + string);
		}
		List<String> list = new ArrayList<String>(set);*/
		
		List<String> list = new ArrayList<String>();
		for (String string : xzqhdm) {
			list.add(string + "=" + encrypt16(string));
		}
		
		return list;
	}
	
	/** 
     * @Description:加密-32位小写 
     */  
    public static String encrypt32(String encryptStr) {  
        MessageDigest md5;  
        try {  
            md5 = MessageDigest.getInstance("MD5");  
            byte[] md5Bytes = md5.digest(encryptStr.getBytes());  
            StringBuffer hexValue = new StringBuffer();  
            for (int i = 0; i < md5Bytes.length; i++) {  
                int val = ((int) md5Bytes[i]) & 0xff;  
                if (val < 16)  
                    hexValue.append("0");  
                hexValue.append(Integer.toHexString(val));  
            }  
            encryptStr = hexValue.toString();  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
        return encryptStr;  
    }  
  
    /** 
     * @Description:加密-16位小写 
     */  
    public static String encrypt16(String encryptStr) {  
        return encrypt32(encryptStr).substring(8, 24); 
    }
}
