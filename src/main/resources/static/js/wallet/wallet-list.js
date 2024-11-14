let isEditing = false;

document.addEventListener("DOMContentLoaded", function() {
    // 각 그룹 항목에 클릭 이벤트 추가
    document.querySelectorAll('.group-item').forEach(item => {
        item.addEventListener('click', function(event) {
            if (isEditing) return;

            // "수정" 버튼을 클릭한 경우에는 redirectToGroup을 실행하지 않음
            if (!event.target.classList.contains('edit-button') && !event.target.classList.contains('group-name-input')) {
                const groupId = this.getAttribute('data-group-id');
                redirectToGroup(groupId);
            }
        });
    });
});

function createNewGroup() {
    fetch('/groups', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ name: '새로운 그룹' })  // 새로운 그룹의 기본 이름
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            }
            throw new Error('Failed to create group');
        })
        .then(data => {
            location.reload(); // 페이지 새로고침으로 새 그룹 목록을 갱신
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function redirectToGroup(groupId) {
    window.location.href = `/groups/${groupId}/cards`;
}

let currentGroupId = null;

function toggleEditMode(button) {
    const groupId = button.getAttribute('data-group-id');
    const groupItem = button.closest('.group-item');
    const groupNameSpan = groupItem.querySelector('.group-name');
    const groupNameInput = groupItem.querySelector('.group-name-input');

    if (button.textContent === "수정") {
        // "수정" 모드로 전환
        isEditing = true;
        button.textContent = "완료";
        button.classList.add("editing"); // 수정 모드 색상 변경
        groupNameSpan.style.display = "none";
        groupNameInput.style.display = "inline-block";
        groupNameInput.focus();
    } else {
        // "완료" 버튼 클릭 시
        isEditing = false;
        const newName = groupNameInput.value.trim();
        if (newName === "") {
            alert("그룹 이름을 입력하세요.");
            return;
        }

        // 서버로 수정 요청
        $.ajax({
            url: `/groups/${groupId}/update`,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ name: newName }),
            success: function() {
                alert("그룹 이름이 수정되었습니다.");
                groupNameSpan.textContent = newName; // UI에 반영
                button.textContent = "수정";
                button.classList.remove("editing"); // 색상 원래대로
                groupNameSpan.style.display = "inline-block";
                groupNameInput.style.display = "none";
            },
            error: function() {
                alert("그룹 이름 수정에 실패했습니다.");
            }
        });
    }
}
