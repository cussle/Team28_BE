$(document).ready(function () {
    $(".nav-item").removeClass("active"); // 모든 nav-item에서 active 클래스 제거
    $(".nav-item[data-page='chats']").addClass("active"); // chats에 active 클래스 추가

    if (window.location.pathname.includes('chats')) {
        const userId = "1"; // 실제로는 로그인한 유저 ID
        let userName;

        // 유저 이름을 가져오는 함수
        function fetchUserName() {
            $.ajax({
                url: `/cards/${userId}`,
                method: "GET",
                success: function (response) {
                    userName = response.name; // API 응답에서 이름을 가져옴
                    console.log("유저 네임: " + userName);
                    fetchUserChatRooms(); // 이름을 가져온 후 채팅방 목록을 호출
                },
                error: function (error) {
                    console.error("본인 정보를 불러오는데 오류가 발생했습니다:", error);
                }
            });
        }

        // 특정 유저의 참여 채팅방 목록을 가져오는 함수
        function fetchUserChatRooms() {
            $.ajax({
                url: `/api/chats/user/${userId}`, // 본인 ID로 채팅방 목록 요청
                method: "GET",
                success: function (chatRooms) {
                    renderChatRooms(chatRooms);
                },
                error: function (error) {
                    console.error("채팅방 목록을 불러오는데 오류가 발생했습니다:", error);
                }
            });
        }

        // 채팅방 목록을 화면에 렌더링하는 함수
        function renderChatRooms(chatRooms) {
            const chatListContainer = $("#chat-list");
            chatListContainer.empty(); // 기존 목록 초기화

            chatRooms.forEach(room => {
                const chatItem = $('<div>', { class: 'chat-item' });

                const chatImage = $('<div>', { class: 'chat-image' }).append(
                    $('<img>', { src: '/images/temp_chat_user_image.png', alt: 'User Avatar' })
                );

                const chatInfo = $('<div>', { class: 'chat-info' });
                const filteredParticipants = room.participants.filter(name => name !== userName);
                const chatName = $('<span>', { class: 'chat-name', text: filteredParticipants.join(', ') });
                const chatMessage = $('<p>', { class: 'chat-message', text: room.lastMessage });
                chatInfo.append(chatName).append(chatMessage);

                const chatTime = $('<div>', { class: 'chat-time' }).append(
                    $('<span>', { text: formatChatTime(room.lastMessageTime) })
                );

                chatItem.append(chatImage).append(chatInfo).append(chatTime);
                chatListContainer.append(chatItem);
            });
        }

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

        // 페이지 로드 시 본인 이름을 가져온 후 채팅방 목록 가져오기
        fetchUserName();
    }
});
