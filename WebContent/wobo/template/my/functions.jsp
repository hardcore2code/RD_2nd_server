<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="../../head.jsp" flush="true" />
<body>
	<div id="wrapper">
		<jsp:include page="../../top.jsp" flush="true"/>
		<!-- Page Content -->
		<div id="page-wrapper">
			<div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">新增功能</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                                                                填写功能内容
                        </div>
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-lg-6">
                                    <form role="form" name="func" id="myForm">
                                    	<input type="hidden" id="funcId" name="func.FUNC_ID" value="${func.FUNC_ID}">
                                        <div class="form-group">
                                            <label>功能名称</label>
                                            <input class="form-control" placeholder="必填" name="func.NAME" value="${func.NAME}">
                                        </div>
                                        <div class="form-group">
                                            <label>功能描述</label>
                                            <textarea class="form-control" rows="3" placeholder="可选" name="func.FUNC_DESC">${func.FUNC_DESC}</textarea>
                                        </div>
                                        <div class="form-group">
                                            <label>是否属于子菜单(只支持二级菜单)</label>
                                            <div class="checkbox">
                                                <label>
                                                    <input type="checkbox"  onchange="ifleaf();" <c:if test="${func.IFROOT eq 1 and func.FUNC_PARENT ne null and func.FUNC_PARENT ne ''}" >checked="checked"</c:if>>是
                                                </label>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label>所属菜单</label>
                                            <select class="form-control" name="func.FUNC_PARENT" <c:if test="${func.FUNC_PARENT eq null or func.FUNC_PARENT eq ''}" >disabled="disabled"</c:if>>
                                            	<option value="none">请选择</option>
                                                <c:forEach items="${funcParentList}" var="funcParent">
                                               		<option value="${funcParent.FUNC_ID}" <c:if test="${funcParent.FUNC_ID eq func.FUNC_PARENT}">selected="selected"</c:if>>${funcParent.NAME}</option>
                                               	</c:forEach>
                                            </select>
                                        </div>
                                        <div class="form-group">
                                            <label>菜单顺序</label>
                                            <input class="form-control" name="func.SORT" placeholder="必填(长度为2)" value="${func.SORT}">
                                        </div>
                                        <div class="form-group">
                                            <label>状态</label>
                                            <select class="form-control" name="func.STATUS" >
                                                <option value="0" <c:if test="${func.STATUS eq '0'}">selected="selected"</c:if>>保存</option>
                                                <option value="1" <c:if test="${func.STATUS eq '1'}">selected="selected"</c:if>>启用</option>
                                            </select>
                                        </div>
                                        <button type="button" class="btn btn-primary" onclick="ajax_submit();">保 存</button>
                                        <button type="reset" class="btn btn-success">重 置</button>
                                        <button type="button" class="btn btn-default" id="_func_funclist" onclick="jump_pub(this)">返回列表</button>
                                    </form>
                                </div>
                                <!-- /.col-lg-6 (nested) -->
                                <div class="col-lg-6">
                                    <h1>菜单属性</h1>
                                    <br>
                                    <form role="form" name="funcAttr" id="myForm2">
                                    	<div class="form-group">
                                            <label>功能ID</label>
                                            <input class="form-control" name="funcAttr.A_ID" placeholder="_模块名称_方法 " value="${funcAttr.A_ID}">
                                        </div>
                                    	<label for="disabledSelect">菜单图标</label>
                                        <div class="form-group input-group">
                                            <input type="text" class="form-control" name="funcAttr.I_CLASS" id="iclass"  placeholder="必填" <c:choose><c:when test="${funcAttr.I_CLASS ne null and funcAttr.I_CLASS ne ''}">value="${funcAttr.CLASSSHOW}"</c:when><c:otherwise>value="fa-folder"</c:otherwise></c:choose>>
                                            <span class="input-group-addon btn btn-info" onclick="showIClass();">预览</span>
                                            <span class="input-group-addon"><i id="showIclass" <c:choose><c:when test="${funcAttr.I_CLASS ne null and funcAttr.I_CLASS ne ''}">class="${funcAttr.I_CLASS}"</c:when><c:otherwise>class="fa fa-folder"</c:otherwise></c:choose>></i></span>
                                        </div>
                                        <fieldset>
                                            <div class="form-group">
                                                <label for="disabledSelect">超链接跳转</label>
                                                <input class="form-control" name="funcAttr.A_HREF"  type="text" value="#" readonly="readonly">
                                            </div>
                                            <div class="form-group">
                                                <label for="disabledSelect">默认点击事件</label>
                                                <input class="form-control" name="funcAttr.A_ONCLICK"  type="text" value="jump_pub(this);" readonly="readonly">
                                            </div>
                                        </fieldset>
                                        <input type="hidden" name="funcAttr.STATUS" value="1" >
                                    </form>
                                </div>
                                <!-- /.col-lg-6 (nested) -->
                            </div>
                            <!-- /.row (nested) -->
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
	<jsp:include page="../../modal.jsp" flush="true" />
	<jsp:include page="../../common.jsp" flush="true" />
