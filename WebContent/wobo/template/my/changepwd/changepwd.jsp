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
                    <h2 class="page-header">修改密码</h2>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <%-- <div class="panel-heading">
                                                                用户名称：${NAME}
                        </div> --%>
                        <div class="panel-body">
                            <div class="row">
                                <!-- /.col-lg-6 (nested) -->
                                <div class="col-lg-6">
                                	<br/>
                                	<br/>
                                    <form role="form" name="func" id="myForm">
                                        <div class="form-group">
                                            <label>原密码</label>
                                            <input class="form-control" type="password" name="oldPwd" placeholder="必填" id="pwd" >
                                        </div>
                                        <div class="form-group">
                                            <label>新密码</label>
                                            <input class="form-control" type="password" name="pwd" placeholder="必须由6-18位字母、数字或下划线组成" id="newpwd">
                                        </div>
                                        <div class="form-group">
                                            <label>确认新密码</label>
                                            <input class="form-control" type="password" name="rePwd" placeholder="两次输入密码请保持一致" id="repeatnewpwd">
                                        </div>
                                        <button type="button" class="btn btn-success" onclick="ajax_submit();">保 存</button>
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
function ajax_submit()
{
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
	if(!/^[a-zA-Z0-9]\w{5,17}$/.test($("#newpwd").val()))
	{
		showModal("密码格式或是长度不正确!");
		return false;
	}
	var url = getUrl("web","changepwd/savePwd");
	$.ajax({
		url:url,
		type:"post",
		data:$('#myForm').serialize(),
		dataType:'json',
		success:function(data)
		{
			showModal(data.msg);
			if(data.resFlag == 0){
				closeModal(getUrl("web","changepwd/changePwd")); //关闭悬浮框时再跳转页面
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
</script>