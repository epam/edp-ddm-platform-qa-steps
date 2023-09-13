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

package platform.qa;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;
import static io.restassured.config.LogConfig.logConfig;
import static org.apache.http.HttpStatus.SC_OK;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import platform.qa.entities.Service;
import platform.qa.entity.ErrorResponse;
import platform.qa.entity.UploadDocumentResponse;

import java.io.File;
import java.nio.file.Files;
import java.util.Map;

@Log4j2
public class DigitalDocumentServiceApi {

    private final RequestSpecification requestSpec;

    public DigitalDocumentServiceApi(Service digitalDocService) {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setConfig(
                        config()
                                .logConfig(logConfig()
                                        .enableLoggingOfRequestAndResponseIfValidationFails()
                                        .enablePrettyPrinting(Boolean.TRUE)))
                .setBaseUri(digitalDocService.getUrl() + "documents")
                .addHeader("x-forwarded-host", digitalDocService.getUrl())
                .addHeader("X-Access-Token", digitalDocService.getUser().getToken())
                .addHeader("X-XSRF-TOKEN", "Token")
                .addHeader("Cookie", "XSRF-TOKEN=Token");

        requestSpec = requestSpecBuilder.build();
    }

    @SneakyThrows
    public UploadDocumentResponse uploadDocument(File file, String processInstanceId, String taskId, String fieldName) {
        log.info("Завантаження документа через форму");
        UploadDocumentResponse response = given()
                .spec(requestSpec)
                .pathParams(Map.of("processInstanceId", processInstanceId, "taskId", taskId, "fieldName", fieldName))
                .contentType("multipart/form-data")
                .multiPart("file", file, Files.probeContentType(file.toPath()))
                .when()
                .post("{processInstanceId}/{taskId}/{fieldName}")
                .then()
                .statusCode(SC_OK)
                .extract()
                .as(UploadDocumentResponse.class);

        log.info(String.format("Документ з id: '%s' було успішно завантажено", response.getId()));
        return response;
    }

    @SneakyThrows
    public ErrorResponse uploadDocument(File file, String processInstanceId, String taskId, String fieldName,
            Integer statusCode) {
        log.info("Завантаження документа через форму з перевіркою на статус код");
        return given()
                .spec(requestSpec)
                .pathParams(Map.of("processInstanceId", processInstanceId, "taskId", taskId, "fieldName", fieldName))
                .contentType("multipart/form-data")
                .multiPart("file", file, Files.probeContentType(file.toPath()))
                .when()
                .post("{processInstanceId}/{taskId}/{fieldName}")
                .then()
                .statusCode(statusCode)
                .extract()
                .as(ErrorResponse.class);
    }
    
    public ErrorResponse uploadDocument(File file, String contentType, String processInstanceId, String taskId,
            String fieldName, Integer statusCode) {
        log.info("Завантаження документа через форму з перевіркою на статус код");
        return given()
                .spec(requestSpec)
                .pathParams(Map.of("processInstanceId", processInstanceId, "taskId", taskId, "fieldName", fieldName))
                .contentType("multipart/form-data")
                .multiPart("file", file, contentType)
                .when()
                .post("{processInstanceId}/{taskId}/{fieldName}")
                .then()
                .statusCode(statusCode)
                .extract()
                .as(ErrorResponse.class);
    }

    public void checkAuth(String path, int statusCode) {
        log.info("Авторизація доступу з перевіркою статус коду");
        given()
                .spec(requestSpec)
                .get(path)
                .then()
                .statusCode(statusCode);
    }

    public byte[] downloadDocumentAsByteArray(String processInstanceId, String taskId, String fieldName, String docId) {
        log.info("Вивантаження документу");
        return given()
                .spec(requestSpec)
                .pathParams(
                        Map.of("processInstanceId", processInstanceId, "taskId", taskId, "fieldName", fieldName,
                                "documentId", docId))
                .get("{processInstanceId}/{taskId}/{fieldName}/{documentId}")
                .then()
                .extract()
                .asByteArray();
    }

    public ErrorResponse downloadDocument(String processInstanceId, String taskId, String fieldName, String docId,
            Integer statusCode) {
        log.info("Вивантаження документу з перевіркою на статус код");
        return given()
                .spec(requestSpec)
                .pathParams(
                        Map.of("processInstanceId", processInstanceId, "taskId", taskId, "fieldName", fieldName,
                                "documentId", docId))
                .get("{processInstanceId}/{taskId}/{fieldName}/{documentId}")
                .then()
                .statusCode(statusCode)
                .extract().as(ErrorResponse.class);
    }
}