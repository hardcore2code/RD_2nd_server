<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="../../head.jsp" flush="true" />
<body>
	<div class="container">
		<div class="row">
			<div class="col-md-4 col-md-offset-4">
				<div class="login-panel panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">请登录</h3>
					</div>
					<div class="panel-body">
						<form id="my_form" role="form">
							<fieldset>
								<div class="form-group">
									<!--input class="form-control" placeholder="E-mail" name="email"
										type="email" autofocus>-->
									<input class="form-control" placeholder="用户名" name="id"
										type="text" autofocus>
								</div>
								<div class="form-group">
									<input class="form-control" placeholder="密码"
										name="pwd" type="password" value="">
								</div>
								<!-- Change this to a button or input when using this as a form -->
								<input type="button" class="btn btn-lg btn-success btn-block"   value="登陆" name="login" onclick="sub_ajax();">
							</fieldset>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- Modal -->
    <jsp:include page="../../modal.jsp" flush="true" />
    <!-- /.modal -->
	<jsp:include page="../../common.jsp" flush="true" />
</body>
<jsp:include page="../../footer.jsp" flush="true" />
<script>
//$.ajaxSetup ({
//    cache: false //设置成false将不会从浏览器缓存读取信息
//});
$(function(){
	$('input:text:first').focus(); //把焦点放在第一个文本框
	var $inp = $('input'); //所有的input元素
	$inp.keypress(function (e) { //这里给function一个事件参数命名为e，叫event也行，随意的，e就是IE窗口发生的事件。
		var key = e.which; //e.which是按键的值
		if (key == 13) {
			sub_ajax();
		}
	});
}); 
function sub()
{
	var frm = $("#my_form");
	frm.submit();
}
function sub_ajax()
{
	if($("input[name=id]").val() == "" || $("input[name=id]").val() == null || $("input[name=pwd]").val() == "" || $("input[name=pwd]").val() == null)
	{
		showModal("请输入完整信息！");
		return false;
	}
	var timestamp = new Date().getTime();
	var basepath = $("#basepath").val();
	var contextpath = $("#contextpath").val();
	$.get(
		contextpath+"/web/login?ts="+timestamp,
		{name:$("input[name='id']").val(),pwd:$("input[name='pwd']").val()},
		function(data)
		{
			//根据错误码判断是否弹出提示框和提示信息
			if(data.errCode == "-1" || data.errCode == "-2")
			{
				showModal(data.msg);
				return false;
			}
			//document.Url = "http://localhost:9001/wb/doIndex";
			//window.location.href = "http://localhost:9001/wb/doIndex";
			window.location.href = contextpath+"/web/doIndex?ts="+timestamp;
		}
	);
}

function back_home()
{
	var timestamp = new Date().getTime();
	var basepath = $("#basepath").val();
	var contextpath = $("#contextpath").val();
	window.location.href = contextpath+"/?ts="+timestamp;
}
</script>
