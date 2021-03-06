# 1. 소개
친구 1명과 같이 진행한 팀프로젝트인 흡연감지 프로젝트입니다.

해당 프로젝트는 <h4> "아두이노 <-> 라즈베리파이 <-> 서버 <-> 클라이언트"</h3> 통신을 진행합니다.

아두이노와 라즈베리파이 같은 경우 블루투스 통신을 합니다.

해당 프로젝트는 메커니즘은 아파트 각 창 틀에 아두이노를 설치, 라즈베리파이는 각 층에 사는 새대주 거실 천장에 설치.

흡연이 감지되면 해당 층에 있는 라즈베리파이로 정보가 전송되게 되며, 전송된 정보는 서버에 저장되고, 흡연감지여부를 알려준다.

각 아파트의 경비실이 클라이언트가 되며, 클라이언트들은 서버에 접속해 실시간으로 흡연감지 여부를 체크하게 된다.

C와 Java간 통신은 Ascii <-> Unicode 통신이므로 Byte통신을 했습니다.

Java같은 경우 BufferedReader를 통해 버퍼에서 데이터를 직접 읽어와 형변환을 통해 Serialize했습니다.

# 2. 구상도
<div>
  <img src="https://user-images.githubusercontent.com/15880397/98201403-eabe3980-1f72-11eb-9b0a-e9c6b9e67de9.PNG" width="70%"></img>
</div>

# 3. 아두이노 제품 스케치
<div>
  <img src="https://user-images.githubusercontent.com/15880397/98201609-63bd9100-1f73-11eb-90d7-83fdaffe4bf2.PNG" width="70%"></img>
</div>

# 4. 개발 환경
* Java : Java SE 8 & Eclipse
* C : GCC & Vim
* Arduino 
  - Software : Arudino IDE 1.6.12
  - Model : Arduino UNO R3
  - Bluetooth : HC-06
  - CO-Sensor : MQ-7
* Raspberry Pi 
  - Model : Pi 2 Model B
  - OS : Raspbian 4.4
  - Bluetooth : CSR 4.0 동글

# 5. 실행 방법 (Window 전용)
1. SmokeSet.exe 을 실행합니다. -> ServerSet.bat 과 ClientSet.bat이 생성됩니다.

2. ServerStartSet.exe 을 실행합니다. -> ServerStart.bat이 생성됩니다.

3. ClientSet.bat을 실행합니다.

4. ServerSet.bat을 실행합니다.

5. MYSQL_BATCH.bat을 실행합니다.

6. ServerStart.bat을 실행합니다.  //서버 실행

7. ClientStart.bat을 실행합니다.  //클라이언트 실행

# 6. 테스트 방법
1. 서버 실행 후 클라이언트를 실행합니다.

2. 서버같은 경우 포트번호는 디폴트 9000으로 셋팅되어 있습니다.

3. 클라이언트가 서버에 접속한 후 모니터링 할 '동'을 선택해 주세요.

4. 예를들어 2동을 선택했다면 서버 콘솔창에 다음과 같이 입력해주면 됩니다.

* 연결 끊김 : 201-302-0

* 흡연 감지 안됨 : 201-302-1

* 흡연 감지 됨 : 201-302-2
