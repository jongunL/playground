<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>PlayGround</title>
<%@include file="/WEB-INF/view/inc/asset.jsp" %>
<style>
	.member_setting_container > .member_setting_n_bar > ul {
		display: flex;
	}
	
	.member_profile_container,
	.member_contents_container,
	.member_setting_n_bar > ul > li,
	.member_setting_n_bar > h1 {
		margin: 0.25rem 0.75rem;
		margin-top: 0.75rem;
	}
	
	.member_profile_container,
	.member_contents_container {
		margin-top: 2.25rem;
	}
	
	.member_setting_n_bar > ul > li {
		cursor: pointer;
	}
	
	.member_setting_n_bar > ul > li.active {
		border-bottom: 2px solid black;
	}
	
	.member_profile_container > form > .member_profile  > .profile.row {
		display: flex;
		margin-bottom: 2rem;
	}
	
	.member_profile  > .profile.row > label {
		flex: 0 0 25%;
		max-width: 25%;
	}
	
	.member_profile  > .profile.row > div {
		flex: 0 0 75%;
		max-width: 75%;
	}
	
	.profile.row > .profile.img > label > img {
		width: 3.5rem;
		height: 3.5rem;
		border-radius: 50%;
		cursor: pointer;
	}
	
	.profile.row > .profile.img > input {
		display: none;
	}
	
	#profile_nickname {
		box-sizing: border-box;
		height: 2.5rem;
		width: 100%;
		padding: 0.25rem 0.75rem;
		margin-bottom: 0.5rem;
	}
	
	#nickname_msg {
		height: 0.9rem;
		font-size: 0.9rem;
	}
	
	.setting_btn_area {
		text-align: right;
	}
	
	.setting_btn_area > button {
		padding: 0.5rem 1.75rem;
	}	
	
	.member_contents_container > .contents_list {
		margin-top: 1rem;
	}
	
	.member_contents_container > .contents_list > .row,
	.member_contents_container > .contents_list > .row > .col.contents,
	.member_contents_container > .contents_list > .row > .col {
		display: flex;
	}
	
	.member_contents_container > .contents_list > .row {
		flex: 1;
		padding: 0.5rem 0;
		border-bottom: 1px solid #ccc;
	}
	
	.member_contents_container > .contents_list > .row:last-child {
		border-bottom: none;
	}
		
	.member_contents_container > .contents_list > .row > .col.select,
	.member_contents_container > .contents_list > .row > .col.remove {
		justify-content: center;
		align-items: center;	
		flex: 0 0 2.5%;
		max-width: 2.5%;
	}
	
	.member_contents_container > .contents_list > .row > .col.remove {
		visibility: hidden;
	}
	
	.member_contents_container > .contents_list > .row:hover > .col.remove {
		visibility: visible;
	}
	
	.member_contents_container > .contents_list > .row > .col.select > input,
	.contents_list > .row:hover > .col.remove > i {
		cursor: pointer;
	}
	
	.member_contents_container > .contents_list > .row > .col.contents {
		cursor: pointer;
		box-sizing: border-box;
		align-items: center;	
		flex: 0 0 95%;
		max-width: 95%;
		padding: 0 0.5rem;
	}
	
	.contents_list > .row > .col.contents > .contents_status {
		margin-left: 0.5rem;
	}
	
	.col.contents > .contents_status > .regdate {
		margin-top: 0.5rem;
		font-size: 0.85rem;
		color: var(--color-hr-dark);
	}
	
	.member_contents_container > .contents_list > .row > .col.contents > img {
		border-radius: 50%;
		height: 3.5rem;
		width: 3.5rem;
	}
	
	.contents_paging {
		display: none;
	}
	
	.contents_paging.active {
		display: flex;
		justify-content: center;
		align-items: center;
		margin-top: 1.25rem;
	}
	
	.contents_paging.active > button,
	.contents_paging.active > .contents_paging_list > span {
		padding: 0.25rem;
		margin: 0 0.15rem;
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
					<div class="member_setting_container">
						<div class="member_setting_n_bar">
							<h1>설정</h1>
							<ul>
								<li id="mp"><a href="/member/profile">프로필</a></li>
								<li id="al"><a href="/member/alarm">알림</a></li>
								<li id="bd"><a href="javascript:;">작성글</a></li>
								<li id="cm"><a href="javascript:;">작성댓글</a></li>
								<li id="ac"><a href="javascript:;">계정</a></li>
							</ul>
						</div>
						<c:if test="${path == '/member/profile'}">
							<jsp:include page="/WEB-INF/view/inc/member/member_profile_inc.jsp">
								<jsp:param value="${memberProfile}" name="memberProfile"/>
							</jsp:include>
							<script type="text/javascript" src="/asset/js/member/profile.js"></script>
						</c:if>
						<c:if test="${path == '/member/alarm'}">
						<div class="member_contents_container">
							<form action="javascript:;" id="contents_container_form">
								<input type="hidden" id="page" name="page">
								<input type="hidden" id="pageSize" name="pageSize">
								<input type="hidden" id="totalCount" name="totalCount">
								<input type="hidden" id="totalPage" name="totalPage">
							</form>
							<div class="contents_btn">
								<button class="select_all_btn">전체선택</button>
								<button class="delete_btn">삭제</button>
							</div>
							<div class="contents_list">
							</div>
							<div class="contents_paging">
								<button class="move_prev_contents_block"><i class="fa-solid fa-angles-left"></i></button>
								<button class="move_prev_contents_page"><i class="fa-solid fa-angle-left"></i></button>
								<span class="contents_paging_list">
									<span>1</span>
									<span>2</span>
									<span>3</span>
									<span>4</span>
									<span>5</span>
								</span>
								<button class="move_next_contents_page"><i class="fa-solid fa-angle-right"></i></button>
								<button class="move_next_contents_block"><i class="fa-solid fa-angles-right"></i></button>
							</div>
						</div>
						</c:if>
					</div>
				</div>
			</article>
			<%@include file="/WEB-INF/view/inc/util_btn.jsp" %>
		</div>
		<%@include file="/WEB-INF/view/inc/footer.jsp" %>
	</div>
	<script type="text/javascript" src="/asset/js/view_mode.js"></script>
	<script type="text/javascript">
		$(() => {
			let currentURI = new URL(window.location.href);
			let path = currentURI.pathname;
			
			if(path == '/member/profile') {
				$('#mp').addClass('active');
			} else if(path == '/member/alarm') {
				$('#al').addClass('active');
			}
			
			get_contents(path);
		});
		
		function get_contents(path) {
			let url = null;			
			if(path == '/member/alarm') {
				url = '/member/getAlarm';
				get_contents_list(url, path);
			}
		}
		
		function get_contents_list(url, path) {
			/*페이징 처리를 위한 값들*/
			let page = $('#contents_container_form > #page');
			let page_size = $('#contents_container_form > #pageSize');
			let total_count = $('#contents_container_form > #totalCount');
			let total_page = $('#contents_container_form > #totalPage');
			
			$.ajax({
				type: 'POST',
				dataType: 'JSON',
				url: url,
				data: {
					path: path,
					page: $('#contents_container_form > #page').val()
				},
				success: function(result) {
					if(result != null && result != undefined && result.alarmList.length > 0) {
						page.val(result.nowPage);
						page_size.val(result.pageSize);
						total_count.val(result.totalCount);
						total_page.val(result.totalPage);
						
						result.alarmList.forEach(function(data) {
							create_alarm_list(data);
						});
						
						if(total_page.val() > 1) {
							$('.contents_paging').addClass('active');
							create_paging_box(page.val(), total_page.val(), page_size.val());
						}						
					}
				},
				error: function(a, b, c) {
					console.log(a, b, c);
				}
			});
		}
		
		function create_paging_box(now, max, size) {
			console.log(now);
			console.log(max);
			console.log(size);
		}
			
		let ck_flag = false;
		$('.select_all_btn').on('click', function(e) {
			let ckbox_list = $('.contents_list > .row > .col.select > input');
			ck_box_flag_update(ckbox_list);
			
			if(ck_flag) {
				ckbox_list.prop('checked', false);
				$('.select_all_btn').text('전체선택');
			} else {
				ckbox_list.prop('checked', true);				
				$('.select_all_btn').text('선택해제');
			}
			
		});
		
		$('.delete_btn').on('click', function(e) {
			let ckbox_list = $('.contents_list > .row > .col.select > input:checked');
			ck_box_flag_update(ckbox_list);
			
			//선택된 checkbox들의 value를 배열에 저장
			if(ck_flag) {
				let values = [];
				
				ckbox_list.each(function() {
					values.push($(this).val());
				});		
				
				remove_contents(values);
			}
		});
		
		function remove_contents(num) {
			let currentURI = new URL(window.location.href);
			let path = currentURI.pathname;

			if(!(num instanceof Array)) {
				num = [num];
			}

			if(path == '/member/alarm') {
				let url = '/member/checkAlarm';
				remove(url, path, num);
			}	
		}
		
		function remove(url, path, num) {		
			$.ajax({
				url: url,
				type: 'POST',
				data: {
					notificationSeq: num
				},
				dataType: 'JSON',
				success: function(result) {
					if(result) {
						$('.contents_list').children().remove();
						get_contents(path);
					} else {
						alert('삭제에 실패했습니다.');
					}
				},
				error: function(a, b, c) {
					console.log(a, b, c);
				}
			});
		}
		
		function ck_box_flag_update(ckbox_list) {
			ck_flag = ckbox_list.is(':checked');
		}
		
		function create_alarm_list(input) {
			let list = $('.contents_list');
			
			let html = '';
			html += '<div class="row" data-n-num="'+input.memberAlarmSeq+'">';
			html += '	<div class="col select">';
			html += '		<input type="checkbox" name="checked" value="'+input.memberAlarmSeq+'">';
			html += '	</div>';
			html += '	<div class="col contents" onclick="location.href=\'/board/view?num='+input.boardTitleSeq+'&board='+input.boardSeq+'&cmt='+input.CommentSeq+'\';">';
			html += '		<img src="/asset/images/profile/'+input.memberProfile+'">';
			html += '		<div class="contents_status">';
			html += '			<div class="contents">'+input.memberNickname + '님의 답글 : ' + input.message+'</div>';
			html += '			<div class="regdate">'+input.created+'</div>';
			html += '		</div>';
			html += '	</div>';
			html += '	<div class="col remove">';
			html += '		<i class="fa-solid fa-x"></i>';
			html += '	</div>';
			html += '</div>';
			
			list.append(html);
			let delete_btn = $('.contents_list > .row[data-n-num='+input.memberAlarmSeq+'] > .col.remove > i');
			delete_btn.on('click', function(e) {
				let parents_element = $(e.target).closest('.row');
				remove_contents(parents_element.data('n-num'));
			});
			
			let contents_ck_box = $('.contents_list > .row[data-n-num='+input.memberAlarmSeq+'] > .col.select > input');
			contents_ck_box.on('click', function(e) {
				let ckbox_list = $('.contents_list > .row > .col.select > input');
				ck_box_flag_update(ckbox_list);
				
				if(ck_flag) {
					$('.select_all_btn').text('선택해제');
				} else {
					$('.select_all_btn').text('전체선택');
				}
			});
		}
		
	</script>
</body>
</html>