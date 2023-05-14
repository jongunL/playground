<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="member_profile_container">
	<form id="profile_form" onsubmit="return false;" method="POST" enctype="multipart/form-data">
		<input type="hidden" id="img_change" name="img_change" value="false">
		<input type="hidden" id="nickname_change" name="nickname_change" value="false">
		<div class="member_profile">
			<div class="profile row">
				<label for="profile_img">프로필 이미지</label>
				<div class="profile img">
					<label for="profile_img">
						<img id="profile_img_prev" src="/asset/images/profile/${memberProfile.profile}">
					</label>
					<input id="profile_img" type="file" accept="image/*" onchange="set_profile_prev(event)" name="profile_img">
				</div>
			</div>
			<div class="profile row">
				<label for="profile_nickname">닉네임</label>
				<div>
					<input type="text" value="${memberProfile.nickname}" id="profile_nickname" name="nickname">
					<div id="nickname_msg"></div>
				</div>
			</div>
		</div>
		<div class="setting_btn_area">
			<button>저장</button>
		</div>
	</form>
</div>