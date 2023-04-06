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
		<c:choose>
			<c:when test="${result eq 1}">
				if(confirm('회원가입이 완료되었습니다. 로그인 페이지로 이동하시겠습니까?')) {
					location.href = '/login';
				} else {
					location.href = '/';	
				}
			</c:when>
			<c:otherwise>
				alert('회원가입에 실패했습니다.')
				histroy.back();
			</c:otherwise>
		</c:choose>
	</script>
</body>
</html>