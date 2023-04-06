<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>PlayGround</title>
<%@include file="/WEB-INF/view/inc/asset.jsp" %>
<style>	
</style>
</head>
<body>
	<div class="root_container">
		<%@include file="/WEB-INF/view/inc/header.jsp" %>
		<div class="content_wrapper">
			<article>
				<div class="board_article">
					<div></div>
				</div>
			</article>
			<%@include file="/WEB-INF/view/inc/util_btn.jsp" %>
		</div>
		<%@include file="/WEB-INF/view/inc/footer.jsp" %>
	</div>
	<script type="text/javascript" src="/asset/js/view_mode.js"></script>
	<script type="text/javascript">
		<c:if test="${result eq true}">
			location.href = '/board/lists?num=${category}';
		</c:if>
		<c:if test="${result eq false}">
			alert('글 작성에 실패했습니다. 잠시후 다시 시도해주세요.');
			location.href = '/board/lists?num=${category}';
		</c:if>
	</script>
</body>
</html>