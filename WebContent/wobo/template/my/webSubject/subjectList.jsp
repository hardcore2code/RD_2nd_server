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
						<h2 class="page-header">科目管理</h2>
					</div>
					<!-- /.col-lg-12 -->
				</div>
				<!-- /.row -->
	          	<div class="row">
	              	<div class="col-lg-12">
	                  	<div class="panel panel-default">
	                        <div class="panel-heading">
	                                                                科目列表                                    
	                        </div>
	                        <!-- /.panel-heading -->
	                        <div class="panel-body">
	                        <a href="#" onclick="jump_pub(this)" id="_webSubject_addSubject" class="btn btn-success" data-toggle="modal">新增</a>
	                        <br/>
	                        <br/>
	                            <div class="table-responsive">
	                                <table class="table table-striped table-bordered table-hover" id="datatable">
	                                    <thead>
	                                        <tr>
	                                        	<th>科目id</th>
	                                            <th>科目名称</th>
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
		          <!-- <div>
					<h3 class="page-header">增加科目</h3>
				  </div>
				   <form role="form" name="func" id="myForm">
				   <div>
					<input class="form-control" id="newSubject" name="newSubject">
				  </div>
				  <div class="form-group" style="margin-top: 10px">
                       <button type="button" class="btn btn-success" onclick="ajax_submit();">确 定</button>
                  </div> 
                  </form>-->
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
<<<<<<< .mine
=======
 	<%-- <jsp:include page="../../../modalForTA.jsp" flush="true" /> --%>
>>>>>>> .r2935
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
           		   "data":"subject_id"
            	   },
            	   {	
           		 	"class":'details-control',
           			"data":"subject_name"
           		   },
            	   {	
           			"visible":false,
           			"data":"subject_id"
           		   	},
            	   {	
           		   	"visible":false,
           		 	"data":"subject_id"
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
	//编辑科目
    $('#datatable tbody').on( 'click', '#a_edit', function () {
    	var row = table.row($(this).parents('tr'));
        var data = row.data();
        window.location.href = getUrl("web","webSubject/updateSubject")+"&subject_id="+data.subject_id;
    } );
  	//删除功能
    $('#datatable tbody').on( 'click', '#a_del', function () {
    	/* var row = table.row($(this).parents('tr'));
        var data = row.data(); 
    	$.ajax({
	    		url:getUrl("web","webSubject/deleteSubject"),
	    		type:"get",
	    		data:{subject_id : data.subject_id},
	    		dataType:'json',
	    		success:function(obj)
	    		{
	    			/* alert("333333333333333"); */
	    			/*showModalDT(obj.msg);
	    			deltable = table;
	    			if(obj.resFlag == '0')
	    			{
	    				$("#delId").val(data.subject_id);
	    			}
	    		},
	    		error:function(obj)
	    		{
	    			/* alert("444444444444444444444"); */
	    			/*showModal(obj.responseText);
	    			return false;
	    		}
	    	}); */
    	$('#confirm').text('');
    	var r=confirm("确定要删除吗？");
    	if (r==true){
    		console.log("111111111111111");
	    	var row = table.row($(this).parents('tr'));
	        var data = row.data();
	        console.log("222222222222222");
	        $.ajax({
	    		url:getUrl("web","webSubject/deleteSubject"),
	    		type:"get",
	    		data:{subject_id : data.subject_id},
	    		dataType:'json',
	    		success:function(obj)
	    		{
	    			console.log("333333333333333");
	    			showModalDT(obj.msg);
	    			deltable = table;
	    			if(obj.resFlag == '0')
	    			{
	    				$("#delId").val(data.subject_id);
	    			}
	    		},
	    		error:function(obj)
	    		{
	    			console.log("444444444444444444444");
	    			showModal(obj.responseText);
	    			return false;
	    		}
	    	});
    	} 
    	// 四个选项都是可选参数
        /* Modal.alert(
    	  {
    	    msg: '内容',
    	    title: '标题',
    	    btnok: '确定',
    	    btncl:'取消'
    	  }); */

    	// 如需增加回调函数，后面直接加 .on( function(e){} );
    	// 点击“确定” e: true
    	// 点击“取消” e: false
    	/* Modal.confirm(
    	  {
    	    msg: "确定要删除吗？"
    	  })
    	  .on( function (e) {
    	    if(e == true){
    	    	console.log("111111111111111");
    	    	var row = table.row($(this).parents('tr'));
    	        var data = row.data();
    	        console.log("222222222222222");
    	        $.ajax({
    	    		url:getUrl("web","webSubject/deleteSubject"),
    	    		type:"get",
    	    		data:{subject_id : data.subject_id},
    	    		dataType:'json',
    	    		success:function(obj)
    	    		{
    	    			console.log("333333333333333");
    	    			showModalDT(obj.msg);
    	    			deltable = table;
    	    			if(obj.resFlag == '0')
    	    			{
    	    				$("#delId").val(data.subject_id);
    	    			}
    	    		},
    	    		error:function(obj)
    	    		{
    	    			console.log("444444444444444444444");
    	    			showModal(obj.responseText);
    	    			return false;
    	    		}
    	    	});
    	    } */
    	  
    });
});

