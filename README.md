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
    * Test Code
        * 스프링 부트 슬라이스 테스트
            * @WebMvcTest
                * MockMvc 빈을 자동 설정 해준다. 따라서 그냥 가져와서 쓰면 됨.
                * 웹 관련 빈만 등록해 준다. (슬라이스)
        * MockMvc
            * 스프링 MVC 테스트 핵심 클래스
            * 웹 서버를 띄우지 않고도 스프링 MVC (DispatcherServlet)이 요청을 처리하는 과정을 확인 할 수 있기 때문에 컨트롤러 테스트용으로 자주 쓰임.
        * 테스트 할 것
            * 입력값들을 전달하면 JSON 응답으로 201이 나오는지 확인.
                * Location 헤더에 생성된 이벤트를 조회할 수 있는 URI가 담겨 있는지 확인.
                * id는 DB에 들어갈 때 자동생성된 값으로 나오는지 확인
            * 입력값으로 누가 id나 eventStatus, offline, free 이런 데이터까지 같이 주면?
                * Bad_Request로 응답 vs 받기로 한 값 이외는 무시
            * 입력 데이터가 이상한 경우 Bad_Request로 응답
                * 입력값이 이상한 경우 에러
                * 비즈니스 로직으로 검사할 수 있는 에러
                * 에러 응답 메시지에 에러에 대한 정보가 있어야 한다.
            * 비즈니스 로직 적용 됐는지 응답 메시지 확인
                * offline과 free 값 확인
            * 응답에 HATEOA와 profile 관련 링크가 있는지 확인.
                * self (view)
                * update (만든 사람은 수정할 수 있으니까)
                * events (목록으로 가는 링크)
            * API 문서 만들기
                * 요청 문서화
                * 응답 문서화
                * 링크 문서화
                * profile 링크 추가
    * 201 응답 받기
        * @RestController
            * @ResponseBody를 모든 메소드에 적용한 것과 동일하다.
        * ResponseEntity를 사용하는 이유
            * 응답 코드, 헤더, 본문 모두 다루기 편한 API
        * Location URI 만들기
            * HATEOAS가 제공하는 linkTo(), methodOn() 사용
        * 객체를 JSON으로 변환
            * ObjectMapper 사용
        * 테스트 할 것 
            * 입력 값들을 전달하면 JSON 응답으로 201이 나오는지 확인
                * Location 헤더에 생성된 이벤트를 조회 할 수 있는 URI가 담겨 있는지 확인
                * id는 DB에 들어갈 때 자동생성된 값으로 나오는지 확인
    * EventRepository 구현
        * 스프링 데이터 JPA 
            * JpaRepository 상속 받기
        * Enum JPA 맵핑시 주의
            * @Enumerated(EnumType.STRING)
        * @MockBean
            * Mokito를 사용해서 mock객체를 만들고 빈으로 등록
            * 기존 빈을 테스트용 빈이 대체
        * 테스트 할것
            * 입력값들을 전달하면 201 응답이 나오는지 확인
                * Location 헤더에 생성된 이벤트를 조회할 수 있는 URI가 담겨 있는지 확인
                * id는 DB에 들어갈 때 자동생성된 값으로 나오는지 확인
    * 입력값 제한하기
        * 입력값 제한
            * id 또는 입력 받은 데이터로 계산해야 하는 값들은 입력을 받지 않아야 한다.
            * EventDto 적용
        * DTO -> 도메인 객체로 값 복사
            * ModelMapper 사용
        * 통합 테스트로 전환
            * @WebMvcTest빼고 다음 애노테이션 추가
               *  @SpringBootTest
               * @AutoconfigureMockMvc
            * Repository @MockBean 코드 제거
        * 테스트 할 것
            * 입력값으로 누가 id나 eventStatus, offline, free이런 데이터까지 같이 주면 ?
                * Bad_Request 응답 vs 값 무시
    * 입력값 이외에 에러 발생
        * ObjectMapper 커스터마이징
            * spring.jackson.deserialization.fail-on-unknown-properties=true
        * 테스트 할 것
            * 입력값으로 Dto에 정의되어 있지 않은 값을 주면
                * Bad_Request 응답 또는 받기로 한 값 무시.
    * Bad Request 처리하기
        * @Valid와 BindingResult (또는 Errors)
            * BindingResult는 항상 @Valid 바로 다음 인자로 사용해야한다. (스프링 MVC)
            * @NotNull @NotEmpty @Min @Max 사용해서 입력값 바인딩할 때 에러 확인할 수 있음
        * 도메인 Validator 만들기
            * Validator 인터페이스 사용하기
            * 없이 만들어도 상관없음
        * 테스트 설명 용 인터페이스 만들기
            * @Target @Retention
        * 테스트 할 것
            * 입력 데이터가 이상한 경우 Bad_Request로 응답
    * Bad Request 응답 본문 만들기
        * 커스텀 JSON Serializer 만들기 
            * extends JsonSerializer<T> (Jackson JSON 제공) 
                * 해당 클래스를 상속받아 Serializer로 등록 해두면 등록한 타입을 Serialize할때 자동으로 사용한다.
            * @JsonComponent (스프링 부트 제공)
        * BindingError
            * FieldError 와 GlobalError (ObjectError)가 있음
            * objectName
            * defaultMessage
            * code
            * field
            * rejectedValue
