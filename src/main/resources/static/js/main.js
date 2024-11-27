// 댓글 펼치기
function showComments(spanElement) {
    const commentsView = spanElement.closest('.comments-view');
    const commentsList = commentsView.querySelector('.comments-list');
    const feed = commentsView.closest('.feeds-container');
    
    // feedId를 'feeds-container'의 data-feed-id에서 가져옴
    const feedId = feed.getAttribute('data-feed-id');
    console.log(feedId);

    commentsList.innerHTML = ''; // 댓글 리스트 초기화

    // 서버에서 댓글을 가져오는 AJAX 요청
    fetch(`/linksy/comments?feedId=${feedId}`)  // 댓글을 가져올 서버 엔드포인트
        .then(response => response.json())  // 서버로부터 JSON 응답을 받음
        .then(comments => {
            // 댓글 항목 생성
            comments.forEach(comment => {
                const commentElement = document.createElement('div');
                commentElement.className = 'comment-item';
                commentElement.innerHTML = `<strong>${comment.member.userNickname}</strong>: <span>${comment.commentContent}</span>`;
                commentsList.appendChild(commentElement);
            });

            // 댓글 영역을 확장
            commentsList.classList.toggle('show');

            // 버튼 텍스트 변경
            const isExpanded = commentsList.classList.contains('show');
            spanElement.innerText = isExpanded ? "댓글 숨기기" : `댓글 ${comments.length}개 보기`;

            // 피드 높이 변경하지 않고 댓글 영역만 확장
            commentsList.style.maxHeight = isExpanded ? `${commentsList.scrollHeight}px` : '0';

            if (isExpanded) {
                commentsList.scrollTop = 0; // 스크롤 초기화
            }
        })
        .catch(error => {
            console.error('댓글을 가져오는 데 실패했습니다:', error);
        });
}

// 더보기 기능
function toggleCaption(readMoreElement) {
    const caption = readMoreElement.previousElementSibling;
    const fullText = caption.getAttribute("data-full-text");

    if (fullText.length > 15) {
        if (readMoreElement.innerText === "더보기") {
            caption.innerText = fullText;
            readMoreElement.innerText = "숨기기";
        } else {
            const truncatedText = fullText.slice(0, 15) + "...";
            caption.innerText = truncatedText;
            readMoreElement.innerText = "더보기";
        }
    } else {
        readMoreElement.style.display = 'none'; // 15글자 이하일 때 버튼 숨기기
    }
}

// 좋아요 토글
function toggleLike(element) {
    const feedId = element.getAttribute('data-feed-id');
    const userId = element.getAttribute('data-user-id');

    fetch('/linksy/toggleLike', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: `feedId=${feedId}&userId=${userId}`
    })
    .then(response => response.json())
    .then(data => {
        // 좋아요 상태 업데이트
        element.className = data.isLiked ? 'bi-heart-fill' : 'bi-heart';

        // 좋아요 수 업데이트
        const likesElement = element.closest('.feed-actions-likes').querySelector('.likes');
        likesElement.textContent = `좋아요 ${data.likeAmount}개`;
    })
    .catch(error => console.error('Error:', error));
}

// 댓글 작성
function addComment(buttonElement) {
    const commentInput = buttonElement.previousElementSibling;
    const commentContent = commentInput.value.trim();
    if (!commentContent) {
        alert("댓글 내용을 입력하세요.");
        return;
    }

    const feed = buttonElement.closest('.feeds-container');
    const feedId = feed.getAttribute('data-feed-id');
    const commentsList = feed.querySelector('.comments-list');
    const commentsView = feed.querySelector('.comments-view');
    const viewCommentsText = commentsView.querySelector('.view-comments');

    // AJAX 요청으로 댓글 추가
    fetch('/linksy/addComment', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `feedId=${feedId}&commentContent=${encodeURIComponent(commentContent)}`
    })
    .then(response => response.json())
    .then(comment => {
        // 댓글 추가 후 UI 업데이트
        const commentElement = document.createElement('div');
        commentElement.className = 'comment-item';
        commentElement.innerHTML = `<strong>${comment.member.userNickname}</strong>: <span>${comment.commentContent}</span>`;
        commentsList.appendChild(commentElement);

        // 댓글 입력 필드 초기화
        commentInput.value = '';

        // 댓글 수 업데이트
        const newCommentCount = commentsList.children.length;
        viewCommentsText.innerText = `댓글 ${newCommentCount}개`;

        // 댓글 리스트 확장 상태 유지
        commentsList.classList.add('show');
        commentsList.style.maxHeight = `${commentsList.scrollHeight}px`;
        viewCommentsText.innerText = "댓글 숨기기";
    })
    .catch(error => {
        console.error('댓글 추가에 실패했습니다:', error);
    });
}

