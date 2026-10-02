// 이미지 미리보기
const imagesInput = document.querySelector("#images");
const imagePreviewList = document.querySelector("#image-preview-list");

if (imagesInput) {
	imagesInput.addEventListener("change", function(e) {
		// preview 영역 초기화
		imagePreviewList.textContent = "";
		
		// 파일 객체 -> 이벤트 객체
		let images = e.target.files; // 배열이 아니라 FileList 객체임.
		
		images = Array.from(images); // forEach 사용을 위해 배열로 변환.
		images.forEach(function(file, index) {
			
			const reader = new FileReader();
			reader.onload = function(event) {
				
				const li = document.createElement("li");
				const img = document.createElement("img");
				
				img.src = event.target.result;
				img.alt = file.name;
				
				li.appendChild(img);
				imagePreviewList.appendChild(li);
				
			}
			
			reader.readAsDataURL(file);
			
		});
	});

}
// 댓글 기능
const commentForm = document.querySelector("#comment-form");
const boardKey = document.querySelector("#board-key");

if (commentForm) {
	commentForm.addEventListener("submit", async function(ev) {
		ev.preventDefault();	// 기본 이벤트를 막고 직접 처리
		
		const contentInput = commentForm.querySelector("textarea");
		const content = contentInput.value.trim();
		
		if (!content) {
			alert("댓글 내용을 입력해주세요.");
			return;
		}
		
		const boardId = boardKey.value;
		
		try {
			const response = await fetch(`/api/board/${boardId}/comment`, {
				method: "POST", 
				headers: {
					"X-Requested-With": "XMLHttpRequest",   // 서버로 비동기 요청임을 전달
					"Content-Type": "application/json"		// 서버로 전달되는 데이터가 json 임을 알림
				}, 
				body: JSON.stringify({content}) // {content: content}
			});
			
			const result = await response.json();
			
			if (!response.ok || !result.success) {
				alert(result.message || "댓글 등록에 실패했습니다.");
				return;
			}
			
			// TODO: 응답 결과를 화면에 표시
			// alert("댓글 작성 성공");
			appendComment(result.data);
			
			contentInput.value = "";
			
		} catch (error) {
			alert("댓글 등록 중 오류가 발생했습니다.");
		}
	});
}

// 댓글 추가 시 화면에 표시
const commentList = document.querySelector("#comment-list");

function appendComment(comment) {
	
	// 템플릿 영역 접근
	const commentTemplate = document.querySelector("#comment-template");
	const cloneComment = commentTemplate.content.cloneNode(true);
	
	const li = cloneComment.querySelector("li");
	li.id = `comment-${comment.commentId}`;
	
	cloneComment.querySelector(".comment-list_writer").textContent = comment.writerNickname;
	cloneComment.querySelector(".comment-list_content").textContent = comment.content;
	cloneComment.querySelector(".comment-list_date").textContent = comment.createAtStr;
	
	cloneComment.querySelector(".comment-delete-btn").dataset.commentId = comment.commentId;
	// => dataset 을 사용하면 data-* 속성으로 추가될 것임.
	
	commentList.appendChild(cloneComment);
}


// 댓글 영역에 표시되는 댓글 삭제 기능
if (commentList) {
	
	commentList.addEventListener("click", async function(e) {
		/*
			if (!e.target.classList.contains("comment-delete-btn")) return;
			
			const delBtn = e.target;
		*/
		
		// closest(선택자) : 클릭한 요소로부터 부모 방향으로 선택자에 해당하는 요소를 찾아줌
		const delBtn = e.target.closest(".comment-delete-btn");
		if (!delBtn) return;	// 삭제 버튼이 아니면 메소드 종료
		
		if (!confirm("댓글을 삭제하시겠습니까?")) return;
		
		const commentId = delBtn.dataset.commentId;
		try {
			const response = await fetch(`/api/comments/${commentId}`, {
				method: "DELETE",   // Restful 설계 원칙에 따라 요청 방식은 get, post, put, patch, delete로 나뉘어짐
				headers: {"X-Requested-With": "XMLHttpRequest"}
			});
			
			const result = await response.json();
			
			if (!response.ok || !result.success) {
				alert(result.message || "댓글 삭제에 실패했습니다.");
				return;
			} 
			
			// 화면상에서 해당 댓글 제거
			document.querySelector(`#comment-${commentId}`).remove();
		} catch (error) {
			alert("댓글 삭제 중 오류가 발생했습니다.");
		}
	});	
	
}








