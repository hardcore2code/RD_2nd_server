<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="../../head.jsp" flush="true" />
<body>
	<div id="wrapper">
	<jsp:include page="../../top.jsp" flush="true"/>
		<!-- Page Content -->
		<div id="page-wrapper">
			<div class="container-fluid">
				<div class="row">
					<div class="col-lg-12">
						<h1 class="page-header">变更主题</h1>
					</div>
					<!-- /.col-lg-12 -->
				</div>
				<!-- /.row -->
			</div>
			<!-- /.container-fluid -->
			<!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                        </div>
                        <div class="panel-body">
                        	<div class="row">
                        		<div class="col-lg-6">
                                    <h1></h1>
                                    <form role="form">
                                        <fieldset>
                                            <div class="form-group">
                                                <label for="disabledSelect">主题列表</label>
                                                <select id="templateId" class="form-control">
                                                	<c:forEach items="${templateList}" var="template">
                                                		<option value="${template.TEMPLATE_ID}" <c:if test="${template.TPSTATUS eq '1'}">selected="selected"</c:if>>${template.NAME}</option>
                                                	</c:forEach>
                                                </select>
                                            </div>
                                            <br/>
                                            <br/>
                                            <button type="button" class="btn btn-primary" onclick="ajax_exchange();">变更</button>
                                            <button type="button" class="btn" onclick="ajax_delete();">删除</button>
                                        </fieldset>
                                    </form>
                                </div>
                                <!-- /.col-lg-6 (nested) -->
                        	</div>
                        </div>
                     </div>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
		</div>
		<!-- /#page-wrapper -->
	</div>
	<jsp:include page="../../modal.jsp" flush="true" />
	<jsp:include page="../../common.jsp" flush="true" />
</body>
<jsp:include page="../../footer.jsp" flush="true" />
<script>
function ajax_exchange()
{
	var url = getUrl("wb","template/exchangesp");
	$.ajax({
		url:url,
		type:"get",
		data:{templateId:$("#templateId").val()},
		dataType:'json',
		success:function(data)
		{
			showModal(data.msg);
			return false;
		},
		error:function(data)
		{
			showModal(data.responseText);
			return false;
		}
	});
}

function ajax_delete()
{
	var url = getUrl("wb","template/del");
	$.ajax({
		url:url,
		type:"get",
		data:{templateId:$("#templateId").val()},
		dataType:'json',
		success:function(data)
		{
			showModal(data.msg);
			$("#templateId option[value="+data.id+"]").remove(); 
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