<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>PlayGround</title>
<%@include file="/WEB-INF/view/inc/asset.jsp" %>
<style>	

	.change_pwd_container {
		display: flex;
		justify-content: center;
		margin: 0.25rem 0.75rem;
		margin-top: 0.75rem;
	}

	.change_pwd_container > .change_pwd_form {
		flex: 1;
		max-width: 70%;
		border: 1px solid #aaa;
		border-radius: 5px;
		box-sizing: border-box;
		padding: 1rem 0.75rem;
	}
	
	.change_pwd_container > .change_pwd_form > h1,
	.change_pwd_container > .change_pwd_form > .change_pwd_input {
		box-sizing: border-box;
		padding: 0.5rem 1.25rem;
	}
	
	.change_pwd_container > .change_pwd_form > h1 {
		font-weight: normal;
		font-size: 1.5rem;
	}
	
	.change_pwd_container > .change_pwd_form > .change_pwd_input {
		margin-top: 1rem;
	}
	
	.change_pwd_form > .change_pwd_input > .cpi.row {
		display: flex;
		flex: 1;
		margin-top: 1.25rem;
	}
	
	.change_pwd_form > .change_pwd_input > .cpi.row > label {
		flex: 0 0 25%;
		margin-top: 0;
	}
	
	.change_pwd_form > .change_pwd_input > .cpi.row > div {
		flex: 0 0 75%;
	}
	
	.cpi.row > div > input {
		box-sizing: border-box;
		width: 100%;
		height: 2.5rem;
	}
	
	.cpi.row > div > div {
		margin-top: 0.25rem;
		font-size: 0.9rem;
		color: var(--color-hr-dark);
	}
	
	.change_pwd_form > .change_pwd_input > .change_pwd_btn {
		margin-top: 0.75rem;
		text-align: right;
	}
	
	.change_pwd_input > .change_pwd_btn > button {
		padding: 0.5rem 1.75rem;
	}
	
	@media screen and (max-width: 1000px) {
	.change_pwd_container > .change_pwd_form {
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
					<div class="change_pwd_container">
						<form action="javascript:;" method="POST" class="change_pwd_form">
							<h1>PlayGround 비밀번호 변경</h1>
							<div class="change_pwd_input">
								<div class="cpi row">
									<label>현재 비밀번호</label>
									<div>
										<input type="password" name="pwd">
										<div id="pwd_msg">현재 비밀번호를 입력해주세요.</div>
									</div>
								</div>
								<div class="cpi row">
									<label>변경할 비밀번호</label>
									<div>
										<input type="password" name="change_pwd" id="pwd1">
										<div id="pwd1_msg">변경할 비밀번호를 입력해주세요.</div>
									</div>
								</div>
								<div class="cpi row">
									<label>비밀번호 확인</label>
									<div>
										<input type="password" name="change_pwd_ck" id="pwd2">
										<div id="pwd2_msg">비밀번호 확인을 입력해주세요.</div>
									</div>
								</div>
								<div class="change_pwd_btn">
									<button id="change_pwd_submit_btn">변경</button>						
								</div>
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
		$('#change_pwd_submit_btn').on('click', () => {
			let result = pwd_v_check();
			if(result) {
				$('.change_pwd_form').attr('action', '/member/changePasswordOk').submit();
			}
		});
		
		function pwd_v_check() {
			let pwd1 = $('#pwd1').val();
			let pwd2 = $('#pwd2').val();

			pwd_validation_ck(pwd1);
			
			
			return false;
		}
		
		$('#pwd1, #pwd2').on('blur keyup', function(e) {
			if(e.keyCode !== 9 && e.which !== 9) {
				let pwd = $('#pwd');
				let pwd1 = $('#pwd1');
				let pwd2 = $('#pwd2');
				
				if($(e.target).is(pwd1)) {
					let val_ck = pwd_validation_ck(pwd1.val());
					pwd_validation_msg(pwd1, pwd1.val(), val_ck);
				} else if($(e.target).is(pwd2)) {
					let eq_ck = pwd_eq_ck(pwd1.val(), pwd2.val());
					pwd_eq_msg(pwd2, pwd2.val(), eq_ck);
				}
			}
		});
		
		function pwd_validation_ck(pwd) {
			const regex = /^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ!@#$%^&*()_+|<>?]{6,16}$/;
			return regex.test(pwd);			
		}
		
		function pwd_eq_ck(pwd1, pwd2) {				
			if(pwd1 == pwd2 && pwd_validation_ck(pwd2)) return true;
			else return false;
		}
		
		function pwd_validation_msg(target, val, flag) {
			let msg = $(target.selector + '_msg');
			if(flag) {
				msg.text('사용가능한 비밀번호입니다').css('color', 'green');
			} else {
				if(val.length <= 0) msg.text('필수 정보입니다.').css('color', 'red');
				else msg.text('비밀번호는 6~16자의 영문자, 숫자, 특수문자를 사용해야합니다.').css('color', 'red');
			}
		}
		
		function pwd_eq_msg(target, val, flag) {
			let msg = $(target.selector + '_msg');
			if(flag) {
				msg.text('비밀번호가 일치합니다.').css('color', 'green');
			} else {
				if(val.length <= 0) msg.text('필수 정보입니다.').css('color', 'red');
				else msg.text('비밀번호가 일치하지 않습니다.').css('color', 'red');
			}
		}
	</script>
</body>
</html>