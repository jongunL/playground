<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>PlayGround</title>
<%@include file="/WEB-INF/view/inc/asset.jsp" %>
<style>	
	.board_channel_search {
		display: flex;
		justify-content: center;
		align-items: center;
		padding: 1rem 0.4rem;
		border-bottom: 1px solid #ccc;
	}
	.board_channel_search > input {
		padding: 0 0.25rem;
		height: 2rem;
		width: 100%;
	}
	
	.board_channel_list {
		margin-top: 1rem;
		display: flex;
		flex-wrap: wrap;
	}
	
	.board_channel {
		box-sizing: border-box;
		padding: 0.4rem 0.4rem;
		width: 33.3%;
	}
	
	.board_channel > .board_channel_info {
		display: flex;
		border: 1px solid #ccc;
		padding: 0.4rem 0.2rem;
	}
	
	.board_channel > .board_channel_info > div {
		overflow: hidden;
	}
	
	
	.board_channel_info > #board_channel_img {
		display: flex;
		justify-content: center;
		align-items: center;
		margin-right: 0.1rem;
	}
	
	.board_channel_info > #board_channel_img > img {
		width: 3rem;
		height: 3rem;
		border-radius: 50%;
	}
	.board_channel_info > .board_channel_description > h1 > a,
	.board_channel_info > .board_channel_description > div > div {
		white-space: nowrap;
		overflow: hidden;
		text-overflow: ellipsis;
	}
	
	.board_channel_info > .board_channel_description > h1 > a,
	.board_channel_info > .board_channel_description > div > div {
		white-space: nowrap;
		overflow: hidden;
		text-overflow: ellipsis;
	}
	
	.board_channel_info > .board_channel_description > h1 > a {
		display: block;
		font-size: 1.2rem;
	}
	
	.board_channel_info > .board_channel_description > div > div {
		font-size: 0.9rem;
	}
	
	.board_article > .board_channel_search {
		display: flex;
		flex-direction: column;
	}
	
	.board_article > .board_channel_search > h1 {
		margin-bottom: 1rem;
	}
	
</style>
</head>
<body>
	<div class="root_container">
		<%@include file="/WEB-INF/view/inc/header.jsp" %>
		<div class="content_wrapper">
			<article>
				<div class="board_article">
					<div class="board_channel_search">
						<h1 class="board_channel_search_type"></h1>
						<input type="text" placeholder="게시판 찾기..." class="search_word">
					</div>
					<div class="board_channel_list">
					</div>
				</div>
			</article>
			<%@include file="/WEB-INF/view/inc/util_btn.jsp" %>
		</div>
		<%@include file="/WEB-INF/view/inc/footer.jsp" %>
	</div>
	<script type="text/javascript" src="/asset/js/view_mode.js"></script>
	<script type="text/javascript">
		let list = [];
		$(() => {
			
			<c:forEach var="list" items="${list}">
			list.push(createObject('${list.boardTitle}', '${list.boardTitleSeq}', '${list.boardTitleImg}', '${list.boardTitleDescription}', '${list.boardTitleSubscriberCount}'));
			</c:forEach>
			
			getList(list);
			
			
			let url = new URL(window.location.href);
			let search_type = $('.board_channel_search_type');
			
			if(url.pathname == '/board/search') {
				search_type.text('게시판 채널');
			} else if (url.pathname == '/board/subscribed') {
				search_type.text('구독 채널');
			}
		});
		
		
		function createObject(boardTitle, boardTitleSeq, boardTitleImg, boardTitleDescription, boardTitleSubscriberCount) {
		    var obj = {};
		    obj.boardTitle = boardTitle;
		    obj.boardTitleSeq = boardTitleSeq;
		    obj.boardTitleImg = boardTitleImg;
		    obj.boardTitleDescription = boardTitleDescription;
		    obj.boardTitleSubscriberCount = boardTitleSubscriberCount;
		    return obj;
		}
		
		function getList(input_list) {
			$('.board_channel_list').empty();
			input_list.forEach((e) => {
				createElement(e);
			});
		}
		
		function createElement(element) {
			html = '';
			html += '<div class="board_channel">';
			html +=	'	<div class="board_channel_info">';
			html += '		<a href="/board/lists?num='+element.boardTitleSeq+'" id="board_channel_img">';
			html += '			<img src="/asset/images/board_img/'+element.boardTitleImg+'">'
			html += '		</a>';
			html += '		<div class="board_channel_description">';
			html += '			<h1>';
			html += '				<a href="/board/lists?num='+element.boardTitleSeq+'">'+element.boardTitle+'</a>';
			html += '			</h1>';
			html += '			<div>';
			html += '				<div>구독자수 : '+element.boardTitleSubscriberCount+'</div>';
			html += '				<div>'+element.boardTitleDescription+'</div>';
			html += '			</div>';
			html += '		</div>';
			html += '	</div>';
			html += '</div>';
			$('.board_channel_list').append(html);
		}

		$('.search_word').on('keyup', function(e) {
			let input_word = $(e.target).val();
						
			if(input_word != '') {
				const filtered_list = list.filter(e => e.boardTitle.includes(input_word) || e.boardTitleDescription.includes(input_word));
				getList(filtered_list);
				
			} else if(input_word == '') {
				getList(list);
			}
			
			
		});
		
		
		
	</script>
</body>
</html>













