<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%  
String path = request.getContextPath();  
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;  
//System.out.println("basePath===>>"+basePath);
%>
<jsp:include page="../../head.jsp" flush="true" />
<body>
	<div id="wrapper">
		<jsp:include page="../../top.jsp" flush="true" />
		<!-- Page Content -->
		<div id="page-wrapper">
			<div class="container-fluid">
				<div class="row">
					<div class="col-lg-12">
						<h1 class="page-header">我的文章</h1>
					</div>
					<!-- /.col-lg-12 -->
				</div>
				<!-- /.row -->
			    <div class="col-lg-12">
                  	<div class="panel panel-default">
                        <div class="panel-heading">
                        </div>
                        <!-- /.panel-heading -->
                        <div class="panel-body">
                            <form role="form" name="blog" enctype="multipart/form-data">
                            	<input type="hidden" id="blogId" value="${blogId}">
                            	<div class="form-group">
                                    <label>标题</label>
                                    <input class="form-control" placeholder="必填" id="titleId" name="title" value="${title}">
                                </div>
                            	<div class="form-group">
                                    <label>便利贴标题</label>
                                    <input class="form-control" placeholder="可选" id="noteId" name="note" value="${noteName}">
                                </div>
                                <br/>
							 	<jsp:include page="../../woboEditor.jsp" flush="true" />
							 	<br/>
							 	<button type="button" class="btn btn-primary" id="btn" onclick="saveBlog()">保存</button>
							 	<button type="button" class="btn btn-default" id="_blog_list" onclick="jump_pub(this)">返回列表</button>
						    </form>
                        </div>
                     		<!-- /.panel-body -->
                   	</div>
                   <!-- /.panel -->
              	</div>
			</div>
			<!-- /.container-fluid -->
		</div>
		<!-- /#page-wrapper -->
	</div>
	<!-- /#wrapper -->
	<jsp:include page="../../modal.jsp" flush="true" />
	<jsp:include page="../../modalForPic.jsp" flush="true" />
	<jsp:include page="../../common.jsp" flush="true" />
	<script src="<%=request.getContextPath() %>/wobo/js/plugins/wysiwyg/bootstrap-wysiwyg.js"></script>
	<script src="<%=request.getContextPath() %>/wobo/js/plugins/wysiwyg/external/jquery.hotkeys.js"></script>
	<script src="<%=request.getContextPath() %>/wobo/js/woboEditor.js"></script>
	<script src="<%=request.getContextPath() %>/wobo/js/ajaxfileupload.js"></script>
</body>
</html>
<script>
var text = '${text}';
if(text != "")
{
	$('#woboEditor').html(text);
}
//保存或更新
function saveBlog()
{
	if($("#titleId").val() == "")
	{
		showModal("请填写标题!");
		return false;
	}
	if($("#titleId").val().length > 50)
	{
		showModal("标题长度过长!");
		return false;
	}
	if($("#noteId").val().length !=0 && $("#noteId").val().length > 30)
	{
		showModal("便利贴标题长度过长!");
		return false;
	}
	var url = "";
	if($("#blogId").val() == "")
	{
		url = getUrl("wb","blog/addBlog");
	}
	else
	{
		url = getUrl("wb","blog/editBlog");
	}
	$.ajax({
		url:url, 
		type:"post",
		data:{blogId:$("#blogId").val(),title:$("#titleId").val(),content:$("#woboEditor").html(),desc:$("#woboEditor").text(),noteName:$("#noteId").val()},
		dataType:'json',
		success:function(data)
		{
			showModal(data.msg);
			$("#blogId").val(data.blogId);
			return false;
		},
		error:function(data)
		{
			showModal(data.responseText);
			return false;
		}
	});
}

function uploadPic()
{
	$("#picModal").modal({
		keyboard:true,
		show:true,
		backdrop:false
	});
}
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
	var path='<%=path %>';
	$.ajaxFileUpload({  
	       url:getUrl("wb","blog/upload"),  
	       secureuri:false,  
	       fileElementId:'fileupload',
	       data:{},
	       dataType: 'text/xml',             
	       success: function (data,status) {  
				$("#diy").attr("src",path+Request(data,'imgtoPath'));
				$("#diy").attr("hidden",false);
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
function Request(url,paraName)
{
	var strHref = url;
	var intPos = strHref.indexOf("?");
	var strRight = strHref.substr(intPos + 1);
	var arrTmp = strRight.split("@");
	for(var i = 0; i < arrTmp.length; i++)
	{
		var arrTemp = arrTmp[i].split("=");
		if(arrTemp[0].toUpperCase() == paraName.toUpperCase()) return arrTemp[1];
	}
	return "";
}
$('#picModal').on('hide.bs.modal', function()
{
	if($("#diy").attr("src") != "")
	{
		$('#fileupload').val("");
		$("#diy").attr("hidden",true);
		$('#imgUploadId').attr("disabled","disabled");
		$('#woboEditor').append("<img width='60%' src='"+$("#diy").attr("src")+"'>");
		$("#diy").attr("src","");
	}
});
</script>
