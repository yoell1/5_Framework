/********* 회원가입 페이지 *********/

// 프로필 이미지 미리보기(프리뷰)
const profileImage = document.querySelector("#profile-image");

profileImage.addEventListener('change', function(e) {
	// 업로드한 파일 정보를 가지고 옴 (첫번째 요소)
	const file = e.target.files[0];
	if (!file) {
		return;
	}
	
	// FileReader : 아직 서버에 업로드하지 않은 파일을 브라우저 메모리에 올리기위해
	//				base64 라는 문자열로 만들어주는 객체
	const reader = new FileReader();
	reader.onload = function(e) {
		
		// 프로필 미리보기 영역에 변환된 이미지 파일을 표시
		const profilePreview = document.querySelector("#profile-preview");
		profilePreview.src = e.target.result;
		profilePreview.style.display = "block";
		
		// 이미지가 없을 경우 표시되는 영역은 display:none 변경
		document.querySelector("#profile-preview-placeholder").style.display = "none";
	}
	
	reader.readAsDataURL(file);		// 업로드한 파일을 base64방식으로 변경
	
});



// 비밀번호 일치 여부 확인
const memberPwd = document.querySelector("#member-pwd");				// 비밀번호 입력창
const memberPwdConfirm = document.querySelector("#member-pwd-confirm");	// 비밀번호 확인 입력창

let checkPwd = false;	// 비밀번호 일치 여부

function validatePwdConfirm() {
	const confirmResult = document.querySelector("#check-pwd-result");
	
	// 비밀번호 확인 입력창이 비어있을 경우 검사 x
	if (!memberPwdConfirm.value.trim()) {
		confirmResult.textContent = "";
		checkPwd = false;
		return;
	}
	
	checkPwd = memberPwd.value === memberPwdConfirm.value;
	
	confirmResult.textContent = checkPwd ? "비밀번호가 일치합니다." : "비밀번호가 일치하지 않습니다.";
	confirmResult.className = checkPwd ? "form-tip form-tip-ok" : "form-tip form-tip-error";
}

memberPwd.addEventListener('input', validatePwdConfirm);
memberPwdConfirm.addEventListener('input', validatePwdConfirm);


let checkId = null;		// 아이디 중복체크 값
const checkIdResult = document.querySelector("#check-id-result");
const memberIdInput = document.querySelector("#member-id");
memberIdInput.addEventListener("input", function() {
	checkIdResult.textContent = "";
	checkId = null;
});

// 아이디 [중복확인] 버튼의 클릭 이벤트 리스너 추가 (TODO: alert)
const checkIdBtn = document.querySelector("#check-id-btn");
checkIdBtn.addEventListener("click", async function() {
	const memberId = memberIdInput.value.trim();
	// 아이디 값이 입력되지 않았을 경우, 요청 x
	if (memberId.length === 0) {
		checkIdResult.textContent = "아이디를 입력해주세요.";
		checkIdResult.className = "form-tip form-tip-error";
		checkId = null;
		return;
	}
	
	// 입력된 아이디값이 중복되는 지 서버로 요청!
	/*
		* fetch API
		  : 브라우저에서 서버로 요청을 보내고 응답을 받을 수 있게 해주는 자바스크립트 내장 함수
		    form 태그의 submit과 달리 "화면 새로고침 없이(비동기적으로)", 
			백엔드 서버와 데이터를 주고 받을 수 있음. 이러한 통신 방식을 AJAX라고 함.
			
		fetch(URL, settings)
		- URL : 요청을 보낼 주소
		- settings : 설정 객체 (요청 방식, 헤더, 데이터 등)
		  - method: 요청 방식
		  - headers: 헤더 설정 
		  
		encodeURIComponent() : 전달하는 파라미터에 &, =와 같은 특수문자가 있을 경우 URL 형식이 깨지는 것을 방지(인코딩)
		"X-Requested-With": "XMLHttpRequest" 
		=> 이 요청이 브라우저 주소창에서 이동한 것이 아니라, 자바스크립트(AJAX)를 통해 보낸 것임을 서버에 알려주는 설정(관례)
	*/
	try {
		const response = await fetch("/member/checkId?memberId=" + encodeURIComponent(memberId), {
			method: "GET",
			headers: { "X-Requested-With": "XMLHttpRequest" }  
		});
		
		// response.json() : json 응답을 자바스크립트 객체로 변경
		const result = await response.json();
		
		// console.log(result);
		checkIdResult.textContent = result.message;
		checkIdResult.className = result.data ? "form-tip form-tip-error" : "form-tip form-tip-ok";
		
		checkId = result.data ? null : memberId;
	} catch (error) {
		console.log(error);
		
		checkIdResult.textContent = "중복 확인 중 오류가 발생했습니다.";
		checkIdResult.className = "form-tip form-tip-error";
		
		checkId = null;
	}
});


// 회원가입 폼 제출 => 비밀번호가 일치했을 때, 사용 가능한 아이디인 경우 제출하도록 처리
const joinForm = document.querySelector("#join-form");
joinForm.addEventListener("submit", function(e) {
	
	if (!checkId) {
		e.preventDefault();
		alert("아이디 중복확인을 진행해주세요.");
		return;
	}
	
	if (!checkPwd) {
		e.preventDefault();		// 기존 폼 제출 동작을 막기!
		alert("비밀번호가 일치하지 않습니다.");
		return;
	}
	
});












