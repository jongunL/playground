<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>PlayGround</title>
<%@include file="/WEB-INF/view/inc/asset.jsp" %>
<link rel="stylesheet" href="/asset/css/board_title.css">
<link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-lite.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-lite.min.js"></script>
<style>	
	.board_article > form {
		padding: 0 2rem;
	}

	.board_submit > h1 {
		font-size: 1.2rem;
		margin: 3.2rem 0 0.3rem;
	}
	
	.board_submit > select,
	.board_submit > input {
		box-sizing: border-box;
		border-radius: 5px;
		width: 100%;
		height: 41px;
		padding: 8px 40px 8px 12px;
		border: 1px solid #ccc;
	}
	.board_submit > select:focus {
		border: 1px solid black;
	}
	
	.board_submit.contents > .contents_box > .content_option,
	.board_submit.contents > .contents_box > .content {
		border: 1px solid #ccc;
		border-radius: 5px;
	}
	
	.board_submit_btns {
		display: flex;
		justify-content: flex-end;
		margin-top: 1.1rem;
	}
	
	.board_submit_btns > button {
		font-size: 1rem;
		margin-right: 0.5rem;
		padding: 0.3rem;
		width: 88px;
		height: 42px;
		background-color: white;
		border: 1px solid black;
		border-radius: 3px;
		color: white;
		font-weight: bold;
	}
	
	.board_submit_btn {
		background-color: #2f8ffd!important;
	}
	
	.board_submit_btn:hover {
		background-color: #0d6efd!important;
	}
	
	.cancel_btn {
		background-color: #fd4647!important;
	}
	
	.cancel_btn:hover {
		background-color: #dc3545!important;
	}
