package com.mine.pub.kit;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 *@author woody
 *@date 2015-3-12
 *@注释：zip 解压 压缩工具包 
 *@懒汉式单例类.在第一次调用的时候实例化
 * */
public class ZipKit {
	 //私有的默认构造子
	private static ZipKit zipKit = null;
	private static final int BUFFEREDSIZE = 1024;
	private ZipFile zipFile;
	private ZipKit()
	{
	}
	//静态工厂方法  
	public synchronized static ZipKit getInstance()
	{
		if(zipKit == null)
		{
			zipKit = new ZipKit();
		}
		return zipKit;
	}
	/**
	 * @author woody
	 * @date 2015-3-12
	 * @注释:解压zip格式的压缩文件到当前文件夹
	 * */
	public synchronized void unzip(String zipFileName)throws FileNotFoundException
	{
		unzip(zipFileName,"");
	}
	/**
	 * @author woody
	 * @date 2015-3-13
	 * @注释: 解压zip格式的压缩文件到指定位置
	 * @param zipFileName 压缩文件
     * @param extPlace 解压目录
	 * */
	@SuppressWarnings("rawtypes")
	public synchronized void unzip(String zipFileName , String extPath)
	{
		InputStream is = null;
		BufferedInputStream bis = null;
		try {
			File f = new File(zipFileName);
			zipFile = new ZipFile(zipFileName);
			if(!f.exists() && f.length()<=0)
			{
				throw new Exception("要解压的文件不存在");
			}
			String strPath,gbkPath,strtemp;
			extPath = "".equals(extPath) ? f.getParent() : extPath;
			File tempFile = new File(extPath);
			strPath = tempFile.getAbsolutePath();
			Enumeration e = zipFile.entries();
			while(e.hasMoreElements())
			{
				ZipEntry zipEntry = (ZipEntry) e.nextElement();
				gbkPath = zipEntry.getName();
				if(zipEntry.isDirectory())
				{
					strtemp = strPath + "\\" + gbkPath;
					File dir = new File(strtemp);
					dir.mkdirs();
				}else
				{
					//读写文件
					is = zipFile.getInputStream(zipEntry);
					bis = new BufferedInputStream(is);
					gbkPath = zipEntry.getName();
					strtemp = strPath + "\\" + gbkPath;
					//建目录
					String strsubdir = gbkPath;
					for(int i = 0 ; i < strsubdir.length() ; i++)
					{
						if(strsubdir.substring(i,i+1).equalsIgnoreCase("\\"))
						{
							String temp = strPath + "\\" + strsubdir.substring(0,i);
							File subdir = new File(temp);
							if(!subdir.exists())
							{
								subdir.mkdir();
							}
						}
					}
					FileOutputStream fos = new FileOutputStream(strtemp);
					BufferedOutputStream bos = new BufferedOutputStream(fos);
					int c;
					while((c = bis.read()) != -1)
					{
						bos.write((byte)c);
					}
					bos.close();
					fos.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally
		{
			try {
				is.close();
				bis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	 /**
	 * 压缩zip格式的压缩文件
	 * @param inputFilename 压缩的文件或文件夹及详细路径
	 * @param zipFilename 输出文件名称及详细路径
	 * @throws IOException
	 */
    public synchronized void zip(String inputFilename, String zipFilename) throws IOException {
        zip(new File(inputFilename), zipFilename);
    }
	
    /**
     * 压缩zip格式的压缩文件
     * @param inputFile 需压缩文件
     * @param zipFilename 输出文件及详细路径
     * @throws IOException
     */
    public synchronized void zip(File inputFile, String zipFilename) throws IOException {
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFilename));
        try {
            zip(inputFile, out, "");
        } catch (Exception e) {
            throw e;
        } finally {
            out.close();
        }
    }
    
	/**
     * 压缩zip格式的压缩文件
     * @param inputFile 需压缩文件
     * @param out 输出压缩文件
     * @param base 结束标识
     * @throws IOException
     */
    private synchronized void zip(File inputFile, ZipOutputStream out, String base) throws IOException {
        if (inputFile.isDirectory()) {
            File[] inputFiles = inputFile.listFiles();
            out.putNextEntry(new ZipEntry(base + "/"));
            base = base.length() == 0 ? "" : base + "/";
            for (int i = 0; i < inputFiles.length; i++) {
                zip(inputFiles[i], out, base + inputFiles[i].getName());
            }
        } else {
            if (base.length() > 0) {
                out.putNextEntry(new ZipEntry(base));
            } else {
                out.putNextEntry(new ZipEntry(inputFile.getName()));
            }
            FileInputStream in = new FileInputStream(inputFile);
            try {
                int c;
                byte[] by = new byte[BUFFEREDSIZE];
                while ((c = in.read(by)) != -1) {
                    out.write(by, 0, c);
                }
            } catch (IOException e) {
                throw e;
            } finally {
                in.close();
            }
        }
    }
	
    public static void main(String args[]) throws Exception
    {
    	ZipKit zipUtils = new ZipKit();
//    	zipUtils.unzip("e://1.zip");
//    	zipUtils.unzip("e://1.zip","f://1");
    	zipUtils.zip("f://test", "f://test.zip");
    }
}
