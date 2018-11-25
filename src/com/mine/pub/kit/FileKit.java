package com.mine.pub.kit;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.jfinal.kit.PathKit;

import sun.misc.BASE64Decoder;

/**
 * FileKit.
 */
public class FileKit {
	public static void delete(File file) {
		if (file != null && file.exists()) {
			if (file.isFile()) {
				file.delete();
			}
			else if (file.isDirectory()) {
				File files[] = file.listFiles();
				for (int i=0; i<files.length; i++) {
					delete(files[i]);
				}
			}
			file.delete();
		}
	}
	/**
	 * @author woody
	 * @date 2015-3-14
	 * @根据路径删除文件及文件夹
	 * */
	public static boolean delete(String filePath)
	{
		boolean flag = false;
		File file = new File(filePath);
		if(!file.exists())
		{
			return flag;
		}
		else
		{
			FileKit.delete(file);
			flag = true;
		}
		return flag;
	}
	/**
	 * @author woody
	 * @date 2015-4-10
	 * @根据base64的字符串转成图片
	 * @返回存放图片的路径
	 * */
	public static String imgFromBase64(String imgBase64,String userId,String timestamp,String imgType)
	{
		//对字节数组字符串进行Base64解码并生成图片  
        if (imgBase64 == null || "".equals(imgBase64)) //图像数据为空  
        {
        	return "-1";
        }  
        BASE64Decoder decoder = new BASE64Decoder(); 
        String imgFilePath = "";
        String relativeImgPath = "";
        try   
        {  
            //Base64解码  
            byte[] b = decoder.decodeBuffer(imgBase64);  
            for(int i=0;i<b.length;++i)  
            {  
                if(b[i]<0)  
                {
                	//调整异常数据  
                    b[i]+=256;  
                }  
            }  
            relativeImgPath = "/upload/pics/"+userId+"/"+timestamp+"."+imgType;//图片的相对路径
            imgFilePath = PathKit.getWebRootPath()+relativeImgPath;//新生成的图片路径及名称
            File imgFile = new File(imgFilePath);
            File file = new File(imgFilePath.substring(0, imgFilePath.length() - (timestamp.length()+5)));
            if(!file.exists()  && !file.isDirectory()) 
            {
            	file.mkdirs(); 
            }else
            {
            	if(imgFile.exists())
            	{
            		relativeImgPath = relativeImgPath.replace("."+imgType, "1."+imgType);
            		imgFilePath = PathKit.getWebRootPath()+relativeImgPath;
            	}
            }
            //生成jpeg图片  
            OutputStream out = new FileOutputStream(imgFilePath);      
            out.write(b);  
            out.flush();  
            out.close();  
        }   
        catch (Exception e)   
        {  
            e.printStackTrace();
        }
        return relativeImgPath;
	}
	/**
	 * @author woody
	 * @date 20150918
	 * @移动文件
	 * */
	public static boolean move(String srcPath , String dirPath ,String fileName)
	{
		File srcFile = new File(srcPath);
		File dirFile = new File(dirPath);
		boolean flag = srcFile.renameTo(new File(dirFile,fileName));
		return flag;
	}
	/**
	 * @author woody
	 * @date 20150918
	 * @拷贝文件
	 * */
	@SuppressWarnings("resource")
	public static void copy(String oldPath,String newPath)
	{
		try{   
            int bytesum = 0;   
            int byteread = 0;   
            File oldfile = new File(oldPath);   
            File newfile = new File(newPath);  
            if(!newfile.getParentFile().exists()) {  
                //如果目标文件所在的目录不存在，则创建父目录  
                System.out.println("目标文件所在目录不存在，准备创建它！");  
                if(!newfile.getParentFile().mkdirs()) {  
                    System.out.println("创建目标文件所在目录失败！");  
                }  
            }  
            if(oldfile.exists()){
                InputStream inStream = new FileInputStream(oldPath);    
                FileOutputStream fs = new FileOutputStream(newPath);   
                byte[] buffer = new byte[1444];   
                while((byteread = inStream.read(buffer)) != -1){   
                    bytesum += byteread;       
                    System.out.println(bytesum);   
                    fs.write(buffer,0,byteread);   
                }   
                inStream.close();   
            }   
	    }   
	    catch(Exception e){   
	        System.out.println("file copy error");   
	        e.printStackTrace();   
	    }    
	}
	/**
	 * @author woody
	 * @date 20150925
	 * @新建文件
	 * */
	public static File createFile(String path,String filename,String content)
	{
		File myFile = null;
		OutputStreamWriter out = null;
		try{
			String fileName = path + filename;
			//创建File对象，参数为String类型，表示目录名
			myFile = new File(fileName);
			//判断文件是否存在，如果不存在则调用createNewFile()方法创建新目录，否则跳至异常处理代码
			if(!myFile.exists())
			{
				myFile.createNewFile();
				//下面把数据写入创建的文件，首先新建文件名为参数创建FileWriter对象
				out = new OutputStreamWriter(new FileOutputStream(myFile),"UTF-8");  
                out.write(content.toCharArray());  
                out.flush();  
                out.close();  
//				FileWriter resultFile = new FileWriter(myFile);
////				//把该对象包装进PrinterWriter对象
//				PrintWriter myNewFile = new PrintWriter(resultFile);
////				再通过PrinterWriter对象的println()方法把字符串数据写入新建文件
//				myNewFile.println(content);
//				resultFile.close();   //关闭文件写入流
			}
		}catch(Exception e)
		{
			System.err.println("create file error!");
			e.printStackTrace();
		}finally{
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return myFile;
	}
	/**
	 * @author woody
	 * @date 20150927
	 * @读取文件内容
	 * */
	@SuppressWarnings("resource")
	public static StringBuffer readFile(String filename)
	{
		StringBuffer sb = null;
		BufferedReader br = null;
		File file=new File(filename);
        try {
        	 if(!file.exists()||file.isDirectory())
                 throw new FileNotFoundException();
        	 br =new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
             String temp=null;
             sb=new StringBuffer();
             temp=br.readLine();
			 while(temp!=null)
			 {
	            sb.append(temp+" ");
	            temp=br.readLine();
			 }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return sb;
	}
	/**
	 * @author woody
	 * @date 20150927
	 * @读取文件夹下文件名称
	 * */
	 public static String[] getFileNames(String filepath)
	 {
		 String[] filelist = null;
		 File file = new File(filepath);
		 if (!file.isDirectory())
		 {
			 filelist = new String[1];
		     System.out.println("文件");
		     System.out.println("path=" + file.getPath());
		     System.out.println("absolutepath=" + file.getAbsolutePath());
		     System.out.println("name=" + file.getName());
		     filelist[0] = file.getName();
		 } else if (file.isDirectory()) 
		 {
		     System.out.println("文件夹");
		     filelist = file.list();
//		     for (int i = 0; i < filelist.length; i++)
//		     {
//		         File readfile = new File(filepath + "\\" + filelist[i]);
//		         if(!readfile.isDirectory())
//		         {
//		             System.out.println("path=" + readfile.getPath());
//		             System.out.println("absolutepath=" + readfile.getAbsolutePath());
//		             System.out.println("name=" + readfile.getName());
//		         } 
//		     }
		 }
		 return filelist;
	 }
}
