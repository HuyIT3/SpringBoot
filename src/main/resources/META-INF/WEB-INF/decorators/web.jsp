<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file ="/common/taglib.jsp" %>
<c:url value="/" var="URL"></c:url>

<!DOCTYPE html>

<head>
  <meta charset="utf-8">
  <title>Shop Online</title>

  
</head>
<body>

<body class = "header-fixed">
	<div class = "wrapper">
		<%@ include file="/common/web/header.jsp" %>
			<!-- body -->
				<sitemesh:write property="body"/>
			<!-- body -->
		<!--  -->
		<jsp:include page="/common/web/footer.jsp"></jsp:include>
	</div>
</body>

</body>	
</html>