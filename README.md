# PolicyNoticeAndroidApp
### 복지  • 일자리 API를 활용한 정책서비스 안내 모바일 어플리케이션

* wampserver64 설치하여 phpmyadmin, mysql, Apache를 사용합니다.
* 모든 PHP파일은 C:\wamp64\www 위치에 저장합니다.
* 개발환경 : Android Studio (Java)
* DB 이름 : userinfo
* Table 이름 : user(유저정보), favorite(즐겨찾기), event(캘린더), inquiry(문의)

**Open API**
* 복지정책 : 공공데이터 포털  한국사회보장정보원_중앙부처복지서비스
* 일자리정책 : 워크넷 채용정보 API

**Function**
-|member|non-member
-----|-----|-----|
회원가입|아이디, 비밀번호, 생애주기, 가구유형, 지역|X
맞춤검색|회원가입 시 입력한 정보를 기반으로 메인화면이 맞춤 형식으로 구현|앱을 실행할 때마다 조건설정을 해주어야 함
즐겨찾기|검색 시 추가한 아이템이 표시됨|X
캘린더|개인적으로 추가하는 것 뿐 아니라 즐겨찾기에 추가한 아이템 중 원하는 것만 캘린더에 추가 가능|X
알림|캘린더에 추가된 일정에 한에서 푸시 알림 설정 가능|X
문의|자주묻는질문, 1:1문의, 도움말|자주묻는질문, 1:1문의, 도움말
기타|회원정보수정, 회원탈퇴|X


**user table**
userID|userPass|userLifearray|userTrgterIndvdl|userArea
------|--------|-------|----------|--------|
aaaa|123|청년|한부모,조손|인천
bbbb|456|영유아|다자녀|대구
cccc|789|임신,출산|선택안함|서울

**favorite table**
id|userID|item_name|item_content|servID|CloseDt
--|------|--------|-------|----------|--------|
1|aaaa|늘푸른노인요양원|요양보호사 모집|K131212206130157|22-07-28
2|bbbb|방과후학교자유수강권|방과후학교...|WLF00000867|-
3|cccc|우리동네재가노인복지센터|온산읍...|K131212206130155|22-11-09

**event table**
userID|ID|title|startdate|enddate|alarmactive
--|------|--------|-------|----------|--------|
aaaa|t0001sl7|일정1|2022-06-06|2022-06-14|1
bbbb|3aktMKVW|일정2|2022-06-09|2022-07-14|1

**inquiry table**
userID|type|title|email|content
--|------|--------|-------|----------
aaaa|시스템오류|문의1|aa1234@naver.com|문의내용1
bbbb|앱 개선사항|문의2|bb5678@gmail.com|문의내용2
