/** cooment_from 데이터를 이용해 동적으로 댓글을 가져오는 ajax 단순 불러오기가 아닌 댓글등록시에는 인자를 통해 태그에 이벤트 추가 */
let comment_list = null;
function get_comment() {
	//댓글을 찾아오기 위한 정보
	let board_num = $('#board_num').val();
	let board_title_num = $('#board_title_num').val();

	//댓글 페이징, 정렬을 위한 데이터 가져오기
	let comment_form = $('.board_comment_data');
	let page = comment_form.find('.comment_page');
	let total_page = comment_form.find('.comment_total_page');
	let sort = comment_form.find('.comment_sort');
	let total_count = comment_form.find('.comment_total_count');
	let page_size = comment_form.find('.comment_page_size');
	
	return new Promise((resolve, reject) => {
		$.ajax({
			url: '/board/comment',
			type: 'GET',
			data: {
				boardSeq : board_num,
				boardTitleSeq : board_title_num,
				page : page.val(),
				sort : sort.val()
			},
			dataType: 'json',
			success: function(result) {				
				if(result != null) {
					//받아온 데이터 초기화
					total_page.val(result.totalPage);
					page.val(result.nowPage);
					sort.val(result.sort);
					total_count.val(result.totalCount);
					page_size.val(result.pageSize);
					
					//태그값 동적 변경
					$('#comment_page').text(page.val());
					$('#comment_total_page').text(total_page.val());
					
					//기존에 존재하는 댓글 삭제
					$('.comment_list').empty();
					
					//태그 동적 추가
					comment_list = result.commentList;
					comment_list.forEach(function(value) {
						put_comment_html(value);
					});
					resolve();
				} else {
					alert('댓글 목록을 가져오는데 실패했습니다.');
				}
			},
			error: function(a, b, c) {
				console.log(a, b, c);
				reject(error);
			}
		});
	});
	
}


/** 등록 버튼 클릭시 데이터를 추가 */
$('.comment_submit_btn > button').on('click', function(event) {
	let comment = $('.comment > textarea').val();
	
	if(comment_length_ck(comment)) {
		insert_comment(comment, event);
	}
});

/** 댓글 등록시 길이 확인 */
function comment_length_ck(comment) {
	if(comment.length < 1) {
		alert('댓글 내용을 입력해주세요.');
		return false;
	} else if(comment.length > 300) {
		alert('댓글은 최대 300자까지 입력가능합니다.');
		return false;
	} else {
		return true;
	}
}

/** comment_val = 입력받은 댓글 내용, form_no 전송하는 form의 no */
function insert_comment(comment_val, event) {
	const target_btn = $(event.target);		
	const root_no = $(event.target).closest('.cmt.row').data('root-no');
	const order_no = $(event.target).closest('.cmt.row').data('order-no');
	const nickname = $(event.target).closest('.cmt.row').find('#comment_nickname').text();
	const board_form_data = {};
	
	//현제 페이징 위치에따라서 추가여부
	const comment_data = $('.board_comment_data');
	const page_size = comment_data.find('.comment_page_size');
	const now_page = comment_data.find('.comment_page');
	const total_page = comment_data.find('.comment_total_page');
	const total_count = comment_data.find('.comment_total_count');
	const page_size_val = parseInt(page_size.val());
	const now_page_val = parseInt(now_page.val());
	const total_page_val = parseInt(total_page.val());
	const total_count_val = parseInt(total_count.val());
		
	$.each($('.board_view_data').serializeArray(), function() {
		board_form_data[this.name] = this.value;
	});
	
	$.ajax({
		url: '/board/comment/add',
		type: 'POST',
		data: {
			boardFormData : JSON.stringify(board_form_data),
			comment : comment_val,
			rootNo : root_no,
			orderNo : order_no,
			nickname : nickname
		},
		dataType: 'json',
		success: function(result) {
			//댓글 등록에 성공했을 경우 공통로직
			if(result != null) {
				total_count.val(total_count_val + 1);
				$('.comment > textarea').val('');
				change_cmt_cnt_status(1);
				$('.comment_submit_container.show').remove();
			} else {
				alert('댓글 등록에 실패했습니다.');
				return;
			}
			
			//답글
			if(target_btn.hasClass('cmt_reply_btn')) {
				get_comment();
			//댓글
			} else {
				//현재 페이지가 마지막 페이지이고, 댓글 추가시 페이지 최대갯수를 초과하지 않는 경우
				if((now_page_val == total_page_val) && (total_count_val/page_size_val < total_page_val)) {
					put_comment_html(result);
				//댓글 추가시 페이지 최대갯수를 초과하는 경우
				} else if(total_count_val/page_size_val >= total_page_val) {
					total_page.val(total_page_val+1);
					$('#comment_total_page').text(total_page.val());
					
					//첫 등록 댓글인 경우
					if(total_count_val == 0) {
						put_comment_html(result);
						$('.comment_paging_box').removeClass('hidden');
					}
				} 
			}	

		},
		error: function(a, b, c) {
			console.log(a, b, c);
			alert('댓글 등록에 실패했습니다.');
		}
	});
}
/** 댓글수정 */
function update_comment(reply_no, comment) {
	$.ajax({
		url: '/board/comment/update',
		type: 'POST',
		data: {
			replyNo: reply_no,
			comment: comment
		},
		dataType: 'json',
		success: function(result) {
			if(result != null && result == true) {
				$('.comment_submit_container.show').remove();
				let target = $('a[data-reply-no='+reply_no+']').closest('.cmt.row');
				target.find('.board_comment_contents').text(comment);
				//요소가 없는경우 추가
				if(target.find('.cmt.fixed').length < 1) {
					target.find('.cmt.regdate').after('<div class="cmt fixed">(수정됨)</div>');					
				}
			} else {
				alert('댓글 수정에 실패했습니다.');
			}
		},
		error: function(a,b,c) {
			console.log(a, b, c);
			alert('댓글 수정에 실패했습니다.');
		}
	});
}

