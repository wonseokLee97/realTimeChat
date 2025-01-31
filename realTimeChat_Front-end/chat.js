class WebSocketManager {
    constructor(token, roomId) {
        // WebSocket 관련 변수 초기화
        this.token = token;
        this.roomId = roomId;
        this.stompClient = null;
        this.subscriptionId = 'entry-' + roomId;
        this.randId = null;
        this.nameTag = null;
        this.chatList = null;
        this.previousScrollPos = null;
        this.localhost = 'http://localhost:8080';
        this.hasMoreData = true;
        this.offset = 0;
    }

    // WebSocket 연결 함수
    async connect() {
        const socket = new SockJS(this.localhost + '/chat');  // SockJS를 사용하여 WebSocket 연결
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
            'X-Client-IP': ipFromToken  // IP 정보 설정
        };

        // WebSocket 서버에 연결
        this.stompClient.connect(headers, this.onConnect.bind(this), this.onError.bind(this));
    }

    // JWT 토큰 디코딩 함수
    async decodeJwt(token) {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(atob(base64).split('').map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)).join(''));
        return JSON.parse(jsonPayload);
    }

    // WebSocket 연결 성공 시 호출되는 콜백 함수
    onConnect(frame) {
        console.log('Connected: ' + frame);
        this.loadChat();  // 기존 채팅 메시지 로드

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
        console.error("WebSocket 연결 실패: ", error);
        this.expired = true;  // 연결 실패 시 상태 변경
        this.reconnect();  // 재접속 시도
    }

    // WebSocket 재연결 시도 함수
    reconnect() {
        setTimeout(() => {
            console.log("Reconnecting WebSocket...");
            if (!this.isConnected()) {
                this.connect();  // 연결이 되어 있지 않으면 재연결
            }
        }, 5000); // 5초 후 재시도
    }

    // WebSocket 연결 상태 확인 함수
    isConnected() {
        return this.stompClient && this.stompClient.connected;
    }

    // // 기존 채팅 목록 로드 함수
    // async loadChat() {
    //     const chatList = await this.getChatList();  // 채팅 목록 불러오기
    //     const chatContainer = $("#chatting");
    //     chatContainer.empty();  // 기존 채팅 목록 초기화

    //     // 채팅 메시지가 있으면 화면에 추가
    //     if (chatList && chatList.length > 0) {
    //         chatList.forEach(chatMessage => {
    //             const chatHtml = this.createChatHtml(chatMessage.randId + " " + chatMessage.nameTag, chatMessage.message, chatMessage.createdAt, chatMessage.ipAddress);
    //             chatContainer.append(chatHtml);
    //         });
    //     }
        
    //     this.scrollChatToBottom();  // 채팅 목록이 하단에 위치하도록 스크롤
    // }

    async loadChat() {
        const chatContainer = $("#chatting");
        chatContainer.empty();  // 기존 채팅 목록 초기화
    
        // 첫 10개 메시지를 화면에 추가
        await this.loadMoreChat();  // offset = 0, limit = 10
    }

    async loadMoreChat() {

        const chatContainer = $("#chatting");

        console.log(`Loading chats from offset: ${this.offset}`);

        let messagesToDisplay = null;
        // 서버에서 10개씩 데이터 가져오기
        if (this.hasMoreData) {
            messagesToDisplay = await this.getChatList(this.offset, 10); // offset과 limit 전달
        }

        if (!messagesToDisplay || messagesToDisplay.length === 0) {
            this.hasMoreData = false;
            console.log("No more chats to load.");
            return; // 더 이상 데이터가 없으면 종료
        }


        messagesToDisplay.forEach(chatMessage => {
            const chatHtml = this.createChatHtml(chatMessage.randId + " " + chatMessage.nameTag, chatMessage.message, chatMessage.createdAt, chatMessage.ipAddress);
            chatContainer.prepend(chatHtml);  // 위쪽에 추가 (스크롤 위치를 위로 올리기 위해)
        });


        if (this.previousScrollPos != null) {
            this.scrollToPreviousPosition();
        } else {
            this.scrollChatToBottom();
        }

        // offset 증가
        this.offset += 10;
    }

    setupScrollEvent() {
        const chatContainer = $("#chatting");
    
        // 스크롤 이벤트 처리
        chatContainer.on('scroll', () => {
            if (chatContainer.scrollTop() === 0) {  // 스크롤이 맨 위에 도달했을 때
                this.loadMoreChat(this.offset);  // 추가 메시지 로드
                // offset += 10;  // 10개씩 증가
            }
        });
    }

    
    // 채팅 목록을 하단으로 스크롤하는 함수
    scrollChatToBottom() {
        const chatContainer = document.getElementById('chatting');
        chatContainer.scrollTop = chatContainer.scrollHeight;
        // console.log(chatContainer.scrollHeight);
        this.previousScrollPos = chatContainer.scrollHeight;
    }

    // 스크롤을 마지막으로 본 채팅 내용 위치로 설정
    scrollToPreviousPosition() {
        const chatContainer = document.getElementById('chatting');
        // console.log(chatContainer.scrollHeight);

        if (chatContainer && this.previousScrollPos != null) {
            const scrollHeight = chatContainer.scrollHeight;
            chatContainer.scrollTop = scrollHeight - this.previousScrollPos;
            this.previousScrollPos = scrollHeight;
        }
    };

    

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

        const period = hours >= 12 ? "오후" : "오전"; // 12시 이후는 '오후', 그 전은 '오전'
        hours = hours % 12; // 12시간제로 변환
        hours = hours ? hours : 12; // 0시를 12로 변환

        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, "0");
        const day = String(date.getDate()).padStart(2, "0");

        return `${year}년 ${month}월 ${day}일 | ${period} ${hours}:${minutes}`;
    }



    // 서버에서 채팅 목록을 불러오는 함수
    async getChatList(offset, limit) {
        try {
            const response = await fetch(
                this.localhost + `/message?roomId=${this.roomId}&limit=${limit}&offset=${offset}`, 
            {
                method: "GET",
                headers: { Authorization: 'Bearer ' + this.token }  // Authorization 헤더 추가
            });

            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }

            const chatList = await response.json();
            return chatList.response;  // 서버에서 받은 채팅 목록 반환
        } catch (error) {
            console.error(error);
        }
    }

    // 새 채팅 메시지 화면에 표시하는 함수
    showChat(chatMessage) {
        const chatHtml = this.createChatHtml(chatMessage.randId + " " + chatMessage.nameTag, chatMessage.message, chatMessage.createdAt, chatMessage.ipAddress);
        $("#chatting").append(chatHtml);  // 새 메시지 추가
        this.scrollChatToBottom();  // 채팅 목록이 하단에 위치하도록 스크롤
    }

    // 채팅 메시지의 HTML 요소 생성 함수
    createChatHtml(sender, message, createdAt, ip) {
        const formattedTime = this.formatDateTime(createdAt);  // 시간 형식 변환
        const shortenedIp = this.getShortenedIp(ip);  // IP 주소 축약

        // 본인 메시지와 다른 사람 메시지 구분
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

    // IP 주소를 축약하는 함수 (예: 192.168.1.1 -> 192.168)
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
        if (message.trim() === "") return;  // 빈 메시지는 전송하지 않음
        const headers = { token: this.token };
        const request = {
            "roomId": this.roomId,
            "message": message,
            "randId": this.randId,
            "nameTag": this.nameTag
        };
        this.stompClient.send("/pub/chat", headers, JSON.stringify(request));  // 서버로 메시지 전송
    }
}

