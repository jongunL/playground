<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>PlayGround</title>
<%@include file="/WEB-INF/view/inc/asset.jsp" %>
<style>	
	.member_find_account_container {
		display: flex;
		justify-content: center;
		margin: 0.25rem 0.75rem;
		margin-top: 0.75rem;
	}
	
	.member_find_account_container > .member_find_account_form {
		flex: 1;
		max-width: 70%;
		border: 1px solid #aaa;
		border-radius: 5px;
		box-sizing: border-box;
		padding: 1rem 0.75rem;
	}
	
	.member_find_account_form > .member_find_account_h,
	.member_find_account_form > .member_find_account_i,
	.member_find_account_form > .member_find_account_btn {
		box-sizing: border-box;
		padding: 0.5rem 1.25rem;
	}
	
	.member_find_account_form > .member_find_account_i {
		display: flex;
		flex: 1;
		margin-top: 1rem;
	}
	
	.member_find_account_form > .member_find_account_i > label {
		box-sizing: border-box;
		flex: 0 0 25%;
		padding: 0.25rem 0;
	}
	
	.member_find_account_form > .member_find_account_i > div {
		flex: 0 0 75%;
	}
	
	.member_find_account_i > div > input {
		box-sizing: border-box;
		width: 100%;
		height: 2.5rem;
		padding: 0.25rem 0.5rem;
	}
	
	.member_find_account_i > div > div,
	.member_find_account_form > .member_find_account_h > div {
		margin-top: 0.25rem;
		font-size: 0.9rem;
		color: var(--color-hr-dark);
	}
	
	.member_find_account_form > .member_find_account_h > h1 {
		font-weight: normal;
		font-size: 1.5rem;
	}
	
	.member_find_account_form > .member_find_account_h > div {
		font-size: 0.9rem;
		color: var(--color-hr-dark);
		margin-top: 0.25rem;
	}
	
	.member_find_account_form > .member_find_account_btn {
		margin-top: 0.75rem;
		text-align: right;
	}
	
	.member_find_account_form > .member_find_account_btn > button {
		padding: 0.5rem 1.75rem;
	}
	
	@media screen and (max-width: 1000px) {
		.member_find_account_container > .member_find_account_form {
			max-width: 100%;
		}
	}
	
</style>
</head>
<body>
	<div class="root_container">
		<%@include file="/WEB-INF/view/inc/header.jsp" %>
		<div class="content_wrapper">
			<article>
				<div class="board_article">
					<div class="member_find_account_container">
						<form action="javascript:;" method="POST" class="member_find_account_form">
							<div class="member_find_account_h">
								<h1>PlayGround 아이디/비밀번호 찾기</h1>
								<div>PlayGround 계정의 ID또는 비밀번호를 찾습니다.</div>
							</div>
							<div class="member_find_account_i">
								<label for="email">이메일</label>
								<div>
									<input type="text" name="email" id="email">
									<div id="email_msg">입력하신 이메일로 안내 메일을 보내드립니다.</div>
								</div>
							</div>
							<div class="member_find_account_btn">
								<button>아이디/비밀번호 찾기</button>
							</div>
						</form>
					</div>
				</div>
			</article>
			<%@include file="/WEB-INF/view/inc/util_btn.jsp" %>
		</div>
		<%@include file="/WEB-INF/view/inc/footer.jsp" %>
	</div>
	<script type="text/javascript" src="/asset/js/view_mode.js"></script>
	<script type="text/javascript">
		$('.member_find_account_form').on('submit', () => {
			let email = $('#email').val();
			let result = email_validation_ck(email);
			
			if(result) {
				send_mail(email);	
			}
		});
		
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
		
		function send_mail(email) {
			let submit_btn = $('.member_find_account_btn > button');
			update_btn_status(submit_btn);
			$.ajax({
				type: 'POST',
				url: '/member/findAccountOk',
				data: {
					email: email
				},
				dataType: 'json',
				success: function(result) {
					if(result == true) {
						location.href = '/member/recovery';
					} else {
						update_btn_status(submit_btn);
						$('#email').focus();
						$('#email_msg').text('해당 이메일로 가입된 아이디가 없습니다.').css('color', 'red');
					}
				},
				error: function(a, b, c) {
					alert('서버오류로 인해 메일전송에 실패하였습니다. 잠시후 다시 시도해주세요.');
					console.log(a, b, c);
				}
			});
		}
		
		function update_btn_status(button) {
			if(button.prop('disabled') == true) {
				button.prop('disabled', false);
				button.css('cursor', 'text');
			} else {
				button.prop('disabled', true);
				button.css('cursor', 'wait');
			}
		}
		
	</script>
</body>
</html>