<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>PlayGround</title>
<%@include file="/WEB-INF/view/inc/asset.jsp" %>
<style>	
	.recover_container {
		display: flex;
		justify-content: center;
		margin: 0.25rem 0.75rem;
		margin-top: 0.75rem;
	}
	
	.recover_container > .recover_form {
		flex: 1;
		max-width: 70%;
		border: 1px solid #aaa;
		border-radius: 5px;
		box-sizing: border-box;
		padding: 1rem 0.75rem;
	}
	
	.recover_container > .recover_form > .recover.row {
		box-sizing: border-box;
		padding: 0.5rem 1.25rem;
	}
	
	.recover_container > .recover_form > .recover.row.i {
		display: flex;
		flex-direction: column;
		flex: 1;
		margin-top: 1rem;
	}

	.recover_form > .recover.row.h > h1 {
		font-weight: normal;
		font-size: 1.5rem;
	}

	
	.recover_form > .recover_btn {
		margin-top: 0.75rem;
		text-align: right;
	}
	
	.recover_form > .recover_btn > button {
		padding: 0.5rem 1.75rem;
	}
	
	.recover_form > .recover.row.i > div {
		display: flex;
		flex: 1;
		margin-top: 0.75rem;
	}
	
	.recover_form > .recover.row.i > div:first-child {
		margin-top: 0;
	}
	
	.recover.row.i > div > label {
		box-sizing: border-box;
		flex: 0 0 25%;
		padding: 0.25rem 0;
	}
	
	
	.recover.row.i > div > div {
		flex: 0 0 75%;
	}
	
	.recover.row.i > div > div:first-child {
		margin-top: 0rem;
	}
	
	.recover.row.i > div > div > input {
		box-sizing: border-box;
		width: 100%;
		height: 2.5rem;
		padding: 0.25rem 0.5rem;
	}
	
	.recover_form > .recover.row.h > div,
	.recover.row.i > div > div > div {
		margin-top: 0.25rem;
		font-size: 0.9rem;
		color: var(--color-hr-dark);
	}
	
	@media screen and (max-width: 1000px) {
		.recover_container > .recover_form {
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
					<div class="recover_container">
						<form action="javascript:;" method="POST" class="recover_form">
							<input type="hidden" name="code" value="${code}">
							<div class="recover row h">
								<h1>비밀번호 변경</h1>
								<div>
									<c:if test="${active eq 'y'}">
										계정 비밀번호를 변경합니다.
									</c:if>
									<c:if test="${active eq 'n' }">
										비활성화된 계정입니다. 비밀번호 변경시 다시 활성화시킬 수 있습니다.
									</c:if>
								</div>
							</div>
							<div class="recover row i">
								<div>
									<label>변경비밀번호</label>
									<div>
										<input type="password" name="pwd1" id="pwd1">
										<div id="pwd1_msg">변경할 비밀번호를 입력해주세요.</div>
									</div>
								</div>
								<div>
									<label>변경 비밀번호 확인</label>
									<div>
										<input type="password" name="pwd2" id="pwd2">
										<div id="pwd2_msg">변경할 비밀번호를 입력해주세요,</div>
									</div>
								</div>
							</div>
							<div class="recover_btn">
								<button>변경하기</button>
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
		$(() => {
			if(${result ne true}) {
				alert('잘못된 요청입니다.');
				location.href = '/';	
			}
		});
		
		$('.recover_form').on('submit', function() {
			let pwd1 = $('#pwd1');
			let pwd2 = $('#pwd2');
			let pwd1_ck = pwd_validation_ck(pwd1.val());
			let pwd2_ck = pwd_eq_ck(pwd1.val(), pwd2.val());
			
			if(!pwd1_ck) {
				pwd_validation_msg(pwd1, pwd1.val(), pwd1_ck);
				pwd1.focus();
			} else if(!pwd2_ck) {
				pwd_eq_msg(pwd2, pwd2.val(), pwd2_ck);
				pwd2.focus();
			} else {
				member_recover_submit();
			}
		});
		
		function member_recover_submit() {
			$.ajax({
				url: '/member/recoverPwOk',
				type: 'POST',
				data: $('.recover_form').serialize(),
				dataType: 'JSON',
				success: function(result) {
					console.log(result);
					if(result) {
						if(confirm('로그인 페이지로 이동하시겠습니까?')) {
							location.href = '/login';
						} else {
							location.href = '/';
						}
					} else {
						alert('비밀번호 변경에 실패했습니다.');
					}
				},
				error: function(a, b, c) {
					console.log(a, b, c);
				}
			});
		}
		
		$('#pwd1, #pwd2').on('blur keyup', function(e) {			
			if(e.keyCode !== 9 && e.which !== 9) {
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