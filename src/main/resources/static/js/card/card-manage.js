// card-manage.js

document.addEventListener("DOMContentLoaded", function () {
    function fetchCardList() {
        fetch("/cards/my", {
            method: "GET",
            headers: {
                "Content-Type": "application/json"
            },
        })
            .then(response => response.json())
            .then(data => populateCardListSection(data))
            .catch(error => console.error("명함 목록 로딩 실패:", error));
    }

    // card-list-section을 채우는 함수
    function populateCardListSection(data) {
        const cardListSection = document.getElementById("card-list-section");
        cardListSection.innerHTML = ''; // 기존 내용을 초기화

        data.forEach(card => {
            const cardContainer = document.createElement("div");
            cardContainer.className = "card-item";
            cardContainer.id = `card-${card.id}`;

            // 개별 카드 정보를 표시하기 위해 card.js의 populateCardSection 함수를 호출하여 HTML을 생성
            populateCardSection(card, cardContainer);

            cardListSection.appendChild(cardContainer);
        });
    }

    // 카드 목록을 가져옴
    fetchCardList();
});
