// WebSocketManager.js
export default class WebSocketManager {
    constructor(token, roomId) {
        // WebSocket 관련 변수 초기화
        this.token = token;
        this.roomId = roomId;
        this.stompClient = null;
        this.subscriptionId = 'entry-' + roomId;
        this.randId = null;
        this.nameTag = null;
        this.previousScrollPos = null;
        this.localhost = 'http://localhost:8080';
        this.hasMoreData = true;
        this.lastMessageId = null;
        this.rec = false;
        this.userScrolled = null;
    }

    // WebSocket 연결 함수
    async connect() {
        const socket = new SockJS(this.localhost + '/connect');  // SockJS를 사용하여 WebSocket 연결
        this.stompClient = Stomp.over(socket);  // Stomp 클라이언트 설정

        // JWT 토큰 디코딩 후 사용자 정보 추출
        const decodedToken = await this.decodeJwt(this.token);
        const ipFromToken = decodedToken.aud[0];

        this.randId = decodedToken.randId;
        this.nameTag = decodedToken.sub;

        // 화면에 접속한 사용자 정보 표시
        $("#current-user-name").text(this.randId);  // 사용자 이름
        $("#current-user-id").text(this.nameTag);   // 사용자 ID

        const headers = {
            Authorization: 'Bearer ' + this.token,
            'X-Client-IP': ipFromToken,  // IP 정보 설정
            'ngrok-skip-browser-warning': '69420',  // ngrok 경고 우회 헤더 추가
        };

        // WebSocket 서버에 연결
        this.stompClient.connect(headers, this.onConnect.bind(this), this.onError.bind(this));
    }

