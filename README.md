# :pushpin: AI 배달 서비드(Backend)



## Production


## 📹 Demo Video
클릭하시면 영상을 볼 수 있습니다. <br /><br />
[![Video Label]()

## 📁 System Architecture
<img width="717" alt="image" src="https://github.com/user-attachments/assets/10031dc2-6047-4a50-97bd-5d4c3a2d659d" />

## 💾 ERD
<img width="1009" alt="image" src="https://github.com/user-attachments/assets/11bc9b50-71b2-42f2-a2f6-1298ceeb51c8" />

## 🛠 Installation

### 사용설명서
준비물
- GoogleAI(Gemini) secret key ([발급받으러 가기(비용이 발생할 수 있음)]([https://platform.openai.com/](https://ai.google.dev/)))


1. Docker를 시스템에 설치합니다.
2. 아래의 shell 명령문을 똑같이 따라 칩니다.
```shell
$ git clone https://github.com/josephuk77/delivery-service.git
```
3.  docker-compose.yml 파일과 .env 파일을 알맞은 위치에 작성합니다.
- .docker-compose.yml
```
services:
  backend:
    build:
      context: .
      dockerfile: Dockerfile
    env_file:
      - .env
    ports:
      - "8080:8080"
    depends_on:
      - db
    container_name: delivery-backend
    volumes:
      - ./src/main/java:/delivery/src/main/java  # ✅ Java 소스 코드 동기화
      - ./src/main/resources:/delivery/src/main/resources  # ✅ 설정 파일 동기화
    working_dir: /delivery
    command: ./gradlew bootRun --continuous  # ✅ JAR 빌드 없이 실행

  db:
    image: postgres
    environment:
      - POSTGRES_USER=myuser
      - POSTGRES_PASSWORD=mypassword
      - POSTGRES_DB=delivery
    container_name: delivery-db
    ports:
      - "5432:5432"
    volumes:
      - db_data:/var/lib/postgresql/data

  redis:
    image: redis:latest
    container_name: delivery-redis
    ports:
      - "6379:6379"
    volumes:
      - redis_data:/data
    command: ["redis-server", "--appendonly", "yes", "--bind", "0.0.0.0"]  # ✅ 외부 접근 허용


  pgadmin:
    image: dpage/pgadmin4
    environment:
      - PGADMIN_DEFAULT_EMAIL=myuser@gmail.com
      - PGADMIN_DEFAULT_PASSWORD=mypassword
    ports:
      - "8088:80"
    container_name: delivery-pgadmin

volumes:
  db_data:
  redis_data:  # ✅ Redis 볼륨 추가
```
- .env (docker-compose.yml 파일과 같은 디렉토리)
```
JWT_SECRET_KEY="JWT시크릿키"
ADMIN_KEY="admin계정 생성 키"
GEMINI_SECRET_KEY="genmini시크릿키"
```
- Dockerfile (docker-compose.yml 파일과 같은 디렉토리)
```
FROM eclipse-temurin:17-jdk

WORKDIR /delivery

# 프로젝트 소스 복사
COPY . .

# Gradle 실행 권한 부여
RUN chmod +x gradlew

# JAR 빌드 없이 애플리케이션 실행 (bootRun 모드)
CMD ["./gradlew", "bootRun"]
```
4. 아래의 shell 명령문을 똑같이 따라 칩니다.
```shell
$ cd project
$ docker compose up -d --build
```
5. Docker Desktop에서 Docker Container들이 잘 실행되고 있는지 확인합니다.
6. http://localhost:8080주소로 REST API서버 구축

- - - 
## 🔌 Tech Stack
<div align =center>

Area| Tech Stack|
:--------:|:------------------------------:|
**Backend** |  <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=flat-square&logo=Spring&logoColor=white"> <img src="https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=PostgreSQL&logoColor=white"> 
**AI** | <img src="https://img.shields.io/badge/Google-Gemini-yellow?style=for-the-badge&logo=google"> 
**DevOps** | <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white"> <img src="https://img.shields.io/badge/Amazon_EC2-FF9900?style=for-the-badge&logo=Amazon-EC2&logoColor=black">
**etc** | ![GitHub](https://img.shields.io/static/v1?style=for-the-badge&message=GitHub&color=181717&logo=GitHub&logoColor=FFFFFF&label=) ![Slack](https://img.shields.io/static/v1?style=for-the-badge&message=Slack&color=4A154B&logo=Slack&logoColor=FFFFFF&label=) ![Notion](https://img.shields.io/static/v1?style=for-the-badge&message=Notion&color=000000&logo=Notion&logoColor=FFFFFF&label=) ![Postman](https://img.shields.io/static/v1?style=for-the-badge&message=Postman&color=FF6C37&logo=Postman&logoColor=FFFFFF&label=) <img src="https://img.shields.io/badge/swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black"> <img src="https://img.shields.io/badge/Intellij%20Idea-000?logo=intellij-idea&style=for-the-badge">


- - - 
## 👪 Members

| Name    | <center>이승욱</center> | <center>김정환</center> | <center>김지현</center> | <center>오연주</center>
| ------- | --------------------------------------- | --------------------------------------- | --------------------------------------- | --------------------------------------- |
| Profile |<center><img width="110px" height="110px" src="https://github.com/2023SVBootcamp-Team-A/project/assets/8746067/b0476434-30fd-4222-b98d-21178e774189" /></center>|<center><img width="110px" height="110px" src="https://github.com/user-attachments/assets/31cb0b3e-9acb-4d16-99b5-b21ad2d701e7" /></center>|<center><img width="110px" height="110px" src="" /></center>|<center><img width="110px" height="110px" src="" /></center>|
| Role    | <center>주문, 결제<br> 구현</center> | <center></center> | <center></center> | <center></center> |
GitHub | <center>[@josephuk77](https://github.com/josephuk77)</center> | <center>[@jhwan](https://github.com/JeongHwan95) </center>| <center>[@Kim JiHyun](https://github.com/zomeong) </center>| <center>[@Oh YeonJoo](https://github.com/oyeonjoo)</center>
