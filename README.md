![](./t.jpg)
# 캐치 T니핑 MSA 물류 프로젝트


## 🐹 개발 환경

| 분류         | 상세                                  |
|------------|:------------------------------------|
| IDE        | IntelliJ                            |
| Language   | Java 17                             |
| Framework  | Spring Boot 3.3.3                   |
| Repository | PostgreSQL 16.4, H2 In-memory(Test) |
| Build Tool | Gradle 8.8                          |
| Infra      | Docker, Github Actions              |

## 👻 상세 개발 환경

### Dependencies

- Spring WebMVC
- Spring Validation
- Spring Security
- Spring Data Jpa
- Spring Data Redis
- jjwt 0.12.5
- QueryDSL 5.0.0
- mapStruct 1.5.5.Final
- Lombok
- JUnit
- Swagger 2.6.0
- Jacoco

## 🐰 프로젝트 상세

> MSA 기반 물류 관리 및 배송플랫폼

## 🐳 ERD

![./대규모 AI 시스템 설계.png](./systems design.png)

## 🐙 API docs

- https://www.notion.so/teamsparta/API-f7da2a7b7fe64f9c9eeb8616fd15e9f4

## 🐬 인프라 구조
![인프라 설계서.png](infra.png)

# 🔢 Port 번호

## 순서
- Logistics → Hub → Slack & AI → User
- gateway나 config 등은 User 이후

## DB port 번호
- Logistics → 5433
- Hub → 5434
- Slack & AI → 5435
- User → 5436

## Eureka port 번호
- Eureka → 19090
- Logistics → 19091
- Hub → 19092
- Slack & AI → 19093
- User → 19094
- gateway → 19095
- auth -> 19096

## Prometheus port 번호
- 9090부터 시작

## Grafana port 번호
- 3000부터 시작

## Redis port 번호
- 6379부터 시작

# 담당 파트

| 담당   | 파트                                               |
|-------|:-------------------------------------------------|
| 곽솔래   | 업체 + 허브                                          |
| 이경진   | 상품 + 주문                                          |
| 최준    | 유레카 + 게이트웨이 + Auth + Config(?) + 유저 + Slack + AI |
| 노민경   | 배송 (배송, 배송 경로 기록)                                |
| 공통    | 프로메테우스, 그라파나, Zipkin                             |
| Infra | EC2, Docker, Github Actions                      |


# 🌊흐름 및 엔티티

A 업체에서 주문 -> B 업체에 재고 확인 -> 주문, 배송 생성 (배송 메시지 저장)

-> B 업체 상품 재고 수정 -> B 업체 담당 허브 찾기

-> 배송 담당자 배정 (6시에 배송 메시지 전송) -> 허브 간 이동 -> 허브에서 업체로 이동
