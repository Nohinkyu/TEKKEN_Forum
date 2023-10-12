# 철권포럼

![KakaoTalk_20230920_090919284](https://github.com/Nohinkyu/TEKKEN_Forum/assets/121728722/36336810-171e-4b3a-8f17-11817bce0f4d)
***
철권 게임 플레이시 필요한 프레임데이터, 영상정보, 채팅을 제공하는 애플리케이션 입니다.

- Store : <https://play.google.com/store/apps/details?id=com.nik.tkforum>
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
|---|---|---|
|![로그인](https://github.com/Nohinkyu/TEKKEN_Forum/assets/121728722/103fb9e6-16ab-4c49-9e4f-83ab0e63d1fe)|![프레임데이터-다운로드](https://github.com/Nohinkyu/TEKKEN_Forum/assets/121728722/30bc3995-8c26-4dd4-b878-37b6bf370b4f)|![캐릭터-리스트](https://github.com/Nohinkyu/TEKKEN_Forum/assets/121728722/24b165b7-8e78-492d-bc99-04f373b02d79)|


|프레임 데이터 검색|메뉴얼|동영상 검색|
|---|---|---|
|![프레임-데이터-검색](https://github.com/Nohinkyu/TEKKEN_Forum/assets/121728722/ed0a1504-8b56-4302-9256-b4b37b6ee3dd)|![메뉴얼](https://github.com/Nohinkyu/TEKKEN_Forum/assets/121728722/3ac1c7f1-4f4b-4389-9198-9464ff974b2f)|![동영상검색](https://github.com/Nohinkyu/TEKKEN_Forum/assets/121728722/97c4b74c-3743-4609-8c02-3c0f6a8646ac)|


|채팅|프레임 데이터 다시받기|
|---|---|
|![채팅](https://github.com/Nohinkyu/TEKKEN_Forum/assets/121728722/f3eb3af9-a0b5-41a2-bd60-1257896001ec)|![프레임데이터-다시-다운로드](https://github.com/Nohinkyu/TEKKEN_Forum/assets/121728722/34a9d6eb-155f-4c18-84a9-103ea8c70deb)|


|알림권한|FCM|
|---|---|
|![알람-허용](https://github.com/Nohinkyu/TEKKEN_Forum/assets/121728722/6700e6e7-d33b-4f7a-bad8-a04da6b50e2e)|![fcm](https://github.com/Nohinkyu/TEKKEN_Forum/assets/121728722/f8c2cffc-30ac-487c-948e-99ce993681c2)|


# v1.0.0
- 현재 프레임 데이터 업로드 중입니다.
- 추후 업로드 완료후 알림으로 알려드리겠습니다.
- 8 출시후 추가적으로 프레임 데이터를 업로드 할 예정입니다.
