let currentURI = null;
$(() => {
	currentURI = new URL(window.location.href);
	let path = currentURI.pathname;
	
	if(path == '/member/profile') {
		$('#mp').addClass('active');
	} else if(path == '/member/alarm') {
		$('#al').addClass('active');
	} else if(path == '/member/board') {
		$('#bd').addClass('active');				
	} else if(path == '/member/comment') {
		$('#cm').addClass('active');								
	} else if(path == '/member/account') {
		$('#ac').addClass('active');												
	}
	get_contents(path);
});

function get_contents(path) {
	let url = null;			
	if(path == '/member/alarm') {
		url = '/member/getAlarm';
		get_contents_list(url, path);
	} else if(path == '/member/board') {
		url = '/member/getBoard';
		get_contents_list(url, path);
	} else if(path == '/member/comment') {
		url = '/member/getComment';
		get_contents_list(url, path);
	}
}

function move(e) {		
	let page = $('#contents_container_form > #page');
	let block_size = $('#contents_container_form > #blockSize');
	let total_page = $('#contents_container_form > #totalPage');
	
	//페이지 계산을 위한 변수
	let page_val = 	parseInt(page.val());
	let block_size_val = parseInt(block_size.val());
	let total_page_val = parseInt(total_page.val());
	let move_page_num = null;
	
	if($(e.currentTarget).hasClass('move_prev_contents_block')) {
		move_page_num = (Math.ceil((page_val - block_size_val)/block_size_val)-1)*5 + 1;
		move_page_num = move_page_num <= 0 ? 1 : move_page_num;
	} else if($(e.currentTarget).hasClass('move_prev_contents_page')) {
		move_page_num = page_val - 1;
		move_page_num = move_page_num <= 0 ? 1 : move_page_num;
	} else if($(e.currentTarget).hasClass('move_next_contents_page')) {
		move_page_num = page_val + 1;				
		move_page_num = move_page_num > total_page_val ? total_page_val : move_page_num;
	} else if($(e.currentTarget).hasClass('move_next_contents_block')) {				
		move_page_num = (Math.ceil((page_val + block_size_val)/block_size_val)-1)*5 + 1;
		move_page_num = move_page_num > total_page_val ? total_page_val : move_page_num;
	}
	
	if(move_page_num != page_val) {
		page.val(move_page_num);
		get_contents(currentURI.pathname);
	}
}

function get_contents_list(url, path) {
	/*페이징 처리를 위한 값들*/
	let page = $('#contents_container_form > #page');
	let page_size = $('#contents_container_form > #pageSize');
	let total_count = $('#contents_container_form > #totalCount');
	let total_page = $('#contents_container_form > #totalPage');
	let block_size = $('#contents_container_form > #blockSize');
	
	$.ajax({
		type: 'POST',
		dataType: 'JSON',
		url: url,
		data: {
			path: path,
			page: $('#contents_container_form > #page').val()
		},
		success: function(result) {	
			$('.contents_list').children().remove();
			if(result != null && result != undefined && result.list.length > 0) {
				page.val(result.nowPage);
				page_size.val(result.pageSize);
				total_count.val(result.totalCount);
				total_page.val(result.totalPage);
				block_size.val(result.blockSize);
				
				$('.member_contents_container .contents_btn').addClass('active');
				result.list.forEach(function(data) {	
					create_contents_list(contents_object(data, path));
				});
				
				$('.contents_paging_list').children().remove();
				if(total_page.val() > 1) {
					$('.contents_paging').addClass('active');
					create_paging_box(page.val(), block_size.val(), total_page.val());
				} else {
					$('.contents_paging').removeClass('active');
				}						
			} else if(result.list.length == 0) {
				$('.member_contents_container .contents_btn').removeClass('active');
				create_no_contents();
			} else {
				alert('불러오는데 실패했습니다. 잠시후 다시 시도해주세요.');
			}
		},
		error: function(a, b, c) {
			console.log(a, b, c);
		}
	}).then(function(response) {
		$('.select_all_btn').text('전체선택');
		if($('.contents_paging').hasClass('active')) {
			$('.contents_paging_list').find('span[data-p-num="'+page.val()+'"]').css('color', 'red').css('text-decoration', 'underline').css('cursor', 'default');
		}
	});
}

function create_no_contents() {
	let html = '';
	html = '<div class="no_contents">활동내역이 없습니다.</div>'
	$('.contents_list').append(html);
}