class ChatApp {
    constructor() {
        this.websocketManager = null;  // WebSocketManager 인스턴스 초기화
        this.localhost = 'http://localhost:8080';
        this.token = localStorage.getItem('token'); // localStorage 에서 가져옴
        this.roomId = "9fc1b646-e043-463b-aa17-ff9063540342"
    }

    // 애플리케이션 초기화 함수
    async init() {
        try {
            const response = await fetch(this.localhost + '/init', {
                headers: {
                    'Authorization': 'Bearer ' + this.token
                }
            });
            
            const data = await response.json();
            console.log(data);
            // response가 오지 않는 경우, 성공적으로 토큰 유효성 검사 완료
            if (data.response) {
                // error가 발생한 경우 (EXPIRED_TOKEN, INVALID_TOKEN)
                if (data.success == false) {
                    // 새로운 토큰으로 변경
                    console.log("토큰이 유효하지 않아 새로운 토큰을 발급합니다.")
                    localStorage.setItem('token', data.response);
                } 
                // 성공적으로 발급 받은경우 (SUCCESS_TOKEN_ISSUANCE)
                else { 
                    console.log("토큰이 존재하지 않아 새로운 토큰을 발급합니다.")
                    localStorage.setItem('token', data.response);
                }
            }
    
            this.token = localStorage.getItem('token');
            this.websocketManager = new WebSocketManager(this.token, this.roomId);  // WebSocketManager 생성
        } catch (error) {
            console.error('Error:', error);
        } finally {
            await this.websocketManager.connect();  // WebSocket 연결
        }


        // const roomId = "cd10e323-abb1-4cba-9a87-56a1c10192ed";  // 채팅방 ID
        this.setupEventHandlers();  // 이벤트 핸들러 설정
        this.websocketManager.setupScrollEvent();  // 스크롤 이벤트 핸들러 설정
    }

    // 이벤트 핸들러 설정 함수
    setupEventHandlers() {
        // 채팅 폼 제출 시 채팅 전송
        $("#chat-form").on('submit', (e) => {
            e.preventDefault();
            this.sendChat();  // 채팅 전송
        });

        // 'send' 버튼 클릭 시 채팅 전송
        $("#send").click(() => {
            this.sendChat();  // 채팅 전송
        });
    }

    // 채팅 전송 함수
    sendChat() {
        const message = $("#message").val();  // 입력된 메시지 가져오기
        this.websocketManager.sendChat(message);  // WebSocket을 통해 메시지 전송
        $("#message").val("");  // 입력 필드 초기화
    }
}

$(document).ready(() => {
    const app = new ChatApp();
    app.init();  // 애플리케이션 초기화
});