* 스프링 HATEOAS
    * 스프링 HATEOAS 소개
        * https://docs.spring.io/spring-hateoas/docs/current/reference/html/
        * 링크 만드는 기능
            * 문자열 가지고 만들기
            * 컨트롤러와 메소드로 만들기
        * 리소스 만드는 기능
            * 리소스 : 데이터 + 링크
        * 링크 찾아주는 기능
            * Traversion
            * LinkDiscoverers
        * 링크
            * HREF
            * REL
                * self
                * profile
                * update-event
                * query-event
    * 스프링 HATEOAS 적용
        * EventResource 만들기
            * extends ResourceSupport -> RepresentaionModel의 문제
                * JsonUnwrapped 로 해결
                * extends EntityModel로 해결
* 스프링 REST Docs 소개
    * REST Docs 자동 설정
        * @AutoConfigureRestDocs
    * REST Docs 코딩
        * andDo(document("doc-name", snippets))
        * snippets
            * links()
            * requestParameters() + parameterWithName()
            * pathParameters() + parametersWithName()
            * requestParts() + partWithname()
            * requestPartBody()
            * requestPartFields()
            * requestHeaders() + headerWithName()
            * requestFields() + fieldWithPath()
            * responseHeaders() + headerWithName()
            * responseFields() + fieldWithPath()
            * ...
        * Relaxed
        * Processor
            * preprocessRequest(prettyPrint())
            * preprocessResponse(prettyPrint())
            * ...
    * 문서 생성하기
        * mvn package
            * test
            * prepare-package :: process-asciidoc
            * prepare-package :: copy-resources
        * 문서 확인
            * /docs/index.html
    * Constraint
        * https://github.com/spring-projects/spring-restdocs/blob/v2.0.2.RELEASE/samples/rest-notes-spring-hateoas/src/test/java/com/example/notes/ApiDocumentation.java
    * RestDockMockMvc 커스터마이징
        * RestDocsMockMvcConfigurationCustomizer 구현한 빈 등록
        * @TestConfiguration
    * 스프링 REST Docs 적용
        * REST Docs 자동 설정
            * @AutoConfigureRestDocs
        * RestDocsMockMvc 커스터마이징 // Request, Response Pretty
            * RestDocsMockMvcConfigurationDustomizer 구현한 빈 등록
            * @TestConfiguration 
            * @Import
    * 스프링 REST Docs : 링크, (Req, Res) 필드와 헤더 문서화
        * 요청 필드 문서화
            * requestFields() + fieldWithPath()
            * responseFields() + fieldWithPath()
            * requestHeaders() + headerWithName()
            * responseHeaders() + headerWithName()
            * links() + linkWithRel()
        * Relaxed 접두어
            * 장점 : 문서일부분만 테스트 할 수 있다.
            * 단점 : 정확한 문서를 생성하지 못한다.
    * 문서 빌드
        * 스프링 REST Docs
            * https://docs.spring.io/spring-restdocs/docs/2.0.2.RELEASE/reference/html5/
            * pom.xml에 메이븐 플러그인 설정
        * 템플릿 파일 추가
            * src/main/asciidoc/index.adoc
        * 문서 생성하기
            * mvn package
                * test
                * prepare-package :: process-asciidoc
                * prepare-package :: copy-resources
        * 문서 확인
            * /docs/index.html
* PostgreSQL 적용
    * 테스트 할 때는 H2 사용, 실제 애플리케이션 서버를 실행할 때 PostgreSQL을 사용하도록 변경.
    * 애플리케이션 설정과 테스트 설정 중복 어떻게 줄일 것인가?
        * 프로파일과 @ActiveProfiles 활용
* 인덱스 핸들러 만들기
    * 다른 리소스에 대한 링크 제공
    * 문서화
* Event 목록 조회 API
    * 페이징, 정렬 
        * 스프링 데이터 JPA가 제공하는 Pageable
    * Page<Event> 안에 들어있는 Event들은 리소스로 어떻게 변경 ?
        * 하나씩 순회하면서 직접 EventResource로 맵핑?
        * PageResourceAssembler<T> 사용
    * 테스트 시 Pageable 파라미터 제공법
        * page : 0부터 시작
        * size : 기본값 20
        * sort : property,property(,ASC|DESC)
    * 1건 조회 API 추가.
        * 조회 대상이 없을시 NotFound Return    
* 테스트 코드 리펙토링
    * 여러 컨트롤러 간의 중복 코드 제거
        * 클래스 상속 사용
        * @Ignore 애노테이션으로 테스트로 간주되지 않도록 설정
* Account 도메인 추가
    * OAuth2 인증을 하기위해 Account 생성
        * id
        * email
        * password
        * roles
    * AccountRoles
        * ADMIN, USER
    * JPA enumeration collection mapping
        ```java
            @ElementCollection    
            @Enumerated(EnumType.STRING)
            private Set<AccountRole> roles;
        ```
