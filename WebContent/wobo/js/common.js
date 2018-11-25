$(function(){
	activeMenu();
});
//bootstrap的alert信息 by woody 2015-3-12
function showModal(obj)
{
	$("#myModal").modal({
		keyboard:true,
		show:true
	});
	$("#modal_val").html(obj);
}
function showWbModal(param1,param2,content)
{
	$(param1).modal({
		keyboard:true,
		show:true
	});
	$(param2).html(content);
}
function showModalDT(obj)
{
	$("#dtModal").modal({
		keyboard:true,
		show:true
	});
	$("#dtmodal_val").html(obj);
}
$('#dtModal').on('hide.bs.modal', function()
{
	var ids = deltable.column(0).data().reduce( function (a,b) {return a+","+b;}).split(",");
	for(var i = 0 ; i < ids.length ; i++)
	{
		if(ids[i] == $("#delId").val())
		{
			deltable.row(i).remove().draw(false);
		}
	}
	deltable = "";
});
function showModalOp(obj)
{
	$("#opModal").modal({
		keyboard:true,
		show:true
	});
	$("#opmodal_val").html(obj);
}
$('#opModal').on('hide.bs.modal', function()
{
	var ids = deltable.column(0).data().reduce( function (a,b) {return a+","+b;}).split(",");
	for(var i = 0 ; i < ids.length ; i++)
	{
		if(ids[i] == $("#delId").val())
		{
			deltable.row(i).remove().draw(false);
		}
	}
	deltable = "";
});
function showModalpub(obj)
{
	$(obj).modal({
		keyboard:true,
		show:true,
		backdrop:false
	});
}
//端关闭showModal时触发事件
function closeModal(obj)
{
	$('#myModal').on('hide.bs.modal', function()
	{
		window.location.href = obj;
	});
}
//判断文件类型 by woody 2015-3-12
function checkFileType(filepath,filetype)
{
	var ext = (filepath.substring(filepath.lastIndexOf(".")+1,filepath.length)).toLowerCase();
	var types = filetype.split(",");
	for(var i=0;i<types.length;i++)
	{
		if(ext == types[i])
		{
			return true;
		}
	}
	return false;
}

function jump_pub(obj)
{
 	var timestamp = new Date().getTime();
	var basepath = $("#basepath").val();
	var contextpath = $("#contextpath").val();
	if(obj.id == "_webhome")
	{
		window.open(basepath+"?ts="+timestamp);
	}
	else if(obj.id == "goIndex")
	{
		window.location.href = contextpath+"?ts="+timestamp;
	}
	else
	{
		var path = (obj.id).replace(/_/g,"/");
		window.location.href = contextpath+"/web"+path+"?activeId="+obj.id+"&ts="+timestamp;
	}
}
function getUrl(baseUrl,toUrl)
{
	var timestamp = new Date().getTime();
	var basepath = $("#basepath").val();
	var contextpath = $("#contextpath").val();
	var path = contextpath+"/"+baseUrl+"/"+toUrl+"?ts="+timestamp;
	return path;
}
function getUrlByPara(baseUrl,toUrl,param)
{
	var timestamp = new Date().getTime();
	var basepath = $("#basepath").val();
	var contextpath = $("#contextpath").val();
	var path = contextpath+baseUrl+"/"+toUrl+"?"+param+"&ts="+timestamp;
	return path;
}
//激活左侧菜单栏
function activeMenu()
{
	if($("#activeId").val() != "" && $("#activeId").val() != null)
	{
		$("#"+$("#activeId").val()).attr("class","active");
		if($("#"+$("#activeId").val()).parent().parent().attr("class").length > 3)  //判断是否是二级菜单，如果是一级菜单则class的值为nav 长度3
		{
			$("#"+$("#activeId").val()).parent().parent().parent().attr("class","active");
			$("#"+$("#activeId").val()).parent().parent().attr("class","nav nav-second-level collapse in");
			$("#"+$("#activeId").val()).parent().parent().attr("aria-expanded","true");
		}
	}
}
//校验邮箱格式
function checkmail(mailparam)
{
	var myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
	if(!myreg.test(mailparam))
	{
		return false;
	}else
	{
		return true;
	}
}
//去除所有空格
function trimAll(obj)
{
	return obj.replace(/\s+/g,"");
}
//去除两头空格
function trimBoth(obj)
{
	return obj.replace(/^\s+|\s+$/g,"");
}
//去掉左空格
function trimL(obj)
{
	return obj.replace(/(^\s*)/g, "");
}
//去掉右空格
function trimR(obj)
{
	return obj.replace(/(\s*$)/g, "");
}
