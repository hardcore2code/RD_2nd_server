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
	#logo{ position:absolute;width:83px;height:40px; z-index:100;  font-size:60px;opacity:0;filter:alpha(opacity=0); margin-top:-5px;} 
	#indexImage{ position:absolute;width:83px;height:40px; z-index:100;  font-size:60px;opacity:0;filter:alpha(opacity=0); margin-top:-5px;}  
</style> 
<body>
	<div id="wrapper">
		<jsp:include page="../../../top.jsp" flush="true"/>
		<!-- Page Content -->
		<div id="page-wrapper">
			<div class="row">
                <div class="col-lg-12">
                    <h2 class="page-header">学校管理</h2>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                        <h3>| 添加学校</h3>
                        </div>
                        <div class="panel-body">
                            <div class="row">
                                <!-- /.col-lg-6 (nested) -->
                                <div class="col-lg-6">
                                	<br/>
                                	<br/>
                                    <form role="form" name="func" id="myForm">
                                        <div class="form-group">
                                        <label for="disabledSelect">中小学</label>
                                        <label for="small" class="radio-inline">
                                        	<input type="checkbox" name="schoolCategory" id="schoolCategory1" value="0"/>小学
                                        </label>
                                        <label for="middle" class="radio-inline">
                                        	<input type="checkbox" name="schoolCategory" id="schoolCategory2" value="1"/>初中
                                        </label>
                                        <label for="high" class="radio-inline">
                                        	<input type="checkbox" name="schoolCategory" id="schoolCategory3" value="2"/>高中
                                        </label>
                                         </div>
                                        <div class="form-group">
                                            <label>所属区</label>
                                            <br/>
                                            <select name="area" id="area" style="width: 400px; height: 35px"  onchange=""> 
                                            <c:forEach var="area" items="${areas}">
                                            	<option value="${area.dict_id}" ${area.dict_id == "0" ? "selected" : ""}>${area.dict_value}</option>
                                            </c:forEach>
                                            </select>
                                            <%-- <select name="county" id="county" style="width: 380px; height: 35px">
                                            <c:forEach var="country" items="${countries}">
                                            	<option value="${country.dict_id}" ${country.dict_id == "0" ? "selected" : ""}>${country.dict_value}</option>
                                            </c:forEach>
                                            </select> --%>
                                        </div>
                                        <div class="form-group">
                                            <label>学校名称</label>
                                            <input class="form-control" id="name" name="name" style="width: 400px">
                                        </div>
                                        <div class="form-group">
                                            <label>学校LOGO</label>
                                            <div class="form-group" align="left">
                                            	<img id="uploadLogo" src="<%=path %>/upload/default/uploadImg.jpg" width="35%" >
                                            	<img id="diyLogo" src="<%=path %>${school_logo }" width="40%" hidden="true">
                                       			<br/><br/><span style=color:#8E8E8E>建议图片格式为：*.jpg,*.png,*.jpeg,<br/>尺寸：最好不小于95*95px, 宽度*高度比例为1:1</span>
                                       		</div>
                                       		<div>
                                       		<input type="file" id="logo" name="logo" onchange="$('#logoUploadId').attr('disabled',false); ajax_upload_logo();" size="1" style="position: absolute;left: 10px;width: 80px;height: 30px;overflow: hidden;line-height: 99em;"><button type="button" id="btnSelect" class="btn btn-default" >上传图片</button>
                                        	<!-- <button type="button" class="btn btn-success" id="logoUploadId" onclick=" " disabled="disabled">上传图片</button> -->
                                        	</div>
                                        </div>
                                       	<div class="form-group">
                                            <label>主页图片</label>
                                            <div class="form-group" align="left">
                                            	<img id="uploadIndex" src="<%=path %>/upload/default/uploadImg.jpg" width="35%" >
                                            	<img id="diyIndex" src="<%=path %>${school_pic }" width="40%" hidden="true">
                                       			<br/><br/><span style=color:#8E8E8E>建议图片格式为：*.jpg,*.png,*.jpeg,<br/>尺寸：1200*250px</span>
                                       		</div>
                                       		<input type="file" id="indexImage" name="indexImage" onchange="$('#imgUploadId').attr('disabled',false);ajax_upload_index();" size="1" style="position: absolute;left: 10px;width: 80px;height: 30px;overflow: hidden;line-height: 99em;"><button type="button" id="btnSelect" class="btn btn-default" >上传图片</button>
                                        	<!-- <button type="button" class="btn btn-success" id="imgUploadId" onclick="ajax_upload_index();" disabled="disabled">上传图片</button> -->
                                       	</div>
                                        <div class="form-group">
                                            <label>学校简介</label>
                                            <textarea class="form-control" name="summary" id="summary" placeholder="学校简介在500汉字以内"></textarea>
                                        </div>
                                       <!--  <div class="form-group">
                                            <label>URL地址</label>
                                            <input class="form-control" placeholder="" id="url" name="url">
                                        </div>
                                        <div class="form-group">
                                            <label>模板</label>
                                            <input type="file" id="model" name="model" onchange="$('#imgUploadId').attr('disabled',false);" size="1" ><button type="button" id="btnSelect" class="btn btn-default" >选择</button>
                                        </div> -->
                                        <div class="form-group">
                                       		 <input type="hidden" value="${logoPath}" id="logoPath" name="logoPath">
                                       		 <input type="hidden" value="${indexPath}" id="indexPath" name="indexPath">
                                        	<button type="button" class="btn btn-success" onclick="ajax_submit();">确 定</button>
                                        	<button type="button" class="btn btn-success" id="_webSchool_schoolList" onclick="jump_pub(this)">返回</button>
                                        </div>
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
<script type="text/javascript">
function ajax_submit()
{
	var chk_value =[];
	$('input[name="schoolCategory"]:checked').each(function(){
	chk_value.push($(this).val());
	}); 
	if(chk_value.length == 0)
	{
		showModal("请选择学校等级!");
		return false;
	}
	if($("#area").val() == "" || $("#area").val() == null || $("#area").val() == "区")
	{
		showModal("请选择区!");
		return false;
	}
	/* if($("#county").val() == "" || $("#county").val() == null || $("#county").val() == "县")
	{
		showModal("请选择县!");
		return false;
	} */
	if($("#name").val() == "" || $("#name").val() == null )
	{
		showModal("请填写学校名称!");
		return false;
	}
	if($("#summary").val().length > 500){
		showModal("学校简介在500字以内!");
		return false;
	}
	var url = getUrl("web","webSchool/saveSchool");
	$.ajax({
		url:url,
		type:"post",
		data:$('#myForm').serialize(),
		dataType:'json',
		success:function(data)
		{
			showModal(data.msg);
			if(data.resFlag == 0){
				closeModal(getUrl("web","webSchool/schoolList")); //关闭悬浮框时再跳转页面
			}
			return false;
		},
		error:function(data)
		{
			showModal(data.responseText);
			return false;
		}
	});
	
}
//上传主页图片
function ajax_upload_index()
{
	var path='<%=path %>';
	//判断是否选择文件
	if($("#indexImage").val() == "")
	{
		showModal("请选择需要上传的图片！");
		return false;
	}
	//判断文件类型是否符合要求
	if(!checkFileType($("#indexImage").val(),"jpg,png,jpeg"))
	{
		showModal("请选择正确的类型！");
		return false;
	}
	$.ajaxFileUpload({  
	       url:getUrl("web","webSchool/uploadIndex"),  
	       secureuri:false,  
	       fileElementId:'indexImage',
	       data:{},
	       dataType: 'text/xml',             
	       success: function (data,status) {  
	    	    var resData = strCommon(data);
				$("#diyIndex").attr("src",path+resData.indexPath);
				$("#uploadIndex").attr("hidden",true);
				$("#diyIndex").attr("hidden",false);
				$("#indexPath").attr("value",resData.indexPath);
				showModal("上传成功!");
	       },
	       error: function (data, status, e){  
	    	    showModal(e);	       
	       }  
	});  
}
//上传logo
function ajax_upload_logo()
{
	var path='<%=path %>';
	//判断是否选择文件
	if($("#logo").val() == "")
	{
		showModal("请选择需要上传的图片！");
		return false;
	}
	//判断文件类型是否符合要求
	if(!checkFileType($("#logo").val(),"jpg,png,jpeg"))
	{
		showModal("请选择正确的类型！");
		return false;
	}
	$.ajaxFileUpload({  
	       url:getUrl("web","webSchool/uploadLogo"),  
	       secureuri:false,  
	       fileElementId:'logo',
	       data:{},
	       dataType: 'text/xml',             
	       success: function (data,status) {  
	    	    var resData = strCommon(data);
				$("#diyLogo").attr("src", path+resData.logoPath);
				$("#uploadLogo").attr("hidden",true);
				$("#diyLogo").attr("hidden",false);
				$("#logoPath").attr("value",resData.logoPath);
				showModal("上传成功!");
	       },
	       error: function (data, status, e){  
	    	    showModal(e);	       
	       }  
	});  
}

function strCommon(obj)
{
	//var res = obj.substr(5,obj.length-11);
	var path1 = obj.split(">{");
	var path2 = path1[1];
	var path3 = path2.split("}<");
	res = path3[0];
	res = res.replace(/\\/g,"/");
	var resJson =  eval('({' + res + '})');
	return resJson;
}

/* function choiceArea(){
	var area = $("#area").val();
	if(area == "0" || area == "1"){
		$("#county").attr("disabled", false);
	}else{
		$("#county").val("0");
		$("#county").attr("disabled", true);
	}
} */

</script>
<jsp:include page="../../../footer.jsp" flush="true" />