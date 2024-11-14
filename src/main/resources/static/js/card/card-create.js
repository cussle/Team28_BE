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
                handleSuccess("명함이 성공적으로 생성되었습니다!", 3000);
                setTimeout(() => {
                    window.location.href = "/cards/manage";
                }, 300); // 토스트가 사라진 후 페이지 이동  // 300ms 딜레이 적용
            } else if (response.status === 401) {
                handleError("인증이 필요합니다. 로그인 후 다시 시도해주세요.", 3000);
            } else {
                return response.json().then(err => {
                    handleError(`명함 생성에 실패했습니다: ${err.message || "알 수 없는 에러입니다."}`, 3000);
                });
            }
        })
        .catch(error => {
            console.error("Error:", error);
            handleError("서버 오류로 인해 명함을 생성할 수 없습니다.", 3000);
        })
        .finally(() => {
            submitButton.disabled = false;
        });
});
