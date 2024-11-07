$(document).ready(function () {
    $(".nav-item").removeClass("active"); // 모든 nav-item에서 active 클래스 제거
    $(".nav-item[data-page='chats']").addClass("active"); // chats에 active 클래스 추가

    const path = window.location.pathname;

    if (path === '/chats') {  // 채팅 목록 페이지
        // 특정 유저의 참여 채팅방 목록을 가져오는 함수
        function fetchUserChatRooms() {
            $.ajax({
                url: `/api/chats/user/${memberId}`, // 본인 ID로 채팅방 목록 요청
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
                const filteredParticipants = room.participants.filter(name => name !== nickname);
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
        fetchUserChatRooms();
    } else if (path.startsWith('/chats/')) {  // 채팅 방 페이지

        // 뒤로가기 아이콘 클릭 시 /chats로 이동
        const backButton = document.getElementById("backButton");
        backButton.addEventListener("click", function() {
            window.location.href = "/chats"; // /chats 페이지로 이동
        });

        const chatId = path.split('/')[2];
        fetchChatRoom(chatId);

        // 임시로 로컬 서버 설정
        const socket = new WebSocket(`ws://localhost:8080/ws?chatId=${chatId}&userId=${memberId}`);

        // 웹소켓 연결
        socket.addEventListener("open", () => {
            console.log("웹소켓 연결 성공");
        });

        // 새로고침 하거나 나갈 때 연결 끊기
        $(window).on('beforeunload', function () {
            if (socket) {
                socket.close();
            }
        });

        // 메시지 보내기
        function sendMessage(messageContent) {
            const message = {
                senderId: memberId,  // 예시: 메시지를 보낸 사용자의 ID (적절한 값으로 수정)
                sender: '나',       // 예시: 메시지를 보낸 사용자 이름 (적절한 값으로 수정)
                content: messageContent  // 메시지 내용
            };

            socket.send(JSON.stringify(message));  // 서버로 메시지 보내기
            console.log("보낸 메시지:", messageContent);

            // 메시지 박스 추가
            const chatRoomContainer = $('#chatWindow');
            makeMessageBox(message, chatRoomContainer);

            // 자동 스크롤
            chatRoomContainer.scrollTop(chatRoomContainer[0].scrollHeight);
        }


        // 버튼 클릭 시 메시지 보내기
        const sendButton = document.getElementById("sendButton");
        sendButton.addEventListener('click', function() {
            const messageInput = document.getElementById("messageInput");
            const messageContent = messageInput.value;
            if (messageContent.trim()) { // 비어있는 메시지는 보내지 않음
                sendMessage(messageContent);
                messageInput.value = '';  // 메시지 전송 후 입력창 비우기
            }
        });

        // Enter 키로 메시지 보내기
        const messageInput = document.getElementById("messageInput");
        messageInput.addEventListener('keydown', function(event) {
            if (event.key === 'Enter') {
                event.preventDefault();  // 기본 Enter 동작 (줄바꿈)을 방지
                const messageContent = messageInput.value;
                if (messageContent.trim()) { // 비어있는 메시지는 보내지 않음
                    sendMessage(messageContent);
                    messageInput.value = '';  // 메시지 전송 후 입력창 비우기
                }
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
                makeMessageBox(message, chatRoomContainer);
            });
        }

        // 메세지 박스 만들기 로직
        function makeMessageBox(message, chatRoomContainer) {
            console.log(message);
            // 메시지 요소 생성
            // memberId면 오른쪽 / 상대면 왼쪽
            const messageElement = $('<div>', { class: message.senderId === memberId ? 'rightMessage right' : 'leftMessage left' });
            //  memberId와 같으면 sender 아니면 customer
            const senderNameClass = message.senderId === memberId ? 'sender' : 'customer';
            // 이름 표시
            const senderName = $('<div>', { class: senderNameClass, text: message.sender });
            // 메세지 표시
            const messageContent = $('<div>', { class: 'message-content' }).append($('<div>', { class: 'message-text', text: message.content }));

            // 메시지 구성
            messageElement.append(senderName).append(messageContent);

            // 채팅방에 메시지 추가
            chatRoomContainer.append(messageElement);
        }

    }
});
