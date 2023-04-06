/** 게시판의 board_num와 보고있는 카테고리의 num을 전송하면 댓글 정보를 가져오는 함수 */
function get_comment(board_num, board_title_num) {
	$.ajax({
		url: '/board/comment',
		type: 'GET',
		data: {
			boardSeq : board_num,
			boardTitleSeq : board_title_num
		},
		dataType: 'json',
		success: function(result) {
			if(result != null) {
				result.forEach(function(value) {
					put_comment_html(value);
				});
			} else {
				alert('댓글 목록을 가져오는데 실패했습니다.');
			}
		},
		error: function(a, b, c) {
			console.log(a, b, c);
		}
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
	const root_no = $(event.target).closest('.cmt.row').data('root-no');
	const order_no = $(event.target).closest('.cmt.row').data('order-no');
	const board_form_data = {};
	
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
			orderNo : order_no
		},
		dataType: 'json',
		success: function(result) {
			if(result == null) { 
				alert('댓글 등록에 실패했습니다.'); 
			} else {
				 put_comment_html(result);
				 $('.comment > textarea').val('');
				 change_cmt_cnt_status(1);
			}
		},
		error: function(a, b, c) {
			console.log(a, b, c);
			alert('댓글 등록에 실패했습니다.');
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
	$.ajax({
		url: '/board/comment/delete',
		type: 'POST',
		data: {
			replyNo: reply_no
		},
		dataType: 'json',
		success: function(result) {
			if(!(result == null || result == false)) {
				change_cmt_cnt_status(-1);
				element.remove();
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

/** comment_data를 전달하면 댓글 추가하기 */
function put_comment_html(comment_data) {
	console.log(comment_data);
	//댓글 추가 html 작성
	let html = '';
	//조건에 따른 html	
	let reply_class = (comment_data.boardCommentGroupOrderSeq > 1)
					? 'reply' : '';	
	let author_html = (comment_data.memberSeq == comment_data.boardAuthSeq)
					? '<div class="cmt auth_ck">작성자</div>' : '';
	let fix_comment_html = (comment_data.boardCommentLastModified != null)
					? '<div class="cmt fixed">(수정됨)</div>' : '';
	let first_comment_html = (comment_data.boardCommentGroupOrderSeq == 1)
					? '<div><a href="javascript:;" class="reply_add" data-reply-no ="'+comment_data.boardCommentSeq+'"">답글</a></div>' : '';
	let comment_author_html = (comment_data.authorCk)
					? '<div><a href="javascript:;" class="reply_update" data-reply-no ="'+comment_data.boardCommentSeq+'">수정</a></div>'
					+ '<div><a href="javascript:;" class="reply_delete" data-reply-no ="'+comment_data.boardCommentSeq+'">삭제</a></div>' : '';
					
	html += '<div class="cmt row '+ reply_class +'" data-root-no ="'+comment_data.boardCommentGroupSeq+'" data-order-no ="'+comment_data.boardCommentGroupOrderSeq+'">';
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
	html += '					<button>';
	html += '						<span><i class="fa-regular fa-thumbs-up"></i></span>';
	html += '						<span>'+ comment_data.boardCommentThumbsUp +'</span>';
	html += '					</button>';
	html += '					<button>';
	html += '						<span><i class="fa-regular fa-thumbs-down"></i></span>';
	html += '						<span>'+ comment_data.boardCommentThumbsDown +'</span>';
	html += '					</button>';
	html += '				</div>';
	html += '			</div>';
	html += '		</div>';
	html += '	</div>';
	html += '	<div class="comment_view_data reply">';
	html += '	</div>';
	html += '</div>';
	$('.comment_list').append(html);
	
	/** 삭제버튼 */
	let delete_btn = $('.reply_delete[data-reply-no='+comment_data.boardCommentSeq+']');
	delete_btn.on('click', function() {
		delete_comment($(this).data('reply-no'), delete_btn.closest('.cmt.row'));
	});
	
	/** 업데이트 버튼 - 구현예정 */
	let update_btn = $('.reply_update[data-reply-no='+comment_data.boardCommentSeq+']');
	update_btn.on('click', function() {
		add_btn.closest('.board_comment_area')
		.next('.comment_view_data.reply')
		.append(get_form_html());
	});
	
	/** 답글버튼 클릭시 폼 보여주기 - 구현중 */
	let add_btn = $('.reply_add[data-reply-no='+comment_data.boardCommentSeq+']');
	add_btn.on('click', function(e) {
		e.stopPropagation();	
		//답글 폼 동적생성
		let form = $(e.currentTarget).closest('.board_comment_area').next('.comment_view_data.reply');
		let form_target = form.find('.comment_submit_container.show');
		//기존에 존재하는 모든 form 제거
		$('.comment_submit_container.show').remove();
		//form_target이 존재하지 않는경우 추가, 존재하는경우 제거
		if(form_target.length == 0) {
			//추가된 동적폼에 이벤트 추가
			let append_html = form.append(get_form_html());
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
			
		} else if(form_target.length == 1) {
			form_target.remove();
		}
	});
}

$(document).on('click', '.comment_util_btns > button', function() {
	emoji_btn();
});

function get_form_html() {
	let form_html = '';
	form_html += '<div class="comment_submit_container show">';
	form_html += '	<div class="comment_input">';
	form_html += '		<div class="comment">';
	form_html += '			<textarea name="comment" placeholder="내용을 입력해주세요."></textarea>';
	form_html += '		</div>';
	form_html += '		<div class="comment_submit_btn">';
	form_html += '			<button type="button">등록</button>';
	form_html += '		</div>';
	form_html += '	</div>';
	form_html += '	<div class="comment_util_btns">';
	form_html += '		<button type="button">이모티콘</button>';
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
	let cureentURI = new URL(window.location.href);
	
	//게시판 보기일때 댓글 가져오기
	if(cureentURI.pathname == '/board/view') {
		let board_num = $('#board_num').val();
		let board_title_num = $('#board_title_num').val();
		get_comment(board_num, board_title_num);
	}
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
