<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="../../head.jsp" flush="true" />
<body>
	<div class="container">
		<div class="row">
			<div class="col-md-4 col-md-offset-4">
				<div class="login-panel panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">请注册</h3>
					</div>
					<div class="panel-body">
						<form id="my_form" role="form">
							<fieldset>
								<div class="form-group">
									<input class="form-control" placeholder="用户名(只支持英文(开头)，数字,下划线)" name="id"
										type="text" autofocus>
								</div>
								<div class="form-group">
									<input class="form-control" placeholder="邮箱E-mail" name="email"
										type="email" >
								</div>
								<div class="form-group">
									<input class="form-control" placeholder="密码(长度不能小于4位)"
										name="pwd" type="password" value="">
								</div>
								<div class="form-group">
									<input class="form-control" placeholder="确认密码"
										name="checkpwd" type="password" value="">
								</div>
								<!-- Change this to a button or input when using this as a form -->
								<input type="button" class="btn btn-lg btn-primary btn-block"  value="注册" name="login" onclick="sub_ajax();">
								<input type="button" class="btn btn-lg btn-primary btn-block" value="回到登陆"  id="_wb" onclick="jump_pub(this);">
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
	if($("input[name=id]").val() == "" || $("input[name=id]").val() == null )
	{
		showModal("请输入用户名!");
		return false;
	}
	if(!/^[a-zA-Z]\w{0,31}$/.test($("input[name=id]").val()))
	{
		showModal("用户名格式或是长度不正确!");
		return false;
	}
	if($("input[name=email]").val() == "" || $("input[name=email]").val() == null )
	{
		showModal("请输入邮箱!");
		return false;
	}
	if(!checkmail($("input[name=email]").val()))
	{
		showModal("邮箱格式有误!");
		return false;
	}
	if($("input[name=pwd]").val() == "" || $("input[name=pwd]").val() == null)
	{
		showModal("请输入密码!");
		return false;
	}
	if($("input[name=checkpwd]").val() == "" || $("input[name=checkpwd]").val() == null)
	{
		showModal("请输入确认密码!");
		return false;
	}
	if($("input[name=checkpwd]").val() != $("input[name=pwd]").val())
	{
		showModal("密码不一致!");
		return false;
	}
	if(!/^[a-zA-Z0-9]\w{0,17}$/.test($("input[name=pwd]").val()))
	{
		showModal("密码格式或是长度不正确!");
		return false;
	}
	$.ajax({
		url:getUrl("wb","register/register"),
		type:"get",
		data:{name:$("input[name='id']").val(),pwd:$("input[name='pwd']").val(),checkpwd:$("input[name='checkpwd']").val(),mail:$("input[name='email']").val()},
		dataType:'json',
		success:function(data)
		{
			showModal(data.msg);
			closeModal(getUrl("wb","doIndex")); //关闭悬浮框时再跳转页面
		},
		error:function(data)
		{
			showModal(data.responseText);
			return false;
		}
	});
}
</script>
