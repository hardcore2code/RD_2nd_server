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
						<h1 class="page-header">功能-类</h1>
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
	                                <table class="table table-striped table-bordered table-hover" id="actiontable">
	                                    <thead>
	                                        <tr>
	                                            <th width="15%">访问路径</th>
	                                            <th>类名及路径</th>
	                                            <th>视图路径</th>
	                                            <th width="10%">状态</th>
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
	<div class="modal fade" id="actionModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    	<div class="modal-dialog">
        	<div class="modal-content">
	            <div class="modal-header">
	                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	                <h4 class="modal-title" id="actionModalLabel">功能和类的关系</h4>
	            </div>
	            <div class="modal-body" id="role_modal_val">
	            	<form class="form-horizontal mytable-center" id="actionForm" name='action'>
	            		<table id='roleModalTable'  style="height: 29%">
	            			<tr>
	            				<td align="right" width="18%"> <label class="control-label" for="inputName" style="width: 100">访问路径：</label> </td>
	            				<td align="left"><input class="form-control"  placeholder="必填" type="text" id="controllerKey" name="action.CONTROLLER_KEY" /></td>
	            			</tr>
	            			<tr>
	            				<td align="right"><label class="control-label" for="inputName">类名：</label></td>
	            				<td align="left"><input class="form-control"  placeholder="必填" type="text" id="controllerClass" name="action.CLASS"/></td>
	            			</tr>
	            			<tr>
	            				<td align="right"><label class="control-label" for="inputName">视图路径：</label> </td>
	            				<td align="left"><input class="form-control"  placeholder="必填" type="text" id="path" name="action.PATH"/></td>
	            			</tr>
	            			<tr>
	            				<td align="right"><label class="control-label" for="inputJob">状态：</label> </td>
	            				<td align="left">
		            				<select class="form-control" id="actionStatus" name="action.STATUS">
		            					<option value='0'>保存</option>
		            					<option value='1'>启用</option>
		            				</select>
	            				</td>
	            			</tr>
	            		</table>
		            </form>
	            </div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-default" id="saveBtn" onclick="saveAction()">保存</button>
	                <button type="button" style="display: none" id="updateBtn" class="btn btn-default"  onclick="editAction()">更新</button>
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
var dataSet = ${actionList};
// 默认开启搜索和排序
$.extend( $.fn.dataTable.defaults, {
    searching: true,
    ordering:  true
} );
var table = "";
var deltable = "";
$(document).ready(function() {
	table = $('#actiontable').DataTable({
    	"data":dataSet,
    	"columns":[
            	   {
           		   "class":'details-control',
           		   "data":"CONTROLLER_KEY"
            	   },
            	   {	
           		 	"class":'details-control',
           			"data":"CLASS"
           		   },
            	   {	
           		 	"class":'details-control',
           			"data":"PATH"
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
            $('<a href="#" onclick="showActioneModal()" id="addAction" class="btn btn-primary" data-toggle="modal">新增</a>' ).appendTo($('.myBtnBox'));
        }
    });
	//编辑
    $('#actiontable tbody').on( 'click', '#a_edit', function () {
    	var row = table.row($(this).parents('tr'));
        var data = row.data();
        var this_tr = $(this).parents('tr');
        $("#controllerKey").val(data.CONTROLLER_KEY);
        $("#controllerClass").val(data.CLASS);
        $("#path").val(data.PATH);
        $("#actionStatus").val(data.STATUS);
        showUpdateModal("#actionModal");
    } );
  	//删除
    $('#actiontable tbody').on( 'click', '#a_del', function () {
    	var row = table.row($(this).parents('tr'));
        var data = row.data();
        $.ajax({
    		url:getUrl("wb","func/delActionMap"),
    		type:"get",
    		data:{key:data.CONTROLLER_KEY},
    		dataType:'json',
    		success:function(obj)
    		{
    			showModalDT(obj.msg);
    			deltable = table;
    			if(obj.resFlag == '0')
    			{
    				$("#delId").val(data.CONTROLLER_KEY);
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
//保存
function saveAction()
{
	if($("#controllerKey").val() == "")
	{
		showModal("访问路径!");
		return false;
	}
	if($("#controllerClass").val() == "")
	{
		showModal("类名及路径!");
		return false;
	}
	if($("#path").val() == "")
	{
		showModal("视图路径!");
		return false;
	}
	$.ajax({
		url:getUrl("wb","func/saveActionMap"),
		type:"post",
		data:$('#actionForm').serialize(),
		dataType:'json',
		success:function(data)
		{
			showModal(data.msg);
		},
		error:function(data)
		{
			showModal(data.responseText);
			return false;
		}
	});
}

$('#actionModal').on('hide.bs.modal', function(obj)
{
    var ifadd = true;
    if($('#controllerKey').val() == "")
    {
    	//不操作
    }
    else if($("#saveBtn").attr("style") == "display:show")
    {
    	var ids = table.column(0).data().reduce( function (a,b) {return a+","+b;}).split(",");
    	for(var i = 0 ; i < ids.length ; i++)
    	{
    		if(ids[i] == $('#controllerKey').val())
    		{
    			ifadd = false;
    		}
    	}
    	if(ifadd)
    	{
    		addRow({'key':$('#controllerKey').val(),'className':$("#controllerClass").val(),'path':$("#path").val(),'status':$("#actionStatus").find("option:selected").text()});
    	}
    	ifadd = true;
    }
    else if($("#updateBtn").attr("style") == "display:show")
    {
    	var ids = table.column(0).data().reduce( function (a,b) {return a+","+b;}).split(",");
    	for(var i = 0 ; i < ids.length ; i++)
    	{
    		if(ids[i] == $('#controllerKey').val())
    		{
    			table.row(i).data().CONTROLLER_KEY = $("#controllerKey").val();
    			table.row(i).column(0).nodes().to$().eq(i).html($("#controllerKey").val());
    			table.row(i).data().CLASS = $("#controllerClass").val();
    			table.row(i).column(1).nodes().to$().eq(i).html($("#controllerClass").val());
    			table.row(i).data().PATH = $("#path").val();
    			table.row(i).column(2).nodes().to$().eq(i).html($("#path").val());
    			table.row(i).data().STATUS = $("#actionStatus").val();
    			table.row(i).data().STATUSNAME = $("#actionStatus").find("option:selected").text();
    			table.row(i).column(3).nodes().to$().eq(i).html($("#actionStatus").find("option:selected").text());
    		}
    	}
    }
    document.getElementById("actionForm").reset();
});
//编辑
function editAction()
{
	if($("#controllerKey").val() == "")
	{
		showModal("访问路径!");
		return false;
	}
	if($("#controllerClass").val() == "")
	{
		showModal("类名及路径!");
		return false;
	}
	if($("#path").val() == "")
	{
		showModal("视图路径!");
		return false;
	}
	$.ajax({
		url:getUrl("wb","func/updateActionMap"),
		type:"post",
		data:$('#actionForm').serialize(),
		dataType:'json',
		success:function(data)
		{
			showModal(data.msg);
		},
		error:function(data)
		{
			showModal(data.responseText);
			return false;
		}
	});
}
function showActioneModal()
{
	showSaveModal("#actionModal");
}
function showSaveModal(obj)
{
	$("#saveBtn").attr("style","display:show");
    $("#updateBtn").attr("style","display:none");
    $("#controllerKey").attr("readonly",false);
    showModalpub(obj);
}
function showUpdateModal(obj)
{
	$("#saveBtn").attr("style","display:none");
    $("#updateBtn").attr("style","display:show");
    $("#controllerKey").attr("readonly","readonly");
    showModalpub(obj);
}
function addRow(obj)
{
	table.row.add(
           {
           	"CONTROLLER_KEY":obj.key,
           	"CLASS":obj.className,
           	"PATH":obj.path,
           	"STATUSNAME":obj.status
           }
    	).draw();
}
</script>