# MSA 물류 프로젝트
## 📣 프로젝트 간단 소개
- MSA 기반 국내 B2B 물류 관리 및 배송 시스템
- 24.09.05 ~ 24.09.19 `15일`, `4`인 프로젝트

## 🐹 개발 환경

| 분류       |  상세                                |
|------------|:------------------------------------|
| IDE        | `IntelliJ`                           |
| Language   | `Java 17`                           |
| Framework  | `Spring Boot 3.3.3`, `Spring Security` |
| Database   | `PostgreSQL`, `Redis` |
|ORM         | `JPA` |
| Build Tool | `Gradle`                          |
| Infra      | `Docker`, `Docker-Compose`        |
|Monitoring  |`zipkin`, `prometheus`, `Grafana` |
|Version control|`git`, `github`|

<br>

## 기능 소개
- 공통 기능
   - [x] is_delete 필드를 추가하여 논리적 삭제를 관리, 모든 조회 시 is_delete가 false인 데이터만을 대상으로 처리
   - [x] Zipkin, prometheus를 통해 분산추적 및 로그 수집
   - [x] 수집한 로그를 Grafana 대시보드를 통해 시각화
 
- gateway
   - [x] CustomPreFilter 각 서비스마다 구현하여 인가 처리
   - [x] gateway 애플리케이션 다운 시 Grafana에서 슬랙을 통해 알람

- 유저 관리
   - [x] jwt 토큰을 이용한 인증/인가
   - [x] 허브 관리자, 배송 관리자, 업체 관리자, 마스터 유저로 구분
   - [x] 회원 가입 시 validation을 통해 유효성 검사


- 허브, 허브 간 이동 정보 관리
   - [x] 
   - [x] 
   - [x] 


- 상품 생성 및 관리
   - [x] 모든 상품은 특정 업체와 허브에 소속되어 있음
   - [x] Gemini API 연동하여 상품 설명 삽입에 대한 추천 응답 생성 가능
   - [x] 생성된 응답은 모두 DB에 저장

- 주문 및 배송 기능
   - [x] 주문 시에 허브 모듈에서 정보 받아와 배송 및 배송 경로 기록 엔티티 생성
   - [x] 주문이 생성되면 관련된 재고가 감소하며, 주문이 취소되면 해당 수량이 복원됨
   - [x] 배송 경로는 최초에 모든 경로가 생성
   - [x] 허브 모듈 FeignClient 예외 상황 시 서킷브레이커 Fallback 로직 작동, 기본 값 반환
   - [x] 주문, 배송 삭제 시 연관된 데이터는 is_delete 필드를 통해 관리

## 🧩 ERD 및 테이블 명세서

![./대규모 AI 시스템 설계.png](./systems design.png)
- [[테이블 명세서 보러가기]](https://www.notion.so/teamsparta/fa7f938fb7554cf884904d99c95c8ca5)

<br>

## 🐙 API docs

- [[보러 가기]](https://www.notion.so/teamsparta/API-f7da2a7b7fe64f9c9eeb8616fd15e9f4) (스웨거 삽입 예정)
<br>

## 인프라 구조
![인프라 설계서.png](infra.png)

## 📂 패키지 구조

| Service	             | Description	    | Authorization	 | Port   |
|:---------------------|:----------------|:---------------|:-------|
| [`Eureka`]           | 서비스 등록 및 로드밸런싱|       | `19090` |
| [`Gateway`]          | 요청 인증 및 라우팅     | jwt 토큰 인증      | `19095` |
| [`Auth`]             | jwt 토큰 발급 및 관리     | jwt 토큰 발급      | `19096` |
| [`User`]             | 사용자 관련 작업 관리   | jwt 토큰 인가      | `19094` |
| [`Hub`]              | 허브, 업체 도메인 작업 관리|jwt 토큰 인가    | `19092` |
| [`Logistics`]        | 물류(상품, 주문, 배송) 도메인 작업 관리|jwt 토큰 인가 | `19091` |
| [`AI`]               | AI, Slack 메세지 도메인 작업 관리|jwt 토큰 인가       | `19093` |


## 실행 순서
- Eureka  → gateway  → Auth  → User, Hub, Logistics, AI

## DB port 번호
- Logistics → 5433
- Hub → 5434
- Slack & AI → 5435
- User → 5436


## 팀원 소개 및 파트 분배

| 팀원   | 담당 파트                                               |
|-------|:-------------------------------------------------|
| [곽솔래](https://github.com/lossol1)   | 업체, 허브                                          |
| [이경진](https://github.com/kyungjinleelee)   | 상품, 상품 AI, 주문, 분산추적                                        |
| [최준](https://github.com/CJ-1998)    | 유레카, 게이트웨이, Auth 및 유저 + Slack + AI |
| [노민경](https://github.com/minjjings)   | 배송 (배송, 배송 경로 기록)                                |
| 공통    | 프로메테우스, Zipkin, Resilience4j                             |

# 🌊흐름 및 엔티티

A 업체에서 주문 -> B 업체에 재고 확인 -> 주문, 배송 생성 (배송 메시지 저장)

-> B 업체 상품 재고 수정 -> B 업체 담당 허브 찾기

-> 배송 담당자 배정 (6시에 배송 메시지 전송) -> 허브 간 이동 -> 허브에서 업체로 이동
