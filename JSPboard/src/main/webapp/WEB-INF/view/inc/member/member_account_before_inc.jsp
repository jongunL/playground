<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="member_account_container before">
	<form action="/member/account" method="POST" class="member_account_login_form">
		<div class="account_info">
			<h1>비밀번호를 다시 입력하세요.</h1>
			<div>중요한 정보에 접근하려고 합니다.<br>비밀번호를 다시 입력하세요.</div>
		</div>
		<div class="account_input">
			<input type="password" name="pwd" id="pwd" placeholder="비밀번호 입력">
		</div>
		<div class="account_btn">
			<button>확인</button>
		</div>
	</form>
</div>