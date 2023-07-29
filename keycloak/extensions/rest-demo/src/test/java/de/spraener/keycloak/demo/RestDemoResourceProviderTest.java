package de.spraener.keycloak.demo;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@Testcontainers
class RestDemoResourceProviderTest {

    @Container
    private static final KeycloakContainer keycloak = new KeycloakContainer()
            .withProviderClassesFrom("build/classes/java/main")
            ;

    @Test
    public void testAnonymousEndpoint() {
        givenSpec()
                .when()
                    .get("hello")
                .then()
                    .statusCode(200)
                    ;
    }

    private RequestSpecification givenSpec() {
        return given().baseUri(keycloak.getAuthServerUrl()).basePath("/realms/master/" + RestDemoResourceProviderFactory.PROVIDER_ID);
    }

}
