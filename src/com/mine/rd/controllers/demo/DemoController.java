package com.mine.rd.controllers.demo;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.ext.kit.excel.PoiExporter;
import com.ext.kit.excel.PoiImporter;
import com.ext.kit.excel.Rule;
import com.ext.render.excel.PoiRender;
import com.jfinal.aop.Clear;
import com.jfinal.plugin.activerecord.ActiveRecordException;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.mine.pub.controller.BaseController;
import com.mine.pub.kit.DateKit;
import com.mine.pub.service.Service;
import com.mine.rd.services.demo.pojo.DemoDao;
import com.mine.rd.services.demo.service.DemoService;
import com.mine.rd.websocket.MyWebSocket;
@Clear
public class DemoController extends BaseController{
	
	private Logger logger = Logger.getLogger(DemoController.class);
	DemoDao dao = new DemoDao();
	static int count = 0;
	
	public void renderCaptcha1() {
		// TODO Auto-generated method stub
		renderMyCaptcha("AAA");
	}
	/**
	 * @author woody
	 * @测试读取特定格式excel
	 * @date 20170804
	 * */
	public void testExcel()
	{
		String toPath = "e:\\C90B7000.xls";
		int resInt = 0;
		File file = new File(toPath);
		List<List<List<String>>> list = PoiImporter.readExcel(file, new Rule());
		System.out.println("list.size===>"+list.size());
		PoiExporter.data(list);
//		for(List<List<String>> lists : list)
//		{
//			lists.remove(0);
//			List<List<String>> listData = lists;
//			for(List<String> listss : listData)
//			{
//				resInt =  doExcelRow(listss);
//				System.out.println("===> " + listss.size());
//			}
//		}
		String filename = "demo.xlsx";
		int num = 1;
		String[] sheetNames = new String[num];
		sheetNames[0] = "111";
		String[][] headers = new String[num][];
		headers[0][0] = "1";
		headers[0][1] = "1";
		headers[0][2] = "1";
		headers[0][3] = "1";
		headers[0][4] = "1";
		headers[0][5] = "1";
		headers[0][6] = "1";
		headers[0][7] = "1";
		headers[0][8] = "1";
		headers[0][9] = "1";
		headers[0][10] = "1";
		headers[0][11] = "1";
		headers[0][12] = "1";
		headers[0][13] = "1";
		headers[0][14] = "1";
		headers[0][15] = "1";
		headers[0][16] = "1";
		headers[0][17] = "1";
		headers[0][18] = "1";
		String[][] columns = new String[num][];
		
		render(PoiRender.me(list).fileName(filename).sheetName(sheetNames).headers(headers).columns(columns).cellWidth(3000));
	}
	public void testExcel2()
	{
		String fileToBeRead = "e:\\leimuban1.xls"; // excel位置
        int coloum = 0; // 比如你要获取第1列
        try {
            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(fileToBeRead));
            HSSFSheet sheet = workbook.getSheet("Sheet1");
 
            for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                HSSFRow row = sheet.getRow((short) i);
                if (null == row) {
                    continue;
                } else {
                	if(row.getRowNum() == 9)
                	{
                		HSSFRow rowadd = createRow(sheet,15);  
                		createCell(rowadd);
                		System.out.println("getRowNum===>"+row.getRowNum());
                		HSSFCell cell = row.getCell(16);
                        if (null == cell) {
                            continue;
                        } else {
                            System.out.println("cell===>"+cell);
//                            System.out.println("celltype===>"+cell.getCellType());
//                            System.out.println("cellstringvalue===>"+cell.getStringCellValue());
                              int temp = (int) cell.getNumericCellValue();
                              cell.setCellValue(temp + 20);
                        }
                	}
                }
            }
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(fileToBeRead);
                workbook.setForceFormulaRecalculation(true);
                workbook.write(out);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		renderJson("{\"key\":\"test\"}");
	}
	/** 
    * 找到需要插入的行数，并新建一个POI的row对象 
    * @param sheet 
    * @param rowIndex 
    * @return 
    */  
    private HSSFRow createRow(HSSFSheet sheet, Integer rowIndex) {  
    	HSSFRow row = null;  
        if (sheet.getRow(rowIndex) != null) {  
            int lastRowNo = sheet.getLastRowNum();  
            sheet.shiftRows(rowIndex, lastRowNo, 1);  
        }  
        row = sheet.createRow(rowIndex);  
        return row;  
    } 
    /** 
     * 创建要出入的行中单元格 
     * @param row 
     * @return 
     */  
    private HSSFCell createCell(HSSFRow row) {  
    	HSSFCell cell = row.createCell(0);  
        cell.setCellValue(999999);  
        row.createCell(1).setCellValue(1.2);  
        row.createCell(2).setCellValue("This is a string cell");  
        return cell;  
    }  
	/**
	 * @author woody
	 * @查看国家对接接口流水表
	 * */
	public void checkUploadInterfaceFlow()
	{
		List<Record> list = Db.find("select count,status,CONVERT(varchar(20),sysdate, 20) sysdate from UPLOAD_INTERFACE_FLOW order by sysdate desc ");
		List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
		for(Record record : list)
		{
			listMap.add(record.getColumns());
		}
		renderJson(listMap);
	}
	
