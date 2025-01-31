# RealTimeChat 서비스

<br>

> <b style="color:#555555">프로젝트 기간 : 2025.01.08 ~ 2025.01.29</b>
## 프로젝트 개요 및 기획 의도
본 프로젝트는 WebSocket을 활용한 실시간 익명 채팅 서비스입니다. <br/>
기존 HTTP의 무상태성(Stateless) 및 비연결성(Connectionless)의 한계를 극복하고, 양방향 통신을 지원하는 WebSocket을 학습하며 구현하는 것이 목표였습니다. <br/>
STOMP 프로토콜을 기반으로 비동기 메시징 구조를 설계하였으며, 서버 부담을 최소화하기 위해 JWT 기반 사용자 인증을 적용하였습니다. 사용자는 로그인 없이 서비스에 접속할 수 있으며, 최초 접속 시 랜덤 닉네임과 고유 네임 태그를 포함한 JWT 토큰을 발급받아 본인을 식별합니다. <br/>
이러한 구조를 통해 별도의 서버 세션 관리 없이도 안정적인 실시간 채팅 환경을 구축하였으며, 메시징 구조를 기반으로 수평적 확장성을 고려한 설계를 적용하였습니다.

<br><br/>

## **Stacks**

#### **Environment**

<div style='display:flex;margin-bottom:20px'>
<img style="margin:0 5px 0 0" src="https://img.shields.io/badge/VScode-007ACC?style=for-the-badge&logo=visualstudio&logoColor=white">
<img style="margin:0 5px 0 0" src="https://img.shields.io/badge/intelliJ-000000?style=for-the-badge&logo=intellijidea&logoColor=white">
<img style="margin:0 5px 0 0" src="https://img.shields.io/badge/GIT-F05032?style=for-the-badge&logo=git&logoColor=white">
<img  style="margin:0 5px 0 0"src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white">
</div>

#### **Development**

<div style='display:flex;margin-bottom:20px'>
<img style="margin:0 5px 0 0" src="https://img.shields.io/badge/java-007396?style=for-the-badge&logoColor=white">
<img style="margin:0 5px 0 0" src="https://img.shields.io/badge/javaScript-3178c6?style=for-the-badge&logo=javascript&logoColor=white">
<img style="margin:0 5px 0 0"src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img style="margin:0 5px 0 0"src="https://img.shields.io/badge/springSecurity-6DB33F?style=for-the-badge&logo=springSecurity&logoColor=white">
<img style="margin:0 5px 0 0"src="https://img.shields.io/badge/jwt-8000FF?style=for-the-badge&logo=jwt&logoColor=white">
<img style="margin:0 5px 0 0"src="https://img.shields.io/badge/STOMP/WS-424242?style=for-the-badge&logo=STOMP/WS&logoColor=white">
<img style="margin:0 5px 0 0"src="https://img.shields.io/badge/JPA-04B431?style=for-the-badge&logo=hibernate&logoColor=white">
</div>

#### **Comunication**
<div style='display:flex;margin-bottom:20px'>
<img style="margin:0 5px 0 0" src="https://img.shields.io/badge/NOTION-000000?style=for-the-badge&logo=notion&logoColor=white">
</div>

<br/><br/>



## 주요 기능 📦

