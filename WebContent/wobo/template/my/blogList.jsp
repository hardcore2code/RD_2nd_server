<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link href="<%=request.getContextPath() %>/wobo/css/plugins/dataTables.bootstrap.css" rel="stylesheet">
<link href="<%=request.getContextPath() %>>/wobo/css/editor.css" rel="stylesheet">
<jsp:include page="../../head.jsp" flush="true" />
<body>
	<div id="wrapper">
		<jsp:include page="../../top.jsp" flush="true" />
		<!-- Page Content -->
		<div id="page-wrapper">
			<div class="container-fluid">
				<div class="row">
					<div class="col-lg-12">
						<h1 class="page-header">我的专栏</h1>
					</div>
					<!-- /.col-lg-12 -->
				</div>
				<!-- /.row -->
	          	<div class="row">
	              	<div class="col-lg-12">
	                  	<div class="panel panel-default">
	                        <div class="panel-heading">
	                                                                专栏列表                                      
	                        </div>
	                        <!-- /.panel-heading -->
	                        <div class="panel-body">
	                        	<a href="#" onclick="jump_pub(this)" id="_blog_blog" class="btn btn-primary" data-toggle="modal">新增</a>
	                            <br/>
	                            <br/>
	                            <div class="table-responsive">
	                                <table class="table table-striped table-bordered table-hover" id="blogtable">
	                                    <thead>
	                                        <tr>
	                                            <th>ID</th>
	                                            <th>标题</th>
	                                            <th width="8%">状态</th>
	                                            <th width="10%">访问量</th>
	                                            <th width="16%">最新更新时间</th>
	                                            <th width="16%">发布时间</th>
	                                            <th width="11%">操作</th>
	                                        </tr>
	                                    </thead>
	                                </table>
	                            </div>
	                        </div>
                      		<!-- /.panel-body -->
                    	</div>
                    <!-- /.panel -->
	              	</div>
	              	<!-- /.col-lg-12 -->
		          </div>
		          <!-- /.row -->
				<!-- /.row -->
			</div>
			<!-- /.container-fluid -->
		</div>
		<!-- /#page-wrapper -->
	</div>
	<!-- /#wrapper -->
	<!-- Modal -->
	<div class="modal fade" id="blogModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	    <div class="modal-dialog">
	        <div class="modal-content">
	            <div class="modal-header">
	                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	                <h4 class="modal-title" id="blogModalLabel">提示</h4>
	            </div>
	            <input type="hidden" id="blogIdModal">
	            <input type="hidden" id="publishDateModal">
	            <input type="hidden" id="statusNameModal">
	            <div class="modal-body" id="blog_val">
	            </div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-primary" data-dismiss="modal">关闭</button>
	            </div>
	        </div>
	        <!-- /.modal-content -->
	    </div>
	    <!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->
	<jsp:include page="../../modal.jsp" flush="true" />
	<jsp:include page="../../modalForDT.jsp" flush="true" />
	<jsp:include page="../../common.jsp" flush="true" />
	<!-- DataTables JavaScript -->
	<script src="<%=request.getContextPath() %>/wobo/js/plugins/dataTables/jquery.dataTables.js"></script>
	<script src="<%=request.getContextPath() %>/wobo/js/plugins/dataTables/dataTables.bootstrap.js"></script>
	<!-- Page-Level Demo Scripts - Tables - Use for reference -->
