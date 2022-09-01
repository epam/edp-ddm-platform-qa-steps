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
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static platform.qa.constants.Constants.COOKIE_HEADER_NAME;
import static platform.qa.constants.Constants.COOKIE_HEADER_VALUE;
import static platform.qa.constants.Constants.XSRF_HEADER_NAME;
import static platform.qa.constants.Constants.XSRF_HEADER_VALUE;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.extern.log4j.Log4j2;
import platform.qa.entities.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.message.ParameterizedMessage;

/**
 * Implement abilities to manipulate decision definitions in camunda
 */
@Log4j2
public class DecisionDefinitionApi {

    private final RequestSpecification requestSpec;

    public DecisionDefinitionApi(Service bpms) {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setConfig(
                        config()
                                .logConfig(logConfig()
                                        .enableLoggingOfRequestAndResponseIfValidationFails()
                                        .enablePrettyPrinting(Boolean.TRUE)))
                .setBaseUri(bpms.getUrl())
                .setContentType(ContentType.JSON)
                .addHeader("X-Access-Token", bpms.getUser().getToken())
                .addHeader(XSRF_HEADER_NAME, XSRF_HEADER_VALUE)
                .addHeader(COOKIE_HEADER_NAME, COOKIE_HEADER_VALUE);

        requestSpec = requestSpecBuilder.build();
    }

    public Map<Object, Object> getDecisionDefinitionByKey(String decisionKey) {
        log.info(new ParameterizedMessage("Отримання DMN по ключу {}", decisionKey));
        log.info("Пошук DMN по ключу " + decisionKey);
        String decisionDefinitionPath = "api/decision-definition/key/" + decisionKey;
        return given()
                .spec(requestSpec)
                .get(decisionDefinitionPath)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("$", not(empty()))
                .contentType(ContentType.JSON)
                .extract().response()
                .jsonPath()
                .getMap("");
    }

    public Map<Object, Object> getDecisionDefinitionById(String decisionId) {
        log.info(new ParameterizedMessage("Отримання DMN по id {}", decisionId));
        log.info("Пошук DMN по id " + decisionId);
        String decisionDefinitionPath = "api/decision-definition/id/" + decisionId;
        return given()
                .spec(requestSpec)
                .get(decisionDefinitionPath)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("$", not(empty()))
                .contentType(ContentType.JSON)
                .extract().response()
                .jsonPath()
                .getMap("");
    }

    public List<HashMap> getAllDecisionDefinitions() {
        log.info("Отримання всіх DMN дефінішенів");
        return given()
                .spec(requestSpec)
                .get("api/decision-definition")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON)
                .extract().response()
                .jsonPath()
                .getList("");
    }
}
