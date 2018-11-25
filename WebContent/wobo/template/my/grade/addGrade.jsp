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
                    <h2 class="page-header">添加年级</h2>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <!-- <div class="panel-heading">
                        </div> -->
                        <div class="panel-body">
                            <div class="row">
                                <!-- /.col-lg-6 (nested) -->
                                <div class="col-lg-6">
                                	<br/>
                                	<br/>
                                    <form role="form" name="func" id="myForm">
                                        <div class="form-group">
                                            <label>年级名称：</label>
                                            <input class="form-control" placeholder="必填" id="grade_name" name="grade_name" value="${grade_name}">
                                        </div>
                                         <div class="form-group">
                                            <label>对应年级：</label>
	                                        <select class="form-control" id="gradeId" name="gradeId">
	                                        	<option value="0" selected>请选择对应年级</option>
	                                        	<option value="1">一年级</option>
	                                        	<option value="2">二年级</option>
	                                        	<option value="3">三年级</option>
	                                        	<option value="4">四年级</option>
	                                        	<option value="5">五年级</option>
	                                        	<option value="6">六年级</option>
	                                        	<option value="7">初一</option>
	                                        	<option value="8">初二</option>
	                                        	<option value="9">初三</option>
	                                        	<option value="10">高一</option>
	                                        	<option value="11">高二</option>
	                                        	<option value="12">高三</option>
	                                        </select>
                                         </div>
                                        <button type="button" class="btn btn-success" onclick="ajax_submit();">保存</button>
                                        <button type="button" class="btn btn-success" id="_grade_gradeManage" onclick="jump_pub(this)">返回</button>
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
	if($("#grade_name").val() == "" || $("#grade_name").val() == null )
	{
		showModal("请填写年级!");
		return false;
	}
	if($("#gradeId option:selected").val() == "" || $("#gradeId option:selected").val() == null || $("#gradeId option:selected").val() == 0)
	{
		showModal("请选择对应年级!");
		return false;
	}
	var url = getUrl("web","grade/saveGrade");
	$.ajax({
		url:url,
		type:"post",
		data:$('#myForm').serialize(),
		dataType:'json',
		success:function(data)
		{
			showModal(data.msg);
			if(data.resFlag == 0){
				closeModal(getUrl("web","grade/gradeManage")); //关闭悬浮框时再跳转页面
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