    // JWT 토큰 디코딩 함수
    async decodeJwt(token) {
        console.log(token);
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(atob(base64).split('').map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)).join(''));
        return JSON.parse(jsonPayload);
    }

    // WebSocket 연결 성공 시 호출되는 콜백 함수
    onConnect(frame) {
        console.log('Connected: ' + frame);

        if (this.rec == false) this.loadChat();  // 기존 채팅 메시지 로드

        // 에러 구독
        this.stompClient.subscribe('/user/queue/errors', (message) => {
            const errorMessage = JSON.parse(message.body);  // 메시지를 파싱
            console.error("Error occurred:", errorMessage);  // 에러 메시지 출력
            console.log("Error Detail:", errorMessage.message);  // 에러 상세 메시지 출력
            // UI에서 에러를 보여줄 수도 있음 (예: alert(errorMessage.message))
        });

        // 채팅 메시지 구독
        this.stompClient.subscribe('/sub/channel/' + this.roomId, (chatMessage) => {
            const message = JSON.parse(chatMessage.body);
            this.showChat(message);  // 새 채팅 메시지 표시
        });

        // 입장자 수 구독
        this.stompClient.subscribe('/sub/channel/entry', (entryCount) => {
            $("#current-user-count").text(entryCount.body);  // 화면에 입장자 수 업데이트
        });
    }

    // WebSocket 연결 실패 시 호출되는 콜백 함수
    onError(error) {
        alert(error + "!!!!!!");
    }

    // WebSocket 연결 상태 확인 함수
    isConnected() {
        return this.stompClient && this.stompClient.connected;
    }

    async loadChat() {
        const chatContainer = $("#chatting");
        chatContainer.empty();  // 기존 채팅 목록 초기화
        // 첫 10개 메시지를 화면에 추가
        await this.loadMoreChat();  // offset = 0, limit = 10
    }

    async loadMoreChat() {
        const chatContainer = $("#chatting");

        console.log(`Loading chats from lastMessageId: ${this.lastMessageId}`);

        let messagesToDisplay = null;
        // 서버에서 10개씩 데이터 가져오기
        if (this.hasMoreData) {
            if (this.lastMessageId == null) {
                messagesToDisplay = await this.getChatListByLimit(10); // offset과 limit 전달
            } else {
                messagesToDisplay = await this.getChatListByLastMessageId(this.lastMessageId, 10);
            }
        }

        if (!messagesToDisplay || messagesToDisplay.length === 0) {
            this.hasMoreData = false;
            console.log("No more chats to load.");
            return; // 더 이상 데이터가 없으면 종료
        }

        for (let i = 0; i < messagesToDisplay.length; i++) {
            const chatMessage = messagesToDisplay[i];
            if (i === messagesToDisplay.length - 1) {
                this.lastMessageId = chatMessage.id;
            }

            const chatHtml = this.createChatHtml(
                chatMessage.randId + " " + chatMessage.nameTag,
                chatMessage.message,
                chatMessage.createdAt,
                chatMessage.ipAddress
            );
            chatContainer.prepend(chatHtml);  // 위쪽에 추가 (스크롤 위치를 위로 올리기 위해)
        }

        if (this.previousScrollPos != null) {
            this.scrollToPreviousPosition();
        } else {
            this.scrollChatToBottom();
        }
    }

    setupScrollEvent() {
        const chatContainer = $("#chatting");
        // 스크롤 이벤트 처리
        chatContainer.on('scroll', () => {
            if (chatContainer.scrollTop() === 0) {  // 스크롤이 맨 위에 도달했을 때
                this.loadMoreChat();  // 추가 메시지 로드
            }
        });
    }

    // 채팅 목록을 하단으로 스크롤하는 함수
    scrollChatToBottom() {
        const chatContainer = document.getElementById('chatting');
        chatContainer.scrollTop = chatContainer.scrollHeight;
        this.previousScrollPos = chatContainer.scrollHeight;
    }

    // 스크롤을 마지막으로 본 채팅 내용 위치로 설정
    scrollToPreviousPosition() {
        const chatContainer = document.getElementById('chatting');
        if (chatContainer && this.previousScrollPos != null) {
            const scrollHeight = chatContainer.scrollHeight;
            chatContainer.scrollTop = scrollHeight - this.previousScrollPos;
            this.previousScrollPos = scrollHeight;
        }
    }

    // 날짜 구분선을 추가하는 함수
    addDateDivider(date) {
        const dateDivider = `<div class="date-divider">${date}</div>`;
        $("#chatting").append(dateDivider);
    }

    // 시간 형식 (오전/오후 HH:MM)으로 변환하는 함수
    formatDateTime(createdAt) {
        const date = new Date(createdAt);
        let hours = date.getHours();
        const minutes = String(date.getMinutes()).padStart(2, "0");
        const period = hours >= 12 ? "오후" : "오전";
        hours = hours % 12;
        hours = hours ? hours : 12;
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, "0");
        const day = String(date.getDate()).padStart(2, "0");
        return `${year}년 ${month}월 ${day}일 | ${period} ${hours}:${minutes}`;
    }

    // limit 기준으로 페이지네이션 (최신 메시지 10개 가져오기)
    async getChatListByLimit(limit) {
        try {
            const response = await fetch(
                this.localhost + `/message?roomId=${this.roomId}&limit=${limit}`, 
                {
                    method: "GET",
                    headers: { 
                        Authorization: 'Bearer ' + this.token,
                        'ngrok-skip-browser-warning': '69420'
                    }
                }
            );
            const chatList = await response.json();
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return chatList.response;
        } catch (error) {
            console.error(error);
        }
    }

    // lastMessageId 기준으로 페이지네이션
    async getChatListByLastMessageId(lastMessageId, limit) {
        try {
            const response = await fetch(
                this.localhost + `/message/lastMessageId?roomId=${this.roomId}&limit=${limit}&lastMessageId=${lastMessageId}`, 
                {
                    method: "GET",
                    headers: { 
                        Authorization: 'Bearer ' + this.token,
                        'ngrok-skip-browser-warning': '69420'
                    }
                }
            );
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            const chatList = await response.json();
            return chatList.response;
        } catch (error) {
            console.error(error);
        }
    }

    // 새 채팅 메시지 화면에 표시하는 함수
    showChat(chatMessage) {
        const chatHtml = this.createChatHtml(
            chatMessage.randId + " " + chatMessage.nameTag, 
            chatMessage.message, 
            chatMessage.createdAt, 
            chatMessage.ipAddress
        );
        $("#chatting").append(chatHtml);

        console.log(this.userScrolled);
        if (!this.userScrolled) {
            this.scrollChatToBottom();
        } else {
            if (chatMessage.nameTag === this.nameTag) {
                this.scrollChatToBottom();
            } else {
                this.alertChat(chatMessage);
            }
        }
    }

    // 알림 표시 함수
    alertChat(chatMessage) {
        $("#new-message-alert").html(`${chatMessage.randId + " " + chatMessage.nameTag}: ${chatMessage.message}`);
        $("#new-message-alert").fadeIn(200);
    }

    // 사용자가 스크롤을 움직였는지 감지
    trackUserScroll() {
        const chatContainer = $("#chatting");
        chatContainer.on("scroll", () => {
            const currentScroll = chatContainer.scrollTop();
            const maxScroll = chatContainer[0].scrollHeight - chatContainer.outerHeight();
            this.userScrolled = currentScroll < maxScroll - 5;
        });
    }

    // 스크롤이 최하단이면 알림 제거
    deleteAlert() {
        $("#chatting").on("scroll", () => {
            const chatContainer = $("#chatting");
            const isAtBottom = (chatContainer[0].scrollHeight - chatContainer.scrollTop()) <= chatContainer.outerHeight();
            if (isAtBottom) {
                $("#new-message-alert").fadeOut(200);
            }
        });
    }

    // 채팅 메시지의 HTML 요소 생성 함수
    createChatHtml(sender, message, createdAt, ip) {
        const formattedTime = this.formatDateTime(createdAt);
        const shortenedIp = this.getShortenedIp(ip);
        if (sender === this.randId + " " + this.nameTag) {
            return `<div class="chat ch2">
                        <div class="icon"><i class="fa-solid fa-user"></i></div>
                        <div class="message-info">
                            <div class="sender">${sender} - (${shortenedIp})</div>
                            <div class="textbox">${message}</div>
                            <div class="extra-info">${formattedTime}</div>
                        </div>
                    </div>`;
        } else {
            return `<div class="chat ch1">
                        <div class="icon"><i class="fa-solid fa-user"></i></div>
                        <div class="message-info">
                            <div class="sender">${sender} - (${shortenedIp})</div>
                            <div class="textbox">${message}</div>
                            <div class="extra-info">${formattedTime}</div>
                        </div>
                    </div>`;
        }
    }

    // IP 주소 축약 함수 (예: 192.168.1.1 -> 192.168)
    getShortenedIp(ip) {
        const sectors = ip.split(".");
        return sectors.slice(0, 2).join(".");
    }

    // WebSocket 연결 종료 함수
    disconnect() {
        if (this.stompClient) {
            this.stompClient.disconnect();
        }
        console.log("Disconnected");
    }

    // 채팅 메시지 전송 함수
    sendChat(message) {
        if (message.trim() === "") return;
        const headers = { token: this.token };
        const request = {
            "roomId": this.roomId,
            "message": message,
            "randId": this.randId,
            "nameTag": this.nameTag
        };
        this.stompClient.send("/pub/chat", headers, JSON.stringify(request));
    }
}
