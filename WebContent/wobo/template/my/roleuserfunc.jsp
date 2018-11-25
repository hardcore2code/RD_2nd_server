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
						<h1 class="page-header">角色-用户-功能</h1>
					</div>
					<!-- /.col-lg-12 -->
				</div>
				<!-- /.row -->
	          	<div class="row">
	              	<div class="col-lg-12">
	                    <div class="panel panel-default">
	                        <div class="panel-heading">
	                                                                角色勾稽关系管理
	                        </div>
	                        <!-- /.panel-heading -->
	                        <div class="panel-body">
	                            <!-- Nav tabs -->
	                            <ul class="nav nav-tabs">
	                                <li class="active"><a href="#rolelist" data-toggle="tab">角色列表</a>
	                                </li>
	                                <li><a href="#userlist" data-toggle="tab">用户列表</a>
	                                </li>
	                                <li><a href="#funclist" data-toggle="tab">功能列表</a>
	                                </li>
	                            </ul>
	                            <!-- Tab panes -->
	                            <div class="tab-content">
	                                <div class="tab-pane fade in active" id="rolelist">
	                                	<br/>
	                                	<div class="col-lg-12">
				                            <div class="table-responsive">
				                                <table class="table table-striped table-bordered table-hover"  id="roletable">
				                                    <thead>
													    <tr>
				                                            <th>ID</th>
				                                            <th>名称</th>
				                                            <th>描述</th>
				                                            <th>状态</th>
													    </tr>
				                                    </thead>
				                                </table>
				                            </div>
						              	</div>
						              	<!-- /.col-lg-12 -->
	                                </div>
	                                <div class="tab-pane fade" id="userlist">
	                                	<br/>
	                                	<div class="col-lg-12">
				                            <div class="table-responsive">
				                                <table class="table table-striped table-bordered table-hover"  id="usertable">
				                                    <thead>
				                                        <tr>
													        <th rowspan="2" >角色ID</th>
													        <th rowspan="2" width="20%" style="vertical-align: middle;">角色名称</th>
													        <th colspan="4" class="mytable-center">用户信息</th>
													    </tr>
													    <tr>
				                                            <th width="20%">用户ID</th>
				                                            <th width="20%">名称</th>
				                                            <th width="20%">昵称</th>
				                                            <th width="20%">操作</th>
													    </tr>
				                                    </thead>
				                                </table>
				                            </div>
						              	</div>
						              	<!-- /.col-lg-12 -->
	                                </div>
	                                <div class="tab-pane fade" id="funclist">
										<br/>
	                                	<div class="col-lg-12">
				                            <div class="table-responsive">
				                                <table class="table table-striped table-bordered table-hover"  id="functable">
				                                    <thead>
				                                        <tr>
													        <th rowspan="2" >角色ID</th>
													        <th rowspan="2" width="20%" style="vertical-align: middle;">角色名称</th>
													        <th colspan="4" class="mytable-center">功能信息</th>
													    </tr>
													    <tr>
				                                            <th width="20%">功能ID</th>
				                                            <th width="20%">名称</th>
				                                            <th width="20%">层级</th>
				                                            <th width="20%">操作</th>
													    </tr>
				                                    </thead>
				                                </table>
				                            </div>
						              	</div>
						              	<!-- /.col-lg-12 -->
	                                </div>
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
			<!-- /.container-fluid -->
		</div>
		<!-- /#page-wrapper -->
	</div>
	<!-- Modal -->
	<div class="modal fade" id="roleUserModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    	<div class="modal-dialog">
        	<div class="modal-content">
	            <div class="modal-header">
	                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	                <h4 class="modal-title" id="roleModalLabel">角色-用户</h4>
	            </div>
	            <div class="modal-body" id="role_modal_val">
	            	<form class="form-horizontal mytable-center" id="roleUserForm" name='roleUser'>
	            		<input type="hidden" id="roleUserFlag" >
	            		<input type="hidden" id="nickName" >
	            	 	<div class="form-group " >
                            <label>角色名称</label>
                            <select class="form-control" name="roleId" id="selectRoleId">
                                <option value="none">请选择</option>
                                <c:forEach items="${roleListSelect}" var="role">
                               		<option value="${role.ROLE_ID}" >${role.NAME}</option>
                               	</c:forEach>
                            </select>
                        </div>
	            	 	<div class="form-group">
                            <label>用户名称</label>
                            <select class="form-control" name="userId" id="selectUserId">
                                <option value="none">请选择</option>
                                <c:forEach items="${userListSelect}" var="user">
                               		<option value="${user.USER_ID}" >${user.NAME}</option>
                               	</c:forEach>
                            </select>
                        </div>
		            </form>
	            </div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-default" id="saveBtn" onclick="saveRoleUser()">保存</button>
	                <button type="button" class="btn btn-primary" data-dismiss="modal">关闭</button>
	            </div>
        	</div>
        	<!-- /.modal-content -->
    	</div>
    	<!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->
	<!-- Modal -->
	<div class="modal fade" id="roleFuncModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    	<div class="modal-dialog">
        	<div class="modal-content">
	            <div class="modal-header">
	                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	                <h4 class="modal-title" id="roleModalLabel">角色-功能</h4>
	            </div>
	            <div class="modal-body" id="role_modal_val">
	            	<form class="form-horizontal mytable-center" id="roleFuncForm" name='roleFunc'>
	            		<input type="hidden" id="roleFuncFlag" >
	            		<input type="hidden" id="levelName" >
	            	 	<div class="form-group " >
                            <label>角色名称</label>
                            <select class="form-control" name="roleId" id="selectRoleId">
                                <option value="none">请选择</option>
                                <c:forEach items="${roleListSelect}" var="role">
                               		<option value="${role.ROLE_ID}" >${role.NAME}</option>
                               	</c:forEach>
                            </select>
                        </div>
	            	 	<div class="form-group">
                            <label>功能名称</label>
                            <select class="form-control" name="funcId" id="selectFuncId">
                                <option value="none">请选择</option>
                                <c:forEach items="${funcListSelect}" var="func">
                               		<option value="${func.FUNC_ID}" >${func.NAME}</option>
                               	</c:forEach>
                            </select>
                        </div>
		            </form>
	            </div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-default" id="saveBtn" onclick="saveRoleFunc()">保存</button>
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
var dataSet = ${roleList};
var userSet = ${userList};
var funcSet = ${funcList};
// 默认开启搜索和排序
$.extend( $.fn.dataTable.defaults, {
    searching: true,
    ordering:  true
} );
var table = "";
var usertable = "";
var functable = "";
var deltable = "";
$(document).ready(function() {
	table = $('#roletable').DataTable({
    	"data":dataSet,
    	"columns":[
           		   	{"data":"ROLE_ID","searchable": false},
           			{"data":"NAME"},
           			{"data":"ROLE_DESC","searchable": false},
           			{"data":"STATUSNAME","searchable": false}
               	  ],
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
	usertable = $('#usertable').DataTable({
    	"data":userSet,
    	"columns":[
					{
					    "data": "ROLEUSER_ID",
					    "visible": false
					},
					{
				    	"data": "ROLENAME"
					},
            	   	{
           		   		"data":"USER_ID"
            	   	},
            	   	{	
           				"data":"NAME"
           		   	},
            	   	{	
           				"data":"NICK_NAME"
         		   	},
         		   	{
               		   	"class":"mytable-center",
               		   	"targets":-1,
            	    	"data":null,
            	    	"defaultContent":"<div class='btn-group-vertical'><button type='button' class='btn btn-primary btn-sm dropdown-toggle' data-toggle='dropdown'>操作<span class='caret'></span></button><ul class='dropdown-menu'><li><a href='#' id='a_del'>删除</a></li></ul></div>"
             		}
               	  ],
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
	    "pagingType":   "full_numbers",
	    "dom": "<'row-fluid'<'span6'f><'span6 myBtnBox'>>tpi",
	    "fnInitComplete": function (oSettings, json) {
            $('<a href="#" onclick="showRoleUserModal()" id="addUserRole" class="btn btn-primary" data-toggle="modal">新增</a>' ).appendTo($('.myBtnBox'));
        }
    });
	functable = $('#functable').DataTable({
    	"data":funcSet,
    	"columns":[
					{
					    "data": "ROLEFUNC_ID",
					    "visible": false
					},
					{
				    	"data": "ROLENAME"
					},
            	   	{
           		   		"data":"FUNC_ID"
            	   	},
            	   	{	
           				"data":"NAME"
           		   	},
            	   	{	
           				"data":"LEVELNAME"
         		   	},
         		   	{
               		   	"class":"mytable-center",
               		   	"targets":-1,
            	    	"data":null,
            	    	"defaultContent":"<div class='btn-group-vertical'><button type='button' class='btn btn-primary btn-sm dropdown-toggle' data-toggle='dropdown'>操作<span class='caret'></span></button><ul class='dropdown-menu'><li><a href='#' id='a_func_del'>删除</a></li></ul></div>"
             		}
               	  ],
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
	    "pagingType":   "full_numbers",
	    "dom": "<'row-fluid'<'span6'f><'span6 myBtnBox2'>>tpi",
	    "fnInitComplete": function (oSettings, json) {
            $('<a href="#" onclick="showRoleFuncModal()" id="addUserFunc" class="btn btn-primary" data-toggle="modal">新增</a>' ).appendTo($('.myBtnBox2'));
        }
    });
	$('#roletable').on('search.dt', function(e,o) {
		usertable.column(1).search(o.oPreviousSearch.sSearch).draw();
		functable.column(1).search(o.oPreviousSearch.sSearch).draw();
    }).dataTable();
	$("#rolelist input[type='search']").attr("placeholder","角色名称");
	//删除功能
    $('#usertable tbody').on( 'click', '#a_del', function () {
    	var row = usertable.row($(this).parents('tr'));
        var data = row.data();
        $.ajax({
    		url:getUrl("wb","role/delRoleUser"),
    		type:"get",
    		data:{roleUserId:data.ROLEUSER_ID},
    		dataType:'json',
    		success:function(obj)
    		{
    			deltable = usertable;
    			showModalDT(obj.msg);
    			if(obj.resFlag == '0')
    			{
    				$("#delId").val(data.ROLEUSER_ID);
    			}
    		},
    		error:function(obj)
    		{
    			showModal(obj.responseText);
    			return false;
    		}
    	});
    } );
	//删除功能
    $('#functable tbody').on( 'click', '#a_func_del', function () {
    	var row = functable.row($(this).parents('tr'));
        var data = row.data();
        $.ajax({
    		url:getUrl("wb","role/delRoleFunc"),
    		type:"get",
    		data:{roleFuncId:data.ROLEFUNC_ID},
    		dataType:'json',
    		success:function(obj)
    		{
    			deltable = functable;
    			showModalDT(obj.msg);
    			if(obj.resFlag == '0')
    			{
    				$("#delId").val(data.ROLEFUNC_ID);
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
function showRoleUserModal()
{
	showModalpub("#roleUserModal");
}
function showRoleFuncModal()
{
	showModalpub("#roleFuncModal");
}
//保存角色-用户信息
function saveRoleUser()
{
	if($("#selectRoleId").val() == "" || $("#selectRoleId").val() == "none")
	{
		showModal("请选择角色!");
		return false;
	}
	if($("#selectUserId").val() == "" || $("#selectUserId").val() == "none")
	{
		showModal("请选择用户!");
		return false;
	}
	$.ajax({
		url:getUrl("wb","role/saveRoleUser"),
		type:"post",
		data:$('#roleUserForm').serialize(),
		dataType:'json',
		success:function(data)
		{
			showModal(data.msg);
			if(data.resFlag == '0')
			{
				$("#roleUserFlag").val(data.resFlag);
				$("#nickName").val(data.nickName);
			}else if(data.resFlag == '1')
			{
				$("roleUserFlag").val(data.resFlag);
			}
		},
		error:function(data)
		{
			showModal(data.responseText);
			return false;
		}
	});
}
//保存角色-功能信息
function saveRoleFunc()
{
	if($("#roleFuncModal #selectRoleId").val() == "" || $("#roleFuncModal #selectRoleId").val()== "none")
	{
		showModal("请选择角色!");
		return false;
	}
	if($("#selectFuncId").val() == "" || $("#selectFuncId").val()== "none")
	{
		showModal("请选择功能!");
		return false;
	}
	$.ajax({
		url:getUrl("wb","role/saveRoleFunc"),
		type:"post",
		data:$('#roleFuncForm').serialize(),
		dataType:'json',
		success:function(data)
		{
			showModal(data.msg);
			if(data.resFlag == '0')
			{
				$("#roleFuncFlag").val(data.resFlag);
				$("#levelName").val(data.levelName);
			}else if(data.resFlag == '1')
			{
				$("roleFuncFlag").val(data.resFlag);
			}
		},
		error:function(data)
		{
			showModal(data.responseText);
			return false;
		}
	});
}
$('#roleUserModal').on('hide.bs.modal', function(obj)
{
	if($("#roleUserFlag").val() == "0")
   	{
   		addRow({'roleId':$('#selectRoleId').val()+';'+$("#selectUserId").val(),'name':$("#selectRoleId").find("option:selected").text(),'userId':$("#selectUserId").val(),'username':$("#selectUserId").find("option:selected").text(),'nickName':$("#nickName").val()});
   	}
    document.getElementById("roleUserForm").reset();
    $("#nickName").val("");
    $("#roleUserFlag").val("");
});
$('#roleFuncModal').on('hide.bs.modal', function(obj)
{
	if($("#roleFuncFlag").val() == "0")
   	{
   		addRow2({'roleId':$('#roleFuncModal #selectRoleId').val()+';'+$("#selectFuncId").val(),'name':$("#roleFuncModal #selectRoleId").find("option:selected").text(),'funcId':$("#selectFuncId").val(),'funcname':$("#selectFuncId").find("option:selected").text(),'levelName':$("#levelName").val()});
   	}
    document.getElementById("roleFuncForm").reset();
    $("#levelName").val("");
    $("#roleFuncFlag").val("");
});
//增加一行
function addRow(obj)
{
	usertable.row.add(
          {
          	"ROLEUSER_ID":obj.roleId,
          	"ROLENAME":obj.name,
          	"USER_ID":obj.userId,
          	"NAME":obj.username,
          	"NICK_NAME":obj.nickName
          }
   	).draw();
}
//增加一行
function addRow2(obj)
{
	functable.row.add(
          {
          	"ROLEFUNC_ID":obj.roleId,
          	"ROLENAME":obj.name,
          	"FUNC_ID":obj.funcId,
          	"NAME":obj.funcname,
          	"LEVELNAME":obj.levelName
          }
   	).draw();
}
</script>