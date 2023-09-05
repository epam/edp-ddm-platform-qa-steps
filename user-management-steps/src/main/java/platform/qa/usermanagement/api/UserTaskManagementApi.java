package platform.qa.usermanagement.api;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;
import static io.restassured.config.LogConfig.logConfig;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import lombok.extern.log4j.Log4j2;
import platform.qa.entities.Service;
import platform.qa.usermanagement.pojo.response.CompleteResponse;
import platform.qa.usermanagement.pojo.response.ErrorResponse;

@Log4j2
public class UserTaskManagementApi {
    private final String portal;
    private final RequestSpecification requestSpec;

    public UserTaskManagementApi(Service taskMng, String portal) {
        this.portal = portal;

        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setConfig(
                        config()
                                .logConfig(logConfig()
                                        .enableLoggingOfRequestAndResponseIfValidationFails()
                                        .enablePrettyPrinting(Boolean.TRUE)))
                .setBaseUri(taskMng.getUrl() + "user-task-management/api")
                .setContentType(ContentType.JSON)
                .addHeader("X-Access-Token", taskMng.getUser().getToken())
                .addHeader("X-XSRF-TOKEN", "Token")
                .addHeader("Cookie", "XSRF-TOKEN=Token");

        requestSpec = requestSpecBuilder.build();
    }

    public CompleteResponse completeTaskById(String taskId, Object payload) {
        CompleteResponse response = given()
                .spec(requestSpec)
                .pathParam("taskId", taskId)
                .body(payload)
                .when()
                .post("/task/{taskId}/complete")
                .then()
                .statusCode(SC_OK)
                .extract().as(CompleteResponse.class);
        log.info(String.format("Complete task by taskId: %s", taskId));
        return response;
    }

    public ErrorResponse completeTaskById(String taskId, Object payload, int statusCode) {
        ValidatableResponse response = given()
                .spec(requestSpec)
                .pathParam("taskId", taskId)
                .body(payload)
                .when()
                .post("/task/{taskId}/complete")
                .then()
                .statusCode(statusCode);
        log.info(String.format("Complete task by taskId: %s", taskId));
        return response.extract().as(ErrorResponse.class);
    }

    public ErrorResponse saveTaskById(String taskId, Object payload, int statusCode) {
        ValidatableResponse response = given()
                .spec(requestSpec)
                .pathParam("taskId", taskId)
                .body(payload)
                .when()
                .post("/task/{taskId}/save")
                .then()
                .statusCode(statusCode);
        log.info(String.format("Save task by taskId: %s", taskId));
        return response.extract().as(ErrorResponse.class);
    }

    public Response saveTaskById(String taskId, Object payload) {
        Response response = given()
                .spec(requestSpec)
                .pathParam("taskId", taskId)
                .body(payload)
                .when()
                .post("/task/{taskId}/save");
        log.info(String.format("Save task by taskId: %s", taskId));
        return response;
    }

    public Response getTaskById(String taskId) {
        Response response = given()
                .spec(requestSpec)
                .pathParam("id", taskId)
                .when()
                .get("/task/{id}");
        log.info(String.format("Get task by taskId: %s", taskId));
        return response;
    }

    public void signForm(String taskId, Object payload) {
        given()
                .spec(requestSpec)
                .pathParams("portal", portal, "taskId", taskId)
                .when()
                .body(payload)
                .post("/{portal}/task/{taskId}/sign-form")
                .then()
                .statusCode(SC_NO_CONTENT);

        log.info(String.format("Sign form for %s", portal));
    }

    public ErrorResponse signForm(String taskId, Object payload, int statusCode) {
        ValidatableResponse response = given()
                .spec(requestSpec)
                .pathParams("portal", portal, "taskId", taskId)
                .when()
                .body(payload)
                .post("/{portal}/task/{taskId}/sign-form")
                .then()
                .statusCode(statusCode);

        log.info(String.format("Sign form for %s", portal));
        return response.extract().as(ErrorResponse.class);
    }
}
