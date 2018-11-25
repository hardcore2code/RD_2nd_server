<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- /.navbar-header -->
<ul class="nav navbar-top-links navbar-right">
	<!-- /.dropdown -->
	<li class="dropdown"><a class="dropdown-toggle"
		data-toggle="dropdown" href="#"> <i class="fa fa-user fa-fw"></i>
			<i class="fa fa-caret-down"></i>
	</a>
		<ul class="dropdown-menu dropdown-user">
			<%-- <li><a href="${ctx}/web/info/"><i class="fa fa-user fa-fw"></i> 个人信息</a></li> --%>
			<li><a href="#" id="_info_info" onclick="jump_pub(this);"><i class="fa fa-user fa-fw"></i> 个人信息</a></li>
			<li class="divider"></li>
			<li><a href="#" onclick="logout();"><i class="fa fa-sign-out fa-fw"></i> 登出</a></li>
		</ul> <!-- /.dropdown-user --></li>
	<!-- /.dropdown -->
</ul>