// 팔로우 상태 확인 함수 추가
async function checkFollowStatus(userId, feedId) {
    try {
        const response = await fetch(`/api/follow/check/${feedId}/${userId}`);
        return await response.json();
    } catch (error) {
        console.error('팔로우 상태 확인 실패:', error);
        return false;
    }
}

// 좋아요 팝업 표시 함수 수정
async function showLikesPopup(element) {
    const feedId = element.getAttribute('data-feed-id');
    const popup = document.getElementById('likesPopup');
    const likesList = popup.querySelector('.likes-list');
    
    // 팝업 표시
    popup.style.display = 'block';
    
    try {
        const response = await fetch(`/api/feed/likes?feedId=${feedId}`);
        const data = await response.json();
        
        likesList.innerHTML = ''; // 기존 목록 초기화
        
        if (data.length === 0) {
            likesList.innerHTML = '<p style="text-align: center; color: #888;">No likes found for this feed.</p>';
            return;
        }

        // 모든 사용자의 팔로우 상태를 한번에 확인
        const followStatuses = await Promise.all(
            data.map(user => checkFollowStatus(user.userId, feedId))
        );
        
        // 팔로우 상태와 함께 UI 렌더링
        data.forEach((user, index) => {
            const likeItem = document.createElement('div');
            likeItem.className = 'likeItem';
            
            const isFollowing = followStatuses[index];
            
            likeItem.innerHTML = `
                <img class="profileImage" src="/images/profile/${user.userImg}" alt="프로필 사진">
                <div class="userInfo">${user.userNickname}</div>
            `;
            
            likesList.appendChild(likeItem);
        });
    } catch (error) {
        console.error('좋아요 목록을 가져오는데 실패했습니다:', error);
        likesList.innerHTML = '<p class="error-message">좋아요 목록을 불러올 수 없습니다.</p>';
    }
}

// 팔로우/언팔로우 토글 함수 추가
async function toggleFollow(button, userId) {
    try {
        const isFollowing = button.textContent.trim() === 'Unfollow';
        const url = isFollowing ? '/api/unfollow' : '/api/follow';
        
        // 현재 로그인한 사용자의 ID도 필요합니다
        const followerId = button.closest('.likeItem').getAttribute('data-current-user-id');
        
        const response = await fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: `followingId=${userId}&followerId=${followerId}`
        });
        
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        
        const data = await response.json();
        console.log('Toggle follow response:', data); // 디버깅용 로그
        
        // 버튼 상태 업데이트
        button.textContent = isFollowing ? 'Follow' : 'Unfollow';
        button.style.backgroundColor = isFollowing ? '#007bff' : '#dc3545';
        
    } catch (error) {
        console.error('팔로우/언팔로우 토글 실패:', error);
        alert('팔로우/언팔로우 처리 중 오류가 발생했습니다.');
    }
}

// 팝업 닫기
function closeLikesPopup() {
    const popup = document.getElementById('likesPopup');
    popup.style.display = 'none';
}

// 팝업 외부 클릭시 닫기
window.onclick = function(event) {
    const popup = document.getElementById('likesPopup');
    if (event.target == popup) {
        popup.style.display = 'none';
    }
}