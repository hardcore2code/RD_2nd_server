<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link href="<%=request.getContextPath() %>/wobo/css/plugins/dataTables.bootstrap.css" rel="stylesheet">
<jsp:include page="../../head.jsp" flush="true" />
<body>
	<div id="wrapper">
		<jsp:include page="../../top.jsp" flush="true"/>
		<!-- Page Content -->
		<div id="page-wrapper">
			<div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">菜单列表</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                                                                列表明细                                       
                        </div>
                        <!-- /.panel-heading -->
                        <div class="panel-body">
                        	<a href="#" onclick="jump_pub(this)" id="_func_index" class="btn btn-primary" data-toggle="modal">新增</a>
                            <br/>
                            <br/>
                            <div class="table-responsive">
                                <table class="table table-striped table-bordered table-hover" id="dataTables-example">
                                    <thead>
                                        <tr>
                                            <th >功能ID</th>
                                            <th >名称</th>
                                            <th >层级</th>
                                            <th >序号</th>
                                            <th >跳转ID</th>
                                            <th >状态</th>
                                            <th width="12%">操作</th>
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
		</div>
		<!-- /#page-wrapper -->
	</div>
	<!-- Modal -->
	<div class="modal fade" id="funcModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	    <div class="modal-dialog">
	        <div class="modal-content">
	            <div class="modal-header">
	                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	                <h4 class="modal-title" id="funcModalLabel">提示</h4>
	            </div>
	            <input type="hidden" id="funcIdModal">
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
<jsp:include page="../../footer.jsp" flush="true" />
<script>
//首次跳转到页面获取后台传来的列表信息
var dataSet = ${funcInfoList};
// 默认开启搜索和排序
$.extend( $.fn.dataTable.defaults, {
    searching: true,
    ordering:  true
} );
var table = "";
var deltable = "";
$(document).ready(function() {
	table = $('#dataTables-example').DataTable({
    	"data":dataSet,
    	"columns":[
            	   {
            		   "visible": false,
            		   "data":"FUNC_ID"
            	   },
            	   {	
           		 	"class":'details-control',
           			"data":"NAME"
           		   	},
            	   {	
           		 	"class":'details-control',
           			"data":"LEVELNAME"
           		   	},
            	   {	
           		 	"class":'details-control',
           			"data":"SORT"
           		   	},
            	   {	
           		 	"class":'details-control',
           			"data":"A_ID"
           		   	},
            	   {	
           		 	"class":'details-control',
           			"data":"STATUSNAME"
           		   	},
           		   	{
           		   	"class":"mytable-center",
           		   	"targets":-1,
        	    	"data":null,
        	    	"defaultContent":"<div class='btn-group-vertical'><button type='button' class='btn btn-primary btn-sm dropdown-toggle' data-toggle='dropdown'>操作<span class='caret'></span></button><ul class='dropdown-menu'><li><a href='#' id='a_edit'>编辑</a></li><li><a href='#' id='a_on'>启动</a></li><li><a href='#' id='a_save'>保存</a></li><li><a href='#' id='a_del'>删除</a></li></ul></div>"
           		   	}
               	  ],
	    "language": {
	        "lengthMenu": "每页 _MENU_ 条记录",
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
	    "order": [[ 3, "asc" ]],
	    "lengthMenu": [[5, 10, 20, -1], [5, 10, 20, "All"]],
	    "pagingType":   "full_numbers",
    });
    // Add event listener for opening and closing details
    $('#dataTables-example tbody').on('click', 'td.details-control', function () {
    	var tr = $(this).closest('tr');
        var row = table.row(tr);
        if ( row.child.isShown() ) {
            // This row is already open - close it
            row.child.hide();
        }
        else {
            // Open this row
            row.child(format(row.data())).show();
        }
    } );
    //编辑功能
    $('#dataTables-example tbody').on( 'click', '#a_edit', function () {
    	var row = table.row($(this).parents('tr'));
        var data = row.data();
        var paras = {"funcId":data.FUNC_ID ,"path":"_func_index"};
        jumpEditFunc(paras);
    } );
    //删除功能
    $('#dataTables-example tbody').on( 'click', '#a_del', function () {
    	var row = table.row($(this).parents('tr'));
        var data = row.data();
        $.ajax({
    		url:getUrl("wb","func/funcDel"),
    		type:"get",
    		data:{funcId:data.FUNC_ID},
    		dataType:'json',
    		success:function(obj)
    		{
    			showModalDT(obj.msg);
    			deltable = table;
    			$("#delId").val(data.FUNC_ID);
    		},
    		error:function(obj)
    		{
    			showModal(obj.responseText);
    			return false;
    		}
    	});
    } );
    //改为保存状态功能
    $('#dataTables-example tbody').on( 'click', '#a_save', function () {
    	var row = table.row($(this).parents('tr'));
        var data = row.data();
        var this_tr = $(this).parents('tr');
        $.ajax({
    		url:getUrl("wb","func/funcSave"),
    		type:"get",
    		data:{funcId:data.FUNC_ID},
    		dataType:'json',
    		success:function(obj)
    		{
    			showModal(obj.msg);
    			this_tr.children().eq(4).html('保存');
    			return false;
    		},
    		error:function(obj)
    		{
    			showModal(obj.responseText);
    			return false;
    		}
    	});
    } );
    //改为启用状态功能
    $('#dataTables-example tbody').on( 'click', '#a_on', function () {
    	var row = table.row($(this).parents('tr'));
        var data = row.data();
        var this_tr = $(this).parents('tr');
        $.ajax({
    		url:getUrl("wb","func/funcOn"),
    		type:"get",
    		data:{funcId:data.FUNC_ID},
    		dataType:'json',
    		success:function(obj)
    		{
    			showModal(obj.msg);
    			this_tr.children().eq(4).html('启用');
    			return false;
    		},
    		error:function(obj)
    		{
    			showModal(obj.responseText);
    			return false;
    		}
    	});
    } );
});
/* Formatting function for row details - modify as you need */
function format (obj) {
    // `d` is the original data object for the row
    return '<table cellpadding="5" cellspacing="0" border="0" style="padding-left:50px;">'+
        '<tr>'+
            '<td>功能ID:</td>'+
            '<td>'+obj.FUNC_ID+'</td>'+
        '</tr>'+
        '<tr>'+
            '<td>父功能名称:</td>'+
            '<td>'+obj.parentName+'</td>'+
        '</tr>'+
    '</table>';
}
//跳转到编辑页面
function jumpEditFunc(obj)
{
 	var timestamp = new Date().getTime();
	var basepath = $("#basepath").val();
	var path = (obj.path).replace(/_/g,"/");
	window.location.href = basepath+"/wb"+path+"?activeId="+obj.path+"&funcId="+obj.funcId+"&ts="+timestamp;
}
</script>