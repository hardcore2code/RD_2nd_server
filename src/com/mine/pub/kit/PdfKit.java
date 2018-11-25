package com.mine.pub.kit;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jfinal.kit.PropKit;
/**
 * @author woody
 * @date 20160218
 * 转换成pdf
 * 注：只能在windows环境下使用
 * */
public class PdfKit
{
	private static final int wdFormatPDF = 17;
	private static final int xlTypePDF = 0;
	private static final int ppSaveAsPDF = 32;
//	private static final int msoTrue = -1;
//	private static final int msofalse = 0;
	//SWFTools的环境安装路径  
//    public static String SWFTOOLS_PATH="H:"+File.separator+"Program Files"+File.separator+"SWFTools"+File.separator;  
    public static String SWFTOOLS_PATH="";  
    //播放器样式文件rfxview.swf的路径  
//    public static String RFXVIEW_SWF_PATH="h:"+File.separator+"Program Files"+File.separator+"SWFTools"+File.separator+"rfxview.swf";  
    public static String RFXVIEW_SWF_PATH="";  
	//直接调用这个方法即可
    public static boolean convert2PDF(String inputFile, String pdfFile) {
        String suffix =  getFileSufix(inputFile);
        File file = new File(inputFile);
        if(!file.exists()){
            System.out.println("文件不存在！");
            return false;
        }
        if(suffix.equals("pdf")){
            System.out.println("PDF not need to convert!");
            return false;
        }
        if(suffix.equals("doc")||suffix.equals("docx")||suffix.equals("txt")){
            return word2PDF(inputFile,pdfFile);
        }else if(suffix.equals("ppt")||suffix.equals("pptx")){
            return ppt2PDF(inputFile,pdfFile);
        }else if(suffix.equals("xls")||suffix.equals("xlsx")){
            return excel2PDF(inputFile,pdfFile);
        }else{
            System.out.println("文件格式不支持转换!");
            return false;
        }
    }
    public static String getFileSufix(String fileName)
    {
        int splitIndex = fileName.lastIndexOf(".");
        return fileName.substring(splitIndex + 1);
    }
    public static boolean word2PDF(String inputFile,String pdfFile)
    {
    	ActiveXComponent app = null;
        try{
        	//打开word应用程序
	        app = new ActiveXComponent("Word.Application");
	        //设置word不可见
	        app.setProperty("Visible", false);
	        //获得word中所有打开的文档,返回Documents对象
	        Dispatch docs = app.getProperty("Documents").toDispatch();
	        //调用Documents对象中Open方法打开文档，并返回打开的文档对象Document
	        Dispatch doc = Dispatch.call(docs,
	                                    "Open",
	                                    inputFile,
	                                    false,
	                                    true
	                                    ).toDispatch();
	        //调用Document对象的SaveAs方法，将文档保存为pdf格式
	        /*
	        Dispatch.call(doc,
	                    "SaveAs",
	                    pdfFile,
	                    wdFormatPDF     //word保存为pdf格式宏，值为17
	                    );
	                    */
	        Dispatch.call(doc,
	                "ExportAsFixedFormat",
	                pdfFile,
	                wdFormatPDF     //word保存为pdf格式宏，值为17
	                );
	        //关闭文档
	        Dispatch.call(doc, "Close",false);
	        //关闭word应用程序
//	        app.invoke("Quit", 0);
	        return true;
	    }catch(Exception e){
//	    	app.invoke("Quit", 0);
	        return false;
	    }
        finally{
        	app.invoke("Quit", 0);
        }
    }
    public static boolean excel2PDF(String inputFile,String pdfFile)
    {
    	ActiveXComponent app = null;
        try{
        	System.out.println("<<===excel start=====================================>>");
            app = new ActiveXComponent("Excel.Application");
            System.out.println("<<===excel end=====================================>>");
	        app.setProperty("Visible", false);
	        Dispatch excels = app.getProperty("Workbooks").toDispatch();
	        Dispatch excel = Dispatch.call(excels,
	                                    "Open",
	                                    inputFile,
	                                    false,
	                                    true
	                                    ).toDispatch();
	        Dispatch.call(excel,
	                    "ExportAsFixedFormat",
	                    xlTypePDF,      
	                    pdfFile
	                    );
	        Dispatch.call(excel, "Close",false);
//	        app.invoke("Quit");
	        return true;
	    }catch(Exception e){
//	    	app.invoke("Quit");
	        return false;
	    }finally{
        	app.invoke("Quit");
        }
    }
    public static boolean ppt2PDF(String inputFile,String pdfFile){
    	ActiveXComponent app = null;
    	try{
        app = new ActiveXComponent("PowerPoint.Application");
        //app.setProperty("Visible", msofalse);
        Dispatch ppts = app.getProperty("Presentations").toDispatch();
         
        Dispatch ppt = Dispatch.call(ppts,
                                    "Open",
                                    inputFile,
                                    true,//ReadOnly
                                    true,//Untitled指定文件是否有标题
                                    false//WithWindow指定文件是否可见
                                    ).toDispatch();
         
        Dispatch.call(ppt,
                    "SaveAs",
                    pdfFile,
                    ppSaveAsPDF 
                    );
                 
        Dispatch.call(ppt, "Close");
//        app.invoke("Quit");
        return true;
        }catch(Exception e){
        	e.printStackTrace();
            return false;
        }
        finally {
        	if (app != null) app.invoke("Quit");
        }
    }
    /**
     * @author woody
     * @date 20160218
     * @pdf to swf
     * */
    public static int convertPDF2SWF(String sourcePath, String destPath, String fileName) throws IOException{  
    	SWFTOOLS_PATH = PropKit.get("SWFTOOLS_PATH");
    	RFXVIEW_SWF_PATH = PropKit.get("RFXVIEW_SWF_PATH");
    	File dest = new File(destPath);       
        if (!dest.exists()) {       
            dest.mkdirs();       
        }       
        // 源文件不存在则返回       
        File source = new File(sourcePath);       
        if (!source.exists()) {       
            return -1;       
        }       
        String[] envp = new String[1];       
        envp[0] = "PATH="+SWFTOOLS_PATH;       
        String command = "cmd /c \""+SWFTOOLS_PATH+"pdf2swf\" -z -s flashversion=9 " + sourcePath + " -o " + destPath + fileName ; 
        System.out.println("11111111======>>"+command);
        Process pro = Runtime.getRuntime().exec(command, envp);       
            
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(pro.getInputStream()));       
        while (bufferedReader.readLine() != null) {       
        }       
        try {       
            pro.waitFor();       
        } catch (InterruptedException e) {       
            e.printStackTrace();       
        }       
        // 然后在套播放器       
        command = "cmd  \""+SWFTOOLS_PATH+"swfcombine\" \""+RFXVIEW_SWF_PATH+"\" viewport=" + destPath + fileName + " -o " + destPath +fileName; 
        System.out.println("222222222======>>"+command);
        pro = Runtime.getRuntime().exec(command, envp);       
        bufferedReader = new BufferedReader(new InputStreamReader(pro.getInputStream()));       
        while (bufferedReader.readLine() != null) {       
        }       
        try {       
            pro.waitFor();       
        } catch (InterruptedException e) {       
            e.printStackTrace();       
        }       
        return pro.exitValue();  
    }
    public static void main(String[] args) throws IOException {  
        String sourcePath = "h:\\1.pdf";       
        String destPath = "h:\\";       
        String fileName = "1.swf";       
        PdfKit.convertPDF2SWF(sourcePath,destPath,fileName);  
    }  
}
