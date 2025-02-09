# RealTimeChat 서비스

<br>

> <b style="color:#555555">프로젝트 기간 : 2025.01.08 ~ 2025.01.29</b>

<br/><br/>

## 기획 의도 🤔
네트워크를 공부하면서 HTTP의 무상태성(Stateless)과 비연결성(Connectionless)이 서버의 부담을 줄이는 중요한 원리이지만, 실시간 채팅과 같이 연결을 유지해야 하는 서비스는 어떻게 동작할까? 라는 궁금증이 생겼습니다. <br/>
만약 실시간 통신이 필수적인 환경에서 Session 유지하기 위한 서버의 부담이 크다면, 이를 효과적으로 줄이는 방법은 무엇일까? 또한, 기존 HTTP 기반 폴링 방식과 비교했을 때 WebSocket이 어떤 장점을 가지며, 어떤 구조로 동작하는지 간단한 서비스를 구현하며 직접 탐구하고 싶었습니다. <br/>
이러한 고민에서 출발하여, WebSocket을 활용한 실시간 익명 채팅 서비스를 기획하고, 서버 부하를 최소화하면서 안정적인 실시간 통신을 구현하는 방법을 고민해보고자 해당 프로젝트를 기획했습니다.

<br/><br/>


## 프로젝트 개요 📋
본 프로젝트는 **WebSocket**을 활용한 실시간 익명 채팅 서비스입니다. <br/>
기존 HTTP의 무상태성(Stateless) 및 비연결성(Connectionless)의 한계를 극복하고, 양방향 통신을 지원하는 WebSocket을 학습하며 구현하는 것이 목표입니다. 기존 HTTP의 무상태성과 비연결성 한계를 극복하고, 양방향 통신을 지원하는 WebSocket의 장점을 최대한 활용하는 것을 목표로 합니다. <br/>
그러나 다음과 같은 문제점이 있었습니다.

<br/>

1. WebSocket은 "소켓 레벨"의 통신 기능만 제공하므로, 별도의 메시징 프로토콜이 필요.
2. WebSocket 자체로는 메시지를 구분하거나, 특정 Topic을 관리하는 기능이 없음. (멀티 유저 환경에서 효율적인 메시지 전송이 힘듬)

<br/>

이러한 문제를 해결하기 위해 **STOMP 프로토콜**과 **Spring 내장 메시지 브로커(필요시 Reids/Kafka등 확장 가능)** 를 도입했으며, 다음과 같은 이점을 가져올 수 있었습니다.

1. 메시지 브로커 기반의 pub/sub 관리를 통해 빠른 메시지 라우팅
2. STOMP Frame 형식(CONNECT, SEND, MESSAGE)을 통한 편리한 메시징

 <br/>

또한, 서버 부담을 최소화하기 위해 JWT 기반 사용자 인증을 적용하였습니다. 사용자는 로그인 없이 서비스에 접속할 수 있으며, 최초 접속 시 랜덤 닉네임과 고유 네임 태그를 포함한 JWT 토큰을 발급받아 본인을 식별합니다. <br/>

이를 통해 별도의 서버 세션 관리 없이도 안정적인 실시간 채팅 환경을 구축하고, 수평적 확장이 가능한 시스템 설계를 적용할 수 있었습니다.

<br><br/>

