# study_spring_rest_api
Inflearn 스프링 REST API 강의 정리

* API
  * Application Programming Interface
* REST
  * **RE**presentational **S**tate **T**ransfer
  * 인터넷 상의 시스템 간의 상호 운용성(interoperability)을 제공하는 방법중 하나
  * 시스템 제각각의 **독립적인 진화**를 보장하기 위한 방법
  * REST API : REST 아키텍처 스타일을 따르는 API
* REST 아키텍처 스타일 [그런 REST API로 괜찮은가](https://www.youtube.com/watch?v=RP_f5dMoHFc)
  * Client-Server
  * Stateless
  * Cache
  * **Uniform Interface**
  * Layered System
  * Code-On-Demand (Optional)
* Uniform Interface (발표 영상 11분 40초)
  * Identification of resources
  * manipulation of resources through representations
  * self-descrive messages
  * hypermedia as the engine of application state (HATEOAS)
* 두 문제를 좀 더 자세히 살펴보자. (발표 영상 37분 50초)  
  * Self-descriptive message
    * 메시지 스스로 메시지에 대한 설명이 가능해야 한다.
    * 서버가 변해서 메시지가 변해도 클라이언트는 그 메시지를 보고 해석이 가능하다.
    * **확장 가능한 커뮤니케이션**
  * HATEOAS
    * 하이퍼미디어(링크)를 통해 애플리케이션 상태 변화가 가능해야 한다.
    * **링크 정보를 동적으로 바꿀 수 있다.** (Versioning 할 필요 없이)
* Self-descriptive message 해결 방법
  * 방법 1 : 미디어 타입을 정의하고 IANA에 등록하고 그 미디어 타입의 리소스 리턴할 때 Content-Type으로 사용한다.
  * **방법 2 : profile 링크 헤더를 추가한다.**
    * 브라우저들이 아직 스펙 지원을 잘 하지 않음.
    * 대안으로 HAL의 링크 데이터에 profile 링크 추가
* HATEOAS 해결 방법
  * 방법 1 : 데이터에 링크 제공
    * 링크를 어떻게 정의 할 것인가 ? : HAL
  * 방법 2 : 링크 헤더나 Location을 제공
* 프로젝트 세팅
    * 의존성
        * Web
        * JPA
        * HATEOAS
        * REST Docs
        * H2
        * PostgreSQL
        * Lombok
    * 자바 버전 11
    * 스프링 부트 핵심 원리
        * 의존성 설정 (pom.xml)
        * 자동 설정 (@EnableAutoConfiguration)
        * 내장 웹 서버 (의존성과 자동 설정의 일부)
        * 독립적으로 실행 가능한 JAR (pom.xml 의 플러그인)
* Event 도메인 구현
    * @EqualsAndHashCode에서 of를 사용하는 이유
        * 모든 필드를 검사할 경우 상호 참초가 일어날 가능성이 존재
    * @Builder를 사용할 때 @AllArgumentsConstructor를 사용하는 이유
        * 모든 필드를 사진 생성자가 public이 아니기 떄문.
        * java bean spec 을 만족하기 위해 기본 생성자도 추가.
    * @Data를 쓰지 않는 이유
        * @Data를 사용할 경우 모든 필드를 참조하는 hashcode및 tostring이 자동으로 설정되기 때문
    * Lombok 애노테이션은 meta 애노테이션이 아니기 때문에 줄여서 사용할 수 없다.
* Event 생성 API 구현 : 비지니스 로직
    * Event 생성 API
        * 입력
            * name
            * description
            * deginEnrollmentDatetime
            * closeEnrollmentDateTime
            * beginEventDateTime
            * endEventDateTime
            * location (optional) 없으면 온라인 모임    
            * basePrice (optional)
            * maxPrice (optional)
            * limitOfEnrollment
        * 결과
            * id
            * ...
            * eventStatus : DRAFE, PUBLISHED, ENROLLMENT_STARTED, ...
            * offline
            * free
            * _links
                * profile (for the self-descriptive message)
                * self
                * publish 
                * ...
        