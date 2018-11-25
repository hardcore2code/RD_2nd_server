<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link href="<%=request.getContextPath() %>/wobo/css/plugins/dataTables.bootstrap.css" rel="stylesheet">
<jsp:include page="../../head.jsp" flush="true" />
<body>
	<div id="wrapper">
		<jsp:include page="../../top.jsp" flush="true" />
		<!-- Page Content -->
		<div id="page-wrapper">
			<div class="container-fluid">
				<div class="row">
					<div class="col-lg-12">
						<h1 class="page-header">评论</h1>
					</div>
					<!-- /.col-lg-12 -->
				</div>
				<!-- /.row -->
				<div class="row">
              	<div class="col-lg-12">
                  	<div class="panel panel-default">
                        <div class="panel-heading">
                                                                评论列表                                      
                        </div>
                        <!-- /.panel-heading -->
                        <div class="panel-body">
                            <div class="table-responsive">
                                <table class="table table-striped table-bordered table-hover" id="commenttable">
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>标题</th>
                                            <th>评论者</th>
                                            <th>状态</th>
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
			</div>
			<!-- /.container-fluid -->
		</div>
		<!-- /#page-wrapper -->
	</div>
	<!-- /#wrapper -->
	<jsp:include page="../../modal.jsp" flush="true" />
	<jsp:include page="../../modalForDT.jsp" flush="true" />
	<jsp:include page="../../common.jsp" flush="true" />
	<!-- DataTables JavaScript -->
	<script src="<%=request.getContextPath() %>/wobo/js/plugins/dataTables/jquery.dataTables.js"></script>
	<script src="<%=request.getContextPath() %>/wobo/js/plugins/dataTables/dataTables.bootstrap.js"></script>
</body>
</html>
<script>
//首次跳转到页面获取后台传来的列表信息
var dataSet = ${commentList};
// 默认开启搜索和排序
$.extend( $.fn.dataTable.defaults, {
    searching: true,
    ordering:  true
} );
var table = "";
var deltable = "";
$(document).ready(function() {
	table = $('#commenttable').DataTable({
    	"data":dataSet,
    	"columns":[
            	   {
            		   "visible": false,
            		   "class":'details-control',
           		   	   "data":"ID"
            	   },
            	   {	
            		   "class":'details-control',
           			   "data":"TITLE"
           		   },
            	   {	
           			"class":'details-control',
           			"data":"NICK_NAME"
           		   },
            	   {	
           			"class":'details-control',
           			"data":"STATUSNAME"
           		   },
           		   {
           		   	"class":"mytable-center",
           		   	"targets":-1,
        	    	"data":null,
        	    	"defaultContent":"<div class='btn-group-vertical'><button type='button' class='btn btn-primary btn-sm' id='a_del'>删除</a></div>"
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
	$('#commenttable tbody').on('click', 'td.details-control', function () {
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
  	//删除
    $('#commenttable tbody').on( 'click', '#a_del', function () {
    	var row = table.row($(this).parents('tr'));
        var data = row.data();
        $.ajax({
    		url:getUrl("wb","comment/delComment"),
    		type:"get",
    		data:{commentId:data.ID},
    		dataType:'json',
    		success:function(obj)
    		{
    			showModalDT(obj.msg);
    			deltable = table;
    			$("#delId").val(data.ID);
    		},
    		error:function(obj)
    		{
    			showModal(obj.responseText);
    			return false;
    		}
    	});
    });
});
function format (obj) {
    return '<table cellpadding="5" cellspacing="0" border="0" style="padding-left:50px;">'+
        '<tr>'+
            '<td>内容:</td>'+
            '<td>'+obj.COMMENT+'</td>'+
        '</tr>'+
    '</table>';
}
</script>