</body>
<jsp:include page="../../footer.jsp" flush="true" />
<script>
function ifleaf()
{
	if($("select[name='func.FUNC_PARENT']").attr('disabled') == "disabled")
	{
		$("select[name='func.FUNC_PARENT']").attr('disabled',false);
		$("input[name='func.FUNC_PATH']").attr('disabled',true);
		$("input[name='func.FUNC_CONTROLLER']").attr('disabled',true);
	}
	else
	{
		$("select[name='func.FUNC_PARENT']").attr('disabled',true);
		$("input[name='func.FUNC_PATH']").attr('disabled',false);
		$("input[name='func.FUNC_CONTROLLER']").attr('disabled',false);
	}
	
}
function ajax_submit()
{
	if($("input[name='func.NAME']").val() == "")
	{
		showModal("请填写功能名称！");
		return false;
	}
	var sort_check = /^[0-9_]{2}$/i;
	var sort = $("input[name='func.SORT']").val();
	if(!sort_check.test(sort))
	{
		showModal("菜单顺序的不符标准");
		return false;
	}
	if($("select[name='func.FUNC_PARENT']").attr('disabled') == "disabled")
	{
	}else
	{
		if($("input[name='funcAttr.A_ID']").val() == "")
		{
			showModal("请填写功能ID！");
			return false;
		}
	}
	if($("input[name='funcAttr.I_CLASS']").val() == "")
	{
		showModal("请填写菜单图标！");
		return false;
	}
	if($("select[name='func.FUNC_PARENT']").attr('disabled') == "disabled")
	{
		
	}else
	{
		if($("select[name='func.FUNC_PARENT']").val() == "none")
		{
			showModal("请选择所属菜单！");
			return false;
		}
	}
	$("input[name='funcAttr.I_CLASS']").val(trimBoth($("#iclass").val()));
	var url = getUrl("wb","func/saveFunc");
	$.ajax({
		url:url,
		type:"post",
		data:$('#myForm').serialize()+"&"+$('#myForm2').serialize(),
		dataType:'json',
		success:function(data)
		{
			showModal(data.msg);
			if(data.resFlag == "0")
			{
				$("#funcId").val(data.funcId);
				if($("select[name='func.STATUS']").val()=='1' && $("select[name='func.FUNC_PARENT']").attr("disabled"))
				{
					$("select[name='func.FUNC_PARENT']").append("<option value='"+data.funcId+"'>"+$("input[name='func.NAME']").val()+"</option>");
				}
			}
			return false;
		},
		error:function(data)
		{
			showModal(data.responseText);
			return false;
		}
	});
}
//预览图标
function showIClass()
{
	$("#showIclass").attr("class","fa "+trimBoth($("#iclass").val()));
}
</script>