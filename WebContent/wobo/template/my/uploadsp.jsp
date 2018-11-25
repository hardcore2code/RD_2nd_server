<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="../../head.jsp" flush="true" />
<style>  
	#fileupload{ position:absolute;width:83px;height:40px; z-index:100;  font-size:60px;opacity:0;filter:alpha(opacity=0); margin-top:-5px;}  
</style> 
<body>
	<div id="wrapper">
	<jsp:include page="../../top.jsp" flush="true"/>
		<!-- Page Content -->
		<div id="page-wrapper">
			<div class="container-fluid">
				<div class="row">
					<div class="col-lg-12">
						<h1 class="page-header">上传主题</h1>
					</div>
					<!-- /.col-lg-12 -->
				</div>
				<!-- /.row -->
			</div>
			<!-- /.container-fluid -->
			<!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                        </div>
                        <div class="panel-body">
                        	<div class="row">
                        		<div class="col-lg-6">
                                    <h1></h1>
                                    <form role="form" >
                                     	<div class="form-group">
                                            <label>主题名称</label>
                                            <input class="form-control" name="templatename" placeholder="必填.">
                                        </div>
                                        <div class="form-group">
                                            <label>主题描述</label>
                                            <input class="form-control" name="templatedesc" placeholder="描述.">
                                        </div>
                                        <fieldset>
                                        	<label>选择上传主题</label>
                                            <div class="form-group input-group">
                                            	<input type="text" class="form-control" id="filepath" placeholder="文件路径...(仅支持zip，并小于20M)">
                                            	<span class="input-group-addon"><i class="fa fa-file-archive-o fa-fw"></i></span>
                                        	</div>
                                            <br/>
                                            <br/>
                                            <input type="file" id="fileupload" name="fileuploaddata" onchange="$('#filepath').val($('#fileupload').val())" size="1" ><button type="button" class="btn btn-primary" >选择文件</button>
                                            <button type="button" class="btn" onclick="ajax_upload();">上传</button>
                                        </fieldset>
                                    </form>
                                </div>
                                <!-- /.col-lg-6 (nested) -->
                        	</div>
                        </div>
                     </div>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
		</div>
		<!-- /#page-wrapper -->
	</div>
	<jsp:include page="../../modal.jsp" flush="true" />
	<jsp:include page="../../common.jsp" flush="true" />
	<script src="<%=request.getContextPath() %>/wobo/js/ajaxfileupload.js"></script>
</body>
<jsp:include page="../../footer.jsp" flush="true" />
<script>
function ajax_upload()
{
	if($("input[name='templatename']").val() == "")
	{
		showModal("请填写主题名称！");
		return false;
	}
	//判断是否选择文件
	if($("#fileupload").val() == "")
	{
		showModal("请选择需要上传的文件！");
		return false;
	}
	//判断文件类型是否符合要求
	if(!checkFileType($("#fileupload").val(),"zip"))
	{
		showModal("请选择正确的类型！");
		return false;
	}
	$.ajaxFileUpload({  
	       url:getUrl("wb","template/upload"),  
	       secureuri:false,  
	       fileElementId:'fileupload',
	       data:{templatename:$("input[name='templatename']").val(),templatedesc:$("input[name='templatedesc']").val()},
	       dataType: 'text/xml',             
	       success: function (data,status) {  
				showModal(data);
	       },
	       error: function (data, status, e){  
	    	    showModal(e);	       
	       }  
	});  
}

//选择文件  by woody 2015-3-11
//该方法已不用！！  by woody 2015-3-12
function selectfile()
{ 
	var ie=navigator.appName=="Microsoft Internet Explorer" ? true : false; 
	if(ie){ 
		document.getElementById("file").click(); 
		document.getElementById("filepath").value=document.getElementById("file").value;
	}else{
		var a=document.createEvent("MouseEvents");//FF的处理 
		a.initEvent("click", false, false);  
		document.getElementById("file").dispatchEvent(a);
		document.getElementById("filepath").value=document.getElementById("file").value;
	} 
} 
</script>