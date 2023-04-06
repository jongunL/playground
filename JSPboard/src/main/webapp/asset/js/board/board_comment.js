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
			result.forEach(function(value) {
				put_comment_html(value);
			});
		},
		error: function(a, b, c) {
			console.log(a, b, c);
		}
	});
}

/** 등록 버튼 클릭시 데이터를 추가 */
$('.comment_submit_btn > button').on('click', function() {
	let comment_val = $('.comment > textarea').val();
	
	if(comment_val.length < 1) {
		alert('댓글 내용을 입력해주세요.');
	} else if(comment_val.length > 300) {
		alert('댓글은 최대 300자까지 입력가능합니다.');
	} else {
		insert_comment(comment_val);
	}

});

/** comment_val = 입력받은 댓글 내용 */
function insert_comment(comment_val) {
	const board_form_data = {};
	$.each($('#board_view_data').serializeArray(), function() {
		board_form_data[this.name] = this.value;
	});
	
	$.ajax({
		url: '/board/comment/add',
		type: 'POST',
		data: {
			boardFormData : JSON.stringify(board_form_data),
			comment : comment_val
		},
		dataType: 'json',
		success: function(result) {
			if(result == null) alert('댓글 등록에 실패했습니다.');
			else put_comment_html(result);
		},
		error: function(a, b, c) {
			console.log(a, b, c);
			alert('댓글 등록에 실패했습니다.');
		}
	});
}

/** comment_data를 전달하면 댓글 추가하기 */
function put_comment_html(comment_data) {
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
					? '<div><a href="javascript:;">답글</a></div>' : '';
	let comment_author_html = (comment_data.authorCk)
					? '<div><a href="javascript:;">수정</a></div>'
					+ '<div><a href="javascript:;">삭제</a></div>' : '';
	
	html += '<div class="cmt row '+ reply_class +'">';
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
	html += '			</div>'
	html += '			<div class="comment_recommend_btns">';
	html += '				<button>';
	html += '					<span><i class="fa-regular fa-thumbs-up"></i></span>';
	html += '					<span>'+ comment_data.boardCommentThumbsUp +'</span>';
	html += '				</button>';
	html += '				<button>';
	html += '					<span><i class="fa-regular fa-thumbs-down"></i></span>';
	html += '					<span>'+ comment_data.boardCommentThumbsDown +'</span>';
	html += '				</button>';
	html += '			</div>';
	html += '		</div>';
	html += '	</div>';
	html += '</div>';
	$('.comment_list').append(html);
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
</div>
</c:forEach>
 */