* 스프링 시큐리티
    * 웹 시큐리티 (filter 기반 시큐리티)
    * 메소드 시큐리티
    * 이 둘 다 Security Interceptor를 사용
        * 리소스에 접근을 허용할 것이냐 말것이냐 결정 로직이 들어있다.
* 예외 테스트
    * @Test(expected)
        * 예외 타입만 확인이 가능함.
    * try-catch
        * 예외 타입과 메시지 확인 가능 하지만 코드가 다소 복잡
    * @Rule ExpectedException
        * 코드는 간결하면서 예외 타입과 메시지 모두 확인 가능.
* 스프링시큐리티 기본 설정
    * 시큐리티 필터 적용하지않음
        * /docs/index.html
    * 로그인 없이 접근 가능
        * GET /api/events
        * GET /api/events/{id}
    * 로그인 해야 접근 가능
        * 나머지
        * POST /api/events
        * PUT /api/events/{id}
    * 스프링 시큐리티 OAuth2.0
        * AuthorizationSever : OAuth2 토큰 발행(/oauth/token) 및 토큰 인증(/oauth/authorize)
            * Order 0 (리소스 서버 보다 우선 순위가 높다)
        * ResourceServer : 리소스 요청 인증 처리 (OAuth 2 토큰 검사)
            * Order 3 (이 값은 현재 고칠 수 없음)
    * 스프링 시큐리티 설정
        * @EnableSebSecurity
        * @EnableGlobalMethodSecurity
        * extends WebSecurityConfigureAdaptor
        * PasswordEncoder : PasswordEncoderFactories.createDelegatingPasswordEncoder()
        * TokenStore : InmemoryTokenStore
        * AuthenticationManagerBean
        * configure(AuthenticationManagerBuilder auth)
            * userDetailsService
            * passwordEncoder
        * configure(HttpSecurity http)
            * /docs/** permitAll()
        * configure(WebSecurity web)
            * ignore
                * /docs/**
                * /favicon.ico
        * PathRequest.toStaticResources() 사용하기
* 스프링 시큐리티 OAuth2 설정 : 인증서버 설정
    * 토큰 발행 테스트
        * User
        * Client
        * POST /oauth/token
            * HTTO Basic 인증 헤더 (클라이언트 아이디 + 클라이언트 시크릿)
            * 요청 매개변수
                * grant_type : password
                * username
                * password
            * 응답에 access_token 나오는지 확인
    * Grant Type : password
        * Grant Type : 토큰 받아오는 방법
        * 서비스 오너가 만든 클라이언트에서 사용하는 Grant Type
    * AuthorizationServer 설정
        * @EnableAuthorizationServer
        * extends AuthorizationServerCofingureAdaptor
        * configure(AuthorizationServerSecurityConfigurer security)
            * passwordEncode 설정
        * configure(ClientDetailsServiceConfigurer clients)
            * 클라이언트 설정
            * grantTypes
                * password
                * refresh_token
            * scopes
            * secret / name
            * accessTokenValiditySeconds
            * refreshTokenValiditySeconds
        * AuthorizationServerEndpointsConfigurer
            * tokenStore
            * authenticationManager
            * userDetailsService
* 스프링 시큐리티 OAuth2 설정 : 리소스 서버 설정
    * 리소스서버는 API서버와 함께 있어야 하는게 맞다. 요청시에 토큰을 검사해야 하기 때문.
    * ResourceServer 설정
        * @EnableResourceServer
        * extends ResourceServerConfigurerAdaptor
        * configurer(ResourceServerSecurityConfigurer resources)
            * 리소스 ID
        * configurer(HttpSecurity http)
            * anonymous
            * GET /api/** permitAll()
            * POST /api/** authenticated
            * PUT /api/** authenticated
            * 에러처리
                * accessDeniedHandler(OAuth2AccessDeniedHandler())
* 외부 설정으로 기본 유저 및 클라이언트 정보 빼내기
    * @ConfigurationProperties
        * spring-boot-configuration-processor dependency 추가
* 스프링 시큐리티 현재 사용자
    * Securitycontext
        * 자바 ThreadLocal 기반 구현으로 인증 정보를 담고 있다.
        * 인증 정보 꺼내는 방법 :
            * Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    * @AuthenticationPrincipal.spring.security.User.user
        * 인증 안한 경우엔 null
        * 인증 한 경우에는 username과 authorities 참조 가능
    * spring.security.User를 상속 받는 클래스를 구현하면
        * 도메인 User를 받을 수 있다.
        * @AuthenticationPrincipal me.study.usesr.UserAdaptor
        * Adaptor.getUser().getId()
    * SpEL을 사용하면
        * @AuthenticationPrincipal(expression="account") me.study.account.Account
    * 커스텀 애노테이션을 만들면 
        * @CurrentUser Account account
        * 인증을 안하고 접근시 
        * expression = '#this == 'anoymousUser' ? null : account'
            * 현재 인증 정보가 anoymousUser인 경우엔 null 아닐시 account 리턴
* Event API 개선 : 출력값 제한하기
    * JsonSerializer<User> 구현       
    * @JsonSerialize(using) 설정
        * @JsonCompponet 를 사용하지 않는다
                            