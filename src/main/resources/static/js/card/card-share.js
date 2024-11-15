// Kakao 공유 버튼
document.getElementById('kakao-share-btn').addEventListener('click', function () {
    if (!cardId) {
        console.error('Card ID가 제공되지 않았습니다.');
        handleError('Card ID가 유효하지 않습니다.', 300);
        return;
    }

    // Kakao 공유 설정
    const content = {
        title: `${cardName}님의 명함`,
        imageUrl: 'https://developers.kakao.com/assets/img/about/logos/kakaolink/kakaolink_btn_medium.png',
        link: {
            mobileWebUrl: `http://3.34.144.148:8080/shared/cards/${cardId}`,
            webUrl: `http://3.34.144.148:8080/shared/cards/${cardId}`
        }
    };

    if (cardCompany || cardPosition) {
        content.description = `회사: ${cardCompany || ''}${cardCompany && cardPosition ? ', ' : ''}직책: ${cardPosition || ''}`;
    }

    Kakao.Link.sendDefault({
        objectType: 'feed',
        content: content,
        buttons: [
            {
                title: '명함 보기',
                link: {
                    mobileWebUrl: `http://3.34.144.148:8080/shared/cards/${cardId}`,
                    webUrl: `http://3.34.144.148:8080/shared/cards/${cardId}`
                }
            }
        ],
        fail: function (error) {
            console.error(error);
            handleError('카카오톡 공유에 실패했습니다.', 300);
        }
    });
});

// QR 생성 및 렌더링
document.getElementById('qr-share-btn').addEventListener('click', function () {
    const cardId = this.getAttribute('data-card-id'); // 버튼에서 cardId 가져오기

    if (!cardId) {
        console.error('Card ID가 제공되지 않았습니다.');
        handleError('Card ID가 유효하지 않습니다.', 300);
        return;
    }

    const qrContainer = document.getElementById('qr-image-container');
    qrContainer.innerHTML = ''; // 이전 QR 코드 제거

    // QR 코드 API 요청
    fetch(`/cards/${cardId}/qrcode-image?t=${new Date().getTime()}`)
        .then(response => {
            if (!response.ok) throw new Error('QR 코드 생성에 실패했습니다.');
            return response.blob();
        })
        .then(blob => {
            const qrImage = document.createElement('img');
            qrImage.src = URL.createObjectURL(blob);
            qrImage.alt = 'QR Code';
            qrImage.style.width = '200px';
            qrImage.style.height = '200px';

            qrContainer.appendChild(qrImage); // QR 이미지 추가
            qrContainer.style.display = 'block'; // QR 컨테이너 표시
        })
        .catch(error => {
            console.error('QR 코드 생성 중 오류:', error);
            handleError('QR 코드 생성에 실패했습니다.', 400);
        });
});
