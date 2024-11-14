document.getElementById('kakao-share-btn').addEventListener('click', function () {
    const cardId = document.getElementById('qr-share-btn').getAttribute('data-card-id');  // cardId를 QR 버튼에서 가져옵니다.

    Kakao.Link.sendDefault({
        objectType: 'feed',
        content: {
            title: '[[${card.name}]]님의 명함',
            description: '회사: [[${card.company}]], 직책: [[${card.position}]]',
            imageUrl: 'https://developers.kakao.com/assets/img/about/logos/kakaolink/kakaolink_btn_medium.png',
            link: {
                mobileWebUrl: 'http://3.34.144.148:8080/cards/' + cardId + '/view',
                webUrl: 'http://3.34.144.148:8080/cards/' + cardId + '/view'
            }
        },
        buttons: [
            {
                title: '명함 보기',
                link: {
                    mobileWebUrl: 'http://3.34.144.148:8080/cards/' + cardId + '/view',
                    webUrl: 'http://3.34.144.148:8080/cards/' + cardId + '/view'
                }
            }
        ],
        fail: function(error) {
            console.error(error);
            handleError('카카오톡 공유에 실패했습니다.', 300);
        }
    });
});

document.getElementById('qr-share-btn').addEventListener('click', function () {
    const cardId = this.getAttribute('data-card-id');  // QR 버튼에서 cardId 가져오기

    if (!cardId) {
        console.error('Card ID가 제공되지 않았습니다.');
        handleError('Card ID가 유효하지 않습니다.', 300);
        return;
    }

    fetch(`/cards/${cardId}/qrcode`)
        .then(response => {
            if (!response.ok) {
                console.error(`서버 응답 오류: ${response.status} ${response.statusText}`);
                return response.text();
            }
            return response.json();
        })
        .then(data => {
            const qrUrl = `${data.qrcode_url}?t=${new Date().getTime()}`;  // 타임스탬프 추가
            const qrImage = document.createElement('img');
            qrImage.src = qrUrl;
            qrImage.alt = 'QR Code';
            qrImage.style.width = '200px';
            qrImage.style.height = 'auto';

            const qrContainer = document.getElementById('qr-image-container');
            qrContainer.innerHTML = '';  // 기존 내용 지우기
            qrContainer.appendChild(qrImage);  // QR 코드 이미지 추가

            handleSuccess('QR 코드가 성공적으로 생성되었습니다.', 400);
        })
        .catch(error => {
            console.error('QR 코드 fetch 오류:', error);
            handleError('QR 코드 생성에 실패했습니다.', 400);
        });
});
