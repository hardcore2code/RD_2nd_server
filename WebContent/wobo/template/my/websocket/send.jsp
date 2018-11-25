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
                    <h1 class="page-header">发送通知消息</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                        </div>
                        <div class="panel-body">
                            <div class="row">
                                <!-- /.col-lg-6 (nested) -->
                                <div class="col-lg-6">
                                    <form role="form" name="func" id="myForm">
                                    	<input type="hidden" name="GENDER" id="genderId" value="${gender }">
                                        <div class="form-group">
                                            <input class="form-control" placeholder="id" id="msgId" name="msgId" >
                                        </div>
                                        <div class="form-group">
                                            <input class="form-control" placeholder="标题" id="title" name="title" >
                                        </div>
                                        <div class="form-group">
                                            <input class="form-control" placeholder="内容" id="content" name="content" >
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
function ajax_submit()
{
	if($("#title").val() == "")
	{
		showModal("请填写昵称!");
		return false;
	}
	if($("#content").val() == "" )
	{
		showModal("请输入邮箱!");
		return false;
	}
	var url = getUrl("wb","ws/send");
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
</script>