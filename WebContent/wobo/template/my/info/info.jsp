<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="../../../head.jsp" flush="true" />
<%
String uri = request.getRequestURI();
uri = uri.substring(0, uri.lastIndexOf("/"));
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>
<style>  
	#fileupload{ position:absolute;width:83px;height:40px; z-index:100;  font-size:60px;opacity:0;filter:alpha(opacity=0); margin-top:-5px;}  
</style> 
<body>
	<div id="wrapper">
		<jsp:include page="../../../top.jsp" flush="true"/>
		<!-- Page Content -->
		<div id="page-wrapper">
			<div class="row">
                <div class="col-lg-12">
                    <h2 class="page-header">个人资料</h2>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <%-- <div class="panel-heading">
                                                                用户名称：${user_id}
                        </div> --%>
                        <div class="panel-body">
                            <div class="row">
                            	<!-- /.col-lg-6 (nested) -->
                                <div class="col-lg-12">
                                    <h4>我的头像</h4>
                                    <input type="hidden" id="imgbasepath" value="<%=path %>">
                                    <form role="form" name="funcAttr" id="myForm2">
                                    	<div class="form-group" align="left">
                                            <%-- <img id="default" src="<%=path %>/upload/default/uploadImg.jpg" width="15%" > --%>
                                            <img id="diy" src="<%=path %>${avatar }" width="15%">
                                            <br/><br/><span style=color:#8E8E8E>图片名称不能为中文<br/>建议图片格式为：*.jpg,*.png,*.jpeg,<br/>尺寸：最好不小于120*120px, 宽度*高度比例为1:1</span>
                                        </div>
                                        <input type="file" id="fileupload" name="fileuploaddata" onchange="$('#imgUploadId').attr('disabled',false);ajax_upload();" size="1" style="position: absolute;left: 10px;width: 80px;height: 30px;overflow: hidden;line-height: 99em;" ><button type="button" class="btn btn-default" >上传</button>
                                        <!-- <button type="button" class="btn btn-success" id="imgUploadId" onclick="ajax_upload();" disabled="disabled">上传</button> -->
                                    	<div id="filepath"></div>
                                    </form>
                                </div>
                                <!-- /.col-lg-6 (nested) -->
                                <div class="col-lg-6">
                                	<br/>
                                	<br/>
                                    <form role="form" name="func" id="myForm">
                                        <div class="form-group">
                                            <label>用户名：${user_name }</label>
                                            <%-- <input class="form-control" placeholder="必填" id="username" name="user_name" value="${user_name}"> --%>
                                        </div>
                                        <div class="form-group">
                                            <label>系统身份：${type }</label>
                                            <%-- <input class="form-control" placeholder="必填" id="type" name="tel" value="${tel}"> --%>
                                        </div>
                                        <!-- <button type="button" class="btn btn-primary" onclick="window.location.href='update_info.jsp'">修改</button> -->
                                    	<!-- <button type="button" class="btn btn-success" onclick="javascript :history.back(-1);">返回</button> -->
                                    </form>
                                </div>
                            </div>
                            <!-- /.row (nested) -->
                        </div>
                        <!-- /.panel-body -->
                    </div>
                    <!-- /.panel -->
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
		</div>
		<!-- /#page-wrapper -->
	</div>
	<jsp:include page="../../../modal.jsp" flush="true" />
	<jsp:include page="../../../common.jsp" flush="true" />
	<script src="<%=request.getContextPath() %>/wobo/js/ajaxfileupload.js"></script>
</body>
<jsp:include page="../../../footer.jsp" flush="true" />
<script>
//上传头像
function ajax_upload()
{
	var path='<%=path %>';
	//判断是否选择文件
	if($("#fileupload").val() == "")
	{
		showModal("请选择需要上传的图片！");
		return false;
	}
	//判断文件类型是否符合要求
	if(!checkFileType($("#fileupload").val(),"jpg,png,jpeg"))
	{
		showModal("请选择正确的类型！");
		return false;
	}
	$.ajaxFileUpload({  
	       url:getUrl("web","info/upload"),  
	       secureuri:false,  
	       fileElementId:'fileupload',
	       data:{},
	       dataType: 'text/xml',             
	       success: function (data) {  
	    	   console.log(data);
	    	    var reData = strCommon(data);
				$("#diy").attr("src",path+reData.path);
				console.log(reData);
				console.log(reData.path);
				/*$("#default").attr("hidden",true);
				//$("#diy").attr("hidden",false); */
				showModal(reData.msg);
	       },
	       error: function (data){  
	    	   console.log("data======>>22222222222222");
	    	   showModal(data.responseText);	
	       }  
	});  
}
function strCommon(obj)
{
	var res = obj.substr(5,obj.length-11);
	var path1 = obj.split(">");
	var path2 = path1[1];
	var path3 = path2.split("<");
	var res = path3[0];
	res = res.replace(/\\/g,"/");
	var resJson =  eval('(' + res + ')');
	return resJson;
}
</script>