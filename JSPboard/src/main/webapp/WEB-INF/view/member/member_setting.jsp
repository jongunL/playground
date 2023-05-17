<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>PlayGround</title>
<%@include file="/WEB-INF/view/inc/asset.jsp" %>
<link rel="stylesheet" href="/asset/css/member/setting.css">
</head>
<body>
	<div class="root_container">
		<%@include file="/WEB-INF/view/inc/header.jsp" %>
		<div class="content_wrapper">
			<article>
				<div class="board_article">
					<div class="member_setting_container">
						<div class="member_setting_n_bar">
							<h1>설정</h1>
							<ul>
								<li id="mp"><a href="/member/profile">프로필</a></li>
								<li id="al"><a href="/member/alarm">알림</a></li>
								<li id="bd"><a href="/member/board">작성글</a></li>
								<li id="cm"><a href="/member/comment">작성댓글</a></li>
								<li id="ac"><a href="/member/account">계정</a></li>
							</ul>
						</div>
						<c:if test="${path == '/member/profile'}">
							<jsp:include page="/WEB-INF/view/inc/member/member_profile_inc.jsp">
								<jsp:param value="${memberProfile}" name="memberProfile"/>
							</jsp:include>
							<script type="text/javascript" src="/asset/js/member/profile.js"></script>
						</c:if>
						<c:if test="${path == '/member/alarm'}">
							<%@include file="/WEB-INF/view/inc/member/member_created_inc.jsp" %>
						</c:if>
						<c:if test="${path == '/member/board'}">
							<%@include file="/WEB-INF/view/inc/member/member_created_inc.jsp" %>
						</c:if>
						<c:if test="${path == '/member/comment'}">
							<%@include file="/WEB-INF/view/inc/member/member_created_inc.jsp" %>
						</c:if>
						<c:if test="${path == '/member/account' and result != true}">
							<%@include file="/WEB-INF/view/inc/member/member_account_before_inc.jsp" %>							
						</c:if>
						<c:if test="${path == '/member/account' and result == true }">
							<%@include file="/WEB-INF/view/inc/member/member_account_after_inc.jsp" %>
						</c:if>
					</div>
				</div>
			</article>
			<%@include file="/WEB-INF/view/inc/util_btn.jsp" %>
		</div>
		<%@include file="/WEB-INF/view/inc/footer.jsp" %>
	</div>
	<script type="text/javascript" src="/asset/js/view_mode.js"></script>
	<script type="text/javascript" src="/asset/js/member/setting.js"></script>
</body>
</html>