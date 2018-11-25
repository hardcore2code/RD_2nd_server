<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link href="<%=request.getContextPath() %>/wobo/css/plugins/dataTables.bootstrap.css" rel="stylesheet">
<jsp:include page="../../../head.jsp" flush="true" />
<%
String uri = request.getRequestURI();
uri = uri.substring(0, uri.lastIndexOf("/"));
String path = request.getContextPath();  
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>
<body>
	<div id="wrapper">
		<jsp:include page="../../../top.jsp" flush="true" />
		<!-- Page Content -->
		<div id="page-wrapper">
			<div class="container-fluid">
				<div class="row">
					<div class="col-lg-12">
						<h2 class="page-header">敏感词管理</h2>
					</div>
					<!-- /.col-lg-12 -->
				</div>
				<!-- /.row -->
	          	<div class="row">
	              	<div class="col-lg-12">
	                  	<div class="panel panel-default">
	                        <div class="panel-heading">
	                                                                敏感词列表                                    
	                        </div>
	                        <!-- /.panel-heading -->
	                        <div class="panel-body">
	                        <a href="#" onclick="jump_pub(this)" id="_webSensitive_addSensitive" class="btn btn-success" data-toggle="modal">新增</a>
	                        <br/>
	                        <br/>
	                            <div class="table-responsive">
	                                <table class="table table-striped table-bordered table-hover" id="datatable">
	                                    <thead>
	                                        <tr>
	                                        	<th>敏感词id</th>
	                                            <th>敏感词词典</th>
	                                            <th></th>
	                                            <th></th>
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
		         <!--  <div>
					<h3 class="page-header">批量增加</h3>
				  </div>
				   <form role="form" name="func" id="myForm">
				   <div>
					<textarea class="form-control" name="newWords" id="newWords" placeholder="敏感词之间请使用中文；隔开"></textarea>
				  </div>
				  <div class="form-group" style="margin-top: 10px">
                       <button type="button" class="btn btn-success" onclick="ajax_submit();">确 定</button>
                  </div>
                  </form> -->
		          <!-- /.row -->
				<!-- /.row -->
			</div>
			
			<!-- /.container-fluid -->
		</div>
		
		<!-- /#page-wrapper -->
	</div>
	<!-- /#wrapper -->
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
} );
var table = "";
var deltable = "";
$(document).ready(function() {
	table = $('#datatable').DataTable({
    	"data":dataSet,
    	"columns":[
            	   {
           		   "visible":false,
           		   "data":"sensitive_id"
            	   },
            	   {	
           		 	"class":'details-control',
           			"data":"sensitive_word"
           		   },
            	   {	
           			"visible":false,
           			"data":"sensitive_id"
           		   	},
            	   {	
           		   	"visible":false,
           		 	"data":"sensitive_id"
           		   	},
           		   	{
           		   	"class":"mytable-center",
           		   	"targets":-1,
        	    	"data":null,
        	    	"defaultContent":"<div class='btn-group-vertical'><button type='button' class='btn btn-success btn-sm dropdown-toggle' data-toggle='dropdown'>操作<span class='caret'></span></button><ul class='dropdown-menu'><li><a href='#' id='a_del'>删除</a></li><li><a href='#' id='a_edit'>修改</a></li></ul></div>"
           		   	}
               	  ],
        "order":[4,'desc'],
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
	//编辑敏感词
    $('#datatable tbody').on( 'click', '#a_edit', function () {
    	var row = table.row($(this).parents('tr'));
        var data = row.data();
        window.location.href = getUrl("web","webSensitive/updateSensitive")+"&sensitive_id="+data.sensitive_id;;
    } );
  	//删除功能
    $('#datatable tbody').on( 'click', '#a_del', function () {
    	var row = table.row($(this).parents('tr'));
        var data = row.data();
        $.ajax({
    		url:getUrl("web","webSensitive/deleteSensitive"),
    		type:"get",
    		data:{sensitive_id : data.sensitive_id},
    		dataType:'json',
    		success:function(obj)
    		{
    			showModalDT(obj.msg);
    			deltable = table;
    			if(obj.resFlag == '0')
    			{
    				$("#delId").val(data.sensitive_id);
    			}
    		},
    		error:function(obj)
    		{
    			showModal(obj.responseText);
    			return false;
    		}
    	});
    } );
});

//添加
function ajax_submit()
{
	if($("#newWords").val() == "" || $("#newWords").val() == null )
	{
		showModal("请填写需要添加敏感词!");
		return false;
	}
	var url = getUrl("web","webSensitive/saveWords");
	$.ajax({
		url:url,
		type:"post",
		data:$('#myForm').serialize(),
		dataType:'json',
		success:function(data)
		{
			showModal(data.msg);
			closeModal(getUrl("web","webSensitive/sensitiveWordsList")); //关闭悬浮框时再跳转页面
			return false;
		},
		error:function(data)
		{
			showModal(data.responseText);
			return false;
		}
	});
}
</script>

