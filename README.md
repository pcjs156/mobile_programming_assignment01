# mobile_programming_assignment01
### 2021-2 국민대학교 소프트웨어학부 모바일프로그래밍 개인 과제 by 소프트웨어학부 20181615 박유빈

- SDK Version: Android 11(R)
- 앱 설치 후 최초 실행시 데이터베이스와 이미지 파일을 준비하는 과정으로 인해 실행이 느릴 수 있습니다.

-----------------------

## 기능 구현 사항 
- **로그인 및 회원가입 기능**: 입력 Form 액티비티를 통해 회원가입을 수행하며, 각 필드의 유효성 검증을 수행합니다. 마지막으로 로그인된 계정의 정보를 Preference를 사용해 로그인 form에 저장해 놓습니다.
- **게스트 로그인 기능**: ID, PW 입력 없이 로그인할 수 있으나, 상품 조회 이외의 다른 기능은 사용할 수 없도록 제한했습니다.
- **제품 조회 기능**: SQLite와 연동하여 상품 이미지의 경로와 상품명을 읽어들여 ListView로 이를 화면에 보여줍니다.
- **제품 추가 기능**: 앱 최초 실행시 정적 파일로 저장된 임의의 이미지 중 하나를 선택하여 DB에 제품 정보로써 추가할 수 있습니다.
- **제품 삭제 기능**: 삭제 버튼을 누르면 삭제 모드에 진입하며, 각 제품을 클릭하면 데이터베이스와 화면에서 해당 제품이 제거됩니다.
- **회원 정보 조회 기능**: 게스트는 접근할 수 없으며, 회원인 경우 제품 조회 페이지 우측 하단에 로그인한 사용자의 ID가 노출됩니다. 해당 버튼을 클릭하면 회원가입시 입력한 정보가 Dialog로 보여집니다.