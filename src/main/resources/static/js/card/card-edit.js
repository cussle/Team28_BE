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
        .catch(error => console.error("명함 데이터 로딩 실패:", error));

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
                    alert("명함이 성공적으로 수정되었습니다!");
                    window.location.href = "/cards/manage";
                } else {
                    alert("명함 수정에 실패했습니다.");
                }
            })
            .catch(error => {
                console.error("명함 수정 실패:", error);
                alert("명함 수정에 실패했습니다.");
            });
    });
});
