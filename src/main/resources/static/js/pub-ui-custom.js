/* pub-js */

/* 스크롤 최상단 이동 버튼 */
function scrollToTop() {
	window.scrollTo({ top: 0, behavior: 'smooth' });
}

/* popbanner-close : pop-info x 버튼 이벤트 */
function popCloseEvent() {
	document.querySelector('.banner-img').style.display = 'none';
	document.querySelector('.xi-close').parentElement.style.display = 'none';
}

// 쿠키를 설정 함수 : setcookie_expires("쿠키명", "쿠키 값", 날짜일 수);
function setcookie_expires(name, value, expiredays) {
	let todayDate = new Date();
	todayDate.setDate(todayDate.getDate() + expiredays); 
	document.cookie = name + '=' + encodeURIComponent(value) + '; path=/; expires=' + todayDate.toUTCString() + ';';
}

// 쿠키 값을 가져오는 함수
function getCookie(name) {
	let cookieArr = document.cookie.split(';');
	for (let i = 0; i < cookieArr.length; i++) {
			let cookiePair = cookieArr[i].split('=');
			if (cookiePair[0].trim() === name) {
					return decodeURIComponent(cookiePair[1]);
			}
	}
	return null;
}

// popbanner-close : pop-info 하루만 안보기
function dontShowAgain() {
	setcookie_expires("myCookie", "true", 1); 
	alert("광고를 하루 동안 보지 않습니다.");
	document.querySelector('.banner-img').style.display = 'none';
	document.querySelector('.pop-info').style.display = 'none';
}

// 로그인 쪽 모달 -- 나중 처리 
/* 
const loginView = (e) => {
	alert(e);
	const modalBackground = document.querySelector(".modal-background");
	if (e == 'open') {
		modalBackground.style.display = 'block';
		alert(e);
	}
	if (e == 'close') {
		modalBackground.style.display = 'none';
		alert(e);
	}
}
const loginChk = () => {};
*/

/* 공통 - 페이지 네이션 : on class 이펙트 */
function pagenation() {
	const paginateContainers = document.querySelectorAll('.paginate');
	paginateContainers.forEach(container => {
		// 각 페이지네이션 컨테이너에서 페이지 번호 링크들만 선택
		const pageLinks = container.querySelectorAll('a[href="#"]');

		pageLinks.forEach(link => {
			link.addEventListener('click', function(e) {
				// 'on' 클래스를 제거하는 부분 (현재 페이지네이션에서만 처리)
				pageLinks.forEach(link => link.classList.remove('on'));

				// 클릭한 페이지 링크에 'on' 클래스를 추가
				this.classList.add('on');
			});
		});
	});
}

/* 문제 /problemCollect : 드롭다운 -- 느린 것 수정 해야됨 */
function listDropdown() {
	let itemli = document.querySelectorAll('.toggle_btn');
	itemli.forEach(function (item) {
		item.addEventListener('click', function () {
			// 현재 클릭한 요소에 'on' 클래스 토글
			this.classList.toggle('on');
	
			// 다른 요소들에서 'on' 클래스 제거
			itemli.forEach(function (otherItem) {
				if (otherItem !== item) {
					otherItem.classList.remove('on');
				}
			});
		});
	});
}

/* 랭크 /rank : 탭 */
function toggleTab() {
	const btnMenu = document.querySelectorAll('.rank_tab_group li');
	const menuContent = document.querySelectorAll('.content-rank-main .content-rank-toggle-menu-item')
	let menuCount = '';
	
	for(let i = 0; i < btnMenu.length; i++){
		btnMenu[i].querySelector('.btn_tab_menu').addEventListener('click', function(e){
				e.preventDefault();
				for(let j = 0; j < btnMenu.length; j++){
					// 나머지 버튼 클래스 제거
					btnMenu[j].classList.remove('on');
					// 나머지 컨텐츠 display:none 처리
					menuContent[j].style.display = 'none';
				}
				// 버튼 관련 이벤트
				this.parentNode.classList.add('on');
				// 버튼 클릭시 컨텐츠 전환
				menuCount = this.getAttribute('href');
				document.querySelector(menuCount).style.display = 'block';
		});
	}
}

/* 채팅 사이드바 /chatView	*/
function chatViewSideBar() {
	const sideBtn = document.querySelectorAll('.chat-nav-side-btn');
	const sideArea = document.querySelector('.chat-nav-side');
	
	sideBtn.forEach(function(btn) {
		btn.addEventListener('click', function() {
			if (sideArea.style.display === 'none') {
				sideArea.style.display = 'block';
			} else {
				sideArea.style.display = 'none'; 
			}
		});
	});
	
}

// 벨 변경 --> 함수랑 같이 묶어서 
document.addEventListener('click', function(event) {
	if (event.target.matches('.xi-bell-o') || event.target.matches('.xi-bell')) {
		event.target.classList.toggle("xi-bell-o");
		event.target.classList.toggle("xi-bell");
	}
});

//메인페이지 공지알림
document.addEventListener("DOMContentLoaded", function () {
	const noticePop = document.querySelector(".notice-pop");
	if (noticePop) {
		noticePop.addEventListener("click", function () {
			if (document.querySelector(".notice-pop-menu").style.display === "none" || document.querySelector(".notice-pop-menu").style.display === "") {
				document.querySelector(".notice-pop-menu").style.display = "block";
			} else if(document.querySelector(".notice-pop-menu").style.display === "block") {
				document.querySelector(".notice-pop-menu").style.display = "none";
			}
		});
	}
});

//메인페이지 분야별 문제 태그
document.addEventListener("DOMContentLoaded", () => {
	const tags = document.querySelectorAll(".card-small-red-tag");
	const colors = ["#FF5733", "#33FF57", "#3357FF", "#F1C40F", "#8E44AD", "#1ABC9C"];
	
	tags.forEach(tag => {
		const text = tag.textContent.trim();

		if (text === "JAVA") {
			tag.style.backgroundColor = "#90EE90";
			tag.style.color = "#000000";
		} else if (text === "javascript") {
			tag.style.backgroundColor = "#F1C40F";
			tag.style.color = "#000000";
		} else {
		const randomColor = colors[Math.floor(Math.random() * colors.length)];
			tag.style.backgroundColor = randomColor;
			tag.style.color = "#000000";
		}
	});
});	