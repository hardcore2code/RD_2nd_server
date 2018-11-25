<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<base href="<%=basePath%>">

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>index.jsp</title>

<script src="demo/file/uploadify/jquery.min.js" type="text/javascript"></script>
<script src="demo/file/uploadify/jquery.uploadify.min.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="demo/file/uploadify/uploadify.css">
</head>
<body>
	<form action="demo/sendFile" method="post" enctype="multipart/form-data">
		<input type="file" name="file_upload" id="file_upload"/>
		<input type="submit" name="uploadbutton" value="上传"/>
	</form>
	
	<!--  文件上传成功之后，文件的地址显示在这里  -->
	<ul id="url"></ul>
	
	<script type="text/javascript">
		$(function(){
			$("#file_upload").uploadify({
				//校验数据

				'swf' : 'demo/file/uploadify/uploadify.swf', //指定上传控件的主体文件，默认‘uploader.swf’
				'uploader' : 'http://192.168.0.19:9001/demo/importExcel', //指定服务器端上传处理文件，默认‘upload.php’
				'auto' : true, //自动上传
				'buttonImage' : 'demo/file/uploadify/uploadify-browse.png', //浏览按钮背景图片
				'multi' : false, //单文件上传
				'fileTypeExts' : '*.*', //允许上传的文件后缀
				'fileSizeLimit' : '300MB', //上传文件的大小限制，单位为B, KB, MB, 或 GB
				'successTimeout' : 30, //成功等待时间
				'onUploadSuccess' : function(file, data, response) {
					//每成功完成一次文件上传时触发一次
					var image=eval("["+data+']')[0];
					//alert('===file:'+file);
					//alert('===data:'+data);//json格式
					//alert('===response:'+response);//true
					//alert('===image:'+image);
					//alert('===image.fileName:'+image.fileName);
					
					$('#url').append("<li><img width=80 src="+image.fileName+" </li>");
					
					/* var image=eval(data);
					alert(image[0]["big"]); */
				},
				'onUploadError' : function(file, data, response) {
					//当上传返回错误时触发
					$('#url').append("<li>" + data + "</li>");
				}
			});
		});
	</script>
</body>
</html>