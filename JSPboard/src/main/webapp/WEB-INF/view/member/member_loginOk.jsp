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
			<c:when test="${(success eq true) and (active eq 'y')}">
				alert('로그인이 완료되었습니다.');
				location.href = '/';
			</c:when>
			<c:when test="${(success eq true) and (active eq 'n')}">
				if(confirm('비활성화된 계정입니다. 비밀번호 변경시 다시 활성화 할 수 있습니다. 다시 활성화 하시겠습니까?')) {
					location.href = '/member/recoverPw?code=${code}&active=${active}';
				} else {
					location.href = '/';
				}
			</c:when>
			<c:otherwise>
				alert('로그인에 실패했습니다.')
				history.back();
			</c:otherwise>
		</c:choose>
	</script>
</body>
</html>