### 1. 익명 기반 실시간 채팅
![chat](https://github.com/user-attachments/assets/de1d7948-0904-4b8e-a1ee-63d804ab78c3)
- JWT 토큰을 활용한 사용자 신원 부여 및 인증 [(코드 링크)](https://github.com/wonseokLee97/realTimeChat/blob/fa5a668bd7ea3cca643b5f16cfa26272eae377a9/realTimeChat_Back-end/src/main/java/com/dev/realtimechat/infra/security/JwtProvider.java#L36C1-L49C1)
  - 사용자가 재접속 시 JWT를 기반으로 인증하여 세션이 새롭게 시작되더라도 이전의 채팅 기록을 자동으로 로딩하고 식별할 수 있음.
  
- Spring 내장 메시징 브로커와 STOMP 프로토콜을 이용하여 실시간 메시지 전송 및 접속 인원 관리 (세션 수)
  - 메시징 Broker [(코드 링크)](https://github.com/wonseokLee97/realTimeChat/blob/master/realTimeChat_Back-end/src/main/java/com/dev/realtimechat/presentation/ChatWebSocketController.java)
  - 접속자 수 Handler [(코드 링크)](https://github.com/wonseokLee97/realTimeChat/blob/fa5a668bd7ea3cca643b5f16cfa26272eae377a9/realTimeChat_Back-end/src/main/java/com/dev/realtimechat/infra/handler/StompListener.java#L10C1-L35C2)

- 별도의 로그인/회원가입 없이 즉시 서비스 사용 가능 (익명 채팅 기반)


<br/>

### 2. 스크롤 기반 동적 데이터 로딩
![loading](https://github.com/user-attachments/assets/8ecf1162-c946-48b7-b3c1-9fe187b5add6)
- 무한 스크롤 구현: 스크롤 위치가 상단에 도달할 때마다 자동으로 추가 데이터를 로딩 [(코드 링크)](https://github.com/wonseokLee97/realTimeChat/blob/fa5a668bd7ea3cca643b5f16cfa26272eae377a9/realTimeChat_Front-end/chat.js#L114C1-L147C6)
 
- 스크롤 위치 고정: 데이터 추가 로딩시 기존의 스크롤 위치를 벗어나지 않도록 할당 [(코드 링크)](https://github.com/wonseokLee97/realTimeChat/blob/fa5a668bd7ea3cca643b5f16cfa26272eae377a9/realTimeChat_Front-end/chat.js#L170C1-L181C1)
 
- offset ~ limit을 이용한 동적 데이터 요청을 통해 필요한 데이터만 로드하여 효율적 처리 [(코드 링크)](https://github.com/wonseokLee97/realTimeChat/blob/fa5a668bd7ea3cca643b5f16cfa26272eae377a9/realTimeChat_Back-end/src/main/java/com/dev/realtimechat/infra/repository/springdatajpa/SpringDataJpaChatRepository.java#L10C1-L24C2) 



<br/><br/>

## 기능별 실행 흐름도 📦

### 1. HTTP to WebSocket Upgrade
![1-1-1](https://github.com/user-attachments/assets/517d4880-8d48-4c62-99fc-33fe42968a41)


1. **init()**: 최초 사용자가 접속시 토큰 유효성과 발급 여부를 확인하기 위해 Server로 request 전송 
2. **WebSocket Upgrade 요청**: HTTP에서 WebSocket으로 프로토콜 업그레이드 요청을 전달

<br/><br/>


### 2. Spring Security 
![3-1](https://github.com/user-attachments/assets/656e5adf-fc65-41c6-a85c-50384de46885)

**[JWT 없는 경우]**
1. Header와 함께 Server로 Request 전송
2. JwtAuthenticationFilter에 진입
3. JwtProvider에서 새 토큰을 생성
4. Client로 Response 전송

<br/>

**[JWT 있는 경우]**
1. Header와 함께 Server로 Request 전송
2. JwtAuthenticationFilter에 진입
3. 요청을 AuthenticationManager로 위임
4. JwtAuthenticationProvider와 JwtProvider에서 토큰을 검증
5. 토큰이 유효하지 않거나 만료된 경우
   5-1. JwtProvider에서 새 토큰을 생성하여 클라이언트로 응답 전송
6. 토큰이 유효하면, 인증 객체를 SecurityContextHolder에 저장


<br/><br/>

### 3. CONNECT 이후, STOMP & Spring Messaging Broker
![2-1](https://github.com/user-attachments/assets/04bd1068-9112-4f39-b6f7-df48757d1c7b)

**1. Subscribe (Client)**
Client는 Connect 이후 두 가지의 destination을 구독(Subscribe)한다. 
- `/sub/channel/roomId`: 실시간 메시지 전송을 수신하기 위함
- `/sub/channel/entry`: 실시간 채팅방 입장 인원을 수신하기 위함
- [(코드 링크)](https://github.com/wonseokLee97/realTimeChat/blob/905bcdfa9ae08d63ea3f131a71e9f77660d661d9/realTimeChat_Front-end/chat.js#L50C1-L65C6)

<br/>

**2. Send (Client)**
- Client는 `/pub/chat` 으로 메시지를 전송하여 채팅방에 메시지를 전달합니다.

<br/>

**3. Message (Server)** 
- Server는 Session의 Connect를 감지하여 `/sub/channel/entry`의 구독자들에게 Session '참여자 수'를 send 한다. [(코드 링크)](https://github.com/wonseokLee97/realTimeChat/blob/fa5a668bd7ea3cca643b5f16cfa26272eae377a9/realTimeChat_Back-end/src/main/java/com/dev/realtimechat/infra/handler/StompListener.java#L10C1-L35C2)
- Server의 메시지 브로커는 `/sub/channel/roomId`의 구독자들에게 실시간 채팅 메시지를 send 한다. [(코드 링크)](https://github.com/wonseokLee97/realTimeChat/blob/fa5a668bd7ea3cca643b5f16cfa26272eae377a9/realTimeChat_Back-end/src/main/java/com/dev/realtimechat/presentation/ChatWebSocketController.java)






<br/><br/>


## 주요 성과 및 배운 점




## 추후 고도화 
1. 원격 캐시 기반 DB Connection Resource 절감
2. 

... 후술예정
