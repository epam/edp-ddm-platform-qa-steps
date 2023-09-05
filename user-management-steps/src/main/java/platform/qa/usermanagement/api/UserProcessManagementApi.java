package platform.qa.usermanagement.api;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;
import static io.restassured.config.LogConfig.logConfig;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import lombok.extern.log4j.Log4j2;
import platform.qa.entities.Service;
import platform.qa.usermanagement.pojo.response.ErrorResponse;

import java.util.HashMap;

@Log4j2
public class UserProcessManagementApi {
    private final RequestSpecification requestSpec;

    public UserProcessManagementApi(Service processMng) {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setConfig(
                        config()
                                .logConfig(logConfig()
                                        .enableLoggingOfRequestAndResponseIfValidationFails()
                                        .enablePrettyPrinting(Boolean.TRUE)))
                .setBaseUri(processMng.getUrl() + "user-process-management/api/")
                .setContentType(ContentType.JSON)
                .addHeader("X-Access-Token", processMng.getUser().getToken())
                .addHeader("X-XSRF-TOKEN", "Token")
                .addHeader("Cookie", "XSRF-TOKEN=Token");

        requestSpec = requestSpecBuilder.build();
    }

    public HashMap startProcess(String key) {
        log.info(String.format("Start process with key: %s", key));
        return given()
                .spec(requestSpec)
                .pathParam("key", key)
                .when()
                .post("/process-definition/{key}/start")
                .then()
                .statusCode(SC_OK).extract().as(HashMap.class);
    }

    public HashMap<String, String> startProcessWithForm(String processDefinitionKey, Object formPayload) {
        ValidatableResponse response = given()
                .spec(requestSpec)
                .pathParam("processDefinitionKey", processDefinitionKey)
                .body(formPayload)
                .when()
                .post("/process-definition/{processDefinitionKey}/start-with-form")
                .then()
                .body("ended", equalTo(true))
                .statusCode(SC_OK);

        log.info(String.format("Start with form for process with key: %s", processDefinitionKey));
        return response.extract().as(HashMap.class);
    }

    public ErrorResponse startProcessWithForm(String processDefinitionKey, Object formPayload, int statusCode) {
        ValidatableResponse response = given()
                .spec(requestSpec)
                .pathParam("processDefinitionKey", processDefinitionKey)
                .body(formPayload)
                .when()
                .post("/process-definition/{processDefinitionKey}/start-with-form")
                .then()
                .statusCode(statusCode);
        log.info(String.format("Start with form for process with key: %s", processDefinitionKey));
        return response.extract().as(ErrorResponse.class);
    }

}
