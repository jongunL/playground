const toggle_btn = document.querySelector('.navbar_togleBtn');
const toggle_btn_icon = document.querySelector('.navbar_togleBtn > i');
const menu = document.querySelector('#header_menu');

//헤더부분
//모바일버전 nav-var
toggle_btn.addEventListener('click', ()=> {
	menu.classList.toggle('active');
	
	if(toggle_btn_icon.classList.contains('fa-bars')) {
		toggle_btn_icon.classList.remove('fa-bars');
		toggle_btn_icon.classList.add('fa-xmark');
	} else {
		toggle_btn_icon.classList.remove('fa-xmark');
		toggle_btn_icon.classList.add('fa-bars');
	}
});

//PC버전 nav-var
$(document).on('click', '#header_profile_img', (e) => {
	e.stopPropagation();
	if($('#header_sub_menu').hasClass('show')) {
		$('#header_sub_menu').removeClass('show');	
		$('#menu_toggle_btn > i').removeClass('fa-angle-up');
		$('#menu_toggle_btn > i').addClass('fa-angle-down');
	} else {
		$('#header_sub_menu').addClass('show');
		$('#menu_toggle_btn > i').removeClass('fa-angle-down');
		$('#menu_toggle_btn > i').addClass('fa-angle-up');
	}
});

$(document).on('click', (e) => {
	let layer_popup = $('#header_sub_menu');
	if(layer_popup.has(e.target).length === 0) {
		layer_popup.removeClass('show');
		$('#menu_toggle_btn > i').removeClass('fa-angle-up');
		$('#menu_toggle_btn > i').addClass('fa-angle-down');
	}
});


//로그아웃 버튼 클릭시
function logout() {	
	$.ajax({
		type: 'GET',
		url: '/logout',
		success: function() {
			location.reload();
		},
		error: function(a,b,c) {
			if(confirm('로그아웃에 실패했습니다. 다시 시도하시겠습니까?')) {
				logout();
			}
			console.log(a,b,c);
		}
	});
}