//구독버튼클릭
/** boardtitleSeq를 넘겨주면 구독을 하는 메서드*/
function subscribe(boardTitleSeq) {
	$.ajax({
		type: 'GET',
		url: '/boardSubscribe',
		data: 'category='+boardTitleSeq,
		dataType: 'json',
		success: function(result) {
			console.log(result.success);
			if(result.success) {
				location.reload();
			} else {
				alert('로그인이 필요한 서비스입니다.');
			}
		},
		error: function(a,b,c) {
			console.log(a,b,c);
		}
	});
}

$('.subscribe_btn').on('mouseover', () => {
	if($('.subscribe_info').hasClass('subscribe')) {
		$('.subscribe_info').text('구독 취소');
		$('#heart > i').removeClass('fa-heart');
		$('#heart > i').addClass('fa-heart-crack');
	}
});

$('.subscribe_btn').on('mouseout', () => {
	if($('.subscribe_info').hasClass('subscribe')) {
		$('.subscribe_info').text('구독중');
		$('#heart > i').removeClass('fa-heart-crack');
		$('#heart > i').addClass('fa-heart');
	}
});