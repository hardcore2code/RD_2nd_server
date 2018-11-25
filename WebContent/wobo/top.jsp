<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Navigation -->
<nav class="navbar navbar-default navbar-static-top" role="navigation"
	style="margin-bottom: 0">
	<div class="navbar-header">
		<button type="button" class="navbar-toggle" data-toggle="collapse"
			data-target=".navbar-collapse">
			<span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span>
			<span class="icon-bar"></span> <span class="icon-bar"></span>
		</button>
		<a class="navbar-brand" href="#" onclick="jump_pub(this)">后台管理</a>
	</div>
	<jsp:include page="top_header.jsp" flush="true"/>
	<jsp:include page="top_sidebar.jsp" flush="true"/>
</nav>
<script>
//$.ajaxSetup ({
//    cache: false //设置成false将不会从浏览器缓存读取信息
//});
function logout()
{
	var timestamp = new Date().getTime();
	var basepath = $("#basepath").val();
	window.location.href = basepath+"/web/logout?ts="+timestamp;
	//$.get("http://localhost:9001/wb/logout?ts="+timestamp,"",function(){});
}
</script>