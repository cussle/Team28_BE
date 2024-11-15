function populateCardSection(data, container) {
    if (data) {
        const githubUsername = data.name || data.nickname;
        const githubLink = githubUsername ? `https://github.com/${githubUsername}` : '#';

        // 카드 요소 생성
        const cardElement = document.createElement("div");
        cardElement.classList.add("card");
        cardElement.id = `card-${data.id}`;

        cardElement.innerHTML = `
            <button class="delete-button" th:attr="card-id=${data.id}" onclick="deleteCardFromGroup(this, event)">삭제</button>
            <div class="card-image">
                <img src="${data.profileImg}" alt="Person profile">
            </div>
            <div class="card-content">
                <h3>${data.nickname || data.name}</h3>
                ${data.company ? `<p>🏢 Company: ${data.company}</p>` : ''}
                ${data.position ? `<p>📌 Position: ${data.position}</p>` : ''}
                ${data.phone ? `<p>📞 Phone: ${data.phone}</p>` : ''}
                <p>💻 GitHub: 
                    <a href="${githubLink}" target="_blank">
                        ${githubUsername || 'GitHub Profile'}
                    </a>
                </p>
                ${data.email ? `<p>📧 Email: ${data.email}</p>` : ''}
                ${data.linkedin ? `<p>🔗 LinkedIn <a href="${data.linkedin}" target="_blank">${data.linkedin}</a></p>` : ''}
                ${data.notion ? `<p>📚 Notion <a href="${data.notion}" target="_blank">${data.notion}</a></p>` : ''}
                ${data.certification ? `<p>📜 Certification: ${data.certification}</p>` : ''}
                ${data.extra ? `<p>📝 Extra: ${data.extra}</p>` : ''}
                ${data.bio ? `<p>📝 Bio: ${data.bio}</p>` : ''}
                ${data.techStack ? `<p>✅ Tech Stack Included</p>` : ''}
                ${data.repository ? `<p>✅ Repository Included</p>` : ''}
                ${data.contributions ? `<p>✅ Contributions Included</p>` : ''}
            </div>
        `;

        // 클릭 이벤트 리스너 추가
        cardElement.addEventListener("click", () => {
            window.location.href = `/cards/${data.id}/view`;
        });

        // 컨테이너에 카드 요소 추가
        container.appendChild(cardElement);
    } else {
        container.innerHTML = `<p>Card data not available.</p>`;
    }
}

// 개별 카드 데이터 가져오기 함수 (이 함수는 개별 카드 페이지에서만 사용됩니다)
document.addEventListener("DOMContentLoaded", function () {
    const cardSection = document.getElementById("cardSection");
    if (cardSection) {
        const cardId = cardSection.getAttribute("data-card-id") || 1;
        fetchCardData(cardId, cardSection);
    }
});

function fetchCardData(cardId, container) {
    fetch(`/cards/${cardId}`)
        .then(response => response.json())
        .then(data => populateCardSection(data, container))
        .catch(error => console.error('Error fetching card data:', error));
}