function contents_object(input, path) {		
	let num = null;
	let url = null;
	let img = null;
	let content = null;
	let regdate = null;
	
	if(path == '/member/alarm') {				
		num = input.memberAlarmSeq;
		url = '\'/board/view?num='+input.boardTitleSeq+'&board='+input.boardSeq+'&cmt='+input.CommentSeq+'\';';
		img = '/asset/images/profile/'+input.memberProfile;
		content = input.memberNickname + '님의 답글:' + input.message;
		regdate = input.created;				
		return get_contents_object(num, url, img, content, regdate);
	} else if(path == '/member/board') {
		num = input.boardSeq;
		url = '\'/board/view?num='+input.boardTitleSeq+'&board='+input.boardSeq+'\';';
		img = '/asset/images/board_img/'+input.boardTitleImg;
		content = '['+input.boardTitle+'] 게시판에 남긴 글 : '+input.boardSubject;
		regdate = input.boardRegdate;				
		return get_contents_object(num, url, img, content, regdate);
	} else if(path == '/member/comment') {
		num = input.boardCommentSeq;
		url = '\'/board/view?num='+input.boardTitleSeq+'&board='+input.boardSeq+'&cmt='+input.boardCommentSeq+'\';';
		img = '/asset/images/board_img/'+input.boardTitleImg;
		content = '['+input.boardTitle+'] 게시판에 남긴 댓글 : '+input.boardComment;
		regdate = input.boardCommentRegdate;
		return get_contents_object(num, url, img, content, regdate);
	}		
}

function get_contents_object(num, url, img, content, regdate) {
	let object = {
			num: num,
			url: url,
			img: img,
			content: content,
			regdate: regdate
	}			
	return object;
}

function create_paging_box(max, block_size, total_page) {
	if(max > 0) {
		let block = Math.ceil(max/block_size);
		let block_start = ((block - 1) * 5) + 1;
		let block_end = block_start + (block_size - 1);
		block_end = block_end > total_page ? total_page : block_end;
		create_paging_button(block_start, block_end);
	}
}

function create_paging_button(start, end) {
	let paging_btn_list = $('.contents_paging_list');
	
	for(start; start<=end; start++) {
		let html = ''
		html += '<span data-p-num="'+start+'">'+start+'</span>';
		let append_html = paging_btn_list.append(html);
		let event_target_html = append_html.find('span[data-p-num="'+start+'"]');
		event_target_html.on('click', function(e) {
			let target_page_val = $(e.target).data('p-num');
			let now_page = $('#contents_container_form > #page');
			
			if(target_page_val != now_page.val()) {
				$('#contents_container_form > #page').val($(e.target).data('p-num'));
				get_contents(currentURI.pathname);
			}
		});
	}
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
	} else if(path == '/member/board') {
		let url = '/member/deleteBoard';
		remove(url, path, num);
	} else if(path == '/member/comment') {
		let url = '/member/deleteComment';
		remove(url, path, num);
	}	
}

function remove(url, path, num) {		
	$.ajax({
		url: url,
		type: 'POST',
		data: {
			seq: num
		},
		dataType: 'JSON',
		success: function(result) {
			if(result) {
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

function create_contents_list(input) {
	let list = $('.contents_list');
	
	let html = '';
	html += '<div class="row" data-n-num="'+input.num+'">';
	html += '	<div class="col select">';
	html += '		<input type="checkbox" name="checked" value="'+input.num+'">';
	html += '	</div>';
	html += '	<div class="col contents" onclick="location.href='+input.url+'">';
	html += '		<img src="'+input.img+'">';
	html += '		<div class="contents_status">';
	html += '			<div class="contents">'+input.content+'</div>';
	html += '			<div class="regdate">'+input.regdate+'</div>';
	html += '		</div>';
	html += '	</div>';
	html += '	<div class="col remove">';
	html += '		<i class="fa-solid fa-x"></i>';
	html += '	</div>';
	html += '</div>';
	
	list.append(html);
	let delete_btn = $('.contents_list > .row[data-n-num='+input.num+'] > .col.remove > i');
	delete_btn.on('click', function(e) {
		let parents_element = $(e.target).closest('.row');
		remove_contents(parents_element.data('n-num'));
	});
	
	let contents_ck_box = $('.contents_list > .row[data-n-num='+input.num+'] > .col.select > input');
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