<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>PlayGround</title>
<%@include file="/WEB-INF/view/inc/asset.jsp" %>
<link rel="stylesheet" href="/asset/css/mainboard.css">
</head>
<body>
	<div class="root_container">
		<%@include file="/WEB-INF/view/inc/header.jsp" %>
		<div class="content_wrapper">
			<article>
				<div class="board_article">
					<div class="board_list">						
						<!-- board category는 최대 12개까지 출력하기 -->
						<c:set var="rows" value="${mainBoard.size()/2}" />
						<c:set var="columns" value="0" />
						<c:forEach var="row" begin="1" end="${rows+(1-(rows%1))%1}">
						<div class="board row">
							<c:forEach var="board" items="${mainBoard}" begin="${columns}" end="${columns+1}">
							<div class="board column">
								<h1>
									<a href="/board/lists?num=${board.key}">
										<span>${board.value.get(0).boardTitle}</span>
										<i class="fa-solid fa-angle-right"></i>
									</a>
								</h1>
								<ul class="contents_list">
									<c:forEach var="content" items="${board.value}">
									<c:if test="${content.boardSeq ne null}">
									<li class="content">
										<a href="/board/view?num=${board.key}&board=${content.boardSeq}">
											<span class="box line">
												<span class="box text">
													<span><c:out value="${content.boardSubject}"/></span>
													<span class="num">[<c:out value="${content.boardCommentCount}"/>]</span>
												</span>
												<span class="box info">
													<span class="time"><c:out value="${content.boardRegdate}"></c:out></span>
												</span>	
											</span>		
											<!-- box line -->					
										</a>
									</li>
									</c:if>
									<c:if test="${content.boardSeq eq null}">
										<li class="content empty">
											<a href="/board/lists?num=${board.key}">
												<span>등록된 게시물이 없습니다.</span>
												<span>첫 게시물 등록의 주인공이 되어보세요!</span>
											</a>
										</li>
									</c:if>
									</c:forEach>
									<!-- content -->
								</ul>
								<!-- contents_list -->
							</div>
							<!-- board column -->
							</c:forEach>
							<c:set var="columns" value="${columns+2}" />
						</div>
						</c:forEach>
						<!-- board row -->
					</div>
					<!-- board_list -->
				</div>
			</article>
			<%@include file="/WEB-INF/view/inc/util_btn.jsp" %>
		</div>
		<%@include file="/WEB-INF/view/inc/footer.jsp" %>
	</div>
	<script type="text/javascript" src="/asset/js/view_mode.js"></script>
</body>
</html>