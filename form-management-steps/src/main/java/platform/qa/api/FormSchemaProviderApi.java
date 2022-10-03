/*
 * Copyright 2022 EPAM Systems.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package platform.qa.api;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;
import static io.restassured.config.LogConfig.logConfig;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.log4j.Log4j2;
import platform.qa.entities.Service;

import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.message.ParameterizedMessage;

@Log4j2
public class FormSchemaProviderApi {
    private final RequestSpecification requestSpec;
    private static final String CREATE_FORM_ENDPOINT = "/forms";
    private static final String GET_FORM_BY_NAME_ENDPOINT = "/forms/{formName}";
    private static final String PUT_FORM_BY_NAME_ENDPOINT = "/forms/{formName}";

    public FormSchemaProviderApi(Service formSchema) {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setConfig(
                        config()
                                .logConfig(logConfig()
                                        .enableLoggingOfRequestAndResponseIfValidationFails()
                                        .enablePrettyPrinting(Boolean.TRUE)))
                .setBaseUri(formSchema.getUrl() + "/api")
                .addHeader("Content-Type", "application/json")
                .addHeader("X-Access-Token", formSchema.getUser().getToken())
                .addHeader("X-XSRF-TOKEN", "Token")
                .addHeader("Cookie", "XSRF-TOKEN=Token");

        requestSpec = requestSpecBuilder.build();
    }

    public Map getFormByName(String formName) {
        log.info(new ParameterizedMessage("Отримання форми за name: {}", formName));
        Response response = given()
                .spec(requestSpec)
                .get(GET_FORM_BY_NAME_ENDPOINT, formName)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON)
                .extract()
                .response();
        return response.body().as(Map.class);
    }

    public Response getSearchFormByName(String formName) {
        log.info(new ParameterizedMessage("Отримання форми за name: {}", formName));
        return given()
                .spec(requestSpec)
                .get(GET_FORM_BY_NAME_ENDPOINT, formName);
    }

    public void deleteForm(String formName) {
        log.info(new ParameterizedMessage("Видалення форми за name: {}", formName));
        given()
                .spec(requestSpec)
                .delete(GET_FORM_BY_NAME_ENDPOINT, formName)
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
        log.info("Form was deleted: " + formName);
    }

    public void createForm(Object payload) {
        log.info("Створення форми");
        given()
                .spec(requestSpec)
                .contentType(ContentType.JSON)
                .body(payload)
                .post(CREATE_FORM_ENDPOINT)
                .then()
                .statusCode(HttpStatus.SC_CREATED);
    }

    public void createForm(List<String> formFileLines) {
        log.info("Створення форми");
        given()
                .spec(requestSpec)
                .when()
                .body(StringUtils
                        .join(formFileLines, StringUtils.SPACE))
                .post(CREATE_FORM_ENDPOINT)
                .then()
                .statusCode(HttpStatus.SC_CREATED);
    }

    public void createFormToObject(List<String> formFileLines) {
        log.info("Створення форми");
        given()
                .spec(requestSpec)
                .when()
                .body(StringUtils
                        .join(formFileLines, StringUtils.SPACE))
                .post(CREATE_FORM_ENDPOINT)
                .then()
                .statusCode(HttpStatus.SC_CREATED);
    }

    public Response sendPostForm(Object payloadForm) {
        log.info("Створення форми");
        return given()
                .spec(requestSpec)
                .contentType(ContentType.JSON)
                .body(payloadForm)
                .post(CREATE_FORM_ENDPOINT);
    }

    public Response sendPutForm(Object payloadForm, String formName) {
        log.info("Оновлення форми");
        return given()
                .spec(requestSpec)
                .contentType(ContentType.JSON)
                .body(payloadForm)
                .put(PUT_FORM_BY_NAME_ENDPOINT, formName);
    }

    public void sendDeleteForm(String formName) {
        log.info(new ParameterizedMessage("Видалення форми за name: {}", formName));
        given()
                .spec(requestSpec)
                .delete(GET_FORM_BY_NAME_ENDPOINT, formName);
        log.info("Form was deleted: " + formName);
    }
}
