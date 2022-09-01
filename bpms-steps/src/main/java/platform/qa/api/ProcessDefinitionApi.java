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
import platform.qa.entities.Definition;
import platform.qa.entities.Instance;
import platform.qa.entities.Service;
import platform.qa.entities.User;

import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.message.ParameterizedMessage;

/**
 * Implement abilities to manipulate process definition in user process management
 */
@Log4j2
public class ProcessDefinitionApi {

    private final RequestSpecification requestSpec;

    public ProcessDefinitionApi(Service bpms) {
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

    public ProcessDefinitionApi(String url, User user) {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setConfig(
                        config()
                                .logConfig(logConfig()
                                        .enableLoggingOfRequestAndResponseIfValidationFails()
                                        .enablePrettyPrinting(Boolean.TRUE)))
                .setBaseUri(url)
                .setContentType(ContentType.JSON)
                .addHeader("X-Access-Token", user.getToken())
                .addHeader(XSRF_HEADER_NAME, XSRF_HEADER_VALUE)
                .addHeader(COOKIE_HEADER_NAME, COOKIE_HEADER_VALUE);

        requestSpec = requestSpecBuilder.build();
    }

    public List<String> getAllDefinitionKeys() {
        log.info("Пошук всіх BPMN процесів");
        String processDefinitionPath = "api/process-definition";
        return given()
                .spec(requestSpec)
                .get(processDefinitionPath)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON)
                .extract()
                .response()
                .jsonPath()
                .getList("key");
    }

