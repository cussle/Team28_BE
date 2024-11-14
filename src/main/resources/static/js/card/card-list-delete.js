function deleteCardFromGroup(button) {
    const cardId = button.getAttribute('card-id');
    const groupId = getCurrentGroupId();

    if (confirm("정말로 이 명함을 그룹에서 삭제하시겠습니까?")) {
        $.ajax({
            url: `/groups/${groupId}/cards/${cardId}/delete`,
            type: 'DELETE',
            success: function() {
                alert("명함이 그룹에서 삭제되었습니다.");
                location.reload();
            },
            error: function() {
                alert("명함 삭제에 실패했습니다.");
            }
        });
    }
}

function getCurrentGroupId() {
    return window.location.pathname.split("/")[2]; // URL 경로에서 그룹 ID 추출
}