/** 댓글 추가 삭제시 화면에 출력되는 댓글수 변경하기 */
function change_cmt_cnt_status(num) {
	let now_cmt_count = parseInt($('#cmt_count').text().match(/\d+/)) + num;
	let formatted_comment_count = now_cmt_count.toLocaleString();
	$('#cmt_count').text('댓글수 : ' + formatted_comment_count);
	$('#cmt_section').text('댓글('+formatted_comment_count+')');
}


/** 삭제버튼 클릭시 넘겨져온 reply_no를 통해 댓글을 삭제, 성공시 엘리먼트도 같이 삭제한다.  */
function delete_comment(reply_no, element) {
	let target_comment = $('a[data-reply-no='+reply_no+']').closest('.cmt.row');
	let target_root_no = target_comment.data('root-no');
	let target_order_no = target_comment.data('order-no');
	let target_reply_list = $('div[data-root-no='+target_root_no+']');
	let target_reply_count = target_reply_list.length;

	//페이징 처리를 위한 데이터
	const comment_form_data = $('.board_comment_data');
	const comment_total_count = comment_form_data.find('.comment_total_count');
	const comment_total_count_val = parseInt(comment_total_count.val());

	$.ajax({
		url: '/board/comment/delete',
		type: 'POST',
		data: {
			replyNo: reply_no
		},
		dataType: 'json',
		success: function(result) {
			if(result != null || result != false) {
				change_cmt_cnt_status(-1);
				comment_total_count.val(comment_total_count_val-1);
				if(target_order_no == 1 && target_reply_count > 1) {
					element.removeClass();
					element.addClass('deleted');
					element.empty();
					element.append('<div class="deleted_area">삭제된 댓글입니다.</div>');
					change_cmt_cnt_status(+1);
				} else if(target_order_no > 1 && target_reply_count == 2) {
					if(target_reply_list.hasClass('deleted')) {
						target_reply_list.remove();
						change_cmt_cnt_status(-1);
					} else {
						element.remove();
					}
				} else {
					element.remove();
				}
				
				//댓글을 삭제한 이후 페이징을 위한 추가작업
				const comment_form_data = $('.board_comment_data');
				const page = comment_form_data.find('.comment_page');
				const page_val = parseInt(page.val());
				
				alert('삭제되었습니다.');
				if($('.comment_list').children().length == 0) {
					if(page_val > 1) {
						page.val(page_val - 1);
					//첫페이지인경우
					} else if(page_val == 1) {
						$('.comment_paging_box').addClass('hidden');
					}
				}
				get_comment();
			} else {
				alert('댓글 삭제에 실패했습니다.');
			}
		},
		error: function(a, b, c) {
			console.log(a, b, c);
			alert('댓글 삭제에 실패했습니다.');
		}
	});
}
let old_comment = '';
let current_update_form_target = '';
/** comment_data를 전달하면 댓글 추가하기 */
function put_comment_html(comment_data) {
	
	let html = '';
	if(comment_data.boardCommentActive == 'y')	{
		//댓글 추가 html 작성
		//조건에 따른 html	
		let reply_class = (comment_data.boardCommentGroupOrderSeq > 1)
						? 'reply' : '';	
		let author_html = (comment_data.boardAuthSeq == comment_data.memberSeq)
						? '<div class="cmt auth_ck">작성자</div>' : '';
		let fix_comment_html = (comment_data.boardCommentLastModified != null)
						? '<div class="cmt fixed">(수정됨)</div>' : '';
		let first_comment_html = (comment_data.boardCommentGroupOrderSeq == 1)
						? '<div><a href="javascript:;" class="reply_add" data-reply-no ="'+comment_data.boardCommentSeq+'"">답글</a></div>' : '';
		let comment_author_html = (comment_data.authorCk)
						? '<div><a href="javascript:;" class="reply_update" data-reply-no ="'+comment_data.boardCommentSeq+'">수정</a></div>'
						+ '<div><a href="javascript:;" class="reply_delete" data-reply-no ="'+comment_data.boardCommentSeq+'">삭제</a></div>' : '';
						
		html += '<div id="cmt_'+comment_data.boardCommentSeq+'" class="cmt row '+ reply_class +'" data-root-no ="'+comment_data.boardCommentGroupSeq+'" data-order-no ="'+comment_data.boardCommentGroupOrderSeq+'" data-no ="'+comment_data.boardCommentSeq+'">';
		html += '	<div class="board_comment_area">';
		html += '		<div class="comment_author_profile">';
		html += '			<img src="/asset/images/profile/'+ comment_data.memberProfile +'">';
		html += '		</div>';
		html += '		<div class="cmt info">';
		html += '			<div class="board_comment_author">';
		html += '				<div id="comment_nickname">'+comment_data.memberNickname+'</div>';
		html += 				author_html;
		html += '				<div class="cmt regdate">'+comment_data.boardCommentRegdate+'</div>';
		html += 				fix_comment_html;
		html += '			</div>';
		html += '			<div class="board_comment_contents">'+comment_data.boardComment+'</div>';
		html += '			<div class="board_comment_bnts">';
		html += '				<div class="comment_curd_btns">';
		html +=						first_comment_html;
		html +=						comment_author_html;
		html += '				</div>'
		html += '				<div class="comment_recommend_btns">';
		html += '					<button class="comment_thumbs_up" type="button">';
		html += '						<span><i class="fa-regular fa-thumbs-up"></i></span>';
		html += '						<span class="comment_thumbs_up_count">'+ comment_data.boardCommentThumbsUp +'</span>';
		html += '					</button>';
		html += '					<button class="comment_thumbs_down" type="button">';
		html += '						<span><i class="fa-regular fa-thumbs-down"></i></span>';
		html += '						<span class="comment_thumbs_down_count">'+ comment_data.boardCommentThumbsDown +'</span>';
		html += '					</button>';
		html += '				</div>';
		html += '			</div>';
		html += '		</div>';
		html += '	</div>';
		html += '	<div class="comment_view_data reply">';
		html += '	</div>';
		html += '</div>';
	} else if(comment_data.boardCommentActive == 'n' && comment_data.boardCommentGroupOrderSeq == 1) {
		html += '<div class="deleted" data-root-no='+comment_data.boardCommentGroupSeq+'>';
		html += '	<div class="deleted_area">삭제된 댓글입니다.</div>';
		html += '</div>';
	}
	$('.comment_list').append(html);
	
	/** 삭제버튼 */
	let delete_btn = $('.reply_delete[data-reply-no='+comment_data.boardCommentSeq+']');
	delete_btn.on('click', function() {
		delete_comment($(this).data('reply-no'), delete_btn.closest('.cmt.row'));
	});
	
	/** 업데이트 버튼클릭시 폼 보여주기 */
	let status = '';
	let update_btn = $('.reply_update[data-reply-no='+comment_data.boardCommentSeq+']');
	update_btn.on('click', function(e) {
		e.stopPropagation();
		//답글 수정 폼 동적생성
		let form = $(e.currentTarget).closest('.cmt.row').find('.board_comment_contents');
		let form_target = form.find('.comment_submit_container');	
		//기존에 존재하는 모든 form 제거
		$('.comment_submit_container.show').remove();
		//form_target이 존재하지 않는 경우 추가, 존재하는 경우 제거			
		if(form_target.length == 0 || status == 'add') {
			//상태유지를 위한 변수
			maintain_state(current_update_form_target, old_comment);
			old_comment = form.text();
			current_update_form_target = form;
			//추가된 동적폼에 이벤트 추가
			form.text('');
			let reply_no = $(this).data('reply-no');
			let append_html = form.append(get_form_html('update'));
			let form_submit_btn = append_html.find('.comment_submit_btn > button');
			append_html.find('.comment > textarea').val(old_comment);
			let comment = '';
			
			form_submit_btn.on('click', function() {
				comment = append_html.find('.comment > textarea').val();	
				if(old_comment == comment) {
					alert('변경된 내용이 없습니다.');
				} else if(comment_length_ck(comment)) {
					update_comment(reply_no, comment);
				}
			});
		} else if(old_comment.length != 0) {
			maintain_state(current_update_form_target, old_comment);
		}
		status = 'update';
	});
	
	/** 답글버튼 클릭시 폼 보여주기 */
	let add_btn = $('.reply_add[data-reply-no='+comment_data.boardCommentSeq+']');
	add_btn.on('click', function(e) {
		maintain_state(current_update_form_target, old_comment);
		e.stopPropagation();	
		//답글 폼 동적생성
		let form = $(e.currentTarget).closest('.board_comment_area').next('.comment_view_data.reply');
		let form_target = form.find('.comment_submit_container.show');
		//기존에 존재하는 모든 form 제거
		$('.comment_submit_container.show').remove();
		//form_target이 존재하지 않는경우 추가, 존재하는경우 제거		
		if(form_target.length == 0 || status == 'update') {
			//추가된 동적폼에 이벤트 추가
			let append_html = form.append(get_form_html('add'));
			let form_submit_btn = append_html.find('.comment_submit_btn > button');
			let comment = '';
			append_html.find('.comment > textarea').on('input', function() {
				comment = $(this).val();
			});
			form_submit_btn.on('click', function(event) {
				if(comment_length_ck(comment)) {
					insert_comment(comment, event);
				}
			});
		}
		status = 'add';
	});
	
	
}
/** 추천, 비추천 버튼 클릭시 */
$(document).on('click', '.comment_thumbs_up, .comment_thumbs_down', function() {
	let recommend = null;
	let comment_no = null;
	let thumbs_up_count = $(this).find('.comment_thumbs_up_count');
	let thumbs_down_count = $(this).find('.comment_thumbs_down_count');
	
	if($(this).hasClass('comment_thumbs_up')) {
		recommend = 1;
	} else if($(this).hasClass('comment_thumbs_down')) {
		recommend = -1;
	}
	comment_no = $(this).closest('.cmt.row').data('no');

	$.ajax({
		url: '/board/comment/recommend',
		data: {
			recommend: recommend,
			comment_no: comment_no
		},
		dataType: 'json',
		success: function(result) {
			let nowCount = 0;
			if(result.already == true) {
				alert('이미 해당 댓글에 추천 또는 비추천을 하셨습니다.');
			} else if(result.success == true) {
				if(recommend == 1) {
					nowCount = parseInt(thumbs_up_count.text());
					thumbs_up_count.text(nowCount + 1);
				} else if(recommend == -1) {
					nowCount = parseInt(thumbs_down_count.text());
					thumbs_down_count.text(nowCount + 1);
				}
			} else if(result.auth == false) {
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

/** 이모티콘 버튼 클릭시 */
$(document).on('click', '.comment_util_btns > .emoji', function() {
	emoji_btn();
});
/** 취소버튼 클릭시 */
$(document).on('click', '.comment_util_btns > .cancel', function() {
	maintain_state(current_update_form_target, old_comment);
	$('.comment_submit_container.show').remove();
});

/** 업데이트 버튼 클릭이후 추가조작시 상태유지하기 */
function maintain_state(form, comment) {		
	if(form.length>0 && comment.length>0) form.text(comment);
}

function get_form_html(type) {
	if(type == 'update') add_update_class = 'update';
	else add_update_class = '';

	let form_html = '';
	form_html += '<div class="comment_submit_container show '+add_update_class+'">';
	form_html += '	<div class="comment_input">';
	form_html += '		<div class="comment">';
	form_html += '			<textarea name="comment" placeholder="내용을 입력해주세요."></textarea>';
	form_html += '		</div>';
	form_html += '		<div class="comment_submit_btn">';
	form_html += '			<button class="cmt_reply_btn" type="button">등록</button>';
	form_html += '		</div>';
	form_html += '	</div>';
	form_html += '	<div class="comment_util_btns">';
	form_html += '		<button type="button" class="emoji">이모티콘</button>';
	form_html += '		<button type="button" class="cancel">취소</button>';
	form_html += '	</div>';
	form_html += '</div>';
	
	return form_html;
}

/** 이모티콘기능 이벤트 */
function emoji_btn() {
	alert('준비중인 기능입니다.');
}

$(() => {
	//현재 url확인
	let currentURI = new URL(window.location.href);
	
	//게시판 보기일때 댓글 가져오기
	if(currentURI.pathname == '/board/view') {
		get_comment()
			.then(() => {
				let cmt = currentURI.searchParams.get('cmt');
				if(cmt != null) {
					console.log(cmt);
					let target = $('.comment_list > .cmt.row[data-no="'+cmt+'"]')[0];
					if(target) {
						//화면이동
						const top = target.getBoundingClientRect().top + window.pageYOffset;
  						scrollTo({ top: top });
						
						//배경색변경
						const originalBackgroundColor = target.style.backgroundColor;
						
						target.style.transition = 'background-color 0.3s';
						target.style.backgroundColor = '#E0FFFF';
						
						//원상복귀
						setTimeout(function() {
						    target.style.backgroundColor = originalBackgroundColor;
						}, 1000);
					}
				}
			});
	}
	
	//현재 보고있는 글 표시하기
	$('.board_list').find('.board_num').each(function() {
		if($(this).text() == currentURI.searchParams.get('board')) {
			$(this).text('');
			$(this).append('<i class="fa-solid fa-angle-right"></i>');
			$(this).closest('.search_board').addClass('active');
		}
	});
	
	//댓글 옵션 선택하기
	if($('.comment_sort').val() == '') {
		$('.board_comment_sort_btn').find('a[data-cmt-sort="registrationDate"]').addClass('active');
	}
	
});
/** 댓글 정렬버튼 클릭시 */
$('.board_comment_sort_btn li a').on('click', function() {
	let comment_data = $('.board_comment_data');
	let registrationDate_btn = $('.board_comment_sort_btn').find('a[data-cmt-sort="registrationDate"]');
	let newest_btn = $('.board_comment_sort_btn').find('a[data-cmt-sort="newest"]');
	
	if($(this).data('cmt-sort') == 'newest') {
		newest_btn.addClass('active');
		registrationDate_btn.removeClass('active');
	} else {
		newest_btn.removeClass('active');
		registrationDate_btn.addClass('active');
	}
	
	comment_data.find('.comment_sort').val($(this).data('cmt-sort'));
	comment_data.find('.comment_page').val('1');
	get_comment();
});


/** 댓글 페이징버튼 클릭시 */
$('.move_prev_comment_page, .move_next_comment_page').on('click', function() {
	let comment_data = $('.board_comment_data');
	let now_page = comment_data.find('.comment_page');
	let total_page = comment_data.find('.comment_total_page');
	let now_page_val = parseInt(now_page.val());
	let total_page_val = parseInt(total_page.val());
	
	//이전버튼 클릭시
	if($(this).hasClass('move_prev_comment_page')) {
		if(now_page_val > 1) {
			now_page.val(now_page_val - 1);
			get_comment();
		}
	//다음부턴 클릭시
	} else if($(this).hasClass('move_next_comment_page')) {
		if(now_page_val < total_page_val) {
			now_page.val(now_page_val + 1);
			get_comment();
		}
	}
	let position = $('#focus_c').offset().top;
	window.scrollTo({
		top: position
  	});
});

/* 댓글 양식 */
/*
<div class="${comment.boardCommentGroupOrderSeq > 1 ? 'cmt row reply' : 'cmt row'}">
	<div class="board_comment_area">
		<div class="comment_author_profile">
			<img src="/asset/images/profile/${comment.memberProfile}">
		</div>
		<div class="cmt info">
			<div class="board_comment_author">
				<div id="comment_nickname">${comment.memberNickname}</div>
				<c:if test="${comment.memberSeq eq comment.boardAuthSeq}">
				<div class="cmt auth_ck">작성자</div>
				</c:if>
				<div class="cmt regdate">${comment.boardCommentRegdate}</div>
				<c:if test="${comment.boardCommentLastModified ne null}">
				<div class="cmt fixed">(수정됨)</div>
				</c:if>
			</div>
			<div class="board_comment_contents">${comment.boardComment}</div>
			<div class="board_comment_bnts">
				<div class="comment_curd_btns">
					<c:if test="${comment.boardCommentGroupOrderSeq eq 1}">
					<div><a href="#">답글</a></div>
					</c:if>
					<c:if test="${comment.authorCk}">
					<div><a href="#">수정</a></div>
					<div><a href="#">삭제</a></div>
					</c:if>
				</div>
				<div class="comment_recommend_btns">
					<button>
						<span><i class="fa-regular fa-thumbs-up"></i></span>
						<span>${comment.boardCommentThumbsUp}</span>
					</button>
					<button>
						<span><i class="fa-regular fa-thumbs-down"></i></span>
						<span>${comment.boardCommentThumbsDown}</span>
					</button>
				</div>
			</div>
		</div>
	</div>
	<div class="comment_view_data reply">
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
</c:forEach>
 */
