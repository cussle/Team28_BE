$(document).ready(function () {
    $(".nav-item").removeClass("active"); // 모든 nav-item에서 active 클래스 제거
    $(".nav-item[data-page='chats']").addClass("active"); // chats에 active 클래스 추가

    if (window.location.pathname.includes('chats')) {
        const sampleChatData = [
            {
                id: "room_123",
                participants: ["John Doe"],
                message: "당일 보내진 메시지일 경우",
                time: "2024-11-05T08:15:00Z"
            },
            {
                id: "room_124",
                participants: ["Jane Smith"],
                message: "올해 보내진 메시지일 경우",
                time: "2024-09-05T12:30:00Z"
            },
            {
                id: "room_125",
                participants: ["죠르디"],
                message: "다른 연도에 보내진 메시지일 경우",
                time: "2023-09-05T12:30:00Z"
            }
        ];

        sampleChatData.forEach(chat => {
            const $chatItem = $('<div>', { class: 'chat-item' });

            const $chatImage = $('<div>', { class: 'chat-image' });
            const $img = $('<img>', { src: '/images/temp_chat_user_image.png', alt: 'User Avatar' });
            $chatImage.append($img);

            const $chatInfo = $('<div>', { class: 'chat-info' });
            const $chatName = $('<span>', { class: 'chat-name', text: chat.participants.join(', ') });
            const $chatMessage = $('<p>', { class: 'chat-message', text: chat.message });
            $chatInfo.append($chatName).append($chatMessage);

            const $chatTime = $('<div>', { class: 'chat-time' });
            const $chatTimeSpan = $('<span>', { text: formatChatTime(chat.time) });
            $chatTime.append($chatTimeSpan);
            $chatItem.append($chatImage).append($chatInfo).append($chatTime);
            $('#chat-list').append($chatItem);
        });

        function formatChatTime(chatTimeText) {
            const chatTime = new Date(chatTimeText);
            const today = new Date();

            const isToday =
                chatTime.getFullYear() === today.getFullYear() &&
                chatTime.getMonth() === today.getMonth() &&
                chatTime.getDate() === today.getDate();

            if (isToday) {
                // 오늘인 경우 24시 형태의 시간만 표시
                return chatTime.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', hour12: false });
            } else if (chatTime.getFullYear() === today.getFullYear()) {
                // 올해일 경우 날짜만 표시 (11월 5일 형태)
                return `${chatTime.getMonth() + 1}월 ${chatTime.getDate()}일`;
            } else {
                // 올해가 아닐 경우 (YYYY.MM.DD.) 형태로 표시
                return `${chatTime.getFullYear()}.${chatTime.getMonth() + 1}.${chatTime.getDate()}.`;
            }
        }
    }
});
