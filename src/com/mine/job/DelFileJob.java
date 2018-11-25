package com.mine.job;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author zyl
 * @date 20171031
 * @定时将数据库中的WGS84坐标系统信息转换成天津90坐标系统信息
 * */
public class DelFileJob  implements Job 
{
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		String path = this.getClass().getResource("").getPath();
		path=path.substring(1,path.indexOf("WebContent")+11);
		autoDelFile(path+"arcgis\\qcCode");
		autoDelFile(path+"arcgis\\shareCars");
	}

	/**
	 * 自动删除文件夹内当天以前的所有文件
	 * @param path 文件夹路径
	 */
	private void autoDelFile(String path) {
		File file = new File(path);
		long nowTime = new Date().getTime();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date parse=null;
		long fileTime=0;
		long differenceTime=0;
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length == 0) {
                System.out.println("文件夹是空的!");
                return;
            } else {
                for (File file2 : files) {
                	String createTime = this.getCreateTime(file2.getAbsolutePath());
            		try {
            			parse = sdf.parse(createTime);
            			fileTime=parse.getTime();
            		} catch (ParseException e) {
            			e.printStackTrace();
            		}
            		differenceTime=nowTime-fileTime;
                	if(differenceTime > 86400000)
                	{
                		file2.delete();
                	}
                }
            }
        } else {
            System.out.println("文件不存在!");
        }
	}
	
	/** 
     * 读取文件创建时间 
     */  
    private String getCreateTime(String filePath){  
        String strTime = null;  
        String ec=filePath.substring(filePath.lastIndexOf("."));
        try {  
            Process p = Runtime.getRuntime().exec("cmd /C dir "           
                    + filePath  
                    + "/tc" );  
            InputStream is = p.getInputStream();   
            BufferedReader br = new BufferedReader(new InputStreamReader(is));             
            String line;  
            while((line = br.readLine()) != null){  
                if(line.endsWith(ec)){  
                    strTime = line.substring(0,17);  
                    break;  
                }                             
             }   
        } catch (IOException e) {  
            e.printStackTrace();  
        }         
        System.out.println("创建时间    " + strTime);     
        //输出：创建时间   2009-08-17  10:21:00  
        return strTime+":00";
    }  
	
    public static void main(String[] args) throws IOException {
    	System.out.println(new DelFileJob());
	}
}