/* function del(){
	alert("22222222222222");
   
} */ 

//添加
function ajax_submit()
{
	if($("#newSubject").val() == "" || $("#newSubject").val() == null )
	{
		showModal("请填写需要添加科目!");
		return false;
	}
	var url = getUrl("web","webSubject/saveSubject");
	$.ajax({
		url:url,
		type:"post",
		data:$('#myForm').serialize(),
		dataType:'json',
		success:function(data)
		{
			showModal(data.msg);
			closeModal(getUrl("web","webSubject/subjectList")); //关闭悬浮框时再跳转页面
			return false;
		},
		error:function(data)
		{
			showModal(data.responseText);
			return false;
		}
	});
}
$(function () {
	  window.Modal = function () {
	    var reg = new RegExp("\\[([^\\[\\]]*?)\\]", 'igm');
	    var alr = $("#ycf-alert");
	    var ahtml = alr.html();

	    //关闭时恢复 modal html 原样，供下次调用时 replace 用
	    //var _init = function () {
	    //	alr.on("hidden.bs.modal", function (e) {
	    //		$(this).html(ahtml);
	    //	});
	    //}();

	    /* html 复原不在 _init() 里面做了，重复调用时会有问题，直接在 _alert/_confirm 里面做 */


	    var _alert = function (options) {
	      alr.html(ahtml);	// 复原
	      alr.find('.ok').removeClass('btn-success').addClass('btn-primary');
	      alr.find('.cancel').hide();
	      _dialog(options);

	      return {
	        on: function (callback) {
	          if (callback && callback instanceof Function) {
	            alr.find('.ok').click(function () { callback(true) });
	          }
	        }
	      };
	    };

	    var _confirm = function (options) {
	      alr.html(ahtml); // 复原
	      alr.find('.ok').removeClass('btn-primary').addClass('btn-success');
	      alr.find('.cancel').show();
	      _dialog(options);

	      return {
	        on: function (callback) {
	          if (callback && callback instanceof Function) {
	            alr.find('.ok').click(function () { callback(true) });
	            alr.find('.cancel').click(function () { callback(false) });
	          }
	        }
	      };
	    };

	    var _dialog = function (options) {
	      var ops = {
	        msg: "提示内容",
	        title: "操作提示",
	        btnok: "确定",
	        btncl: "取消"
	      };

	      $.extend(ops, options);

	      console.log(alr);

	      var html = alr.html().replace(reg, function (node, key) {
	        return {
	          Title: ops.title,
	          Message: ops.msg,
	          BtnOk: ops.btnok,
	          BtnCancel: ops.btncl
	        }[key];
	      });
	      
	      alr.html(html);
	      alr.modal({
	        width: 500,
	        backdrop: 'static'
	      });
	    }

	    return {
	      alert: _alert,
	      confirm: _confirm
	    }

	  }();
	});
</script>

