package bstrzkk.test.spring.project;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@ActiveProfiles(profiles = "test")
@SpringBootTest(classes = BookStoreSpringbootApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {

    protected static final String HOST = "http://localhost";

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(9898);

    @LocalServerPort
    private Integer localServerPort;

    private String getFixtureAsString(String fixtureName) throws IOException {
        return Files.asCharSource(new File(fixtureName), Charsets.UTF_8).read();
    }

    protected String getJsonBodyFromFile(String pathToJsonFile) {
        String body = null;
        try {
            body = getFixtureAsString(pathToJsonFile);
        } catch (IOException e) {
            log.warn("There was a problem retrieving json response data from the source file! [{}]", pathToJsonFile, e);
        }
        return body;
    }

    @BeforeEach
    public void setup() {
        wireMockRule.start();
        WireMock.configureFor("localhost", wireMockRule.port());
        stubFor(get(urlMatching("/api/.*"))
                //get(urlEqualTo("/api/users/2"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                        .withBody(getJsonBodyFromFile("src/integration-test/resources/files/user_id_2_response.json"))));
    }

    @Test
    @DisplayName("Should return status code: 200 [OK] and valid Response message contains details about user with id 2")
    public void testScreeningCompanyByDunsNumber() {
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
}
