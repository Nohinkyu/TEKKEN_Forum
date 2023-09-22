# 철권포럼

![KakaoTalk_20230920_090919284](https://github.com/Nohinkyu/TEKKEN_Forum/assets/121728722/36336810-171e-4b3a-8f17-11817bce0f4d)
***
철권 게임 플레이시 필요한 프레임데이터, 영상정보, 채팅을 제공하는 애플리케이션 입니다.

- 원하는 캐릭터의 프레임표 를 확인하고 검색할수 있으며 필요시 동영상 검색을 통해 영상 정보도 확인할 수 있습니다.
- 채팅을 통해 유저간 게임 정보를 공유하고 같이 게임을 플레이할 유저를 찾을수 있습니다.
- 철권8 발매후 8시리즈 캐릭터에 대한 정보도 업데이트할 예정입니다.

# 기술 스택
- Language: Kotlin
- Architecture: MVVM
- Asynchronous: Coroutines + Flow
- Networking: Retrofit, OkHttp3, Moshi
- JetPack: ViewModel, Navigation, DataBinding, Paging
- Image: Coil
- Local DB: Room
- Firebase: Realtime Database, Cloud Messaging, Auth
- Dependency injection: Hilt
- Open Api: KAKAO Video Search

# 주요 기능 소개
|로그인|프레임 데이터 다운로드|캐릭터리스트|
|------|---|---|
|![로그인](https://github.com/Nohinkyu/TEKKEN_Forum/assets/121728722/0ad85b99-bed9-4b79-818e-361dcc76c251)|![프레임데이터-다운로드](https://github.com/Nohinkyu/TEKKEN_Forum/assets/121728722/61e54fb7-cffe-4b64-b561-7c2ef4a63777)|![캐릭터-리스트](https://github.com/Nohinkyu/TEKKEN_Forum/assets/121728722/7b616035-b744-4e63-9542-7584d57d44a8)|
