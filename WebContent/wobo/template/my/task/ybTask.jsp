<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link href="<%=request.getContextPath() %>/wobo/css/plugins/dataTables.bootstrap.css" rel="stylesheet">
<jsp:include page="../../../head.jsp" flush="true" />
<body>
	<div id="wrapper">
		<jsp:include page="../../../top.jsp" flush="true"/>
		<!-- Page Content -->
		<div id="page-wrapper">
			<div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">已办任务</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                                                                任务列表                                    
                        </div>
                        <!-- /.panel-heading -->
                        <div class="panel-body">
                            <div class="table-responsive">
                                <table class="table table-striped table-bordered table-hover" id="dataTables-dbtask">
                                    <thead>
                                        <tr>
                                            <th >流程名称</th>
                                            <th >流程编号</th>
                                            <th >流程启动时间</th>
                                            <th >任务名称</th>
                                            <th >任务创建时间</th>
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
var dataSet = ${ybWorkitemList};
// 默认开启搜索和排序
$.extend( $.fn.dataTable.defaults, {
    searching: true,
    ordering:  true
} );
var table = "";
var deltable = "";
$(document).ready(function() {
	table = $('#dataTables-dbtask').DataTable({
    	"data":dataSet,
    	"columns":[
            	   {	
           		 	"class":'details-control',
           			"data":"processName"
           		   	},
            	   {	
           		 	"class":'details-control',
           			"data":"orderNo"
           		   	},
            	   {	
           		 	"class":'details-control',
           			"data":"orderCreateTime"
           		   	},
            	   {	
           		 	"class":'details-control',
           			"data":"taskName"
           		   	},
            	   {	
           		 	"class":'details-control',
           			"data":"taskCreateTime"
           		   	},
           		   	{
           		   	"class":"mytable-center",
           		   	"targets":-1,
        	    	"data":null,
        	    	"defaultContent":"<button type='button' class='btn btn-primary btn-sm'>操作</button>"
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
});
//跳转到编辑页面
function jumpEditFunc(obj)
{
 	var timestamp = new Date().getTime();
	var basepath = $("#basepath").val();
	var path = (obj.path).replace(/_/g,"/");
	window.location.href = basepath+"/wb"+path+"?activeId="+obj.path+"&funcId="+obj.funcId+"&ts="+timestamp;
}
</script>