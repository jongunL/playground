//window.onload()
$(()=>{
	if(getCookie('view_mode') == 'dark') {
		$('.root_container').addClass('dark');
		$('#header_logo_img').attr('src', '/asset/images/darkmode_logo.png');
		$('.mode_btn').html('<span>라이트 모드로 보기</span><i class="fa-solid fa-sun"></i>');
	} else {
		$('#header_logo_img').attr('src', '/asset/images/logo.png');
		$('.mode_btn').html('<span>다크 모드로 보기</span><i class="fa-solid fa-moon"></i>');
	}
});

$(".mode_btn").on("click", function() {		

	$('.root_container').toggleClass('dark');
	
	if($('.root_container').hasClass('dark') == true ) {
		setCookie('view_mode', 'dark', 1);	
		$('#header_logo_img').attr('src', '/asset/images/darkmode_logo.png');
		$('.mode_btn').children('span').text('라이트 모드로 보기');
		$('.mode_btn').children('i').removeClass('fa-moon');
		$('.mode_btn').children('i').addClass('fa-sun');
	} else {
		setCookie('view_mode', 'light', 1);								
		$('#header_logo_img').attr('src', '/asset/images/logo.png');
		$('.mode_btn').children('span').text('다크 모드로 보기');
		$('.mode_btn').children('i').removeClass('fa-sun');
		$('.mode_btn').children('i').addClass('fa-moon');
	}
	
});		