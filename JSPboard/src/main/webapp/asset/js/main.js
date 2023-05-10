const toggle_btn = document.querySelector('.navbar_togleBtn');
const toggle_btn_icon = document.querySelector('.navbar_togleBtn > i');
const menu = document.querySelector('#header_menu');

//헤더부분
//모바일버전 nav-var
toggle_btn.addEventListener('click', ()=> {
	menu.classList.toggle('active');
	
	if(toggle_btn_icon.classList.contains('fa-bars')) {
		toggle_btn_icon.classList.remove('fa-bars');
		toggle_btn_icon.classList.add('fa-xmark');
	} else {
		toggle_btn_icon.classList.remove('fa-xmark');
		toggle_btn_icon.classList.add('fa-bars');
	}
});

//PC버전 nav-var
$(document).on('click', '#header_profile_img', (e) => {
	alarm_popup_ck(e);
	e.stopPropagation();
	if($('#header_sub_menu').hasClass('show')) {
		$('#header_sub_menu').removeClass('show');	
		$('#menu_toggle_btn > i').removeClass('fa-angle-up');
		$('#menu_toggle_btn > i').addClass('fa-angle-down');
	} else {
		$('#header_sub_menu').addClass('show');
		$('#menu_toggle_btn > i').removeClass('fa-angle-down');
		$('#menu_toggle_btn > i').addClass('fa-angle-up');
	}
});

$(document).on('click', (e) => {
	layer_popup_ck(e);
	alarm_popup_ck(e);
});

function layer_popup_ck(e) {
	let layer_popup = $('#header_sub_menu');
	if(layer_popup.has(e.target).length === 0) {
		layer_popup.removeClass('show');
		$('#menu_toggle_btn > i').removeClass('fa-angle-up');
		$('#menu_toggle_btn > i').addClass('fa-angle-down');
	}
}

function alarm_popup_ck(e) {
	let alarm_popup = $('#header_alarm_sub_menu');
	
	if(alarm_popup.has(e.target).length === 0) {
		alarm_popup.removeClass('show');
		$('#header_alarm > a > i').addClass('fa-regular');	
		$('#header_alarm > a > i').removeClass('fa-solid');
	}
}

function get_alarm_list() {
	$.ajax({
		type: 'POST',
		dataType: 'JSON',
		url: '/member/getAlarm',
		success: function(result) {
			if(result.length > 0) {
				result.forEach(function(data) {
					create_element(data);
				});
			} else {
				check_alarm_empty();
			}
		},
		error: function(a, b, c) {
			console.log(a, b, c);
		}
	});
}

function create_element(data) {	
	console.log(data);
	let html = '';
	html += '<li class="alarm" data-n-num='+data.memberAlarmSeq+'>';
	html += '	<a href="/board/view?num='+data.boardTitleSeq+'&board='+data.boardSeq+'">';
	html += '		<img src="/asset/images/profile/'+data.memberProfile+'">';
	html += '	</a>';
	html += '	<div id="alarm_content">';
	html += '		<a href="/board/view?num='+data.boardTitleSeq+'&board='+data.boardSeq+'&cmt='+data.CommentSeq+'">';
	html += '			<div class="contents">';
	html += '				'+data.memberNickname+'님의 답글 : '+data.message;
	html += '			</div>';
	html += '			<div class="regdate">'+data.created+'</div>';
	html += '		</a>';
	html += '	</div>';
	html += '	<div class="remove_btn">';
	html += '		<i class="fa-solid fa-x"></i>';
	html += '	</div>';
	html += '</li>';
		
	$('#header_alarm_sub_menu').append(html);
	
	let delete_btn = $('.alarm[data-n-num='+data.memberAlarmSeq+'] > .remove_btn > i');
	delete_btn.on('click', function(e) {
		let parents_element = $(e.target).closest('.alarm');
		check_alarm(parents_element.data('n-num'), parents_element);
	});
}

//알림 모두읽기 버튼 클릭시
$('#alarm_all_ck').on('click', function() {
	$.ajax({
		type: 'POST',
		dataType: 'JSON',
		url: '/member/checkAllAlarm',
		success: function(result) {
			if(result == true) {
				$('#header_alarm_sub_menu > .alarm').remove();
				check_alarm_empty();
			} else {
				alert('알림 제거에 실패하였습니다.');
			}
		},
		error: function(a, b, c) {
			console.log(a, b, c);
		}
	});
});


function check_alarm_empty() {
	if($('#header_alarm_sub_menu > .alarm').length === 0) {
		let html = '';
		html += '<li class="alarm">';
		html += '	<div class="no_alarm">받은 알람이 없습니다.</div>';
		html += '</li>';
		$('#header_alarm_sub_menu').append(html);
		$('#header_alarm_counter').css('display', 'none');
		$('#header_alarm_counter').text('0');
	}
}


//알림 x버튼 클릭시
function check_alarm(num, element) {
	
	$.ajax({
		type: 'POST',
		data: {
			notificationSeq : num
		},
		dataType: 'JSON',
		url: '/member/checkAlarm',
		success: function(result) {
			if(result == true) {
				element.remove();
				check_alarm_empty();
			} else {
				alert('알림 제거에 실패하였습니다.');
			}
		},
		error: function(a, b, c) {
			console.log(a, b, c);
		}
	});
}

//알람버튼 클릭시
$(document).on('click', '#header_alarm', (e) => {
	layer_popup_ck(e);
	e.stopPropagation();
	
	if($('#header_alarm_sub_menu').hasClass('show')) {
		$('#header_alarm_sub_menu').removeClass('show');	
		$('#header_alarm > a > i').addClass('fa-regular');	
		$('#header_alarm > a > i').removeClass('fa-solid');	
	} else {
		get_alarm_list();
		$('#header_alarm_sub_menu').addClass('show');
		$('#header_alarm > a > i').removeClass('fa-regular');	
		$('#header_alarm > a > i').addClass('fa-solid');	
	}
});

$('#header_alarm_sub_menu').on('click', (e) => {
  e.stopPropagation();
});


//알람 유무체크
$(() => {
	$.ajax({
		type: 'POST',
		dataType: 'JSON',
		url: '/member/getAlarmCount',
		success: function(result) {
			console.log(result);
			if(result > 0) {
				$('#header_alarm_counter').css('display', 'block');
				$('#header_alarm_counter').text(result > 9 ? '+9' : result);
			} else {
				$('#header_alarm_counter').css('display', 'none');		
			}
		},
		error: function(a, b, c) {
			console.log(a, b, c);
		}
	});
});

//로그아웃 버튼 클릭시
function logout() {	
	$.ajax({
		type: 'GET',
		url: '/logout',
		success: function() {
			location.reload();
		},
		error: function(a,b,c) {
			if(confirm('로그아웃에 실패했습니다. 다시 시도하시겠습니까?')) {
				logout();
			}
			console.log(a,b,c);
		}
	});
}