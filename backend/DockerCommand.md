

# 처음 실행
`docker compose up --build`

# 코드 수정 후
`docker compose up --build`

# 완전 초기화
`docker compose down -v
docker system prune -a
docker compose up --build`



🚨 자주 하는 실수
docker up build ❌ → 없음
.env 안 맞아서 실행 안됨
포트 충돌 (8080 already in use)
DB 볼륨 남아서 데이터 꼬임