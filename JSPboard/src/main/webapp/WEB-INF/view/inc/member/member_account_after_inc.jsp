<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="member_account_container after">
	<div class="member_security mac row">
		<h1>보안<i class="fa-solid fa-lock"></i></h1>
		<div class="ms row">
			<div class="mac col">비밀번호</div>
			<div class="member_security_btn mac col">
				<button onclick="location.href='/member/changePassword'">비밀번호 변경</button>
			</div>
		</div>
	</div>
</div>