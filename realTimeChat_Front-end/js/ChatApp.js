// ChatApp.js
import WebSocketManager from './WebSocketManager.js';

export default class ChatApp {
    constructor() {
        this.websocketManager = null;  // WebSocketManager 인스턴스
        this.localhost = 'http://localhost:8080';
        this.token = localStorage.getItem('token'); // localStorage에서 토큰 가져오기
        this.roomId = "723a2a33-577b-4380-8116-a1aa8e5868d3";
    }

    // 애플리케이션 초기화 함수
    async init() {
        try {
            console.log(this.localhost + '/init');
            const response = await fetch(this.localhost + '/init', {
                method: "GET",
                headers: { 
                    'Authorization': 'Bearer ' + this.token,
                    'ngrok-skip-browser-warning': '69420',
                }
            });

            const data = await response.json();
            console.log(data);
            if (data.response) {
                if (data.success == false) {
                    console.log("토큰이 유효하지 않아 새로운 토큰을 발급합니다.");
                    localStorage.setItem('token', data.response);
                } else { 
                    console.log("토큰이 존재하지 않아 새로운 토큰을 발급합니다.");
                    localStorage.setItem('token', data.response);
                }
            }

            this.token = localStorage.getItem('token');
            this.websocketManager = new WebSocketManager(this.token, this.roomId);
        } catch (error) {
            console.error('Error:', error);
        } finally {
            await this.websocketManager.connect();  // WebSocket 연결
        }

        this.setupEventHandlers();  // 이벤트 핸들러 설정
        this.websocketManager.setupScrollEvent();  // 스크롤 이벤트 설정
        this.websocketManager.trackUserScroll();
        this.websocketManager.deleteAlert();
    }

    // 이벤트 핸들러 설정 함수
    setupEventHandlers() {
        $("#chat-form").on('submit', (e) => {
            e.preventDefault();
            this.sendChat();
        });

        $("#send").click(() => {
            this.sendChat();
        });
    }

    // 채팅 전송 함수
    sendChat() {
        const message = $("#message").val();
        this.websocketManager.sendChat(message);
        $("#message").val("");
    }
}
