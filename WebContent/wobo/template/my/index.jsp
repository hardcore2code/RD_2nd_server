<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="../../head.jsp" flush="true"/>
<!-- Timeline CSS -->
<link href="<%=request.getContextPath() %>/wobo/css/plugins/timeline.css" rel="stylesheet">
<!-- Morris Charts CSS -->
<link href="<%=request.getContextPath() %>/wobo/css/plugins/morris.css" rel="stylesheet">
<body>
    <div id="wrapper">
	<jsp:include page="../../top.jsp" flush="true"/>
        <div id="page-wrapper">
			<div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header"></h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
        </div>
        <!-- /#page-wrapper -->
    </div>
    <!-- /#wrapper -->
	<jsp:include page="../../common.jsp" flush="true" />
    <!-- Flot Charts JavaScript -->
	<script src="<%=request.getContextPath() %>/wobo/js/plugins/flot/excanvas.min.js"></script>
	<script src="<%=request.getContextPath() %>/wobo/js/plugins/flot/jquery.flot.js"></script>
	<script src="<%=request.getContextPath() %>/wobo/js/plugins/flot/jquery.flot.pie.js"></script>
	<script src="<%=request.getContextPath() %>/wobo/js/plugins/flot/jquery.flot.resize.js"></script>
	<script src="<%=request.getContextPath() %>/wobo/js/plugins/flot/jquery.flot.tooltip.min.js"></script>
	<script src="<%=request.getContextPath() %>/wobo/js/plugins/flot/flot-data.js"></script>
</body>
<jsp:include page="../../footer.jsp" flush="true" />
<script>
function goList()
{
	window.open($("#basepath").val()+"/"+'<%=request.getSession().getAttribute("userName") %>'+"/list");
}
</script>