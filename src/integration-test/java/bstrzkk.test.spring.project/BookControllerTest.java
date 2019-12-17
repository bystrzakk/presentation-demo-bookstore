package bstrzkk.test.spring.project;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static bstrzkk.test.spring.project.utils.TestUtils.getJsonBodyFromFile;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@ActiveProfiles(profiles = "test")
@SpringBootTest(classes = BookStoreSpringbootApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {

    protected static final String HOST = "http://localhost";

    private static WireMockRule wireMockRule = new WireMockRule(9898);

    @LocalServerPort
    private Integer localServerPort;

    @BeforeAll
    public static void setupWiremockAndStubs() {
        wireMockRule.start();
        configureFor("localhost", wireMockRule.port());
        stubCreateUser();
        stubGetUser();
        stubCreateUser();
        stubDefaultMapping();
    }

    @AfterAll
    public static void shutDownWireMock(){
        wireMockRule.stop();
    }

    private static void stubGetUser() {
        stubFor(get(urlEqualTo("/api/users/2"))
                //urlMatching("/api/.*")   - regex
                //urlPathEqualTo("/api/user/2")  - only path
                //urlPathMatching("/api/.*")  -  only path with regex
                .willReturn(aResponse()
                        .withStatus(OK.value())
                        .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                        .withBody(getJsonBodyFromFile("src/integration-test/resources/files/response/user_id_2_response.json"))));
    }

    private static void stubCreateUser() {
        final String jsonBodyFromFile = getJsonBodyFromFile("src/integration-test/resources/files/request/create_user_request.json");
        stubFor(post(urlEqualTo("/api/users"))
                .inScenario("Retry Scenario")
                .withRequestBody(equalToJson(jsonBodyFromFile))
                .willReturn(aResponse()
                        .withStatus(CREATED.value())
                        .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                        .withBody(jsonBodyFromFile))
                .willSetStateTo("Cause Success"));

        stubFor(post(urlEqualTo("/api/users"))
                .inScenario("Retry Scenario")
                .whenScenarioStateIs("Cause Success")
                .withRequestBody(equalToJson(jsonBodyFromFile))
                .willReturn(aResponse()
                        .withStatus(OK.value())
                        .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                        .withBody(jsonBodyFromFile)));
    }

    private static void stubDefaultMapping() {
        stubFor(any(anyUrl())
                .atPriority(10)
                .willReturn(aResponse()
                        .withStatus(404)
                        .withBody("{\"status\":\"Error\",\"message\":\"Endpoint not found\"}")));
    }

    @Test
    @DisplayName("Should return status code: 200 [OK] and valid Response message contains details about user with id 2")
    public void testExternalCallForUser() {
        given()
                .pathParam("id", 2)
                .when()
                .get(HOST + ":" + localServerPort + "/users/{id}")
                .then()
                .body("id", equalTo(2))
                .body("email", equalTo("janet.weaver@reqres.in"))
                .body("first_name", equalToIgnoringCase("Janet"))
                .body("last_name", equalToIgnoringCase("Weaver"))
                .log().body()
                .statusCode(OK.value());
    }

    @Test
    @DisplayName("Should return status code: 404 [NOT_FOUND] when called endpoint is not mapped")
    public void testDefaultMapping() {
        given()
                .when()
                .get(HOST + ":" + localServerPort + "/unmapped/address")
                .then()
                .body("status", equalTo(NOT_FOUND.value()))
                .body("error", equalToIgnoringCase(NOT_FOUND.getReasonPhrase()))
                .body("message", equalToIgnoringCase("No message available"))
                .body("path", equalToIgnoringCase("/unmapped/address"))
                .statusCode(NOT_FOUND.value());
    }

    @Test
    @DisplayName("Should return status code: 201 [CREATED] and 200 [OK] on second call with the same data")
    public void testCreateNewUser() {
        final String jsonBodyFromFile = getJsonBodyFromFile("src/integration-test/resources/files/request/create_user_request.json");

        given()
                .contentType(APPLICATION_JSON_VALUE)
                .body(jsonBodyFromFile)
                .when()
                .post(HOST + ":" + localServerPort + "/users")
                .then()
                .log().body()
                .statusCode(CREATED.value());

        given()
                .contentType(APPLICATION_JSON_VALUE)
                .body(jsonBodyFromFile)
                .when()
                .post(HOST + ":" + localServerPort + "/users")
                .then()
                .log().body()
                .statusCode(OK.value());
    }
}
