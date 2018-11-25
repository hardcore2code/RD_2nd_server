package com.ext.kit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName: CreateHtmlUtils  
 * @Description: Java 根据模板创建 html
 * @author 
 * @date 2016年4月22日 下午3:51:16
 */
public class CreateHtmlUtils {
    
    public static void main(String[] args) {
        String filePath = "E:\\hh_web_space\\ecp\\web\\ecp_web_page\\src\\main\\webapp\\template\\template.html";
        String imagePath ="http://localhost:8080/ecp/upload/1461293787628/1461293787628.jpg";
        String disrPath = "E:\\hh_web_space\\ecp\\web\\ecp_web_page\\src\\main\\webapp\\template\\";
        String fileName = "liuren";
//        MakeHtml(filePath,imagePath,disrPath,fileName);
    }
    /**
     * @Title: MakeHtml 
     * @Description: 创建html
     * @param    filePath 设定模板文件
     * @param    param 生成时的参数
     * @param    disrPath  生成html的存放路径
     * @param    fileName  生成html名字 
     * @return void    返回类型 
     * @throws
     */
    public static void MakeHtml(String filePath,Map param,String disrPath,String fileName ){
        try {
            System.out.print(filePath);
            String templateContent = "";
            FileInputStream fileinputstream = new FileInputStream(filePath);// 读取模板文件
            int lenght = fileinputstream.available();
            byte bytes[] = new byte[lenght];
            fileinputstream.read(bytes);
            fileinputstream.close();
            templateContent = new String(bytes,"GBK");
            //System.out.print(templateContent);
            Set keySet = param.keySet();
            for (Object object : keySet) {
            	templateContent = templateContent.replaceAll("###"+object+"###", param.get(object.toString()).toString());
			}
            //System.out.print(templateContent);
            
            String fileame = fileName + ".html";
            fileame = disrPath+"/" + fileame;// 生成的html文件保存路径。
            File dir = new File(disrPath);
            if (!dir.exists()) {
            	dir.mkdirs();
            }
            FileOutputStream fileoutputstream = new FileOutputStream(fileame);// 建立文件输出流
            //System.out.print("文件输出路径:");
            //System.out.print(fileame);
            byte tag_bytes[] = templateContent.getBytes();
            fileoutputstream.write(tag_bytes);
            fileoutputstream.close();
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }
    
    
}