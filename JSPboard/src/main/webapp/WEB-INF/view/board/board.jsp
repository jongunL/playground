<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>PlayGround</title>
<%@include file="/WEB-INF/view/inc/asset.jsp" %>
<link rel="stylesheet" href="/asset/css/board_title.css">
<style>		
	/* 최근방문 목록 */
	.channel_visit_history {
		margin-top: 15px;
		padding-bottom: 15px;
		border-bottom: 1px solid black;
	}
	
	.vlist_category_list {
		display: flex;
		background-color: #f5f5f5;
		padding: 5px;
	}
	.vlist_category_list > .v_category {
		margin-right: 20px;
	}
	
	.vlist_category_list > .v_category > a {
		margin-right: 2.5px;
	}
	
	.vlist_category_list > .v_category > a:hover {
		text-decoration: underline;
	}
	
	.vlist_category_list > .v_category > i {
		cursor: pointer;
	}
	
	.vlist_info {
		margin-right: 10px;
	}
	
	/* 게시판 버튼 */
	.board_btns .sub_category a,
	.board_btns button,
	.board_show_amout {
		cursor: pointer;
	}
	
	/* 활성화시 */
	.board_btns > .board_category > .category > button.on {
		background-color: black;
		color: white;
	}
	
	.board_btns > .board_category .sub_category > a.on {
		font-weight: bold;
	}
	
	/* 기존 */
	.board_btns {
		display: flex;
		justify-content: space-between;
		margin-top: 15px;
		padding-bottom: 15px;
	}
	
	.board_btns > .board_category {
		display: flex;
		align-items: flex-end;
	}
	.board_btns button {
		width: 82px;
		height: 32px;
		border: 1px solid #ccc;
		background-color: #fff;
	}
	
	.board_btns .board_show_amout {
		height: 32px;
	}
	
	.board_btns button:hover {
		border-color: black;
	}
	
	.board_btns > .board_category .sub_category {
		margin-top: auto;
	}
		
	.board_btns > .board_category .sub_category> a:hover {
		text-decoration: underline;
	}
	
	.board_btns > .board_category > .category {
		margin-right: 15px;
		white-space: nowrap;
	}
	/* 게시판 글 양식*/
	.board_view_container {
		width: 100%;
	}
	.board_view_container .board_view_header,
	.board_view_container .board_view_contents,
	.board_view_comments {
		padding: 0.75rem 1rem;	
	}
	
	.board_view_container .board_view_header {
		margin-top: 2.5rem;
		border-top: 3px solid black;
		border-bottom: 1px solid #ccc;
	}
	.board_view_container .board_view_header .board_view_title {
		font-size: 1rem;
	}
	.board_view_container .board_view_header .board_view_status {
		display: flex;
		justify-content: space-between;
		font-size: 0.9rem;
		margin-top: 0.75rem;
	}
	.board_view_container > .board_view_header {
		display: flex;
	}
	.board_view_container > .board_view_header > .board_view_body {
		flex: 1;
	}
	.board_view_header > .board_author_profile {
		display: flex;
		justify-content: center;
		align-items: center;
	}
	
	.board_view_header > .board_author_profile > img {
		width: 3rem;
		height: 3rem;
		margin-right: 1rem;
		border-radius: 50%;
	}
	
	.board_view_status > div > span {
		display: inline-block;
	}
	.board_view_status > div > span::after {
		content: "|";
		padding: 0 0.5rem;
	}
	.board_view_status > div > span:last-child:after {
		content: none;
	}
	.board_view_info > span > a {
		background-color: #ddd;
		border-radius: 15px;
		padding: 0.15rem 0.3rem;
	}
	
	.board_view_contents {
		margin-top: 1.25rem;
	}
	.board_view_contents .board_recommend_btns {
		display: flex;
		justify-content: center;
		margin: 3.5rem 0;
	}
	.board_view_contents .board_recommend_btns > button {
		width: 4.5rem;
		height: 3rem;
		border-radius: 5px;
		margin: 0.3rem;
		cursor: pointer;
		outline: none;
		background-color: transparent;
		border: 1px solid #bbb;
	}
	.board_view_contents img {
		max-width: 100%;
		max-height: 100%;
	}
	
	.board_view_contents .board_recommend_btns > button:hover {
		background-color: #eee;
	}
	.board_view_contents .board_recommend_btns > button:first-child {
		color: tomato;
	}
	.board_view_contents .board_recommend_btns > button:last-child {
		color: cornflowerblue;
	}
	/* 게시판 댓글 양식 */
	.board_comments_util {
		margin-bottom: 0.5rem;
	}
	
	.board_comments_util > .board_comment_sort,
	.board_comments_util > .board_comment_sort > ul {
		display: flex;
		align-items: flex-end;
	}

	.board_comments_util > .board_comment_sort > ul > li {
		margin-left: 0.75rem;
	}	
	.board_comment_sort > ul > li > a {
		font-size: 0.8rem;
		color: #1f1f1f;
	}
	
	.board_comment_sort > ul > li > a,
	.board_comment_sort > h4 > span {
		display: inline-block;
	}
	
	.board_comment_container {
		border-top: 3px solid black;
	}
	.board_comment_container .comment_list .cmt.row {
		display: flex;
		flex-direction: column;
	}
	
	.board_comment_container .comment_list .cmt.row {
		border-bottom: 1px solid #ccc;
	}
	
	.board_comment_container .comment_list .cmt.row.reply {
		position: relative;
		border-top: 1px solid #ccc;
		border-bottom: 1px solid #ccc;
	}
	
	.board_comment_container .comment_list .cmt.row.reply .board_comment_area {
		margin-left: 3rem;
	}
	
	/* 답글쓰기폼 css */
	.comment_view_data.reply {
		padding-left: 3rem;
		background-color: #eee;
	}
	
	.comment_view_data.reply > .comment_submit_container {
		display: none;
	}
	
	.comment_view_data.reply > .comment_submit_container.show {
		display: block;
	}
	
	.board_comment_container .comment_list .cmt.row.reply .board_comment_area:before {
		content: "\f3e5";
		font-family: "Font Awesome 6 Free";
		font-weight: 900;
		margin-right: 10px;
		position: absolute;
		transform: rotate(180deg);
		top: 1rem;
		left: 1rem;
	}
		
	.board_comment_container > .comment_list > .cmt.row > .board_comment_area {
		display: flex;
		padding: 1rem 0.4rem;
	}
	
	.board_comment_area > .comment_author_profile {
		display: flex;
		justify-content: center;
		align-items: flex-start;
	}
	
	.board_comment_area > .comment_author_profile > img {
		width: 3rem;
		height: 3rem;
		border-radius: 50%;
		margin-right: 1rem;
	}
	
	.board_comment_area,
	.board_comment_area > .cmt.info {
		flex: 1;
	}
	
	.board_comment_area > div > .board_comment_author {
		display: flex;
	}
	.board_comment_author > #comment_nickname,
	.board_comment_author > .cmt.auth_ck,
	.board_comment_author > .cmt.regdate,
	.board_comment_author > .cmt.fixed {
		display: flex;
		justify-content: center;
		align-items: center;
		line-height: 1.08rem;
		font-size: 1rem;
	}
	.board_comment_author > .cmt.auth_ck:before,
	.board_comment_author > .cmt.auth_ck:after {
		content: "·";
		font-size: 1.4rem;
		display: inline-block;
		vertical-align: middle;
	}
	
	.board_comment_author > #comment_nickname {
		margin-right: 0.5rem;
	}
	.board_comment_author > .cmt.auth_ck,
	.board_comment_author > .cmt.regdate,
	.board_comment_author > .cmt.fixed {
		font-size: 0.9rem;
	}
	
	.board_comment_author > .cmt.auth_ck {
		margin-right: 0.5rem;
		color: red;
	}
	.board_comment_author > .cmt.regdate,
	.board_comment_author > .cmt.fixed {
		color: #aaa;
	}
	.board_comment_area > div > .board_comment_contents {
		margin-top: 0.4rem;
	}
	
	.board_comment_area > div > .board_comment_bnts {
		display: flex;
		justify-content: space-between;
		margin-top: 0.5rem;
	}
	.board_comment_bnts > .comment_curd_btns {
		display: flex;
		font-size: 0.9rem;
		color: #1f1f1f;
	}
	.board_comment_bnts > .comment_curd_btns > div {
		margin-right: 0.25rem;
		font-size: 0.8rem;
		display: flex;
		justify-content: center;
		align-items: center;
		cursor: pointer;
	}
	
	.board_comment_bnts > .comment_recommend_btns > button {
		border: 1px solid #ccc;
		background-color: white;
		outline: none;
		padding: 0.25rem 0.5rem;
		cursor: pointer;
	}
	.board_comment_bnts > .comment_recommend_btns > button:hover {
		background-color: #eee;
	}
	
	.board_comment_bnts > .comment_recommend_btns > button:first-child {
		color: tomato;
	}
	.board_comment_bnts > .comment_recommend_btns > button:last-child {
		color: cornflowerblue;
	}
	/* 게시판 글 댓글 페이징양식 */
	.comment_paging_box {
		display: flex;
		justify-content: center;
		align-items: center;
		padding: 1.75rem 0;
	}
	.comment_paging_box > .comment_pageing > button {
		width: 1.5rem;
		height: 1.5rem;
		cursor: pointer;
		background-color: white;
		border: 1px solid #ccc;
	}
	.comment_paging_box > .comment_pageing > button:hover {
		background: #eee;
	}
	
	/* 게시판 글 댓글 작성양식 */
	.comment_view_data {
		background-color: #eee;		
	}
	
	.comment_submit_container {
		padding: 0.75rem 1rem;
	}
	
	.board_view_comments > .comment_view_data > .comment_submit_container {
		display: flex;
		flex-direction: column;
		border-top: 3px solid black;
		border-bottom: 3px solid black;
	}
	.comment_submit_container > .comment_util_btns > button {
		margin-top: 1rem;
		width: 5rem;
		height: 2.5rem;
		padding: 0.25rem 0.75rem;
		background-color: white;
		white-space: nowrap;
		cursor: pointer;
		border: 1px solid #ccc;
		border-radius: 5px;
	}
	
	.comment_submit_container > .comment_input {
		display: flex;
	}
	
	.comment_input > .comment {
		flex: 1;
	}
	.comment_input > .comment >textarea,
	.comment_input > .comment_submit_btn > button {
		border: 1px solid #ccc;
	}
	
	.comment_input > .comment > textarea {
		height: 5rem;
		width: 100%;
		resize: none;
		vertical-align: middle;
		padding: 0.4rem;
	}
	
	.comment_input > .comment_submit_btn > button {
		height: 100%;
		width: 5rem;
		margin-left: 1.5rem;
		cursor: pointer;
		background-color: white;
		border-radius: 5px;
	}
	
	/* 게시판 리스트 양식 */
	.board_root_container {
		position: relative;
		width: 100%;
	}
	
	.board_root_container > .board_list {
		border-collapse: collapse;
		width: 100%;
		height: 100%;
		border-top: 3px solid black;
		border-bottom: 1.5px solid black;
	}

	.board_root_container > .board_list > thead {
		border-bottom: 1px solid black;
	}
	
	.board_root_container > .board_list > tbody > tr {
		border-bottom: 1px solid #020202;
	}
	
	.board_root_container > .board_list .col {
		padding: 6px 0;
	}
	.board_root_container > .board_list > tbody > tr > td {
		padding: 3px 0;
	}
	
	.board_root_container > .board_list > tbody > .search_board:hover {
		background-color: #eeeeee;
	}
	
	.board_root_container > .board_list > tbody > .no_search_board:hover {
		background-color: transparent;
	}
	
	.board_root_container > .board_list > tbody > tr > .board_subject > a,
	.board_root_container > .board_list > tbody > tr > .board_author > a {
		display: block;
		width: 100%;
	}
	
	.board_root_container > .board_list > tbody > tr > .board_subject .subject:hover {
		text-decoration: underline;
	}
	
	.board_root_container > .board_list .col.count,
	.board_root_container > .board_list .col.recommend {
		width: 3rem;
	}
	
	.board_root_container > .board_list .col.num,
	.board_root_container > .board_list .col.sub_category,
	.board_root_container > .board_list .col.regdate {
		 width: 6rem;
	}

	.board_root_container > .board_list .col.author {
		width: 7rem;
	}
	
	.board_list > tbody > tr > td {
		text-align: center;
		white-space: nowrap;
	}
	
	.board_list > tbody > tr .board_subject {
		text-align: left;
	}
	
	.board_list > tbody > tr mark,
	.board_list > tbody > tr .board_author {
		position: relative;
	}
	
	.board_author > .board_author_status {
		position: absolute;
		left : 50%;
		transform: translateX(-50%);
		top: 100%;
		display: none;
		z-index: 1;
		border: 1px solid black;
	}
	
	.board_author > .board_author_status.show {
		display: block;
	}
	
	.board_author_status > div {
		background-color: white;
	}
	
	.board_author_status > div > a {
		display: block;
		padding: 0.2rem 0.4rem;
		font-size: 0.9rem;
	}
	.board_author_status > div:hover > a {
		text-decoration: underline;
	}
	
	.no_search_board > td {
		width: 100%;
		height: 7.5rem;
	}
	
	/* 페이징 양식 */
	.board_paging_box {
		display: flex;
		justify-content: center;
		align-items: center;
		margin-top: 1.5rem;
	}
	
	.board_paging_box a {
		font-size: 1.1rem;
	}
	
	.board_paging_box > .paging_box > .paing > a {
		padding: 0.2rem 0.3rem;
	}
	.board_paging_box > .paging_box > .paing > a:hover {
		text-decoration: underline;
	}
	
	.board_paging_box > .paging_box > .paing > a.on {
		color: red;
		text-decoration: underline;
		cursor: text;
	}
	
	/* 검색 양식 */
	.board_search_btns {
		display: flex;
		justify-content: center;
		align-items: center;
		margin-top: 1.5rem;
	}
	
	.board_search_btns select,
	.board_search_btns input,
	.board_search_btns button {
		box-sizing: border-box;
		height: 2.2rem;
	}
	
	.board_search_btns input {
		padding: 2px 5px;
	}
	.board_search_btns input:focus {
		outline: none;
	}

	.board_search_btns select {
		width: 5.5rem;
	}
	.board_search_btns button {
		width: 3.5rem;
	}
	.board_search_btns input {
		width: 15rem;
	}
	
	.search_option,
	.search_btn {
		cursor: pointer;
	}
	
