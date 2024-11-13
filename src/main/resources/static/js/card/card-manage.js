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
                <button class="edit-button" data-id="${card.id}">수정</button>
                <button class="delete-button" data-id="${card.id}">삭제</button>
            `;

                // 수정 버튼 클릭 시
                cardItem.querySelector('.edit-button').addEventListener('click', function(event) {
                    event.stopPropagation();  // 카드 클릭 이벤트가 실행되지 않도록
                    const cardId = this.getAttribute("data-id");
                    window.location.href = `/cards/${cardId}/edit`;  // 수정 페이지로 이동
                });

                // 삭제 버튼 클릭 시
                cardItem.querySelector('.delete-button').addEventListener('click', function(event) {
                    event.stopPropagation();  // 카드 클릭 이벤트가 실행되지 않도록
                    const cardId = this.getAttribute("data-id");

                    // 삭제 확인
                    if (confirm("정말로 이 명함을 삭제하시겠습니까?")) {
                        fetch(`/cards/${cardId}`, {
                            method: "DELETE",
                            headers: {
                                "Content-Type": "application/json"
                            },
                        })
                            .then(response => {
                                if (response.ok) {
                                    alert("명함이 삭제되었습니다.");
                                    // 삭제된 카드 항목 제거
                                    cardItem.remove();
                                } else {
                                    alert("명함 삭제에 실패했습니다.");
                                }
                            })
                            .catch(error => {
                                console.error("명함 삭제 실패:", error);
                                alert("명함 삭제에 실패했습니다.");
                            });
                    }
                });

                // 카드 클릭 시 상세 페이지로 이동
                cardItem.addEventListener("click", () => {
                    window.location.href = `/cards/${card.id}/view`;
                });

                cardListSection.appendChild(cardItem);
            });
        })
        .catch(error => console.error("명함 목록 로딩 실패:", error));
});
