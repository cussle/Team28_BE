fetch('/register', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json'
    }
})
    .then(response => response.json())
    .then(data => {
        window.location.href = "/home";
    })
    .catch(error => {
        console.error('Error:', error);
        window.location.href = "/login"; // 오류 발생 시 /login으로 redirect
    });