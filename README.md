[![License: LGPL v3](https://img.shields.io/badge/License-LGPL%20v3-blue.svg)](https://www.gnu.org/licenses/lgpl-3.0)
[![Korean](https://img.shields.io/badge/language-Korean-blue.svg)](#korean)


<a name="korean"></a>

BuilderValidator-v1.0-egv<br>(공간자료 검수편집도구 v1.0 전자정부프레임워크 기반)
=======
- 이 프로젝트는 [Validator v1.0](https://github.com/ODTBuilder/Validator-v1.0)의 전자정부 호환성인증 버전입니다.<br>
- Web기반의 공간정보 검수편집을 지원하며 전자정부 표준프레임워크 3.7에 대한 호환(레벨2)성 인증을 받았습니다.<br>
- 국토공간정보연구사업 중, [공간정보 SW 활용을 위한 오픈소스 가공기술 개발]과제의 단위성과물입니다. <br>
<p align="center"><img src="https://user-images.githubusercontent.com/13480171/50678152-b14bd880-1040-11e9-8ead-cf99ca1d4579.PNG" width="30%"> </p>


## 1. 연구기관
- 세부 책임 : 부산대학교 <link>http://www.pusan.ac.kr/<br>
- 연구 책임 : 국토연구원 <link>http://www.krihs.re.kr/

## 2. Architecture
<p align="center"><img src="https://user-images.githubusercontent.com/13480171/50677303-73e54c00-103c-11e9-91b9-180f9d7f333e.png" width="65%"></p><br>

## 3. Getting Started
### 3.1 환경 ###
- 개발언어	JAVA / java-1.8.0-openjdk-1.8.0.111-3(JAVA 1.8 문제없음)<br>
-	웹 서버/ 컨테이너	Tomcat / v8.5<br>
-	웹 프레임워크	전자정부 표준프레임워크 / 3.7.0<br>
-	공간정보 데이터 서버	Geoserver / 2.7.6<br>
-	공간정보 데이터베이스	PostgreSQL / 9.4, PostGIS / 2.1<br>
- 클라이언트	개발언어	HTML / 5, JavaScript / ECMA 5<br>
-	웹 프레임워크	jQuery / 2.2.2<br><br>

### 3.2 사전설치 및 DB생성 ###
- PostgreSQL, PostGIS, Geoserver설치<br>
-	Database 및 테이블생성(설정포함)<br>
- 샘플데이터 insert<br>
- Geoserver 설정<br><br>

### 3.3 프로젝트 빌드 ###
- 프로젝트 추가<br>
- 프로젝트 설정<br>
- Maven 설정 및 빌드<br>

자세한내용은 [OpenGDS(Builder-Validator) 전자정부 호환성 연동가이드](https://github.com/ODTBuilder/BuilderValidator-v1.0-egv/blob/master/OpenGDS(Builder-Validator)%20%EC%A0%84%EC%9E%90%EC%A0%95%EB%B6%80%20%ED%98%B8%ED%99%98%EC%84%B1%20%EC%97%B0%EB%8F%99%EA%B0%80%EC%9D%B4%EB%93%9C%20.pdf) 을 참조하십시오.

### 4. 사용 라이브러리 ###
1. jQuery 2.2.2 (MIT License, CC0) http://jquery.com/
2. jQuery UI 1.11.4 (MIT License & GPL License, this case MIT License), start theme. http://jqueryui.com/
3. GeoTools 16.5 (LGPL) http://www.geotools.org/
4. ApachePOI 3.14 (Apache License 2.0) http://poi.apache.org
5. ApacheCommons 1.3.3 (Apache License 2.0) commons.apache.org/proper/commons-logging/
6. JACKSON 1.9.7 (Apache License (AL) 2.0, LGPL 2.1)
7. JSON 20160212 (MIT License)
8. Openlayers3 3.13.0 (FreeBSD) www.openlayers.org
9. Spectrum 1.8.0 (MIT) http://numeraljs.com/
10. Bootstrap v3.3.2 (MIT) http://getbootstrap.com
11. JSTS (EPL) http://bjornharrtell.github.io/jsts/

## 5. Mail

Developer : SG.LEE
ghre55@git.co.kr
