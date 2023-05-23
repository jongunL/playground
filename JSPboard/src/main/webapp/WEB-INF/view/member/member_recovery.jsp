<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>PlayGround</title>
<%@include file="/WEB-INF/view/inc/asset.jsp" %>
<style>	
	.member_recovery_container {
		display: flex;
		justify-content: center;
		margin: 0.25rem 0.75rem;
		margin-top: 0.75rem;
	}
	
	.member_recovery_container > .member_recovery_from {
		flex: 1;
		max-width: 70%;
		border: 1px solid #aaa;
		border-radius: 5px;
		box-sizing: border-box;
		padding: 1rem 0.75rem;
	}
	
	.member_recovery_from > h1 {
		font-weight: normal;
		font-size: 1.5rem;
	}
	
	.member_recovery_from > .member_recovery_btn {
		margin-top: 0.75rem;
		text-align: right;
	}
	
	.member_recovery_from > .member_recovery_contents {
		margin-top: 0.75rem;
	}
	
	.member_recovery_from > .member_recovery_btn > button {
		padding: 0.5rem 1.75rem;
	}
	
	@media screen and (max-width: 1000px) {
	.member_recovery_container > .member_recovery_from {
		max-width: 100%;
	}
</style>
</head>
<body>
	<div class="root_container">
		<%@include file="/WEB-INF/view/inc/header.jsp" %>
		<div class="content_wrapper">
			<article>
				<div class="board_article">
					<div class="member_recovery_container">
						<div class="member_recovery_from">
							<h1>PlayGround</h1>
							<div class="member_recovery_contents">
								<div>인증 메일을 전송했습니다. 메일의 링크를 클릭해서 나머지 절차를 진행해주세요.</div>
							</div>
							<div class="member_recovery_btn">
								<button onclick="location.href='/'">초기 화면으로</button>
							</div>
						</div>
					</div>
				</div>
			</article>
			<%@include file="/WEB-INF/view/inc/util_btn.jsp" %>
		</div>
		<%@include file="/WEB-INF/view/inc/footer.jsp" %>
	</div>
	<script type="text/javascript" src="/asset/js/view_mode.js"></script>
</body>
</html>