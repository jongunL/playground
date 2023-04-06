<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<header>
	<div id="header_info">
		<a id="header_logo" href="/">
			<img id="header_logo_img" alt="main logo" src="/asset/images/logo.png" draggable="false">
			<span>PlayGround!</span>
		</a>
		<a href="#" class="navbar_togleBtn">
			<i class="fas fa-bars"></i>
		</a>
	</div>
	<div>
	</div>
	<ul id="header_menu">
		<li><a href="/searchCh">채널검색</a></li>
		<!-- 로그인 되어있는 경우 -->
		<c:if test="${((not empty cookie.auth.value) and (cookie.auto.value eq 'true')) or (not empty sessionScope.auth)}">
		<li class="mobile change"><a href="#" onclick="logout()">로그아웃</a></li>
		<li class="mobile change"><a href="#">구독채널</a></li>
		<li class="mobile change"><a href="#">마이페이지</a></li>
		<li id="header_alarm">
			<a class="desktop change" href="#"><i class="fa-regular fa-bell"></i></a>
			<a class="mobile change" href="#">알림</a>
		</li>
		<li id="header_profile_img">
			<a href="#"><img src="/asset/images/profile/default01.jpg"></a>
			<span class="mobile change" id="header_profile_intro">
				<span>닉네임</span>
				<span>아이디</span>
			</span>
			<span id="menu_toggle_btn" class="desktop change">
				<i class="fa-solid fa-angle-down"></i>
			</span>
		</li>
		<ul class="desktop change sub_menu" id="header_sub_menu">
			<li><a href="#">마이페이지</a></li>
			<li><a href="#">구독채널</a></li>
			<li><a href="#" onclick="logout()">로그아웃</a></li>
		</ul>		
		</c:if>
		<!-- 로그인 안되어있는 경우 -->
		<c:if test="${((empty cookie.auth.value) or (cookie.auto.value ne 'true')) and (empty sessionScope.auth)}">
		<li><a href="/register">회원가입</a></li>
		<li><a href="/login">로그인</a></li>	
		</c:if>
	</ul>
</header>