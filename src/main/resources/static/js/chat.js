$(document).ready(function () {
    $(".nav-item").removeClass("active"); // 모든 nav-item에서 active 클래스 제거
    $(".nav-item[data-page='chats']").addClass("active"); // chats에 active 클래스 추가

    const path = window.location.pathname;
    let websocket;
    const userId = "1"; // 실제로는 로그인한 유저 ID
    let userName;

    if (path === '/chats') {
        // 채팅 목록 페이지

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
    } else if (path.startsWith('/chats/')) {

        // 채팅방 세부 정보 요청 가져오기 => 지난 대화 불러와 로드
        const chatId = path.split('/')[2];
        fetchChatRoom(chatId);


        // 윈도우가 언로드될 때 웹소켓 연결 끊기
        $(window).on('beforeunload', function () {
            if (websocket) {
                websocket.close();
            }
        });

        // 특정 채팅방의 세부 정보를 가져오는 함수
        function fetchChatRoom(chatId) {
            $.ajax({
                url: `/api/chats/${chatId}`, // 특정 채팅방 ID로 요청
                method: "GET",
                success: function (chatRoom) {
                    renderChatRoom(chatRoom);
                },
                error: function (error) {
                    console.error("채팅방 정보를 불러오는데 오류가 발생했습니다:", error);
                }
            });
        }

        // 채팅방 세부 정보를 화면에 렌더링하는 함수
        function renderChatRoom(chatRoom) {
            const chatRoomContainer = $("#chatWindow");
            chatRoomContainer.empty(); // 기존 내용 초기화

            chatRoom.messages.forEach(message => {
                // 각 메시지 요소 생성
                let messageElement;

                // 메시지 내용을 담을 div 생성
                const messageContent = $('<div>', { class: 'message-content' });
                let senderName; // 발신자 이름

                // 로그인한 사용자의 메시지인지 확인하여 왼쪽 또는 오른쪽 정렬 클래스 적용
                if (message.senderId === userId) {
                    messageElement = $('<div>', { class: 'rightMessage' });
                    messageElement.addClass('right'); // 본인의 메시지: 오른쪽 정렬
                    senderName = $('<div>', { class: 'sender', text: message.sender }); // 발신자 이름
                } else {
                    messageElement = $('<div>', { class: 'leftMessage' });
                    messageElement.addClass('left'); // 상대방의 메시지: 왼쪽 정렬
                    senderName = $('<div>', { class: 'customer', text: message.sender }); // 발신자 이름
                }

                // 발신자 이름과 메시지 내용 추가
                const messageText = $('<div>', { class: 'message-content', text: message.content}); // 메시지 텍스트

                messageContent.append(messageText);
                messageElement.append(senderName).append(messageContent);
                chatRoomContainer.append(messageElement);
            });
        }


    }
});