</style>
</head>
<body>
	<div class="root_container">
		<%@include file="/WEB-INF/view/inc/header.jsp" %>
		<div class="content_wrapper">
			<article>
				<div class="board_article">
					<jsp:include page="/WEB-INF/view/inc/board/board_title.jsp">
						<jsp:param value="${boardTitle}" name="boardTitle"/>
						<jsp:param value="${subscribe}" name="subscribe"/>
					</jsp:include>
					<form action="/board/submit" method="post" id="board_form">
						<input type="hidden" class="files_data" name="filesData">
						<input type="hidden" name="category" value="${boardTitle.boardTitleSeq}">
						<input type="hidden" name="board" value="${board}">
						<input type="hidden" name="type" value="${type}">
						<div class="board_submit_container">
							<div class="board_submit_article">
								<div class="board_submit sub_category">
									<h1>주제</h1>
									<select name="board_sub_title">
										<c:forEach var="boardSubTitle" items="${boardSubTitleList}">
										<option value="${boardSubTitle.boardSubTitleSeq}">${boardSubTitle.boardSubTitle}</option>
										</c:forEach>
									</select>
								</div>
								<div class="board_submit subject">
									<h1>제목</h1>
									<input type="text" class="board_subject" name="board_subject" placeholder="제목을 입력해주세요.">
								</div>
								<div class="board_submit contents">
									<h1>본문</h1>
									<div class="contents_box">
										<textarea id="summernote" name="content"></textarea>
									</div>
								</div>
							</div>
							<div class="board_submit_btns">
								<button class="board_submit_btn" type="button">등록</button>
								<button class="cancel_btn" type="button">취소</button>
							</div>
						</div>
					</form>
				</div>
			</article>
			<%@include file="/WEB-INF/view/inc/util_btn.jsp" %>
		</div>
		<%@include file="/WEB-INF/view/inc/footer.jsp" %>
	</div>
	<script type="text/javascript" src="/asset/js/view_mode.js"></script>
	<script type="text/javascript" src="/asset/js/board/board_title.js"></script>
	<script type="text/javascript">	
		$(() => {
			var h1Button = function (context) {
				  var ui = $.summernote.ui;
				  // create button
				  var button = ui.button({
				    contents: 'h1',
				    click: function () {
				      context.invoke('editor.formatBlock', 'h1');
				    }
				  });
			  return button.render();   // return button as jquery object
			}
	
			var h2Button = function (context) {
				  var ui = $.summernote.ui;
				  // create button
				  var button = ui.button({
				    contents: 'h2',
				    click: function () {
				      context.invoke('editor.formatBlock', 'h2');
				    }
				  });
			  return button.render();   // return button as jquery object
			}
			
			var h3Button = function (context) {
				  var ui = $.summernote.ui;
				  // create button
				  var button = ui.button({
				    contents: 'h3',
				    click: function () {
				      context.invoke('editor.formatBlock', 'h3');
				    }
				  });
			  return button.render();   // return button as jquery object
			}
			
			//기본설정
			$('#summernote').summernote({
			    placeholder: '내용을 입력하세요. 내용은 최대 500글자까지 입력가능합니다.',
			    minHeight: 330,
			    maxHeight: null,
			    tooltip: false,
			    tabsize: 2,
			    tabDisable: true,
			    codeviewFilter: true,
			    codeviewIframeFilter: true,
			    disableDragAndDrop: true,
			    lang: 'ko-KR',
			    toolbar: [
			    	['mybutton', ['h1', 'h2', 'h3']],
			        ['font', ['bold', 'italic', 'underline', 'strikethrough']],
			        ['para', ['ul', 'ol', 'paragraph']],
			        ['table', ['table']],
			        ['insert', ['link', 'picture', 'video']],
			        ['view', ['codeview', 'undo', 'redo']]
			    ],
			    buttons: {
			    	h1: h1Button,
			    	h2: h2Button,
			    	h3: h3Button
			    },
			    //이미지 인코딩 데이터데신 경로로 변경하기 위한 콜백함수
			    callbacks: {
			    	onImageUpload : function(files) {
			    		sendFile(files, this);
			    	}
			    }
			    
			});
			//콜백함수
			//이미지 정보 전송하기
			const filesData = [];
			
			function sendFile(files, editor) {
				let data = new FormData();
				Array.from(files).forEach((file) => {
					data.append('files', file);
				});
				$.ajax({
					data: data,
					type: 'POST',
					url: '/board/write/changeSummerNoteImageFile',
					dataType: 'json',
					cache: false,
					contentType: false,
					processData: false,
					success : function(data) {		
						data.forEach((result) => {
							const fileData = {
									path: result.path,
									saveFileName: result.saveFileName,
									originalFileName: result.originalFileName,
							};
							filesData.push(fileData);
							$(editor).summernote('insertImage', result.path + result.saveFileName);
						});				
					},
					error : function(a, b, c) {
						console.log(a, b, c);
						alert('이미지 업로드에 실패했습니다. 다시 시도해주세요');
					},
				});
			}
			
			//summernote 리사이즈바 삭제
			$('.note-resizebar').remove();
	
			//수정하기일 경우 데이터 추가
			if(${type eq 'edit'}) {
				$('select[name=board_sub_title]').val('${subCategory}');
				$('.board_subject').val('${subject}');
				$('#summernote').summernote('code', '${boardContent}');
			}
			
			//전송버튼 클릭시
			$('.board_submit_btn').on('click', function() {
				if(submit_check()) {
					$('.files_data').val(JSON.stringify(filesData));
					$('#board_form').submit();
				}
			});
			
			//게시판 작성양식 체크
			function submit_check() {
				let subject_length = $('.board_subject').val().length;
				let content_length = $(".note-editable").text().length;
				
				if(subject_length == 0) {
					alert('제목을 입력해주세요.');
					$('.board_subject').focus();
					return false;
				} else if(subject_length < 2) {
					alert('제목은 최소 2글자 이상이어야 합니다.');
					$('.board_subject').focus();
					return false;
				} else if(content_length < 5) {
					alert('내용은 적어도 5글자를 입력하셔야합니다.');
					$(".note-editable").focus();
					return false;
				} else if(content_length > 500) {
					alert('내용은 최대 500글자 입력가능합니다.');
					$(".note-editable").focus();
					return false;
				} else {
					return true;
				}
			}
			
			//취소버튼 클릭시 이동할 페이지
			$('.cancel_btn').on('click', function() {
				let confirm_msg = '글 작성을 취소하시겠습니까?';
				let cancel_location ='/board/lists?num=${boardTitle.boardTitleSeq}';
				
				if(${type eq 'edit'}) {
					confirm_msg = '글 수정을 취소하시겠습니까?';
					cancel_location = '/board/view?num=${boardTitle.boardTitleSeq}&board=${board}';
				}
				
				if(confirm(confirm_msg)) {
					history.back();
					location.href=cancel_location;
				}
			});
		});
	</script>
</body>
</html>





















