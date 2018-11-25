<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String uri = request.getRequestURI();
uri = uri.substring(0, uri.lastIndexOf("/"));
String path = request.getContextPath();  
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;  
%>
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="Mosaddek">
    <meta name="keyword" content="FlatLab, Dashboard, Bootstrap, Admin, Template, Theme, Responsive, Fluid, Retina">
    <link rel="shortcut icon" href="img/favicon.png">
    <title>爱我吧 | ${blog.TITLE}-${blog.NICK_NAME}</title>
    <!-- Bootstrap core CSS -->
    <link href="<%=uri %>/css/bootstrap.min.css" rel="stylesheet">
    <link href="<%=uri %>/css/theme.css" rel="stylesheet">
    <link href="<%=uri %>/css/bootstrap-reset.css" rel="stylesheet">
    <!--external css-->
    <link href="<%=uri %>/assets/font-awesome/css/font-awesome.css" rel="stylesheet" />
    <link rel="stylesheet" href="<%=uri %>/css/flexslider.css"/>
    <link href="<%=uri %>/assets/bxslider/jquery.bxslider.css" rel="stylesheet" />
    <!-- Custom styles for this template -->
    <link href="<%=uri %>/css/style.css" rel="stylesheet">
    <link href="<%=uri %>/css/style-responsive.css" rel="stylesheet" />
    <script src="<%=path %>/wobo/js/share.js"></script>
    <!-- HTML5 shim and Respond.js IE8 support of HTML5 tooltipss and media queries -->
    <!--[if lt IE 9]>
      <script src="<%=uri %>/js/html5shiv.js"></script>
      <script src="<%=uri %>/js/respond.min.js"></script>
    <![endif]-->
  </head>
  <input type="hidden" id="basepath" value="<%=basePath %>" />
  <body>
    <br>
    <br>
    <!--container start-->
    <div class="container">
        <div class="row">
            <!--blog start-->
            <div class="col-lg-10">
                <div class="blog-item">
                    <div class="row">
                        <div class="col-lg-2 col-sm-2 text-right">
                        	<div class="date-wrap">
                                <span class="date">${blog.wdate}</span>
                                <span class="month">${blog.wmonth}</span>
                            </div>
                            <div class="comnt-wrap">
                                <span class="comnt-ico">
                                    <i class="icon-comments"></i>
                                </span>
                                <span class="value">${commentSize }</span>
                            </div>
                            <div class="comnt-wrap" >
                                <span class="comnt-ico" id="${blog.BLOG_ID}" onclick="loveIt(this);" onmousedown="loveitbtndown(this);" onmouseup="loveitbtnon(this);">
                                    <i class="icon-heart" style="color: #FF7373;" title="我爱她"  ></i>
                                </span>
                                <span class="value">${blog.COUNT_SHARE }</span>
                            </div>
                            <div class="author">
                                By <a href="#">${blog.NICK_NAME }</a>
                            </div>
                            <div class="shate-view">
                                <ul class="list-unstyled">
                                    <li><a href="javascript:;">${blog.COUNT_VIEW } 浏览</a></li>
                                    <c:if test="${ifFromList eq '0'}">
                                    <li><a href="<%=path %>/<%=request.getAttribute("userName") %>/list">返回列表</a></li>
                                	</c:if>
                                </ul>
                            </div>
                        </div>
                        <div class="col-lg-10 col-sm-10">
                            <h1 id="wbTitle" >${blog.TITLE}</h1>
                            <div  id="wbText"></div>
                            <hr>
                            <div class="post-comment">
                                <h3 class="skills">填写评论</h3>
                                <form class="form-horizontal" role="form" id="commentForm" name="myComment">
                                	<input type="hidden" id="checkMathRes" name="myComment.checkMathRes" value="${checkMathRes}">
                                	<input type="hidden" id="blogId" name="myComment.blogId" value="${blog.BLOG_ID}">
                                    <div class="form-group">
                                    	<div class="col-lg-3 input-group">
                                        	<span class="input-group-addon">@</span>
                                            <input type="text" value="${blog.NICK_NAME }" id="toUser" name="myComment.toUser" class="col-lg-12 form-control" readonly="readonly">
                                        	<input type="hidden" id="toUserId" name="myComment.toUserId" value="${blog.USER_ID}">
                                        </div>
                                        <div class="col-lg-3">
                                        	<c:choose>
                                        		<c:when test="${sessionScope.nickName == null or sessionScope.nickName == ''}">
                                        			<input type="text" id="commentName" name="myComment.nickName" placeholder="贵姓" class="col-lg-12 form-control">
                                        		</c:when>
                                        		<c:otherwise>
                                        			<input type="text" id="commentName" name="myComment.nickName" value="${sessionScope.nickName }" readonly="readonly"  placeholder="贵姓" class="col-lg-12 form-control">
                                        		</c:otherwise>
                                        	</c:choose>
                                        	<input type="hidden" id="userId" name="myComment.userId" value="<%=request.getSession().getAttribute("userId")%>">
                                        </div>
                                        <div class="col-lg-3">
                                            <select class="form-control" name="myComment.gender" id="gender">
                                            	<option value="0">性别</option>
                                            	<option value="1">男</option>
                                            	<option value="2">女</option>
                                            </select>
                                        </div>
                                        <div class="col-lg-3 input-group">
                                        	<span class="input-group-addon" id="mathShow">${checkMathShow }</span>
                                            <input id="checkMath" type="text" name="myComment.yourCheckRes"  placeholder="请输入结果" class="col-lg-12 form-control">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <div class="col-lg-12">
                                            <textarea placeholder="内容,仅限20个字" id="yourComment" name="myComment.content" rows="8" class="form-control"></textarea>
                                        </div>
                                    </div>
                                    <p>
                                        <button type="button" onclick="ajax_sub()" class="btn btn-danger pull-right">提交</button>
                                    </p>
                                </form>
                            </div>
                            <div class="media">
                                <h3>评论</h3>
                                <div id="myComments">
                                <c:forEach items="${commentList}" var="comment">
	                                <hr>
	                                <a class="pull-left" href="javascript:;">
	                                    <img class="media-object"  <c:choose><c:when test="${comment.headImgFlag == '0'}">src="<%=uri %>/${comment.headImg}"</c:when><c:otherwise>src="${ctx}${comment.headImg}"</c:otherwise></c:choose> alt="">
	                                </a>
	                                <div class="media-body">
	                                    <h4 class="media-heading">
	                                        ${comment.NICK_NAME } <span>|</span>
	                                        <span>${comment.REPLY_DATE } </span>
	                                    </h4>
	                                    <p>@${comment.TO_USER }  : ${comment.COMMENT }</p>
	                                    <a  onclick="towho(this);" class="btn btn-default btn-xs">回复</a>
	                                    <input type="hidden" name="commentUserId" value="${comment.USER_ID }">
	                                    <input type="hidden" name="commentNickName" value="${comment.NICK_NAME }">
	                                </div>
                                </c:forEach>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!--blog end-->
        </div>
    </div>
    <!--container end-->
    <!-- Modal -->
    <jsp:include page="modal.jsp" flush="true" />
 	<!-- js placed at the end of the document so the pages load faster -->
    <script src="<%=uri %>/js/jquery.js"></script>
    <script src="<%=uri %>/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="<%=uri %>/js/hover-dropdown.js"></script>
    <script defer src="<%=uri %>/js/jquery.flexslider.js"></script>
    <script type="text/javascript" src="<%=uri %>/assets/bxslider/jquery.bxslider.js"></script>
    <script src="<%=uri %>/js/jquery.easing.min.js"></script>
    <script src="<%=uri %>/js/link-hover.js"></script>
    <!--common script for all pages-->
    <script src="<%=uri %>/js/common-scripts.js"></script>
    <script src="<%=path %>/wobo/js/common.js"></script>
	<script>
  </script>
  </body>