## **Stacks** 🔧

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
- JWT 토큰을 활용한 사용자 신원 부여 및 인증 
  - 세션이 새롭게 Connect 되더라도(사용자의 재접속 or 새로고침) LocalStorage의 JWT를 기반으로 이전의 본인 채팅 기록을 식별할 수 있음(파란색 채팅이 본인). <br/> [(코드 링크)](https://github.com/wonseokLee97/realTimeChat/blob/fa5a668bd7ea3cca643b5f16cfa26272eae377a9/realTimeChat_Back-end/src/main/java/com/dev/realtimechat/infra/security/JwtProvider.java#L36C1-L49C1)
  
- Spring 내장 메시징 브로커와 STOMP 프로토콜
  - 메시지 브로커를 기반으로 실시간 메시지 및 접속 인원을 새로고침 없이 브라우저에 로딩 <br/> [(메시징 Broker 코드 링크)](https://github.com/wonseokLee97/realTimeChat/blob/master/realTimeChat_Back-end/src/main/java/com/dev/realtimechat/presentation/ChatWebSocketController.java) <br/> [(접속자 수 Handler 코드 링크)](https://github.com/wonseokLee97/realTimeChat/blob/fa5a668bd7ea3cca643b5f16cfa26272eae377a9/realTimeChat_Back-end/src/main/java/com/dev/realtimechat/infra/handler/StompListener.java#L10C1-L35C2)

- 즉시 서비스 사용
  - 별도의 로그인/회원가입 없이 즉시 서비스 사용 가능 (익명 채팅 기반)


<br/>

### 2. 스크롤 기반 동적 데이터 로딩
![loading](https://github.com/user-attachments/assets/8ecf1162-c946-48b7-b3c1-9fe187b5add6)
- 무한 스크롤 구현
  - 스크롤 위치가 상단에 도달할 때마다 자동으로 추가 데이터를 로딩 <br/> [(코드 링크)](https://github.com/wonseokLee97/realTimeChat/blob/fa5a668bd7ea3cca643b5f16cfa26272eae377a9/realTimeChat_Front-end/chat.js#L114C1-L147C6)
 
- 스크롤 위치 고정
  - 데이터 추가 로딩시 기존의 스크롤 위치를 벗어나지 않도록 할당 <br/> [(코드 링크)](https://github.com/wonseokLee97/realTimeChat/blob/fa5a668bd7ea3cca643b5f16cfa26272eae377a9/realTimeChat_Front-end/chat.js#L170C1-L181C1)
 
- 동적 데이터 로딩
  - offset ~ limit을 이용한 동적 데이터 요청을 통해 필요한 데이터만 로드하여 효율적 처리 <br/> [(코드 링크)](https://github.com/wonseokLee97/realTimeChat/blob/fa5a668bd7ea3cca643b5f16cfa26272eae377a9/realTimeChat_Back-end/src/main/java/com/dev/realtimechat/infra/repository/springdatajpa/SpringDataJpaChatRepository.java#L10C1-L24C2) 



<br/><br/>

## 기능별 실행 흐름도 〰

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
- 클라이언트는 연결 후 `/sub/channel/roomId`와 `/sub/channel/entry`를 구독합니다.
- `/sub/channel/roomId`: 실시간 메시지 전송을 수신하기 위함
- `/sub/channel/entry`: 실시간 채팅방 입장 인원을 수신하기 위함 <br/> [(코드 링크)](https://github.com/wonseokLee97/realTimeChat/blob/905bcdfa9ae08d63ea3f131a71e9f77660d661d9/realTimeChat_Front-end/chat.js#L50C1-L65C6)

<br/>

**2. Send (Client)**
- Client는 `/pub/chat` 으로 메시지를 전송하여 채팅방에 메시지를 전달합니다.

<br/>

**3. Message (Server)** 
- 서버는 새로운 세션의 Connect를 감지하면 메시지 브로커를 통해 `/sub/channel/entry` 구독자들에게 Session '참여자 수'를 send 한다. <br/> [(코드 링크)](https://github.com/wonseokLee97/realTimeChat/blob/fa5a668bd7ea3cca643b5f16cfa26272eae377a9/realTimeChat_Back-end/src/main/java/com/dev/realtimechat/infra/handler/StompListener.java#L10C1-L35C2)
- 서버는 클라이언트의 메시지 발행을 감지하면 메시지 브로커를 통해 `/sub/channel/roomId`의 구독자들에게 실시간 채팅 메시지를 send 한다. <br/> [(코드 링크)](https://github.com/wonseokLee97/realTimeChat/blob/fa5a668bd7ea3cca643b5f16cfa26272eae377a9/realTimeChat_Back-end/src/main/java/com/dev/realtimechat/presentation/ChatWebSocketController.java)

<br/><br/>

## 아키택처 및 ERD

<div style="margin:10px 0 20px 0;display:flex">
 <img style="margin: 0 10px 0 0" src='https://github.com/user-attachments/assets/61b771a4-05b0-4290-b308-eaf042464451' width="800px"/>
 <img style="margin: 0 10px 0 0" src='https://github.com/user-attachments/assets/902b572c-4385-4017-b2d7-0ddaa56461d3' width="200px" height="400px"/>
</div>


<br/><br/><br/><br/>

## 패키징 구조

![패키징](https://github.com/user-attachments/assets/62971eab-adf8-453f-8b52-3ffc003c82c0)


```
└─com
    └─dev
        └─realtimechat
            │  RealTimeChatApplication.java
            │
            ├─chat
            │  ├─application
            │  │      ChatService.java
            │  │      ChatServiceImpl.java
            │  │
            │  ├─config
            │  │      WebSocketConfig.java
            │  │
            │  ├─domain
            │  │      Chat.java
            │  │
            │  ├─handler
            │  │      StompListener.java
            │  │      WebSocketBroadCaster.java
            │  │
            │  ├─infrastructure
            │  │  │  ChatRepository.java
            │  │  │
            │  │  ├─jdbc
            │  │  ├─jpa
            │  │  │      JpaChatRepository.java
            │  │  │
            │  │  └─springdatajpa
            │  │          SpringDataJpaChatRepository.java
            │  │
            │  └─presentation
            │          ChatController.java
            │          ChatWebSocketController.java
            │
            ├─chatRoom
            │  ├─application
            │  │      ChatRoomService.java
            │  │      ChatRoomServiceImpl.java
            │  │
            │  ├─domain
            │  │      Champion.java
            │  │      ChatRoom.java
            │  │
            │  ├─infrastructure
            │  │  │  ChatRoomRepository.java
            │  │  │
            │  │  ├─jdbc
            │  │  ├─jpa
            │  │  │      JpaChatRoomRepository.java
            │  │  │
            │  │  └─springdatajpa
            │  │          SpringDataJpaChatRoomRepository.java
            │  │
            │  └─presentation
            │          ChatRoomController.java
            │
            ├─config
            │      CorsConfig.java
            │      MainConfig.java
            │      WebSecurityConfig.java
            │
            └─shared
                ├─common
                │  ├─api
                │  │      ApiResponse.java
                │  │      ErrorResponse.java
                │  │
                │  ├─dto
                │  │      ChatMessageDto.java
                │  │      ChatRoomDto.java
                │  │
                │  ├─exception
                │  └─type
                │          ErrorType.java
                │          SuccessType.java
                │          Type.java
                │
                ├─jwt
                │      JwtAuthenticationFilter.java
                │      JwtAuthenticationProvider.java
                │      JwtAuthenticationToken.java
                │      JwtChannelInterceptor.java
                │      JwtException.java
                │      JwtProperties.java
                │      JwtProvider.java
                │      TokenClaims.java
                │
                └─utils
                        IpAddressUtil.java
```


## 주요 성과 및 배운 점




## 추후 고도화 
1. 원격 캐시 기반 DB Connection Resource 절감
2. 

... 후술예정
