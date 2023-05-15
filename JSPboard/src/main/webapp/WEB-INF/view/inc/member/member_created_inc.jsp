<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="member_contents_container">
	<form action="javascript:;" id="contents_container_form">
		<input type="hidden" id="page" name="page">
		<input type="hidden" id="pageSize" name="pageSize">
		<input type="hidden" id="totalCount" name="totalCount">
		<input type="hidden" id="totalPage" name="totalPage">
		<input type="hidden" id="blockSize" name="blockSize">
	</form>
	<div class="contents_btn">
		<button class="select_all_btn">전체선택</button>
		<button class="delete_btn">삭제</button>
	</div>
	<div class="contents_list">
	</div>
	<div class="contents_paging">
		<button class="move_prev_contents_block" onclick="move(event)"><i class="fa-solid fa-angles-left"></i></button>
		<button class="move_prev_contents_page" onclick="move(event)"><i class="fa-solid fa-angle-left"></i></button>
		<span class="contents_paging_list">
		</span>
		<button class="move_next_contents_page" onclick="move(event)"><i class="fa-solid fa-angle-right"></i></button>
		<button class="move_next_contents_block" onclick="move(event)"><i class="fa-solid fa-angles-right"></i></button>
	</div>
</div>