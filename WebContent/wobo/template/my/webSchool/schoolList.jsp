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
						<h2 class="page-header">学校管理</h2>
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
	                        <input type="button" class="btn btn-success " onclick="addSchool()" id="goTextId" value="添加学校">
	                        <script type="text/javascript">
	                        function addSchool()
	                        {
	                        	window.location.href = getUrl("web","webSchool/addSchool");
	                        }
	                        </script>
	                        <br>
	                        <br>
	                         <div class="form-group">
	                        		<select name="school_category" id="school_category" style="width:100px" onchange="byCategory()">
	                        			<option value="all" ${school_category == "all"?"selected" : ""}>全部</option>
	                        			<option value="0" ${school_category == "0"?"selected" : ""}>小学</option>
	                        			<option value="1" ${school_category == "1"?"selected" : ""}>初中</option>
	                        			<option value="2" ${school_category == "2"?"selected" : ""}>高中</option>
	                        		</select>
	                        </div>
	                            <div class="table-responsive">
	                                <table class="table table-striped table-bordered table-hover" id="datatable">
	                                    <thead>
	                                        <tr>
	                                        	<th>学校id</th>
	                                            <th>学校名称</th>
	                                            <th>创建时间</th>
	                                            <th>运维管理</th>
	                                            <th>学校状态</th>
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
<script type="text/javascript">
	function byCategory(){
		var data = $("#school_category").val();
	    window.location.href = getUrl("web","webSchool/schoolList")+"&category="+data;
	}
</script>
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
           		   "data":"school_id"
            	   },
            	   {	
           		 	"class":'details-control',
           			"data":"school_name"
           		   },
            	   {	
           		 	"class":'details-control',
           			"data":"sysdate"
           		   	},
            	   {	
           		 	"class":'details-control',
           			"data":"user_name"
           		   	},
           		 	{	
             		 "class":'details-control',
             		 "data":"statusCN"
               		},
           		   	{
           		   	"class":"mytable-center",
           		   	"targets":-1,
        	    	"data":null,
       	    		"fnCreatedCell":function(nTd,sData,oData,iRow, iCol){
       	    			if(oData.status == '1'){
       	    				console.log("oData.status====================>>"+oData.status);
       	    				$(nTd).html("<div class='btn-group-vertical'><button type='button' class='btn btn-success btn-sm dropdown-toggle' data-toggle='dropdown'>操作<span class='caret'></span></button><ul class='dropdown-menu'><li><a href='#' id='a_close'>关闭</a></li><li><a href='#' id='a_del'>删除</a></li><li><a href='#' id='a_edit'>修改</a></li></ul></div>");
       	    			}else{
       	    				console.log("oData.status====================>>"+oData.status);
       	    				$(nTd).html("<div class='btn-group-vertical'><button type='button' class='btn btn-success btn-sm dropdown-toggle' data-toggle='dropdown'>操作<span class='caret'></span></button><ul class='dropdown-menu'><li><a href='#' id='a_open'>开启</a></li><li><a href='#' id='a_del'>删除</a></li><li><a href='#' id='a_edit'>修改</a></li></ul></div>");
       	    			}
	   	    			}
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
	//编辑学校
    $('#datatable tbody').on( 'click', '#a_edit', function () {
    	var row = table.row($(this).parents('tr'));
        var data = row.data();
        window.location.href = getUrl("web","webSchool/updateSchool")+"&school_id="+data.school_id;
    } );
  	//关闭学校
    $('#datatable tbody').on( 'click', '#a_close', function () {
    	var row = table.row($(this).parents('tr'));
        var data = row.data();
        $.ajax({
    		url:getUrl("web","webSchool/closeSchool"),
    		type:"get",
    		data:{school_id : data.school_id, flag : data.status},
    		dataType:'json',
    		success:function(obj)
    		{
    			showModal(obj.msg);
    			deltable = table;
    			if(obj.resFlag == '0')
    			{
    				$("#closeId").val(data.school_id);
    			} 
    			closeModal(getUrl("web","webSchool/schoolList")); //关闭悬浮框时再跳转页面
    		},
    		error:function(obj)
    		{
    			showModal(obj.responseText);
    			return false;
    		}
    	});
    } );
  //开启学校
    $('#datatable tbody').on( 'click', '#a_open', function () {
    	var row = table.row($(this).parents('tr'));
        var data = row.data();
        $.ajax({
    		url:getUrl("web","webSchool/closeSchool"),
    		type:"get",
    		data:{school_id : data.school_id, flag : data.status},
    		dataType:'json',
    		success:function(obj)
    		{
    			showModal(obj.msg);
    			deltable = table;
    			if(obj.resFlag == '0')
    			{
    				$("#closeId").val(data.school_id);
    			} 
    			closeModal(getUrl("web","webSchool/schoolList")); //关闭悬浮框时再跳转页面
    		},
    		error:function(obj)
    		{
    			showModal(obj.responseText);
    			return false;
    		}
    	});
    } );
  	//删除功能
    $('#datatable tbody').on( 'click', '#a_del', function () {
    	var row = table.row($(this).parents('tr'));
        var data = row.data();
        $.ajax({
    		url:getUrl("web","webSchool/deleteSchool"),
    		type:"get",
    		data:{school_id : data.school_id},
    		dataType:'json',
    		success:function(obj)
    		{
    			showModalDT(obj.msg);
    			deltable = table;
    			if(obj.resFlag == '0')
    			{
    				$("#delId").val(data.school_id);
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

</script>

