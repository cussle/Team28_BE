// card-manage.js
document.addEventListener("DOMContentLoaded", function() {

    fetch("/cards/my", {
        method: "GET",
        headers: {
            "Content-Type": "application/json"
        },
    })
        .then(response => response.json())
        .then(data => {
            const cardListSection = document.getElementById("card-list-section");

            data.forEach(card => {
                // 카드 요소 생성
                const cardItem = document.createElement("div");
                cardItem.className = "card-item";

                // 카드 내용 추가
                cardItem.innerHTML = `
                <h2>${card.name}</h2>
                <p>회사: ${card.company}</p>
                <p>직책: ${card.position}</p>
                <p>전화번호: ${card.phone}</p>
                <p>소개: ${card.bio}</p>
            `;

                // 클릭 이벤트 추가
                cardItem.addEventListener("click", () => {
                    // 카드 상세 페이지로 이동
                    window.location.href = `/cards/${card.id}/view`;
                });

                cardListSection.appendChild(cardItem);
            });
        })
        .catch(error => console.error("명함 목록 로딩 실패:", error));
});
