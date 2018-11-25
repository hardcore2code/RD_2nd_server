<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link href="<%=request.getContextPath() %>/wobo/css/plugins/dataTables.bootstrap.css" rel="stylesheet">
<link href="<%=request.getContextPath() %>>/wobo/css/editor.css" rel="stylesheet">
<jsp:include page="../../../head.jsp" flush="true" />
<body>
	<div id="wrapper">
		<jsp:include page="../../../top.jsp" flush="true" />
		<!-- Page Content -->
		<div id="page-wrapper">
			<div class="container-fluid">
				<div class="row">
					<div class="col-lg-12">
						<h2 class="page-header">年级管理</h2>
					</div>
					<!-- /.col-lg-12 -->
				</div>
				<!-- /.row -->
	          	<div class="row">
	              	<div class="col-lg-12">
	                  	<div class="panel panel-default">
	                        <div class="panel-heading">
	                                                                年级管理列表                                      
	                        </div>
	                        <!-- /.panel-heading -->
	                        <div class="panel-body">
	                        	<a href="#" onclick="jump_pub(this)" id="_grade_addGrade" class="btn btn-success" data-toggle="modal">新增</a>
	                            <br/>
	                            <br/>
	                            <div class="table-responsive">
	                                <table class="table table-striped table-bordered table-hover" id="blogtable">
	                                    <thead>
	                                        <tr>
	                                        	<th>年级id</th>
	                                            <th>年级名称</th>
	                                            <th>sysdate</th>
	                                            <th width="15%">操作</th>
	                                            <!-- <th width="16%">最新更新时间</th>
	                                            <th width="16%">发布时间</th>
	                                            <th width="11%">操作</th> -->
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
	                <button type="button" class="btn btn-success" data-dismiss="modal">关闭</button>
	            </div>
	        </div>
	        <!-- /.modal-content -->
	    </div>
	    <!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->
	<jsp:include page="../../../modal.jsp" flush="true" />
	<jsp:include page="../../../modalForDT.jsp" flush="true" />
	<jsp:include page="../../../common.jsp" flush="true" />
	<!-- DataTables JavaScript -->
	<script src="<%=request.getContextPath() %>/wobo/js/plugins/dataTables/jquery.dataTables.js"></script>
	<script src="<%=request.getContextPath() %>/wobo/js/plugins/dataTables/dataTables.bootstrap.js"></script>
	<!-- Page-Level Demo Scripts - Tables - Use for reference -->
</body>
<jsp:include page="../../../footer.jsp" flush="true" />
<script>
//首次跳转到页面获取后台传来的列表信息
var dataSet = ${dataList};
// 默认开启搜索和排序
$.extend( $.fn.dataTable.defaults, {
    searching: true,
    ordering:  true
});
var table = "";
var deltable = "";
$(document).ready(function() {
	table = $('#blogtable').DataTable({
    	"data":dataSet,
    	"columns":[
				   {
				   "visible":false,
	   			   "data":"grade_id"
	   			   }, 
            	   {
           		   "data":"grade_name"
            	   },
            	   {
            		"visible":false,
           			"data":"sysdate"
           		   },
           		   {
           		   	"class":"mytable-center",
           		   	"targets":-1,
        	    	"data":null,
        	    	"fnCreatedCell":function(nTd,sData,oData,iRow, iCol){
       	    			if(oData.status == '1'){
       	    				console.log("oData.status====================>>"+oData.status);
       	    				$(nTd).html("<div class='btn-group-vertical'><button type='button' class='btn btn-success btn-sm dropdown-toggle' data-toggle='dropdown'>操作<span class='caret'></span></button><ul class='dropdown-menu'><li><a href='#' id='a_delete'>删除</a></li><li><a href='#' id='a_update'>修改</a></li></ul></div>");
       	    			}else{
       	    				console.log("oData.status====================>>"+oData.status);
       	    				$(nTd).html("<div class='btn-group-vertical'><button type='button' class='btn btn-success btn-sm dropdown-toggle' data-toggle='dropdown'>操作<span class='caret'></span></button><ul class='dropdown-menu'><li><a href='#' id='a_reset'>重置</a></li><li><a href='#' id='a_update'>修改</a></li></ul></div>");
       	    			}
	   	    		 }
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
  	//删除
    $('#blogtable tbody').on( 'click', '#a_delete', function () {
    	var row = table.row($(this).parents('tr'));
        var data = row.data();
        $.ajax({
    		url:getUrl("web","grade/deleteGrade"),
    		type:"get",
    		data:{grade_id:data.grade_id},
    		dataType:'json',
    		success:function(obj)
    		{
    			showModalDT(obj.msg);
    			deltable = table;
    			if(obj.resFlag == '0')
    			{
    				$("#delId").val(data.grade_id);
    			}
    		},
    		error:function(obj)
    		{
    			showModal(obj.responseText);
    			return false;
    		}
    	});
    });
  //重置
    $('#blogtable tbody').on( 'click', '#a_reset', function () {
    	var row = table.row($(this).parents('tr'));
        var data = row.data();
        $.ajax({
    		url:getUrl("web","grade/resetGrade"),
    		type:"get",
    		data:{grade_id:data.grade_id},
    		dataType:'json',
    		success:function(obj)
    		{
    			showModalDT(obj.msg);
    			deltable = table;
    			if(obj.resFlag == '0')
    			{
    				$("#resetId").val(data.grade_id);
    			}
    		},
    		error:function(obj)
    		{
    			showModal(obj.responseText);
    			return false;
    		}
    	});
    });
  	//修改
    $('#blogtable tbody').on( 'click', '#a_update', function () {
    	var row = table.row($(this).parents('tr'));
        var data = row.data();
        var paras = {grade_id:data.grade_id , "path":"_grade_update"};
        jumpEditBlog(paras);
    } ); 
//跳转到编辑页面
function jumpEditBlog(obj)
{
 	var timestamp = new Date().getTime();
	var basepath = $("#basepath").val();
	var path = (obj.path).replace(/_/g,"/");
	window.location.href = basepath+"/web"+path+"?activeId="+obj.path+"&grade_id="+obj.grade_id+"&ts="+timestamp;
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
});
</script>