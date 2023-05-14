$(() => {
	old_nickname = $('#profile_nickname').val();
});


function set_profile_prev(event) {			
	if(event.target.files && event.target.files[0]) {
		let reader = new FileReader();
			reader.onload = function() {
			let output = $('#profile_img_prev').attr('src', reader.result);
 		};
 		reader.readAsDataURL(event.target.files[0]);
 		$('#img_change').val('true');
	}
}


$('#profile_form').on('submit', function(e) {
	nickname_ck();
	let img_chnage_val = $('#img_change').val();
	let nickname_change_val = $('#nickname_change').val();
			
	if(img_chnage_val == 'true' || nickname_change_val == 'true') {
		change_profile();				
	} else {
		alert('변경된 사항이 없습니다.');
	}
});

function change_profile() {
	let form_data = new FormData($('#profile_form')[0]);
	
	$.ajax({
		type: 'POST',
		url: '/member/change/profile',
		data: form_data,
		processData: false,
		contentType: false,
		dataType: 'json',
		success: function(result) {
			if(result) {
				alert('변경이 완료되었습니다.');
			} else {
				alert('변경에 실패하였습니다.');
			}
			location.reload();
		},
		error: function(a, b, c) {
			console.log(a, b, c);
		}
	});
}

$('#profile_nickname').on('keyup', function(e) {
	if(e.keyCode == 13) {
		$('.setting_btn_area > button').focus();
	}
});

let old_nickname = null;				
function nickname_ck() {	
	let input_nickname = $('#profile_nickname').val();
	
	if(old_nickname == input_nickname) {
		$('#nickname_msg').text('');
		$('#nickname_change').val('false');
	} else {
		if(!nickname_validation_ck(input_nickname)) return;
		nickname_duplication_ck(input_nickname);	
	}	
}

/** nickname 유효성검사 */
function nickname_validation_ck(nickname) {			
	const regex = /^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ]{1,10}$/;
	if(regex.test(nickname)) {
		return true;
	} else {
		if(nickname.length <= 0 ) $('#nickname_msg').text('필수 정보입니다.').css('color', 'red');
		else $('#nickname_msg').text('닉네임은 영문자, 숫자, 한글의 조합으로 최대 10자리를 입력할 수 있습니다.').css('color', 'red');
		$('#nickname_change').val('false');
		return false;
	}
}

function nickname_duplication_ck(nickname) {
	$.ajax({
		type: 'POST',
		url: '/nicknameCheck',
		data: 'nickname=' + nickname,
		dataType: 'text',
		async: false,
		success: function(result) {
			if(result > 0) {	
				$('#nickname_change').val('false');
				$('#nickname_msg').text('이미 사용중인 닉네임 입니다').css('color', 'red');
			} else {
				$('#nickname_change').val('true');
				$('#nickname_msg').text('사용가능한 닉네임 입니다').css('color', 'green');
			}
		},
		error: function(a, b, c) {
			console.log(a, b, c);
		}
	});
}//db_duplication_ck