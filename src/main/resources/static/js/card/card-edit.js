document.addEventListener("DOMContentLoaded", function() {
    const cardId = window.location.pathname.split("/")[2];

    // 기존 데이터 가져오기
    fetch(`/cards/${cardId}`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json"
        },
    })
        .then(response => response.json())
        .then(data => {
            document.getElementById("company").value = data.company;
            document.getElementById("position").value = data.position;
            document.getElementById("phone").value = data.phone;
            document.getElementById("bio").value = data.bio;
        })
        .catch(error => {
            console.error("명함 데이터 로딩 실패:", error);
            handleError("명함 데이터를 불러오는 데 실패했습니다.", 3000);
        });

    // 수정하기 버튼 클릭 시
    document.getElementById("card-edit-form").addEventListener("submit", function(e) {
        e.preventDefault();

        const updatedData = {
            company: document.getElementById("company").value,
            position: document.getElementById("position").value,
            phone: document.getElementById("phone").value,
            bio: document.getElementById("bio").value,
        };

        fetch(`/cards/${cardId}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(updatedData)
        })
            .then(response => {
                if (response.ok) {
                    handleSuccess("명함이 성공적으로 수정되었습니다!", 3000);
                    setTimeout(() => {
                        window.location.href = "/cards/manage";
                    }, 300); // 토스트가 사라진 후 페이지 이동
                } else {
                    handleError("명함 수정에 실패했습니다.", 3000);
                }
            })
            .catch(error => {
                console.error("명함 수정 실패:", error);
                handleError("명함 수정에 실패했습니다.", 3000);
            });
    });
});
