<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- 게시판 소개 -->
<div class="board_title">
	<div class="board_title_info">
		<a href="/board/lists?num=${boardTitle.boardTitleSeq}" id="board_title_img"><img src="/asset/images/board_img/${boardTitle.boardTitleImg}"></a>
		<div class="board_title_description">
			<h1><a href="/board/lists?num=${boardTitle.boardTitleSeq}">${boardTitle.boardTitle}</a></h1>
			<div>
				<div>${boardTitle.boardTitleDescription}</div>
				<div>구독자수 : ${boardTitle.boardTitleSubscriberCount}</div>
			</div>
		</div>
	</div>
	<div class="board_title_btns">
		<button class="subscribe_btn" onclick="subscribe(${boardTitle.boardTitleSeq})">
			<c:choose>
				<c:when test="${subscribe eq true}">
				<span class="subscribe_info subscribe">구독중</span>
				<span id="heart">
					<i class="fa-solid fa-heart"></i>
				</span>
				</c:when>
				<c:otherwise>
				<span class="subscribe_info">구독하기</span>
				<span id="heart">
					<i class="fa-regular fa-heart"></i>
				</span>
				</c:otherwise>
			</c:choose>
		</button>
	</div>
</div>