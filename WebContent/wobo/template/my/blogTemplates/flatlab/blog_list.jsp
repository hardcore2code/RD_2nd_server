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
    <link rel="shortcut icon" href="<%=uri %>/img/favicon.png">
    <title>爱我吧 | ${nickName}列表</title>
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
    <!-- HTML5 shim and Respond.js IE8 support of HTML5 tooltipss and media queries -->
    <!--[if lt IE 9]>
      <script src="js/html5shiv.js"></script>
      <script src="js/respond.min.js"></script>
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
            <div class="col-lg-10 ">
            	<c:forEach items="${blogList}" var="blog">
                <div class="blog-item">
                	<c:if test="${blog.imgview ne '' and blog.imgview ne null }">
                    <div class="row">
                        <div class="col-lg-2 col-sm-2">
                            <div class="date-wrap">
                                <span class="date">${blog.wdate}</span>
                                <span class="month">${blog.wmonth}</span>
                            </div>
                            <div class="comnt-wrap">
                                <span class="comnt-ico">
                                    <i class="icon-comments"></i>
                                </span>
                                <span class="value">${blog.commentSize }</span>
                            </div>
                            <div class="comnt-wrap">
                                <span class="comnt-ico">
                                    <i class="icon-heart" style="color: #FF7373;"></i>
                                </span>
                                <span class="value">${blog.COUNT_SHARE }</span>
                            </div>
                        </div>
                        <div class="col-lg-10 col-sm-10">
                            <div class="blog-img">
                                <img src="<%=path %>${blog.imgview }" />
                            </div>
                        </div>
                    </div>
                    </c:if>
                    <div class="row">
                        <div class="col-lg-2 col-sm-2 text-right">
                        	<c:if test="${blog.imgview eq '' or blog.imgview eq null }">
                        		<div class="date-wrap">
	                                <span class="date">${blog.wdate}</span>
	                                <span class="month">${blog.wmonth}</span>
	                            </div>
	                            <div class="comnt-wrap">
	                                <span class="comnt-ico">
	                                    <i class="icon-comments"></i>
	                                </span>
	                                <span class="value">${blog.commentSize }</span>
	                            </div>
	                            <div class="comnt-wrap">
	                                <span class="comnt-ico">
	                                    <i class="icon-heart" style="color: #FF7373;"></i>
	                                </span>
	                                <span class="value">${blog.COUNT_SHARE }</span>
	                            </div>
                        	</c:if>
                            <div class="author">
                                By <a href="#">${blog.NICK_NAME }</a>
                            </div>
                            <div class="shate-view">
                                <ul class="list-unstyled">
                                    <li><a href="javascript:;">${blog.COUNT_VIEW } 浏览</a></li>
                                </ul>
                            </div>
                        </div>
                        <div class="col-lg-10 col-sm-10">
                            <h1><a href="<%=path %>/<%=request.getAttribute("userName") %>/detail/${blog.BLOG_ID}?fromList=0" id="${blog.BLOG_ID}" >${blog.TITLE }.</a></h1>
                            <p>${blog.BLOG_DESC }</p>
                            <a href="<%=path %>/<%=request.getAttribute("userName") %>/detail/${blog.BLOG_ID}?fromList=0" id="${blog.BLOG_ID}"  class="btn btn-danger">继续阅读</a>
                        </div>
                    </div>
                </div>
                </c:forEach>        
                <div class="text-center">
                    <ul class="pagination">
                        <li><a href="#">«</a></li>
                        <li><a href="<%=path %>/<%=request.getAttribute("userName") %>/list">首页</a> </li>
						<!-- 从action中获取分页页码下表的开始和结束下标 -->
						<c:forEach begin="${begin }" end="${end }" step="1" var="i">
							<c:if test="${pages.pageNumber eq i }">
								<li class="active"><a>${i }</a></li>
							</c:if>
							<c:if test="${pages.pageNumber ne i }">
								<li><a href="<%=path %>/<%=request.getAttribute("userName") %>/list/${i }">${i }</a></li>
							</c:if>
						</c:forEach>
						<!-- 显示尾页下标 -->
						<li><a href="<%=path %>/<%=request.getAttribute("userName") %>/list/${pages.totalPage }">尾页</a></li>
                        <li><a href="#">»</a></li>
                    </ul>
                </div>
            </div>
            <!--blog end-->
        </div>
    </div>
    <!--container end-->    
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
//该方法已不用
function jumpDetail(obj)
{
	window.location.href = getUrlByPara("<%=path %>/wb/blog","detail","blogId="+obj.id);
}
</script>