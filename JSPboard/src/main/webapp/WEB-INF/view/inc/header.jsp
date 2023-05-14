<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<header>
	<div id="header_info">
		<a id="header_logo" href="/">
			<img id="header_logo_img" alt="main logo" src="/asset/images/logo.png" draggable="false">
			<span>PlayGround!</span>
		</a>
		<a href="javascript:;" class="navbar_togleBtn">
			<i class="fas fa-bars"></i>
		</a>
	</div>
	<div>
	</div>
	<ul id="header_menu">
		<li><a href="/board/search">채널검색</a></li>
		<!-- 로그인 되어있는 경우 -->
		<c:if test="${((not empty cookie.auth.value) and (cookie.auto.value eq 'true')) or (not empty sessionScope.auth)}">
		<li class="mobile change"><a href="javascript:;" onclick="logout()">로그아웃</a></li>
		<li class="mobile change"><a href="/board/subscribed">구독채널</a></li>
		<li class="mobile change"><a href="/member/profile">마이페이지</a></li>
		<li id="header_alarm">
			<a class="desktop change" href="javascript:;"><i class="fa-regular fa-bell"></i></a>
			<a class="mobile change" href="javascript:;">알림</a>
			<span id="header_alarm_counter"></span>
			<ul class="sub_menu" id="header_alarm_sub_menu">
				<li id="alarm_sub_menu_h">
					<div>알림</div>
					<div id="alarm_all_ck">알림전체삭제</div>
				</li>
				<!-- 태그 동적생성 -->
			</ul>
		</li>
		<li id="header_profile_img">
			<a href="javascript:;"><img id="header_profile_img_show" src="/asset/images/profile/default01.jpg"></a>
			<span class="mobile change" id="header_profile_intro">
				<span id="nickname">닉네임</span>
				<span id="regdate">가입일자</span>
			</span>
			<span id="menu_toggle_btn" class="desktop change">
				<i class="fa-solid fa-angle-down"></i>
			</span>
			<ul class="desktop change sub_menu" id="header_sub_menu">
				<li><a href="/member/profile">마이페이지</a></li>
				<li><a href="/board/subscribed">구독채널</a></li>
				<li><a href="javascript:;" onclick="logout()">로그아웃</a></li>
			</ul>	
		</li>
		</c:if>
		<!-- 로그인 안되어있는 경우 -->
		<c:if test="${((empty cookie.auth.value) or (cookie.auto.value ne 'true')) and (empty sessionScope.auth)}">
		<li><a href="/register">회원가입</a></li>
		<li><a href="/login">로그인</a></li>	
		</c:if>
	</ul>
</header>