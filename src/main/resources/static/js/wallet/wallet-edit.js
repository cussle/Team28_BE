document.addEventListener("DOMContentLoaded", function() {
    const addToGroupButton = document.getElementById('add-to-group-btn');
    const groupModal = document.getElementById('group-modal');
    const modalOverlay = document.getElementById('modal-overlay');

    // 현재 페이지의 명함 ID를 가져오기
    const cardId = document.getElementById('qr-share-btn').getAttribute('data-card-id');

    // 그룹에 추가 버튼 클릭 시 모달 표시
    addToGroupButton?.addEventListener('click', function() {
        console.log("모달 표시 중...");
        groupModal.classList.add('visible')
        modalOverlay.classList.add('visible');
    });

    // // 모달 외부 클릭 시 모달 닫기
    // document.addEventListener('click', function(event) {
    //     if (event.target === groupModal || event.target.closest('#group-modal') === null) {
    //         console.log("닫음");
    //         groupModal.style.display = 'none';
    //     }
    // });

    // 그룹 선택 시 명함을 해당 그룹에 추가
    document.querySelectorAll('.group-option').forEach(function(button) {
        button.addEventListener('click', function() {
            const groupId = this.getAttribute('data-group-id');

            // AJAX 요청으로 그룹에 명함 추가
            $.ajax({
                url: `/groups/${groupId}/cards/${cardId}`,
                type: 'POST',
                success: function() {
                    alert('그룹에 명함이 추가되었습니다.');
                    groupModal.style.display = 'none';
                },
                error: function() {
                    alert('명함 추가에 실패했습니다.');
                }
            });
        });
    });
});
