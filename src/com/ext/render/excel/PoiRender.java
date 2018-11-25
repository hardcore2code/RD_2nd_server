/**
 * Copyright (c) 2011-2013, kidzhou 周磊 (zhouleib1412@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ext.render.excel;

import com.ext.kit.excel.PoiExporter;
import com.jfinal.kit.PathKit;
import com.jfinal.log.Log;
import com.jfinal.render.Render;
import com.jfinal.render.RenderException;
import com.mine.pub.kit.DateKit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

public class PoiRender extends Render {

    protected final Log LOG = Log.getLog(getClass());
    private final static String CONTENT_TYPE = "application/msexcel;charset=" + getEncoding();
    private List<?>[] data;
    private String[][] headers;
    private String[] sheetNames = new String[]{};
    private int cellWidth;
    private String[][] columns;
    private String fileName = "file1.xls";
    private int headerRow;
    private String version;
    private String type;
    private String path;

    public PoiRender(List<?>[] data) {
        this.data = data;
    }

    public static PoiRender me(List<?>... data) {
        return new PoiRender(data);
    }

    //type=0请求方式为get，type=1请求方式为post
    @Override
    public void render() {
        response.reset();
        response.setHeader("Content-disposition", "attachment; filename=" + fileName);
        response.setContentType(CONTENT_TYPE);
        File file = null;
		FileOutputStream fileOutputStreane = null;
        OutputStream os = null;
        try {
        	if("0".equals(type)){
            	os = response.getOutputStream();
            	PoiExporter.data(data).version(version).sheetNames(sheetNames).headerRow(headerRow).headers(headers).columns(columns)
                .cellWidth(cellWidth).export().write(os);
            }else{
//            	String exportFileName = "D:/workspaces_svn/OnlineSchool/WebContent/createExcel/" + DateKit.toStr(new Date(), "yyyyMMddHHmmss");
            	String exportFileName = "/webapps/upload/exportExcel/" + DateKit.toStr(new Date(), "yyyyMMddHHmmss");
            	file = new File(exportFileName);
                if(!file.exists() && !file.isDirectory()) 
                {
                	file.mkdirs(); 
                }
                path = exportFileName + "/" + fileName;
                File filePath = new File(path);
                System.out.println("path================>"+ path);
            	fileOutputStreane = new FileOutputStream(filePath);
            	PoiExporter.data(data).version(version).sheetNames(sheetNames).headerRow(headerRow).headers(headers).columns(columns)
                .cellWidth(cellWidth).export().write(fileOutputStreane);
            	setPath(path);
            }
        } catch (FileNotFoundException e1) {
        	e1.printStackTrace();
        } catch (Exception e) {
            throw new RenderException(e);
        } finally {
            try {
                if (os != null) {
                    os.flush();
                    os.close();
                }
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }
    
    /**
     * 导出Excel生成文件供下载方式
     * */
    public String createFile() {
        File file = null;
		FileOutputStream fileOutputStreane = null;
        try {
//        	String exportFileName = "D:/workspaces_svn/OnlineSchool/WebContent/createExcel/" + DateKit.toStr(new Date(), "yyyyMMddHHmmss");
        	String exportFileName = PathKit.getWebRootPath() + "/exportExcel/" + DateKit.toStr(new Date(), "yyyyMMddHHmmss");
        	file = new File(exportFileName);
            if(!file.exists() && !file.isDirectory()) 
            {
            	file.mkdirs(); 
            }
            path = exportFileName + "/" + fileName;
            File filePath = new File(path);
            System.out.println("path================>"+ path);
        	fileOutputStreane = new FileOutputStream(filePath);
        	PoiExporter.data(data).version(version).sheetNames(sheetNames).headerRow(headerRow).headers(headers).columns(columns)
            .cellWidth(cellWidth).export().write(fileOutputStreane);
        } catch (FileNotFoundException e1) {
        	e1.printStackTrace();
        } catch (Exception e) {
            throw new RenderException(e);
        }
//        String paths = "/webapps/exportExcel/"+path.split("exportExcel/")[1];
        String paths = "/exportExcel/"+path.split("exportExcel/")[1];
        System.out.println("paths================>"+ paths);
        return paths;
    }
    public PoiRender headers(String[]... headers) {
        this.headers = headers;
        return this;
    }

    public PoiRender headerRow(int headerRow) {
        this.headerRow = headerRow;
        return this;
    }

    public PoiRender columns(String[]... columns) {
        this.columns = columns;
        return this;
    }

    public PoiRender sheetName(String... sheetName) {
        this.sheetNames = sheetName;
        return this;
    }

    public PoiRender cellWidth(int cellWidth) {
        this.cellWidth = cellWidth;
        return this;
    }

    public PoiRender fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public PoiRender version(String version) {
        this.version = version;
        return this;
    }
    
    public PoiRender type(String type) {
        this.type = type;
        return this;
    }

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