</style>
</head>
<body>
	<div class="root_container">
		<%@include file="/WEB-INF/view/inc/header.jsp" %>
		<div class="content_wrapper">
			<article>
				<div class="board_article">
					<!-- 게시판 소개 -->
					<jsp:include page="/WEB-INF/view/inc/board/board_title.jsp">
						<jsp:param value="${boardTitle}" name="boardTitle"/>
						<jsp:param value="${subscribe}" name="subscribe"/>
					</jsp:include>
					<!-- 게시판 방문목록 -->
					<div class="channel_visit_history">
						<div class="vlist_category_list">
							<div class="vlist_info">최근방문목록</div>
						<!-- 
							<div class="v_category">
								<a href="#">방문목록</a>
								<i class="fa-solid fa-x"></i>
							</div>
						 -->
						</div>
					</div>
					<!-- 게시판 글 -->
					<c:if test="${board ne null}">
					<div class="board_view_container">
						<form class="board_view_data" name="bvd">
							<input type="hidden" id="board_num" name= "board_num" value="${board.boardSeq}">
							<input type="hidden" id="board_title_num" name= "board_title_num" value="${board.boardTitleSeq}">
							<input type="hidden" id="board_author_num" name="board_author_num" value="${board.memberSeq}">
						</form>
						<div class="board_view_header">
							<div class="board_author_profile">
								<img src="/asset/images/profile/${board.memberProfile}">
							</div>
							<div class="board_view_body">
								<h1 class="board_view_title">
									<span>[${board.boardSubTitle}]</span>
									<span>${board.boardSubject}</span>
								</h1>
								<div class="board_view_status">
									<div class="board_view_author">
										<span>${board.memberNickname}</span>
										<span>${board.boardRegdate}</span>
									</div>
									<div class="board_view_info">
										<span>조회수 : ${board.boardViews}</span>
										<span>추천수 : ${board.boardThumbsUp}</span>
										<span>
											<a href="#focus_c">
												<span id="cmt_count">댓글수 : ${board.boardCommentCount}</span>
											</a>
										</span>
									</div>
								</div>
							</div>
						</div>
						<div class="board_view_contents">
							<div class="board_contents">
								<div>${board.boardContent}</div>
							</div>
							<div class="board_recommend_btns">
								<button class="thumbs_up" type="button" data-board-num="${board.boardSeq}">
									<span><i class="fa-regular fa-thumbs-up"></i></span>
									<span class="thumbs_up_count">${board.boardThumbsUp}</span>
								</button>
								<button class="thumbs_down" type="button" data-board-num="${board.boardSeq}">
									<span><i class="fa-regular fa-thumbs-down"></i></span>
									<span class="thumbs_down_count">${board.boardThumbsDown}</span>
								</button>
							</div>
						</div>
						<div class="board_view_comments" id="focus_c">
							<!-- 게시판 댓글목록 -->
							<!-- 댓글 상태 -->
							<div class="board_comments_util">
								<div class="board_comment_sort">
									<h4>
										<span><i class="fa-regular fa-comment"></i></span>
										<span>
											<span id="cmt_section">댓글(${board.boardCommentCount})</span>
										</span>
									</h4>
									<ul>
										<li><a href="#">등록일순</a></li>
										<li><a href="#">최신순</a></li>
									</ul>
								</div>
							</div>
							<!-- 댓글 -->
							<div class="board_comment_container">
								<div class="comment_list">
									<!-- ajax - 동적태그생성 -->
								</div>
								<div class="comment_paging_box">
									<div class="comment_pageing">
										<button><i class="fa-solid fa-angle-left"></i></button>
										<span>
											<span id="comment_page">1</span>
											<span>/</span>
											<span id="comment_total_page">2</span>
										</span>
										<button><i class="fa-solid fa-chevron-right"></i></button>
									</div>
								</div>
							</div>
							<!-- 게시판 댓글작성 -->
							<div class="comment_view_data">
								<div class="comment_submit_container">
									<div class="comment_input">
										<div class="comment">
											<textarea name="comment" placeholder="내용을 입력해주세요."></textarea>
										</div>
										<div class="comment_submit_btn">
											<button type="button">등록</button>
										</div>
									</div>
									<div class="comment_util_btns">
										<button type="button">이모티콘</button>
									</div>
								</div>
							</div>
						</div>
					</div>
					</c:if>
					<!-- 게시판 카테고리 -->
					<div class="board_btns">
						<div class="board_category">
							<div class="category">
								<button type="button" onclick="listSearchType('all')">전체글</button>
								<button type="button" onclick="listSearchType('recommend')">화제글</button>
							</div>
							<div class="sub_category">
								<a href="javascript:;" onclick="listSearchSubTitle('all')">전체</a>
								<c:forEach var="subTitle" items="${boardSubTitleList}">
								<a href="javascript:;" onclick="listSearchSubTitle(${subTitle.boardSubTitleSeq})">${subTitle.boardSubTitle}</a>
								</c:forEach>
							</div>
						</div>
						<div class="board_util_btn">
							<select class="board_show_amout" name="board_show_amout">
								<option value="30">30개씩보기</option>
								<option value="50" selected>50개씩보기</option>
								<option value="100">100개씩보기</option>
								<option value="150">150개씩보기</option>
							</select>
							<c:if test="${boardTitleSeq ne 1}">
							<button class="write_board" onclick="location.href='/board/write?num=${boardTitle.boardTitleSeq}'">
								<i class="fa-solid fa-pencil"></i>
								<span>글쓰기</span>
							</button>
							</c:if>
						</div>
					</div>
					<!-- 게시판 리스트 -->
					<div class="board_root_container">
						<table class="board_list">
							<thead>
								<tr>
									<th class="col num">번호</th>
									<th class="col sub_category">주제</th>
									<th class="col subject">제목</th>
									<th class="col author">작성자</th>
									<th class="col regdate">작성일</th>
									<th class="col count">조회</th>
									<th class="col recommend">추천</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="list" items="${list}">
								<tr class="search_board">
									<td class="board_num">${list.boardSeq}</td>
									<td class="board_sub_category">${list.boardSubTitle}</td>
									<td class="board_subject">
										<a href="javascript:;" onclick="showBoardContents(${list.boardSeq})">
											<span class="subject">${list.boardSubject}</span>
											<span class="reply_numbox">[${list.boardCommentCount}]</span>
										</a>
									</td>
									<td class="board_author">
										<a class="author" href='javascript:;'>${list.memberNickname}</a>
										<div class="board_author_status">
											<div><a href="/board/lists?num=${list.boardTitleSeq}&search=author&keyword=${list.memberNickname}">작성글 검색</a></div>
											<div><a href="#" onclick="alert('구현중인 기능입니다.')">프로필보기</a></div>
											<div><a href="#" onclick="alert('구현중인 기능입니다.')">신고</a></div>
										</div>
									</td>
									<td class="board_regdate">${list.boardRegdate}</td>
									<td class="board_count">${list.boardViews}</td>
									<td class="board_recommend">${list.boardThumbsUp}</td>
								</tr>
								</c:forEach>
								<c:if test="${fn:length(list) eq 0}">
								<tr class="no_search_board">
									<td colspan="7">등록된 게시물이 없습니다.</td>
								</tr>
								</c:if>
							</tbody>
						</table>
					</div>
					<!-- 게시판 버튼 -->
					<div class="board_btns">
						<div class="board_category">
							<div class="category">
								<button type="button" onclick="listSearchType('all')">전체글</button>
								<button type="button" onclick="listSearchType('recommend')">화제글</button>
							</div>
						</div>
						<c:if test="${boardTitleSeq ne 1}">
						<div class="board_util_btn">
							<button class="write_board" onclick="location.href='/board/write?num=${boardTitle.boardTitleSeq}'">
								<i class="fa-solid fa-pencil"></i>
								<span>글쓰기</span>
							</button>
						</div>
						</c:if>
					</div>
					<!-- 페이징 -->
					<div class="board_paging_box">
						<div class="paging_box">
							<!-- 페이징을 위한 값들 -->
							<c:set var="firstStartBlock" value="${startBlock}" />
							<c:set var="blockSize" value="${blockSize}" />
							<c:set var="blockNum" value="${startBlock}" />
							<c:set var="totalPage" value="${totalPage}" />
							<c:set var="nowPage" value="${nowPage}" />
							<c:if test="${totalPage ne 0}">
							<a href="javascript:;" onclick="move_prev_block(${firstStartBlock}, ${blockSize}, ${nowPage})"><i class="fa-solid fa-angles-left"></i></a>
							<a href="javascript:;" onclick="move_prev_page(${nowPage})"><i class="fa-solid fa-angle-left"></i></a>	
							</c:if>
							<!-- 페이지이동을 위한 링크 생성 -->
							<!-- 블록사이즈만큼 반복 -->
							<span class="paing">
								<c:forEach begin="1" end="${blockSize}">
									<!-- blockNum가 최대 출력할 수 있는 페이지수를 넘지 않을경우 태그생성한뒤 n값 증가 -->
									<c:if test="${ blockNum <= totalPage }">
										<c:if test="${blockNum != nowPage}">
											<a href="javascript:;" onclick="move_page(${blockNum})">${blockNum}</a>
										</c:if>
										<c:if test="${blockNum == nowPage}">
											<a href="javascript:;" class="on">${blockNum}</a>
										</c:if>
										<c:set var="blockNum" value="${blockNum + 1}" />
									</c:if>
								</c:forEach>
							</span>
							<c:if test="${totalPage ne 0}">
							<a href="javascript:;" onclick="move_next_page(${nowPage}, ${totalPage})"><i class="fa-solid fa-angle-right"></i></a>
							<a href="javascript:;" onclick="move_next_block(${firstStartBlock}, ${blockSize}, ${totalPage}, ${nowPage})"><i class="fa-solid fa-angles-right"></i></a>
							</c:if>
						</div>
					</div>
					<!-- 검색 -->
					<div class="board_search_btns">
						<select class="search_option" name="search">
							<option value="subject_contents">제목+내용</option>
							<option value="subject">제목</option>
							<option value="contents">내용</option>
							<option value="author">작성자</option>
						</select>
						<input type="text" class="keyword">
						<button class="search_btn">검색</button>
					</div>
				</div>
			</article>
			<%@include file="/WEB-INF/view/inc/util_btn.jsp" %>
		</div>
		<%@include file="/WEB-INF/view/inc/footer.jsp" %>
	</div>
	<script type="text/javascript" src="/asset/js/view_mode.js"></script>
	<script type="text/javascript" src="/asset/js/board/board_title.js"></script>
	<script type="text/javascript" src="/asset/js/board/board_comment.js"></script>
	<script type="text/javascript">
		
		$(() => {
			//방문목록 표시
			let visitList = '${visitCategoryList}'.split(';');
			
			visitList.forEach(function(visitCategory) {
				if(visitCategory != '') {
					let category = visitCategory.split(',');
					if(category[1] != '${boardTitle.boardTitle}') {	
						$('.vlist_category_list').append(
							'<div class="v_category">'
								+'<a href="/board/lists?num='+category[0]+'">'+ category[1] +'</a>'
								+'<i onclick="remove(this)" class="fa-solid fa-x"></i>'
								+'<input type="hidden" id="vlist_category_val" value="'+category[0]+','+category[1]+';'+'">'
							+'</div>'
						);		
					}
				}
			});
			
			//활성화된 옵션 선택
			let type = '${type}';
			let type_btns = $('.category > button');
			type_btns.each(function() {
			    let btn = $(this);
			    if (btn.attr('onclick').includes(type)) {
			        btn.addClass('on');
			    }
			});
			
			let head = '${head}';
			let sub_category_list = $('.sub_category > a');
			sub_category_list.each(function() {
				let sub_category = $(this);
				if(sub_category.attr('onclick').includes(head)) {
					sub_category.addClass('on');
				}
			});
			
			let page_size_value = '${pageSize}';
			let page_size_options = $('.board_show_amout option');
			page_size_options.each(function() {
				let option = $(this);
				if(option.val() == page_size_value) {
					page_size_options.removeAttr('selected');
					$(this).prop('selected', true)
				}
			});
			
			
			//검색이력이 있다면 검색창에 값 넣어주기
			let search = '${search}';
			let keyword = '${keyword}';
			let search_options = $('.search_option option');
			if(keyword != null && keyword != '') {
				$('.keyword').val(keyword);
				
				//검색된 내용에 형관팬 칠해주기
				if(search == 'subject_contents' || search == 'subject' || search == 'contents') {
					$('table tbody tr .board_subject .subject').each(function() {
						let text = $(this).text();
						let keywordRegex = new RegExp(keyword, 'gi');
						if(keywordRegex.test(text)) {
							text = text.replace(keywordRegex, '<mark>$&</mark>');
							$(this).html(text);
						}
					});
				} 

				//검색한 카테고리로 바꿔주기
				search_options.each(function() {
					let option = $(this);
					if(option.val() == search) {
						search_options.prop('selected', false);
						option.prop('selected', true);
					}
				});
			}

			//로그인되지 않은 상태라면, 댓글입력 및 전송 막기
			let loginCk = ${((empty cookie.auth.value) or (cookie.auto.value ne 'true')) and (empty sessionScope.auth)};
			if(loginCk) {
				$('.comment textarea').attr('placeholder', '댓글은 로그인후 작성이 가능합니다.').css('cursor', 'not-allowed').prop('disabled', true).css('background-color', 'white');
				$('.comment_submit_btn > button').css('cursor', 'not-allowed').prop('disabled', true);
				$('.comment_util_btns > button').css('cursor', 'not-allowed').prop('disabled', true);
			}
			
			/** TODO 나중에 댓글에 이모티콘 입력기능 추가하기 */
			$('.comment_util_btns > button').on('click', function() {
				alert('준비중인 기능입니다.');
			});

		});
		
		
		//방문목록 삭제
		function remove(event) {
			let category = $(event).siblings('#vlist_category_val').val();
			
			$.ajax({
				type: 'GET',
				url: '/removeVisitCategory',
				data: 'category='+category,
				success: function() {
					$(event).parent().remove();
				},
				error: function(a,b,c) {
					console.log(a,b,c);
				}
			});
		}
		
		//검색옵션 설정
		function listSearchType(type) {
			let url = new URL(window.location.href);
			url.pathname = '/board/lists';
			url.searchParams.delete('board');
			url.searchParams.delete('head');
			url.searchParams.delete('search');
			url.searchParams.delete('keyword');
			url.searchParams.delete('page');
			url.searchParams.delete('page_size');
			
			if(type == 'all') {
				url.searchParams.delete('type');
			} else if(type == 'recommend') {
				url.searchParams.set('type', type);
			}
			location.href = url.href;
		}
		
		function listSearchSubTitle(subTitleSeq) {
			let url = new URL(window.location.href);
			url.pathname = '/board/lists';
			url.searchParams.delete('search');
			url.searchParams.delete('keyword');
			url.searchParams.delete('board');
			if(subTitleSeq == 'all') {
				url.searchParams.delete('head');
			} else {
				url.searchParams.set('head', subTitleSeq);
			}
			location.href = url.href;
		}
		
		//페이지 출력갯수 변경
		$('.board_show_amout').on('change', () => {
			let url = new URL(window.location.href);
			url.pathname = '/board/lists';
			url.searchParams.delete('board');
			url.searchParams.set('page_size', $('.board_show_amout').val());
			location.href = url.href;
		});
		
		//검색
		$('.board_search_btns > .search_btn').on('click', function() {
			if($('.keyword').val().length > 0) {
				search_move();
			} else {
				alert('키워드를 입력해주세요');
				$('.keyword').focus();
			}
		});

		$('.board_search_btns > .keyword').on('keyup', function(e) {
			if(e.keyCode == 13) {
				if($('.keyword').val().length > 0) {
					search_move();
				} else {
					alert('키워드를 입력해주세요');
					$('.keyword').focus();
				}
			}
		});
		
		function search_move() {
			let url = new URL(window.location.href);
			url.pathname = '/board/lists';
			url.searchParams.delete('board');
			url.searchParams.delete('head');
			url.searchParams.set('search', $('.search_option').val());
			url.searchParams.set('keyword', $('.keyword').val());
			location.href = url.href;
		}
		
		//board 페이지로 이동하기
		function showBoardContents(board_num) {
			let url = new URL(window.location.href);
			url.searchParams.set('board', board_num);
			url.pathname = '/board/view';
			location.href = url.href;
		}
		
		//페이지 숫자클릭 이동
		function move_page(blockNum) {
			let url = new URL(window.location.href);
			url.pathname = '/board/lists';
			url.searchParams.delete('board');
			url.searchParams.set('page', blockNum);
			location.href = url.href;
		}
		//페이지 버튼클릭 이동
		function move_prev_block(first_start_block, block_size, page) {
			let url = new URL(window.location.href);
			url.pathname = '/board/lists';
			url.searchParams.delete('board');
			if(first_start_block - block_size > 0) {
				url.searchParams.set('page', first_start_block-block_size);
				location.href = url.href;
			} else if((first_start_block!=page) && first_start_block - block_size < 0) {
				url.searchParams.set('page', 1);
				location.href = url.href;
			}
		}
		function move_prev_page(page) {
			if(page > 1) {
				let url = new URL(window.location.href);
				url.pathname = '/board/lists';
				url.searchParams.delete('board');
				url.searchParams.set('page', page-1);
				location.href = url.href;			
			} 
		}
		function move_next_block(first_start_block, block_size, total_page, page) {
			let url = new URL(window.location.href);
			url.pathname = '/board/lists';
			url.searchParams.delete('board');
			if(first_start_block + block_size <= total_page) {
				url.searchParams.set('page', first_start_block+block_size);
				location.href = url.href;
			} else if((page != total_page) && first_start_block + block_size > total_page) {
				url.searchParams.set('page', total_page);
				location.href = url.href;
			}
		}

		function move_next_page(page, total_page) {
			if(page + 1 <= total_page) {
				let url = new URL(window.location.href);
				url.pathname = '/board/lists';
				url.searchParams.delete('board');
				url.searchParams.set('page', page+1);
				location.href = url.href;	
			} 
		}
		
		
		//board 작성자 클릭시 팝업 출력
		$(document).on('click', '.board_author', (e) => {
			e.stopPropagation();
			let user_target = $(e.currentTarget).find('.board_author_status');			
			//기존에 존재하는 모든 user_target의 show 클래스를 제거
			$('.board_author_status.show').removeClass('show');
			
			if(user_target.hasClass('show')) {
				user_target.removeClass('show');
			} else {
				user_target.addClass('show');
			}
		});
		
		//작성자 외 영역 클릭시 팝업 종료
		$(document).on('click', (e) => {
			let user_popup = $('.board_author_status');
			if(user_popup.has(e.target).length === 0) {
				user_popup.removeClass('show');
			}
		});
		
		//추천, 비추천버튼 클릭시 값 증가시키기
		$('.thumbs_up, .thumbs_down').on('click', function(e) {
			let recommend = null;
			let board_seq = null;
			
			if($(this).hasClass('thumbs_up')) {
				recommend = 1;
			} else if($(this).hasClass('thumbs_down')) {
				recommend = -1;
			}
			board_seq = $(this).data('board-num');
			
			$.ajax({
				url: '/board/recommend',
				data: {
					recommend: recommend,
					board_seq: board_seq
				},
				dataType: 'json',
				success: function(data) {
					console.log(data.result);
					
					if(data.result.already == true) {
						alert('이미 해당 게시글에 추천 또는 비추천을 하셨습니다.');
					} else if(data.result.success == true) {
						let nowCount = 0;
						if(recommend == 1) {
							nowCount = parseInt($('.thumbs_up_count').text());
							$('.thumbs_up_count').text(nowCount + 1);
						} else if(recommend == -1) {
							nowCount = parseInt($('.thumbs_down_count').text());
							$('.thumbs_down_count').text(nowCount + 1);
						}
					} else if(data.result.auth == false) {
						alert('로그인 후 이용 가능한 서비스입니다.');
					} else {
						alert('추천 등록에 실패했습니다.');
					}
				},
				error: function(a, b, c) {
					console.log(a, b, c);
					alert('추천 등록에 실패했습니다.');
				}
			});
		});
		
	</script>
</body>
</html>