</body>
</html>
<script>
//首次跳转到页面获取后台传来的列表信息
var dataSet = ${blogList};
// 默认开启搜索和排序
$.extend( $.fn.dataTable.defaults, {
    searching: true,
    ordering:  true
} );
var table = "";
var deltable = "";
$(document).ready(function() {
	table = $('#blogtable').DataTable({
    	"data":dataSet,
    	"columns":[
            	   {
           		   "data":"BLOG_ID"
            	   },
            	   {	
           			"data":"TITLE"
           		   },
            	   {	
           			"data":"STATUSNAME"
           		   },
            	   {	
           			"data":"COUNT_VIEW"
           		   },
            	   {	
           			"data":"SAVEDATE"
           		   },
            	   {	
           			"data":"PUBLISHDATE"
           		   },
           		   {
           		   	"class":"mytable-center",
           		   	"targets":-1,
        	    	"data":null,
        	    	"defaultContent":"<div class='btn-group-vertical'><button type='button' class='btn btn-primary btn-sm dropdown-toggle' data-toggle='dropdown'>操作<span class='caret'></span></button><ul class='dropdown-menu'><li><a href='#' id='a_edit'>编辑</a></li><li><a href='#' id='a_pub'>发布</a></li><li><a href='#' id='a_cancel'>撤销</a></li><li><a href='#' id='a_del'>删除</a></li></ul></div>"
           		   }
               	  ],
     	"order": [[ 3, "desc" ]],
	    "language": {
	        "lengthMenu": "每页 _MENU_ 条记录 ",
	        "zeroRecords": "没有找到记录",
	        "info": "第 _PAGE_ 页 ( 总共 _PAGES_ 页,共_TOTAL_条 )",
	        "infoEmpty": "无记录",
	        "search": "搜索 : ",
	        "paginate": {
	            "first":      "首页",
	            "last":       "尾页",
	            "next":       "下一页",
	            "previous":   "前一页"
	        },
	        "infoFiltered": "(从 _MAX_ 条记录过滤)"
	    },
	    "pagingType":   "full_numbers"
    });
	//编辑文章
    $('#blogtable tbody').on( 'click', '#a_edit', function () {
    	var row = table.row($(this).parents('tr'));
        var data = row.data();
        var paras = {"blogId":data.BLOG_ID ,"path":"_blog_blog"};
        jumpEditBlog(paras);
    } );
  	//删除文章
    $('#blogtable tbody').on( 'click', '#a_del', function () {
    	var row = table.row($(this).parents('tr'));
        var data = row.data();
        $.ajax({
    		url:getUrl("wb","blog/delBlog"),
    		type:"get",
    		data:{blogId:data.BLOG_ID},
    		dataType:'json',
    		success:function(obj)
    		{
    			showModalDT(obj.msg);
    			deltable = table;
    			if(obj.resFlag == '0')
    			{
    				$("#delId").val(data.BLOG_ID);
    			}
    		},
    		error:function(obj)
    		{
    			showModal(obj.responseText);
    			return false;
    		}
    	});
    });
  	//发布文章
    $('#blogtable tbody').on( 'click', '#a_pub', function () {
    	var row = table.row($(this).parents('tr'));
        var data = row.data();
        var this_tr = $(this).parents('tr');
        if(data.STATUSNAME == "发布")
        {
        	showModal("该文章已发布!");
        	return false;
        }
        $.ajax({
    		url:getUrl("wb","blog/publishBlog"),
    		type:"get",
    		data:{blogId:data.BLOG_ID},
    		dataType:'json',
    		success:function(obj)
    		{
    			showModalBlog(obj.msg);
    			if(obj.resFlag == '0')
    			{
    				$("#blogIdModal").val(data.BLOG_ID);
    				$("#publishDateModal").val(obj.publishDate);
    				$("#statusNameModal").val("发布");
    			}
    		},
    		error:function(obj)
    		{
    			showModal(obj.responseText);
    			return false;
    		}
    	});
    });
  	//撤销文章
    $('#blogtable tbody').on( 'click', '#a_cancel', function () {
    	var row = table.row($(this).parents('tr'));
        var data = row.data();
        var this_tr = $(this).parents('tr');
        if(data.STATUSNAME == "保存")
        {
        	showModal("该文章无需撤销!");
        	return false;
        }
        $.ajax({
    		url:getUrl("wb","blog/cancelBlog"),
    		type:"get",
    		data:{blogId:data.BLOG_ID},
    		dataType:'json',
    		success:function(obj)
    		{
    			showModalBlog(obj.msg);
    			if(obj.resFlag == '0')
    			{
    				$("#blogIdModal").val(data.BLOG_ID);
    				$("#publishDateModal").val(data.PUBLISHDATE);
    				$("#statusNameModal").val("保存");
    			}
    		},
    		error:function(obj)
    		{
    			showModal(obj.responseText);
    			return false;
    		}
    	});
    });
});
//跳转到编辑页面
function jumpEditBlog(obj)
{
 	var timestamp = new Date().getTime();
	var basepath = $("#basepath").val();
	var path = (obj.path).replace(/_/g,"/");
	window.location.href = basepath+"/wb"+path+"?activeId="+obj.path+"&blogId="+obj.blogId+"&ts="+timestamp;
}
function showModalBlog(obj)
{
	$("#blogModal").modal({
		keyboard:true,
		show:true
	});
	$("#blog_val").html(obj);
}
$('#blogModal').on('hide.bs.modal', function()
{
	var ids = table.column(0).data().reduce( function (a,b) {return a+","+b;}).split(",");
	for(var i = 0 ; i < ids.length ; i++)
	{
		if(ids[i] == $('#blogIdModal').val())
		{
			table.row(i).data().STATUSNAME = $("#statusNameModal").val();
			table.row(i).column(2).nodes().to$().eq(i).html($("#statusNameModal").val());
			table.row(i).data().PUBLISHDATE = $("#publishDateModal").val();
			table.row(i).column(5).nodes().to$().eq(i).html($("#publishDateModal").val());
		}
	}
	$('#blogIdModal').val("");
	$('#publishDateModal').val("");
	$('#statusNameModal').val("");
});
</script>