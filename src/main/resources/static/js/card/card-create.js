// card-create
document.getElementById("card-create-form").addEventListener("submit", function (e) {
    e.preventDefault();

    const data = {
        company: document.getElementById("company").value,
        position: document.getElementById("position").value,
        phone: document.getElementById("phone").value,
        bio: document.getElementById("bio").value,
    };

    const submitButton = document.querySelector(".btn-submit");
    submitButton.disabled = true; // 중복 제출 방지

    fetch("/cards", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    })
        .then(response => {
            if (response.ok) {
                alert("명함이 성공적으로 생성되었습니다!");
                window.location.href = "/cards/manage";
            } else if (response.status === 401) {
                alert("인증이 필요합니다. 로그인 후 다시 시도해주세요.");
            } else {
                return response.json().then(err => {
                    alert(`명함 생성에 실패했습니다: ${err.message || "알 수 없는 에러입니다."}`);
                });
            }
        })
        .catch(error => {
            console.error("Error:", error);
            alert("서버 오류로 인해 명함을 생성할 수 없습니다.");
        })
        .finally(() => {
            submitButton.disabled = false;
        });
});
