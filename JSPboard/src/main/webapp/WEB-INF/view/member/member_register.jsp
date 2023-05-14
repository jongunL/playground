<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>PlayGround</title>
<%@include file="/WEB-INF/view/inc/asset.jsp" %>
<link rel="stylesheet" href="/asset/css/register.css">
</head>
<body>
	<div class="root_container">
		<%@include file="/WEB-INF/view/inc/header.jsp" %>
		<div class="content_wrapper">
			<article>
				<div class="board_article">
					<!-- 회원가입 양식 -->
					<form action="/registerOk" method="POST" id="regist_form">
						<!-- 유효성검사 -->
						<input type="hidden" id="id_ck" value="false">
						<input type="hidden" id="pwd1_ck" value="false">
						<input type="hidden" id="pwd2_ck" value="false">
						<input type="hidden" id="nickname_ck" value="false">
						<input type="hidden" id="email_ck" value="false">
						<input type="hidden" id="email_auth_ck" value="false">
						<!-- container -->
						<div class="join_content">
							<!-- 로고 -->
							<h1><a href="/">PlayGround!</a></h1>
							<div class="join_group">
								<!-- 아이디 -->
								<div class="join_row">
									<h3 class="join_title">아이디</h3>
									<span class="ps_box"><input type="text" id="id" name="id"></span>
									<span class="error_next_box" id="id_msg"></span>
								</div>
								<!-- 비밀번호 -->
								<div class="join_row">
									<!-- 비밀번호 입력 -->
									<h3 class="join_title">비밀번호</h3>
									<span class="ps_box"><input type="password" id="pwd1" name="pwd1"></span>
									<span class="error_next_box" id="pwd1_msg"></span>
									<!-- 입력 비밀번호 재확인 -->
									<h3 class="join_title">비밀번호 재확인</h3>
									<span class="ps_box"><input type="password" id="pwd2" name="pwd2"></span>
									<span class="error_next_box" id="pwd2_msg"></span>
								</div>
								<!-- 닉네임 -->
								<div class="join_row">
									<h3 class="join_title">닉네임</h3>
									<span class="ps_box"><input type="text" id="nickname" name="nickname"></span>
									<span class="error_next_box" id="nickname_msg"></span>
								</div>
								<!-- 이메일 -->
								<div class="join_row">
									<h3 class="join_title">이메일</h3>
									<!-- 아이디입력 -->
									<span class="ps_box">
										<input type="text" id="email" name="email">
									</span>
									<span class="error_next_box" id="email_msg"></span>
									<!-- 인증번호 입력 -->
									<span class="ps_box email_auth">
										<span class="email_auth_area">
											<input type="text" id="email_auth" name="email_auth" placeholder="인증번호를 입력하세요" disabled="disabled">
											<a href="#" id="auth_send" role="button">
												<span id="auth_btn">인증번호 받기</span>
											</a>
										</span>
									</span>
									<span class="error_next_box" id="emali_auth_check"></span>
								</div>
								<!-- 버튼 -->
								<div class="btn_area">
									<button type="button" id="btn_join">
										<span>가입하기</span>
									</button>
								</div>
							</div>
						</div>
					</form>
					<!-- 회원가입 양식 끝 -->
				</div>
			</article>
			<%@include file="/WEB-INF/view/inc/util_btn.jsp" %>
		</div>
		<%@include file="/WEB-INF/view/inc/footer.jsp" %>
	</div>
	<script type="text/javascript" src="/asset/js/view_mode.js"></script>
	<script type="text/javascript">		
		/* id 중복성, 유효성검사 */
		$('#id').on('blur', () => {
			if(!id_validation_ck($('#id').val())) return;
			db_duplication_ck('#id', $('#id').val());
		});
		/** id 유효성검사 */
		function id_validation_ck(id) {
			const regex = /^[a-zA-Z0-9]{4,16}$/;
			if(regex.test(id)) {
				return true;
			} else {
				if(id.length <= 0) $('#id_msg').text('필수 정보입니다.').css('color', 'red');
				else $('#id_msg').text('아이디는 영문자, 숫자의 조합으로 4~16글자를 입력할 수 있습니다.').css('color', 'red');
				$('#id_ck').val('false');
				return false;
			}
		};
		
		/** 비밀번호 유효성검사 */
		$('#pwd1').on('blur', () => {
			if($('#pwd2').val().length > 0) pwd_equla_ck();
			if(!pwd_validation_ck($('#pwd1').val())) return;
			$('#pwd1_ck').val('true');
		});	
		
		
		function pwd_validation_ck(pwd) {
			const regex = /^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ!@#$%^&*()_+|<>?]{6,16}$/;

			if(regex.test(pwd)) {
				$('#pwd1_msg').text('사용가능한 비밀번호입니다').css('color', 'green');
				return true;
			} else {
				if(pwd.length <= 0 ) $('#pwd1_msg').text('필수 정보입니다.').css('color', 'red');
				else $('#pwd1_msg').text('6~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.').css('color', 'red');
				$('#pwd1_ck').val('false');
				return false;
			}
		}
		
		$('#pwd2').on('keyup, blur', () => {
			pwd_equla_ck();
		});
		
		function pwd_equla_ck() {			
			if($('#pwd1').val() == $('#pwd2').val() && $('#pwd2').val().length > 0) {
				$('#pwd2_msg').text('비밀번호가 일치합니다.').css('color', 'green');
				$('#pwd2_ck').val('true');
				return true;
			} else {
				if($('#pwd2').val().length <= 0) $('#pwd2_msg').text('필수 정보입니다.').css('color', 'red');
				else $('#pwd2_msg').text('비밀번호가 일치하지 않습니다.').css('color', 'red');
				$('#pwd2_ck').val('false');
				return false;
			}
		}		
		
		/* nickname 중복성, 유효성검사 */
		$('#nickname').on('blur', () => {
			if(!nickname_validation_ck($('#nickname').val())) return;
			db_duplication_ck('#nickname', $('#nickname').val());
		});
		/** nickname 유효성검사 */
		function nickname_validation_ck(nickname) {
			const regex = /^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ]{1,10}$/;
			if(regex.test(nickname)) {
				return true;
			} else {
				if(nickname.length <= 0 ) $('#nickname_msg').text('필수 정보입니다.').css('color', 'red');
				else $('#nickname_msg').text('닉네임은 영문자, 숫자, 한글의 조합으로 최대 10자리를 입력할 수 있습니다.').css('color', 'red');
				$('#nickname_ck').val('false');
				return false;
			}
		}
		
		/* email 중복성, 유효성검사 */
		let email = '';
		let email_auth_status = false;
		let is_change = false;
		$('#email').on('blur', () => {
			let temp = $('#email').val().trim();
			
			//입력된 이메일이 변경된 경우
			if(temp != email || temp == '') {
				//인증 진행중인 경우
				if(email_auth_status) {
					is_change = confirm('인증 진행중인 이메일입니다. 정말로 변경할까요?');
					//이메일 변경여부
					if(is_change) {
						auth_disable();
						email = temp;
						email_check(email);
					} else {
						$('#email').val(email);
					}
				//인증 미진행인 경우
				} else {
					email = temp;
					email_check(email);
				}
				
			}
		});
		
		function email_check(email) {
			//유효성검사
			if(!email_validation_ck(email)) return;
			//중복성검사
			db_duplication_ck('#email', email);
		}
		
		/** email 유효성검사 */
		function email_validation_ck(email) {
			const regex = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
			if(regex.test(email)) {
				return true;
			} else {
				if(email.length <= 0) $('#email_msg').text('필수 정보입니다.').css('color', 'red');
				else $('#email_msg').text('잘못된 이메일 형식입니다. 다시 입력해주세요.').css('color', 'red');
				return false;
			}
		}
		
		/* email 인증 */
		$('#auth_send').on('mouseup', () => {	
			let email_ck = $('#email_ck').val();

			if(email_ck == 'false') {
				auth_disable();
			} else if(email_auth_status == false) {
				auth_able();
				sendMail(email);
			} else if(email_auth_status == true) {
				compareCode($('#email_auth').val());
			}
		});
		
		function auth_able() {
			$('#email_auth').attr('disabled', false);
			$('#auth_btn').text('인증하기');
		}
		
		function auth_disable() {
			$('#email_ck').val('false');
			$('#email_auth_ck').val('false');
			
			$('#email_auth').val('');
			$('#email_auth').attr('disabled', true);
			$('#auth_btn').text('인증번호 받기');
			$('#emali_auth_check').text('');			
			email_auth_status = false;
			code_equla_status = false;
			time = -1;
		}	
		
		let time = 0;
		let auth_status_msg = '';
		function sendMail(email) {
			auth_status_msg = '인증을 완료해주세요.';
			
			$.ajax({
				type: 'POST',
				//url: '/sendMail',
				url: '/temp',
				data: 'email='+email,
				dataType: 'text',
				success: function(result) {
					//메일 전송에 성공한 경우
					if(result < 0) {
						//메일 전송에 실패한 경우	
						auth_disable();
						$('#emali_auth_check').text('잠시후 다시 시도해주세요.').css('color', 'red');
					} else {
						//메일 전송에 성공한 경우
						time = result;
						countDown();
						email_auth_status = true;
					}
				},
				error: function(a, b, c) {
					console.log(a, b, c);
				}
			});
		}
		
		let code_equla_status = false;
		function compareCode(code) {
			$.ajax({
				type: 'POST',
				url: '/compareCode',
				data: 'code='+code,
				dataType: 'json',
				success: function(result) {
					code_equla_status = result.success;
					if(code_equla_status == true) auth_status_msg = '인증이 완료되었습니다.';
					if(code_equla_status == false) {
						countDown();
						auth_status_msg = '코드가 일치하지 않습니다.';
					}
				},
				error: function(a, b, c) {
					console.log(a, b, c);
				}
			});
		}
				
		function countDown() {
			time--;		
			//인증이 완료된경우
			if(code_equla_status == true) {
				$('#emali_auth_check').text(auth_status_msg).css('color', 'green');
				$('#email_auth').attr('disabled', true);
				$('#email_auth_ck').val('true');
			//인증이 진행중인경우
			} else if(time > 0) {
				let min = time%60;
				$('#emali_auth_check').text(auth_status_msg+'('+ Math.floor(time/60) +':'+ (min < 10 ? '0'+min : min) +')').css('color', 'red');
				setTimeout(countDown, 1000);
			//이메일 인증기간이 지난경우
			} else if(!is_change) {
				$('#emali_auth_check').text('인증기한이 만료되었습니다. 다시 시도해주세요').css('color', 'red');
				auth_disable();
			}
		}
		
		/** html id와 id의 value를 넣어주면 DB에서 중복체크하는 메서드 */
		function db_duplication_ck(id, data) {
			id = id.replace('#', '');

			$.ajax({
				type: 'POST',
				url: '/'+ id +'Check',
				data: id + '=' + data,
				dataType: 'text',
				success: function(result) {
					let temp = '';
					if(id == 'id') {
						temp = '아이디';
					} else if(id == 'nickname') {
						temp = '닉네임';
					} else if(id == 'email') {
						temp = '이메일';
					}
					if(result > 0) {	
						$('#' + id + '_ck').val('false');
						$('#'+id+'_msg').text('이미 사용중인 '+temp+'입니다').css('color', 'red');
					} else {
						$('#' + id + '_ck').val('true');
						$('#'+id+'_msg').text('사용가능한 '+temp+'입니다').css('color', 'green');
					}
				},
				error: function(a, b, c) {
					console.log(a, b, c);
				}
			});
		}//db_duplication_ck
		
		//enter키 눌렀을때 다음 input 태그로 foscus 전환
		$('#id, #nickname, #pwd1, #pwd2, #email').on('keyup', (event) => {			
			if(event.keyCode == 13) {
				const arr = ['id', 'pwd1', 'pwd2', 'nickname', 'email', 'auth_send'];
				for(let i =0; i< arr.length; i++) {
					if((arr[i] == event.target.id) && (i+1 != arr.length)) {
						$('#'+arr[i+1]).focus();
						break;
					}
				}
			}
		});
		
		$('#btn_join').on('click', () => {
			let result = validate_pass_check();
			if(!result) {
				$('#regist_form').attr('action', '/registerOk').submit();
			}
		});
		
		function validate_pass_check() {
			let first_err_location = null;
			let error = false;
			
			if($('#id_ck').val() != 'true') {
				id_validation_ck($('#id').val());
				if(first_err_location == null) first_err_location = $('#id');
				error = true;
			}  
			
			if($('#pwd1_ck').val() != 'true') {
				pwd_validation_ck($('#pwd1').val());
				if(first_err_location == null) first_err_location = $('#pwd1');
				error = true;
			} else if($('#pwd2_ck').val() != 'true') {
				pwd_equla_ck();
				if(first_err_location == null) first_err_location = $('#pwd2');
				error = true;
			}  
			
			if($('#nickname_ck').val() != 'true') {
				nickname_validation_ck($('#nickname').val());
				if(first_err_location == null) first_err_location = $('#nickname');
				error = true;
			}  
			
			if($('#email_ck').val() != 'true') {
				email_validation_ck($('#email').val());
				if(first_err_location == null) first_err_location = $('#email');
				error = true;
			} else if($('#email_auth_ck').val() != 'true') {
				$('#emali_auth_check').text('이메일인증을 하셔야합니다.').css('color', 'red');	
				error = true;
			}

			if(first_err_location != null) first_err_location.focus();
			return error;
		}
	</script>
</body>
</html>