</html>
<script>
$(document).ready(function() {
	var text = '${blog.TEXT}';
	if(text != "")
	{
		$('#wbText').html(text);
	}
});
var old_html = "";
var add_comment = "";
function ajax_sub()
{
	if($("#commentName").val() == null || $("#commentName").val() == "")
	{
		showWbModal("#myBlogModal","#modalBlog_val","请输入您的名称!");
		return false;
	}
	if($("#gender").val() == null || $("#gender").val() == "" || $("#gender").val() == "0")
	{
		showWbModal("#myBlogModal","#modalBlog_val","请选择您的性别!");
		return false;
	}
	if($("#checkMath").val() != $("#checkMathRes").val())
	{
		showWbModal("#myBlogModal","#modalBlog_val","您输入运算结果不正确!");
		return false;
	}
	if($("#yourComment").val() == null || $("#yourComment").val() == "" )
	{
		showWbModal("#myBlogModal","#modalBlog_val","请输入评论!");
		return false;
	}
	if($("#yourComment").val().length > 20 )
	{
		showWbModal("#myBlogModal","#modalBlog_val","输入评论过长!");
		return false;
	}
	$.ajax({
		url:getUrl("<%=request.getAttribute("userName") %>","postComment"),
		type:"post",
		data:$('#commentForm').serialize(),
		dataType:'json',
		success:function(data)
		{
			if(data.resFlag == "0")
			{
				$("#checkMathRes").val(data.checkMathRes);
				$("#mathShow").text(data.checkMathShow);
				$("#checkMath").val("");
				old_html = $("#myComments").html();
				if(data.headImgFlag == '0')
				{
					add_comment = "<hr><a class='pull-left' href='javascript:;'><img class='media-object' src='<%=uri %>/"+data.headImg+"' id='addImg' alt=''></a><div class='media-body'><h4 class='media-heading'>"+data.nickName+"<span>|</span><span>"+data.replyDate+"</span></h4><p>@"+data.toUser+"  : "+data.comment +"</p><a  onclick='towho(this)' class='btn btn-default btn-xs'>回复</a><input type='hidden' name='commentUserId' value='"+data.userId+"'><input type='hidden' name='commentNickName' value='"+data.nickName+"'></div>";
				}else
				{
					add_comment = "<hr><a class='pull-left' href='javascript:;'><img class='media-object' src='<%=path %>"+data.headImg+"' id='addImg' alt=''></a><div class='media-body'><h4 class='media-heading'>"+data.nickName+"<span>|</span><span>"+data.replyDate+"</span></h4><p>@"+data.toUser+"  : "+data.comment +"</p><a  onclick='towho(this)' class='btn btn-default btn-xs'>回复</a><input type='hidden' name='commentUserId' value='"+data.userId+"'><input type='hidden' name='commentNickName' value='"+data.nickName+"'></div>";
				}
			}
			showWbModal("#myBlogModal","#modalBlog_val",data.msg);
		},
		error:function(data)
		{
			showWbModal("#myBlogModal","#modalBlog_val",data.responseText);
			return false;
		}
	});
}
//回复特定一个人
function towho(obj)
{
	$("#toUserId").val($(obj).next().val());
	$("#toUser").val($(obj).next().next().val());
	$("#commentName").focus();
}
$('#myBlogModal').on('hide.bs.modal', function(obj)
{
	$("#myComments").html(add_comment+old_html);
	old_html = "";
	add_comment = "";
});
//点赞
function loveIt(obj)
{
	$.ajax({
		url:getUrl("<%=request.getAttribute("userName") %>","loveit"),
		type:"get",
		data:{blogId:obj.id},
		dataType:'json',
		success:function(data)
		{
			$(obj).next().text(data.loveitnum);
		},
		error:function(data)
		{
			showWbModal("#myBlogModal","#modalBlog_val",data.responseText);
			return false;
		}
	});
}
function loveitbtndown(obj)
{
	$(obj).attr("class","value");
}
function loveitbtnon(obj)
{
	$(obj).attr("class","comnt-ico");
}
</script>

<script>
window._bd_share_config={"common":{"bdSnsKey":{},"bdText":"","bdMini":"1","bdMiniList":["mshare","tsina","weixin","renren","tieba","douban","sqq","mail","copy","print"],"bdPic":"","bdStyle":"0","bdSize":"16"},"slide":{"type":"slide","bdImg":"3","bdPos":"right","bdTop":"100"},"image":{"viewList":["qzone","tsina","tqq","renren","weixin"],"viewText":"分享到：","viewSize":"16"},"selectShare":{"bdContainerClass":null,"bdSelectMiniList":["qzone","tsina","tqq","renren","weixin"]}};
</script>