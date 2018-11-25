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
						<h2 class="page-header">运维用户管理</h2>
					</div>
					<!-- /.col-lg-12 -->
				</div>
				<!-- /.row -->
	          	<div class="row">
	              	<div class="col-lg-12">
	                  	<div class="panel panel-default">
	                        <div class="panel-heading">
	                                                                运维用户管理列表                                      
	                        </div>
	                        <!-- /.panel-heading -->
	                        <div class="panel-body">
	                        	<a href="#" onclick="jump_pub(this)" id="_manageoperator_addOperator" class="btn btn-success" data-toggle="modal">新增</a>
	                            <br/>
	                            <br/>
	                            <div class="table-responsive">
	                                <table class="table table-striped table-bordered table-hover" id="datatable">
	                                    <thead>
	                                        <tr>
	                                        	<th>用户名</th>
	                                            <th>真实姓名</th>
	                                            <th>学校名称</th>
	                                            <th>创建时间</th>
	                                            <th>操作</th>
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
	<jsp:include page="../../../modalForOp.jsp" flush="true" />
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
} );
var table = "";
var deltable = "";
$(document).ready(function() {
	table = $('#datatable').DataTable({
    	"data":dataSet,
    	"columns":[
				   {
	   			   "data":"user_id"
	   			   }, 
            	   {
           		   "data":"user_name"
            	   },
            	   {
           			"data":"school_name"
           		   },
            	   {
           			"data":"create_time"
           		   },
           		   {
           		   	"class":"mytable-center",
           		   	"targets":-1,
        	    	"data":null,
        	    	"defaultContent":"<div class='btn-group-vertical'><button type='button' class='btn btn-success btn-sm dropdown-toggle' data-toggle='dropdown'>操作<span class='caret'></span></button><ul class='dropdown-menu'><li><a href='#' id='a_del'>删除</a></li><li><a href='#' id='a_update'>修改</a></li></ul></div>"  
        	    	/* "defaultContent":"<div class='btn-group-vertical'><button type='button' class='btn btn-success btn-sm dropdown-toggle' data-toggle='dropdown' id='a_del'>删除</button></div>"*/
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
	/* //关闭
    $('#blogtable tbody').on( 'click', '#a_close', function () {
    	var row = table.row($(this).parents('tr'));
        var data = row.data();
        $.ajax({
    		url:getUrl("web","manageoperator/closeOperator"),
    		type:"get",
    		data:{ID:data.ID},
    		dataType:'json',
    		success:function(obj)
    		{
    			showModalDT(obj.msg);
    			deltable = table;
    			if(obj.resFlag == '0')
    			{
    				$("#delId").val(data.ID);
    			}
    		},
    		error:function(obj)
    		{
    			showModal(obj.responseText);
    			return false;
    		}
    	});
    } ); */
  //删除功能
   $('#datatable tbody').on( 'click', '#a_del', function () {
    	var row = table.row($(this).parents('tr'));
        var data = row.data();
        $.ajax({
    		url:getUrl("web","manageoperator/delOperator"),
    		type:"get",
    		data:{ID : data.user_id},
    		dataType:'json',
    		success:function(obj)
    		{
    			showModalOp(obj.msg);
    			deltable = table;
    			if(obj.resFlag == '0')
    			{
    				$("#delId").val(data.user_id);
    			}
    			return false;
    		},
    		error:function(obj)
    		{
    			showModal(obj.responseText);
    			return false;
    		}
    	});
    } );
  	//删除
  	/* $('#datatable tbody').on( 'click', '#a_del', function () {
    	var row = table.row($(this).parents('tr'));
        var data = row.data();
        $.ajax({
    		url:getUrl("web","manageoperator/delOperator"),
    		type:"get",
    		data:{ID:data.ID},
    		dataType:'json',
    		success:function(obj)
    		{
    			showModalDT(obj.msg);
    			deltable = table;
    			alert(obj.resFlag);
    			if(obj.resFlag == '0')
    			{
    				alert(data.ID);
    				$("#delId").val(data.ID);
    			}
    		},
    		error:function(obj)
    		{
    			showModal(obj.responseText);
    			return false;
    		}
    	});
    });   */
  	//修改
    $('#datatable tbody').on( 'click', '#a_update', function () {
    	var row = table.row($(this).parents('tr'));
        var data = row.data();
        var paras = {ID:data.user_id , "path":"_manageoperator_update"};
        jumpEditBlog(paras);
    } ); 
	//跳转到编辑页面
	function jumpEditBlog(obj)
	{
	 	var timestamp = new Date().getTime();
		var basepath = $("#basepath").val();
		var path = (obj.path).replace(/_/g,"/");
		window.location.href = basepath+"/web"+path+"?activeId="+obj.path+"&ID="+obj.ID+"&ts="+timestamp;
	}
});
</script>