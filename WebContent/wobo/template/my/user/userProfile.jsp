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
                    <h1 class="page-header">用户信息</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                                                                用户名称：${NAME}
                        </div>
                        <div class="panel-body">
                            <div class="row">
                            	<!-- /.col-lg-6 (nested) -->
                                <div class="col-lg-6">
                                    <h3>头像</h3>
                                    <form role="form" name="funcAttr" id="myForm2">
                                    	<div class="form-group" align="left">
                                            <img id="defaultMale" src="<%=path %>/upload/default/male.jpg" width="50%" >
                                            <img id="defaultFemale" src="<%=path %>/upload/default/female.jpg" width="50%" hidden="true">
                                            <img id="diy" src="<%=path %>${diyImg }" width="80%" hidden="true">
                                        </div>
                                        <input type="file" id="fileupload" name="fileuploaddata" onchange="$('#imgUploadId').attr('disabled',false);" size="1" ><button type="button" class="btn btn-default" >选择文件</button>
                                        <button type="button" class="btn btn-primary" id="imgUploadId" onclick="ajax_upload();" disabled="disabled">上传</button>
                                    	<div id="filepath"></div>
                                    </form>
                                </div>
                                <!-- /.col-lg-6 (nested) -->
                                <div class="col-lg-6">
                                	<br/>
                                	<br/>
                                    <form role="form" name="func" id="myForm">
                                    	<input type="hidden" name="GENDER" id="genderId" value="${gender }">
                                        <div class="form-group">
                                            <label>昵称</label>
                                            <input class="form-control" placeholder="必填" id="nickname" name="NICK_NAME" value="${NICK_NAME}">
                                        </div>
                                        <div class="form-group">
                                            <label>电话</label>
                                            <input class="form-control" placeholder="" id="tel" name="TEL" value="${TEL}">
                                        </div>
                                        <div class="form-group">
                                            <label>邮件</label>
                                            <input class="form-control" placeholder="必填" id="mail" name="MAIL" value="${MAIL}" type="email">
                                        </div>
                                        <label for="disabledSelect">性别</label>
                                        <label class="radio-inline">
                                            <input type="radio" name="optionsRadiosInline" id="optionsRadiosInline1" value="1" onclick="radioClick(this);" <c:if test="${gender eq '1'}">checked</c:if>>男
                                        </label>
                                         <label class="radio-inline">
                                            <input type="radio" name="optionsRadiosInline" id="optionsRadiosInline2" value="2" onclick="radioClick(this);" <c:if test="${gender eq '2'}">checked</c:if>>女
                                        </label>
                                        <h3>修改密码</h3>
                                        <div class="form-group">
                                            <label>原密码</label>
                                            <input class="form-control" type="password" name="OLDPWD" placeholder="必填" id="pwd" >
                                        </div>
                                        <div class="form-group">
                                            <label>新密码</label>
                                            <input class="form-control" type="password" name="NEWPWD" placeholder="必填" id="newpwd">
                                        </div>
                                        <div class="form-group">
                                            <label>确认新密码</label>
                                            <input class="form-control" type="password" name="REPEATNEWPWD" placeholder="必填" id="repeatnewpwd">
                                        </div>
                                        <button type="button" class="btn btn-primary" onclick="ajax_submit();">保 存</button>
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
$(document).ready(function() {
	if($("#diy").attr("src") != "")
	{
		$("#defaultMale").attr("hidden",true);
		$("#defaultFemale").attr("hidden",true);
		$("#diy").attr("hidden",false);
	}else{
		if($("#optionsRadiosInline2").prop("checked") == true)
		{
			$("#defaultMale").attr("hidden",true);
			$("#defaultFemale").attr("hidden",false);
			$("#diy").attr("hidden",true);
		}else{
			$("#defaultMale").attr("hidden",false);
			$("#defaultFemale").attr("hidden",true);
			$("#diy").attr("hidden",true);
		}
	}
});
function radioClick(obj)
{
	if($("#diy").attr("src") == "")
	{
		if(obj.value=="1")
		{
			$("#defaultMale").attr("hidden",false);
			$("#defaultFemale").attr("hidden",true);
			$("#diy").attr("hidden",true);
		}else if(obj.value=="2")
		{
			$("#defaultMale").attr("hidden",true);
			$("#defaultFemale").attr("hidden",false);
			$("#diy").attr("hidden",true);
		}
	}
	$("#genderId").val(obj.value);
}
function ajax_submit()
{
	if($("#nickname").val() == "")
	{
		showModal("请填写昵称!");
		return false;
	}
	if($("#mail").val() == "" || $("#mail").val() == null )
	{
		showModal("请输入邮箱!");
		return false;
	}
	if(!checkmail($("#mail").val()))
	{
		showModal("邮箱格式有误!");
		return false;
	}
	if(!($("#pwd").val() == "" && $("#newpwd").val() == "" && $("#repeatnewpwd").val() == ""))
	{
		if($("#pwd").val() == "" || $("#newpwd").val() == "" || $("#repeatnewpwd").val() == "")
		{
			showModal("密码信息不全!");
			return false;
		}
	}
	if($("#newpwd").val() != $("#repeatnewpwd").val())
	{
		showModal("两次密码不一致!");
		return false;
	}
	var url = getUrl("wb","user/saveUser");
	$.ajax({
		url:url,
		type:"post",
		data:$('#myForm').serialize(),
		dataType:'json',
		success:function(data)
		{
			showModal(data.msg);
			return false;
		},
		error:function(data)
		{
			showModal(data.responseText);
			return false;
		}
	});
}
//上传头像
function ajax_upload()
{
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
	       url:getUrl("wb","user/upload"),  
	       secureuri:false,  
	       fileElementId:'fileupload',
	       data:{},
	       dataType: 'text/xml',             
	       success: function (data,status) {  
				$("#diy").attr("src",strCommon(data));
				$("#defaultMale").attr("hidden",true);
				$("#defaultFemale").attr("hidden",true);
				$("#diy").attr("hidden",false);
				showModal("上传成功!");
	       },
	       error: function (data, status, e){  
	    	    showModal(e);	       
	       }  
	});  
}
function strCommon(obj)
{
	var res = obj.substr(5,obj.length-11);
	res = res.replace(/\\/g,"/");
	return res;
}
</script>