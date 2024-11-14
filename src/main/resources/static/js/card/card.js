// card.js
// 카드 섹션을 채우는 함수
function populateCardSection(data, container) {
    if (data) {
        const githubUsername = data.username || data.nickname;
        const githubLink = githubUsername ? `https://github.com/${githubUsername}` : '#';

        container.innerHTML = `
            <div class="card">
                <div class="card-image">
                    <img src="${data.profileImg}" alt="Person profile">
                </div>
                <div class="card-content">
                    <h3>${data.name || 'Username'}</h3>
                    ${data.company ? `<p>🏢 Company: ${data.company}</p>` : ''}
                    ${data.position ? `<p>📌 Position: ${data.position}</p>` : ''}
                    ${data.phone ? `<p>📞 Phone: ${data.phone}</p>` : ''}
                    <p>💻 GitHub: 
                        <a href="${githubLink}" target="_blank">
                            ${githubUsername || 'GitHub Profile'}
                        </a>
                    </p>
                    ${data.email ? `<p>📧 Email: ${data.email}</p>` : ''}
                    ${data.bio ? `<p>📝 Bio: ${data.bio}</p>` : ''}
                </div>
            </div>
        `;
    } else {
        container.innerHTML = `<p>Card data not available.</p>`;
    }
}

// 개별 카드 데이터 가져오기 함수 (이 함수는 개별 카드 페이지에서만 사용됩니다)
document.addEventListener("DOMContentLoaded", function () {
    const cardSection = document.getElementById("cardSection");
    if (cardSection) {
        const urlParams = new URLSearchParams(window.location.search);
        const cardId = urlParams.has('cardId') ? parseInt(urlParams.get('cardId'), 10) : 1;
        fetchCardData(cardId, cardSection);
    }
});

function fetchCardData(cardId, container) {
    fetch(`/cards/${cardId}`)
        .then(response => response.json())
        .then(data => populateCardSection(data, container))
        .catch(error => console.error('Error fetching card data:', error));
}