    public List<Definition> getAllDefinitions() {
        log.info("Пошук всіх процесів");
        String processDefinitionPath = "api/process-definition";
        return given()
                .spec(requestSpec)
                .get(processDefinitionPath)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON)
                .extract()
                .response()
                .jsonPath()
                .getList("", Definition.class);
    }

    public String getDefinitionNameByKey(String processId) {
        log.info(new ParameterizedMessage("Пошук оновленого BPMN процесу {}", processId));
        String processDefinitionPath = "api/process-definition/key/" + processId;
        String name = given()
                .spec(requestSpec)
                .get(processDefinitionPath)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("$", not(empty()))
                .contentType(ContentType.JSON)
                .extract()
                .response()
                .jsonPath()
                .get("name")
                .toString();

        log.info("process-definition name: " + name);
        return name;
    }

    public String getDefinitionIdByName(String definitionName) {
        log.info(new ParameterizedMessage("Пошук ідентифікатора BPMN процесу за ім'ям {}", definitionName));
        String definitionId = given()
                .spec(requestSpec)
                .queryParam("name", definitionName)
                .get("api/process-definition")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("$", not(empty()))
                .contentType(ContentType.JSON)
                .extract()
                .response()
                .jsonPath()
                .getString("id[0]");

        log.info("process-definition id: " + definitionId);
        return definitionId;
    }

    public Map getDefinitionById(String definitionId) {
        log.info(new ParameterizedMessage("Пошук BPMN процесу за definitionId: {}", definitionId));
        Map processDefinitionData = given()
                .spec(requestSpec)
                .pathParam("definitionId", definitionId)
                .get("api/process-definition/{definitionId}")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("$", not(empty()))
                .contentType(ContentType.JSON)
                .extract()
                .response()
                .jsonPath()
                .getMap("");

        log.info("process-definition data: " + StringUtils.join(processDefinitionData));
        return processDefinitionData;
    }

    public List getDefinitionByKey(String processDefinitionKey) {
        log.info(new ParameterizedMessage("Пошук BPMN процесу за ключем: {}", processDefinitionKey));
        List processDefinitionData = given()
                .spec(requestSpec)
                .pathParam("processDefinitionKey", processDefinitionKey)
                .get("api/process-definition?processDefinitionKey={processDefinitionKey}")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("$", not(empty()))
                .contentType(ContentType.JSON)
                .extract()
                .as(List.class);

        log.info("process-definition data: " + StringUtils.join(processDefinitionData));
        return processDefinitionData;
    }

    public Map getDefinitionByName(String definitionName) {
        log.info(new ParameterizedMessage("Пошук BPMN процесу за ім'ям: {}", definitionName));
        Map processDefinitionData = given()
                .spec(requestSpec)
                .queryParam("name", definitionName)
                .get("api/process-definition")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("$", not(empty()))
                .contentType(ContentType.JSON)
                .extract()
                .response()
                .jsonPath()
                .getMap("[0]");

        log.info("process-definition data: " + StringUtils.join(processDefinitionData));
        return processDefinitionData;
    }

    public Definition getProcessDefinitionByName(String definitionName) {
        log.info(new ParameterizedMessage("Пошук BPMN процесу за ім'ям: {}", definitionName));
        Definition processDefinitionData = given()
                .spec(requestSpec)
                .queryParam("name", definitionName)
                .get("api/process-definition")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("$", not(empty()))
                .contentType(ContentType.JSON)
                .extract()
                .response()
                .jsonPath()
                .getObject("[0]", Definition.class);

        log.info("process-definition data: " + StringUtils.join(processDefinitionData));
        return processDefinitionData;
    }

    public boolean deleteDefinitionByKey(String processId) {
        log.info(new ParameterizedMessage("Видалення BPMN процесу processId: {}", processId));
        String processDefinitionPath = "api/process-definition/key/".concat(processId).concat("/?cascade=true");
        int statusCode = given()
                .spec(requestSpec)
                .delete(processDefinitionPath)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response()
                .getStatusCode();

        log.info("process-definition key: " + processId);
        return statusCode == HttpStatus.SC_OK;
    }

    public String startProcessInstance(String processKey) {
        log.info(new ParameterizedMessage("Старт бізнес процесу processKey: {}", processKey));
        String id = given()
                .spec(requestSpec)
                .body("{}")
                .post("api/process-definition/key/" + processKey + "/start")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("$", not(empty()))
                .extract()
                .response()
                .jsonPath()
                .get("id")
                .toString();

        log.info("Process instance id: " + id);
        return id;
    }

    public Instance startProcessInstanceWithInitiator(String processKey, String userId) {
        log.info(new ParameterizedMessage("Старт бізнес процесу processKey: {}", processKey));
        Instance instance = given()
                .spec(requestSpec)
                .body(Map.of("variables", Map.of("initiator", Map.of("value", userId, "type", "String")),
                        "businessKey", RandomStringUtils.randomAlphanumeric(8)))
                .post("api/process-definition/key/" + processKey + "/start")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("$", not(empty()))
                .extract()
                .response()
                .jsonPath()
                .getObject("", Instance.class);

        log.info("Process instance started: " + instance);
        return instance;
    }

    public Definition getProcessDefinitionById(String id) {
        log.info(new ParameterizedMessage("Отримання экземпляру процесу за його Id: {}", id));
        Definition definition = given()
                .spec(requestSpec)
                .get("api/process-definition/" + id)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("$", not(empty()))
                .extract()
                .response()
                .jsonPath()
                .getObject("", Definition.class);

        log.info("Process definition: " + definition + "By Id: " + id);
        return definition;
    }

    public Instance startProcessInstanceByDefinitionId(String id) {
        log.info(new ParameterizedMessage("Старт процес інстансу за definitionId : {}", id));
        Instance instance = given()
                .spec(requestSpec)
                .body("{}")
                .post("api/process-definition/" + id + "/start")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("$", not(empty()))
                .extract()
                .response()
                .jsonPath()
                .getObject("", Instance.class);

        log.info("Process instance: " + instance + " was started by definition Id: " + id);
        return instance;
    }

    public int suspendProcessDefinitionById(boolean suspended, String definitionId) {
        log.info(new ParameterizedMessage("Призупинення бізнес процесу з id {}", definitionId));
        int statusCode = given()
                .spec(requestSpec)
                .body(Map.of("suspended", suspended, "includeProcessInstances", Boolean.TRUE))
                .put("api/process-definition/" + definitionId + "/suspended/")
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT)
                .extract()
                .response()
                .getStatusCode();

        log.info("Process definition " + definitionId + "  Suspend: " + suspended);
        return statusCode;
    }

    public int suspendProcessDefinitionByKey(boolean suspended, String key) {
        log.info(new ParameterizedMessage("Призупинення бізнес процесу з ключем {}", key));
        int statusCode = given()
                .spec(requestSpec)
                .body(Map.of("suspended", suspended, "includeProcessInstances", Boolean.TRUE))
                .put("api/process-definition/key/" + key + "/suspended/")
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT)
                .extract()
                .response()
                .getStatusCode();

        log.info("Process definition " + key + "  Suspend: " + suspended);
        return statusCode;
    }

    private List<Definition> getDefinitionsByParams(Map<String, ?> queryParams) {
        log.info(new ParameterizedMessage("Отримання списку процесів за параметрами: {}", queryParams));
        List<Definition> definitionList = given()
                .spec(requestSpec)
                .queryParams(queryParams)
                .get("api/process-definition/")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response()
                .jsonPath()
                .getList("", Definition.class);

        log.info("Got process definitions by params:" + queryParams + " list: " + definitionList);
        return definitionList;
    }

    public void deleteCreatedProcessDefinitions() {
        List<Definition> createdDefinitions = getDefinitionsByParams(Map.of("keyLike", "%_AUTO"));
        if (createdDefinitions != null && !createdDefinitions.isEmpty()) {
            createdDefinitions.forEach(definition -> deleteDefinitionByKey(definition.getKey()));
        }
    }
}
