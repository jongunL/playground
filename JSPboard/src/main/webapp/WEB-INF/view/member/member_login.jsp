<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>PlayGround</title>
<%@include file="/WEB-INF/view/inc/asset.jsp" %>
<link rel="stylesheet" href="/asset/css/login.css">
</head>
<body>
	<div class="root_container">
		<%@include file="/WEB-INF/view/inc/header.jsp" %>
		<div class="content_wrapper">
			<article>
				<div class="board_article">
					<!-- 로그인 컨테이너 -->
					<div class="login_container">
						<!-- 로그인폼 양식 -->
						<form action="/loginOk" method="POST" id="login_form">
							<div class="login_group">
								<!-- 전송양식 -->
								<h1><a href="/">PlayGround!</a></h1>
								<div class="login_row">
									<!-- id -->
									<span class="ps_box id"><input type="text" placeholder="아이디" name="id" id="id"></span>
									<!-- pwd -->
									<span class="ps_box pwd"><input type="password" placeholder="비밀번호" name="pwd" id="pwd"></span>
									<!-- 로그인 옵션 -->
									<span class="ps_box option">
										<label for="remember_id">아이디 기억하기</label>
										<input type="checkbox" name="login_status_remember" id="remember_id" value="true">
										<label for="auto_login">로그인 상태 유지</label>
										<input type="checkbox" name="login_status_auto" id="auto_login" value="true">
									</span>
								</div>
								<!-- 로그인 상태메시지 -->
								<div id="login_status"></div>
								<!-- 전송버튼 -->
								<div class="btn_area">
									<input id="login_btn" class="btn btn_primary" type="button" value="로그인">
								</div>
							</div>
							<!-- 부가 서비스 양식 -->
							<div class="link_area">
								<div><a href="/findAccount">아이디/비밀번호 찾기</a></div>
								<div><a href="/register">회원가입 하기</a></div>
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
			if(!(${id eq null} || ${id eq ''})) {
				//찾아온 아이디가 있는경우
				$('#id').val('${id}');
				$('#pwd').focus();
				$('#remember_id').attr('checked', true);
			} else {
				//찾아온 아이디가 없는경우
				$('#id').focus();				
			}
		});

		//엔터로 로그인 시도하는 경우
		$('#id, #pwd').on('keyup', (event) => {
			if(event.keyCode == 13) {
				input_check();
			}
		});
		//버튼을 클릭해 로그인 시도하는 경우
		$('#login_btn').on('click', () => {
			input_check();
		});
		
		function input_check() {
			//아이디 입력이 안되어 있는 경우
			if($('#id').val().length < 1) {
				$('#login_status').text('아이디를 입력해 주세요.');
				$('#id').focus();
				return;
			//비밀번호 입력이 안되어 있는 경우
			} else if($('#pwd').val().length < 1) {
				$('#login_status').text('비밀번호를 입력해 주세요.');
				$('#pwd').focus();
				return;
			//모두 입력되어 있는 경우
			} else {
				console.log('실행됨');
				//DB에서 id, pwd 체크
				login_ck();
			}
		}
		
		//DB에 접근
		function login_ck() {
			$.ajax({
				type: 'POST',
				url: '/loginCheck',
				data: 'id='+$('#id').val()+'&pwd='+$('#pwd').val(),
				dataType: 'json',
				success: function(result) {
					if(result.success) {
						//로그인에 성공했을 경우 폼데이터 전송
						$('#login_form').attr('action', '/loginOk').submit();
					} else {
						//로그인에 실패했을 경우 메시지 표시
						$('#login_status').html('아이디 또는 비밀번호를 잘못 입력했습니다.</br> 입력하신 내용을 다시 확인해주세요.');
						$('#pwd').val('').focus();
					}
				},
				error: function(a, b, c) {
					console.log(a, b, c);
				}
			});
		}
			
	</script>
</body>
</html>