//	public void register()
//	{
//		mixReturn("");
//	}
	public void wsTest()
	{
		System.out.println("wsTest===>>"+count++);
		renderJson("111");
	}
	
	public void wsSend()
	{
		for(MyWebSocket item: MyWebSocket.webSocketSet){  
            try {
            	System.out.println("aaaaa");
            	item.sendMessage("");
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
		}
		renderJson("111");
	}
//	public void getMenuList()
//	{
//		List list = CacheKit.getKeys("mySession");
//		String userId = this.getMySession("userId").toString();
//		setAttr("menulist", dao.getMenuList(userId));
//		System.out.println("===>"+dao.getMenuList(userId));
//		renderJsonForCors();
//	}
//	public void demoFile()
//	{
//		mixRenderReturn("index.html");
//	}
//	public void demoFile222()
//	{
//		mixRenderReturn("file/index.jsp");
//	}
//	public void test111()
//	{
//		mixRenderReturn("HtmlPage1.html");
//	}
//	public void test2()
//	{
//		mixRenderReturn("HtmlPage2.html");
//	}
//	public void demoExcelImport()
//	{
//		mixRenderReturn("file/excelImport.jsp");
////		render("excelImport.html");
//	}
//	public void sendFile()
//	{
//		//上传并获取request
//		//	UploadFile uploadFile = getFile("fileuploaddata", "uploads/coures/"+uerid, 150*1024*1024); //最大不要超过150m
//		UploadFile uploadFile = getFile("fileuploaddata", "::c:/1111111", 150*1024*1024); //最大不要超过150m
//		String uploadFilePath = uploadFile.getUploadPath() + "/" + uploadFile.getFileName();
//		PdfKit.convert2PDF(uploadFilePath, "c:/1111111/"+uploadFile.getFileName()+".pdf");
//		PdfKit.convert2PDF(uploadFilePath, "h:/1111111/"+uploadFile.getFileName()+".pdf");
//		PdfKit.convert2PDF(uploadFilePath, "h:/1111112/123.pdf");
//		mixReturn();
//	}
//	public void test1(){
//		mixRenderReturn("file/index.jsp");
//	}
//	
//	public void toPdf()
//	{
//		mixRenderReturn("file/indexFile.jsp");
//	}
//	
//
//	public void toPdfFile()
//	{
//		//上传并获取request
//		UploadFile uploadFile = getFile("Filedata", "testToPdf/", 150*1024*1024); //最大不要超过150m
//		String uploadFilePath = uploadFile.getUploadPath() + "/" + uploadFile.getFileName();
//		PdfKit.convert2PDF(uploadFilePath, uploadFile.getUploadPath()+uploadFile.getFileName()+".pdf");
//		String path = uploadFile.getUploadPath()+uploadFile.getFileName()+".pdf";
//		path = path.replace('\\', '/');
//		path = "http://localhost:9001/upload" + path.split("/upload")[1];
//		setAttr("path", path);
//		mixReturn();
////		upload(7, "1");
//	}
//	
//	/**
//	 * @author woody
//	 * @date 20151019
//	 * @导入excel
//	 * */
//	
//	public void importExcel()
//	{
//		String toPath = "";
//		@SuppressWarnings("unused")
//		int resInt = 0;
//		try {
//			//上传并获取request
//			//显示进度条
//			UploadFile uploadFile = getFile("Filedata", "import/student/excel", 15*1024*1024); //最大不要超过150m
//			//正常上传
////			UploadFile uploadFile = getFile("fileuploaddata", "import/student/excel", 15*1024*1024); //最大不要超过150m
//			if(uploadFile != null && uploadFile.getUploadPath() != null )
//			{
//				toPath = uploadFile.getUploadPath() + "/" + uploadFile.getFileName();
//				File file = new File(toPath);
//				List<List<List<String>>> list = PoiImporter.readExcel(file, new Rule());
//				System.out.println("list.size===>"+list.size());
//				for(List<List<String>> lists : list)
//				{
//					lists.remove(0);
//					List<List<String>> listData = lists;
//					for(List<String> listss : listData)
//					{
//						resInt =  doExcelRow(listss);
//						System.out.println("===> " + listss.size());
//					}
//				}
//			}
//			mixReturn("");
//		} 
//		catch (Exception e) {
//			System.err.println("上传失败");
//			logger.error(e.getMessage(),e);
//			setAttr("msg", "上传失败");
//			renderText("Error");
//			e.printStackTrace();
//			throw(e);
//		}
//	}
	/**
	 * @author woody
	 * @date 20151019
	 * @处理excel中每行记录
	 * @return 0-序号为非法字符 1-成功
	 * */
	@SuppressWarnings("unused")
	private int doExcelRow(List<String> list)
	{
		String[] strs = list.get(0).split("\\."); 
		int resInt = 0; //默认非法
		if(strs.length == 3 || strs.length == 4)
		{
			Map<String,String> map = new HashMap<String, String>();
			map.put("NO", strs[0]);
			resInt = 1;
		}
		else
		{
			resInt = 0;
		}
		return resInt;
	}
	/**
	 * @author woody
	 * @date 20150919
	 * @导出excel
	 * */
//	public void export()
//	{
//		ExcelDemoDao dao = new ExcelDemoDao();
//		String filename = "demo.xlsx";
//		int num = dao.querySheets().size();
//		String[] sheetNames = new String[num];
//		List<?>[] sheetAllList = new ArrayList<?>[num];
//		String[][] headers = new String[num][];
//		String[][] columns = new String[num][];
//		dao.getExcelInfo(sheetNames,sheetAllList,headers,columns);
//		mixRenderReturn(PoiRender.me(sheetAllList).fileName(filename).sheetName(sheetNames).headers(headers).columns(columns).cellWidth(3000));
//	}
//
//	public void export_bak()
//	{
//		String filename = "demo.xlsx";
//		List<Record> list = CacheKit.get("mydict", "demo");
//		int num = list.size();
//		String[] sheetNames = new String[num];
//		List<?>[] projectAllList = new ArrayList<?>[num];
//		String[][] headers = new String[num][];
//		String[][] columns = new String[num][];
//		String[] cols = new String[5];
//		cols[0] = "id";
//		cols[1] = "id_main";
//		cols[2] = "dict_id";
//		cols[3] = "dict_value";
//		cols[4] = "status";
//		for(int i = 0 ; i < list.size() ; i++ )
//		{
//			List<Object> data = Lists.newArrayList();
//			sheetNames[i] = "demo"+i;
//			for(Record one : list)
//			{
//				data.add(one.getColumns());
//			}
//			projectAllList[i] = data;
//			headers[i] = cols;
//			columns[i] = cols;
//		}
//
//		mixRenderReturn(PoiRender.me(projectAllList).fileName(filename).sheetName(sheetNames).headers(headers).columns(columns).cellWidth(3000));
//	}
	
	/**
	 * @author ouyangxu
	 * @date 20160815
	 * @app 富文本编辑器上传图片
	 **/
	public void textImage(){
		logger.info("富文本编辑器上传图片");
		try {
			UploadFile uploadFile = getFile("Filedata", "textimage/", 20*1024*1024); //最大不要超过20m
			String uploadFilePath = "";
			String path = "uploadAttachment" + DateKit.toStr(new Date(), "yyyyMMddHHmmss");
			String fileName = "";
			boolean flag = uploadFile.getFile().renameTo(new File(uploadFile.getUploadPath() + "/" + path + uploadFile.getFileName().substring(uploadFile.getFileName().lastIndexOf("."))));
			fileName = uploadFile.getFileName();
			System.out.println(fileName);
			System.out.println(flag);
			if(flag == true){
				uploadFilePath = uploadFile.getUploadPath() + "/" + path + uploadFile.getFileName().substring(uploadFile.getFileName().lastIndexOf("."));
				System.out.println(uploadFilePath);
			}
			uploadFilePath = uploadFilePath.replaceAll("\\\\", "/");
			uploadFilePath = uploadFilePath.replaceAll("//", "/");
			String[] picPath = uploadFilePath.split("onlineschool");
			System.out.println("/webapps"+picPath[1]);
			System.out.println("path============>/webapps"+picPath[1]);
			String resJson="";
			if(!"".equals(picPath[1])){
				//富文本上传成功
				resJson="{\"error\":0,\"url\":\"/webapps"+picPath[1]+"\"}";	
			}else{
				//富文本上传失败
				resJson="{\"error\":1,\"message\":\"fail\"}";	
			}
			System.out.println("11=========>" + resJson);
			HttpServletResponse response = this.getResponse();
			try {
				response.setHeader("Content-Type", "text/html;charset=UTF-8");
				response.getWriter().write(resJson);
			} catch (IOException e) {
				e.printStackTrace();
			}	
		} catch (Exception e) {
			logger.error("富文本编辑器上传图片异常===>" + e.getMessage());
			e.printStackTrace();
			throw new ActiveRecordException(e);
		}
		renderNull();
	}
	public void uploadLocalTransferData()
	{
		Service service = new DemoService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("上传国家数据==>" + e.getMessage());
			e.printStackTrace();
		}
		renderJson();
	}
}
