// src/main/resources/static/js/oauth/redirect.js

document.addEventListener("DOMContentLoaded", function() {
    fetch('/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json()
        })
        .then(data => {
            if (data.is_registered) {
                window.location.href = '/home';
            } else {
                window.location.href = '/register';
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('오류가 발생했습니다. 다시 시도해주세요.');
            window.location.href = '/login';
        });
});
