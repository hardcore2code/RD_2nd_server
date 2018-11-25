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
						<h1 class="page-header">角色维护</h1>
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
	                            <div class="table-responsive">
	                                <table class="table table-striped table-bordered table-hover" id="roletable">
	                                    <thead>
	                                        <tr>
	                                            <th>ID</th>
	                                            <th>名称</th>
	                                            <th>描述</th>
	                                            <th>状态</th>
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
	<!-- Modal -->
	<div class="modal fade" id="roleModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    	<div class="modal-dialog">
        	<div class="modal-content">
	            <div class="modal-header">
	                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	                <h4 class="modal-title" id="roleModalLabel">角色信息</h4>
	            </div>
	            <div class="modal-body" id="role_modal_val">
	            	<form class="form-horizontal mytable-center" id="roleForm" name='role'>
	            		<input type="hidden" id="roleId" name="role.ROLE_ID" />
	            		<input type="hidden" id="roleNameShow"  />
	            		<input type="hidden" id="roleDescShow"  />
	            		<input type="hidden" id="roleStatusShow"  />
	            		<table id='roleModalTable' align="center" style="height: 29%">
	            			<tr>
	            				<td align="right"> <label class="control-label" for="inputName">ID：</label> </td>
	            				<td id="roleIdShow" align="left"></td>
	            			</tr>
	            			<tr>
	            				<td align="right"><label class="control-label" for="inputName">名称：</label></td>
	            				<td align="left"><input class="form-control"  placeholder="必填" type="text" id="roleName" name="role.NAME"/></td>
	            			</tr>
	            			<tr>
	            				<td align="right"><label class="control-label" for="inputJob">描述：</label> </td>
	            				<td align="left"><textarea class="form-control" id="roleDesc" name="role.DESC"></textarea></td>
	            			</tr>
	            			<tr>
	            				<td align="right"><label class="control-label" for="inputJob">状态：</label> </td>
	            				<td align="left">
		            				<select class="form-control" id="roleStatus" name="role.STATUS">
		            					<option value='0'>保存</option>
		            					<option value='1'>启用</option>
		            				</select>
	            				</td>
	            			</tr>
	            		</table>
		            </form>
	            </div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-default" id="saveBtn" onclick="saveRole()">保存</button>
	                <button type="button" style="display: none" id="updateBtn" class="btn btn-default"  onclick="editRole()">更新</button>
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
// 默认开启搜索和排序
$.extend( $.fn.dataTable.defaults, {
    searching: true,
    ordering:  true
} );
var table = "";
var deltable = "";
$(document).ready(function() {
	table = $('#roletable').DataTable({
    	"data":dataSet,
    	"columns":[
            	   {
           		   "class":'details-control',
           		   "data":"ROLE_ID"
            	   },
            	   {	
           		 	"class":'details-control',
           			"data":"NAME"
           		   },
            	   {	
           		 	"class":'details-control',
           			"data":"ROLE_DESC"
           		   	},
            	   {	
           		 	"class":'details-control',
           			"data":"STATUSNAME"
           		   	},
           		   	{
           		   	"class":"mytable-center",
           		   	"targets":-1,
        	    	"data":null,
        	    	"defaultContent":"<div class='btn-group-vertical'><button type='button' class='btn btn-primary btn-sm dropdown-toggle' data-toggle='dropdown'>操作<span class='caret'></span></button><ul class='dropdown-menu'><li><a href='#' id='a_edit'>编辑</a></li><li><a href='#' id='a_del'>删除</a></li></ul></div>"
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
            $('<a href="#" onclick="showRoleModal()" id="addRole" class="btn btn-primary" data-toggle="modal">新增</a>' ).appendTo($('.myBtnBox'));
        }
    });
	//编辑角色
    $('#roletable tbody').on( 'click', '#a_edit', function () {
    	var row = table.row($(this).parents('tr'));
        var data = row.data();
        var this_tr = $(this).parents('tr');
        $("#roleIdShow").text(data.ROLE_ID);
        $("#roleId").val(data.ROLE_ID);
        $("#roleName").val(data.NAME);
        $("#roleDesc").val(data.ROLE_DESC);
        $("#roleStatus").val(data.STATUS);
        $("#roleNameShow").val(data.NAME);
        $("#roleDescShow").val(data.ROLE_DESC);
        $("#roleStatusShow").val(data.STATUS);
        showUpdateModal("#roleModal");
    } );
  //删除功能
    $('#roletable tbody').on( 'click', '#a_del', function () {
    	var row = table.row($(this).parents('tr'));
        var data = row.data();
        $.ajax({
    		url:getUrl("wb","role/delRole"),
    		type:"get",
    		data:{roleId:data.ROLE_ID},
    		dataType:'json',
    		success:function(obj)
    		{
    			showModalDT(obj.msg);
    			deltable = table;
    			if(obj.resFlag == '0')
    			{
    				$("#delId").val(data.ROLE_ID);
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
//保存角色信息
function saveRole()
{
	if($("#roleName").val() == "")
	{
		showModal("请填写角色名称!");
		return false;
	}
	$.ajax({
		url:getUrl("wb","role/saveRole"),
		type:"post",
		data:$('#roleForm').serialize(),
		dataType:'json',
		success:function(data)
		{
			showModal(data.msg);
			if(data.resFlag == '0')
			{
				$('#roleId').val(data.roleId);
				$('#roleIdShow').text(data.roleId);
				$("#roleNameShow").val(data.name);
		        $("#roleDescShow").val(data.desc);
		        $("#roleStatusShow").val(data.statusName);
			}
		},
		error:function(data)
		{
			showModal(data.responseText);
			return false;
		}
	});
}

$('#roleModal').on('hide.bs.modal', function(obj)
{
    var ifadd = true;
    if($('#roleId').val() == "")
    {
    	//不操作
    }
    else if($("#saveBtn").attr("style") == "display:show")
    {
    	var ids = table.column(0).data().reduce( function (a,b) {return a+","+b;}).split(",");
    	for(var i = 0 ; i < ids.length ; i++)
    	{
    		if(ids[i] == $('#roleId').val())
    		{
    			ifadd = false;
    		}
    	}
    	if(ifadd)
    	{
    		addRow({'roleId':$('#roleId').val(),'name':$("#roleNameShow").val(),'desc':$("#roleDescShow").val(),'status':$("#roleStatusShow").val()});
    	}
    	ifadd = true;
    }
    else if($("#updateBtn").attr("style") == "display:show")
    {
    	var ids = table.column(0).data().reduce( function (a,b) {return a+","+b;}).split(",");
    	for(var i = 0 ; i < ids.length ; i++)
    	{
    		if(ids[i] == $('#roleId').val())
    		{
    			table.row(i).data().NAME = $("#roleNameShow").val();
    			table.row(i).column(1).nodes().to$().eq(i).html($("#roleNameShow").val());
    			table.row(i).data().ROLE_DESC = $("#roleDescShow").val();
    			table.row(i).column(2).nodes().to$().eq(i).html($("#roleDescShow").val());
    			table.row(i).data().STATUS = $("#roleStatusShow").val();
    			table.row(i).column(3).nodes().to$().eq(i).html($("#roleStatusShow").val() == '0' ? '保存' : '启用');
    		}
    	}
    }
    document.getElementById("roleForm").reset();
    $("#roleIdShow").text("");
	$("#roleId").val("");
	
});
//编辑角色信息
function editRole()
{
	if($("#roleName").val() == "")
	{
		showModal("请填写角色名称!");
		return false;
	}
	$.ajax({
		url:getUrl("wb","role/saveRole"),
		type:"post",
		data:$('#roleForm').serialize(),
		dataType:'json',
		success:function(data)
		{
			showModal(data.msg);
			if(data.resFlag == '0')
			{
				$('#roleNameShow').val($("#roleName").val());
				$('#roleDescShow').val($("#roleDesc").val());
				$('#roleStatusShow').val($("#roleStatus").val());
			}
		},
		error:function(data)
		{
			showModal(data.responseText);
			return false;
		}
	});
}
function showRoleModal()
{
	showSaveModal("#roleModal");
}
function showSaveModal(obj)
{
	$("#saveBtn").attr("style","display:show");
    $("#updateBtn").attr("style","display:none");
    showModalpub(obj);
}
function showUpdateModal(obj)
{
	$("#saveBtn").attr("style","display:none");
    $("#updateBtn").attr("style","display:show");
    showModalpub(obj);
}
function addRow(obj)
{
	table.row.add(
           {
           	"ROLE_ID":obj.roleId,
           	"NAME":obj.name,
           	"ROLE_DESC":obj.desc,
           	"STATUSNAME":obj.status
           }
    	).draw();
}
</script>