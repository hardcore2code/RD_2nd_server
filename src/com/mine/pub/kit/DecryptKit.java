package com.mine.pub.kit;

import java.io.IOException;


public class DecryptKit {
	//ASCII转换为16进制
	public static String convertStringToHex(String str) {  
        char[] chars = str.toCharArray();  
  
        StringBuffer hex = new StringBuffer();  
        for (int i = 0; i < chars.length; i++) {  
            hex.append(Integer.toHexString((int) chars[i]));  
        }  
        return hex.toString();  
    }  
	
	 //16进制转换为ASCII
    public static String convertHexToString(String hex) {  
  
        StringBuilder sb = new StringBuilder();  
        StringBuilder temp = new StringBuilder();  
  
        // 49204c6f7665204a617661 split into two characters 49, 20, 4c...  
        for (int i = 0; i < hex.length() - 1; i += 2) {  
  
            // grab the hex in pairs  
            String output = hex.substring(i, (i + 2));  
            // convert hex to decimal  
            int decimal = Integer.parseInt(output, 16);  
            // convert the decimal to character  
            sb.append((char) decimal);  
  
            temp.append(decimal);  
        }  
  
        return sb.toString();  
    }  
	
    //base64解码成明文
	public static String decode(String str) {    
	   byte[] bt = null;  
	   String res = "";
	   try {    
	       sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();    
	       bt = decoder.decodeBuffer( str ); 
	       res =  new String(bt, "GB2312");
	   } catch (IOException e) {    
	       e.printStackTrace();    
	   }    
       return res;
   } 
	
	//base64解码成明文
	public static String encode(String str) {    
		String res = "";
		try {    
			sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
			res= encoder.encode(str.getBytes());
		} catch (Exception e) {    
			e.printStackTrace();    
		}    
		return res;
	} 
	
	
	public static void main(String args[])
	{
		
        String hex = "4D444178";
//        hex=encode(hex);
//        System.out.println("base64enCode====="+hex);
//        hex=convertHexToString(hex);
        System.out.println("转为ASCII====="+hex);
        String base64 = convertHexToString(hex);
        System.out.println("11====>>"+base64);
        System.out.println("明文====>>"+decode(base64));
		
	}
	
}
