디렉토리 구조 
app/src/main/java/com/example/siheung_seemoney/
│
├── HomeActivity.kt
│
├── ui/                  # 화면
│   ├── home/            # 메인 (예산/부채 카드)
│   ├── analysis/        # 그래프 분석
│   ├── news/            # 뉴스
│   ├── participation/   # 투표/제안
│   └── mypage/          # 마이페이지
│
├── viewmodel/           # 화면 상태 관리
│   ├── HomeViewModel.kt
│   ├── AnalysisViewModel.kt
│   ├── NewsViewModel.kt
│   └── ParticipationViewModel.kt
│
├── data/                # 데이터 계층 (핵심)
│   ├── model/           # DTO (서버 응답)
│   ├── remote/          # API
│   └── repository/      # 데이터 중개
│
├── adapter/             # RecyclerView
│
├── util/                # 공통 함수
│
└── base/                # 공통 Activity/Fragment (선택)