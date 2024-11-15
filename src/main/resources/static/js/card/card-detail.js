// Edit button functionality
const editButton = document.querySelector('.edit-button');
if (editButton) {
    editButton.addEventListener('click', function(event) {
        const cardId = this.getAttribute("data-id");
        console.log(cardId)
        window.location.href = `/cards/${cardId}/edit`;  // Redirect to edit page
    });
}

// Delete button functionality
const deleteButton = document.querySelector('.delete-button');
if (deleteButton) {
    deleteButton.addEventListener('click', function(event) {
        const cardId = this.getAttribute("data-id");

        if (confirm("정말로 이 명함을 삭제하시겠습니까?")) {
            fetch(`/cards/${cardId}`, {
                method: "DELETE",
                headers: {
                    "Content-Type": "application/json"
                },
            })
            .then(response => {
                if (response.ok) {
                    handleSuccess("명함이 삭제되었습니다.", 400);
                    window.location.href = `/cards/manage`
                } else {
                    handleError("명함 삭제에 실패했습니다.", 300);
                }
            })
            .catch(error => {
                console.error("명함 삭제 실패:", error);
                handleError("명함 삭제에 실패했습니다.", 300);
            });
        }
    });
}
