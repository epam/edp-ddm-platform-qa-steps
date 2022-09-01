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
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.log4j.Log4j2;
import platform.qa.entities.Instance;
import platform.qa.entities.InstanceHistory;
import platform.qa.entities.Service;

import java.util.List;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.message.ParameterizedMessage;

/**
 * Implement abilities to manipulate process instance in user process management
 */
@Log4j2
public class ProcessInstanceApi {

    private final RequestSpecification requestSpec;

    public ProcessInstanceApi(Service bpms) {
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

    public Map<String, Object> getProcessInstanceVariables(String processId) {
        log.info(new ParameterizedMessage("Отримання значення змінної бізнес процесу {}", processId));
        return given()
                .spec(requestSpec)
                .get("api/process-instance/" + processId + "/variables")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("$", not(empty()))
                .extract()
                .response()
                .jsonPath()
                .getMap("");
    }

    public int suspendProcessInstance(Boolean suspend, String instanceId) {
        log.info(new ParameterizedMessage("Призупинення экземпляру процесу {}", instanceId));
        int statusCode = given()
                .spec(requestSpec)
                .body(Map.of("suspended", suspend))
                .put("api/process-instance/" + instanceId + "/suspended")
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT)
                .extract()
                .response()
                .getStatusCode();

        log.info("Process instance " + instanceId + "  Suspend: " + suspend);
        return statusCode;
    }

    public int deleteProcessInstance(String instanceId) {
        log.info(new ParameterizedMessage("Видалення экземпляру процесу {}", instanceId));
        Response response = given()
                .spec(requestSpec)
                .delete("api/process-instance/" + instanceId)
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT)
                .extract()
                .response();

        log.info("Process instance was deleted: " + instanceId);
        return response.getStatusCode();
    }

    public String getProcessInstanceStatus(String instanceId) {
        log.info(new ParameterizedMessage("Отримання статусу экземпляру процесу {}", instanceId));
        String status = given()
                .spec(requestSpec)
                .get("api/process-instance/" + instanceId)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("$", not(empty()))
                .extract()
                .response()
                .jsonPath()
                .get("suspended")
                .toString();

        log.info("Process instance status: " + status);
        return status;
    }

    public int getProcessInstanceStatusCode(String instanceId) {
        log.info(new ParameterizedMessage("Отримання коду статусу экземпляру процесу {}", instanceId));
        int statusCode = given()
                .spec(requestSpec)
                .get("api/process-instance/" + instanceId)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("$", not(empty()))
                .extract()
                .response()
                .getStatusCode();

        log.info("Process instance status code: " + statusCode);
        return statusCode;
    }

    public int getProcessInstanceStatusCode(String instanceId, int status) {
        log.info(new ParameterizedMessage("Отримання коду статусу экземпляру процесу {} з перевіркою на введений "
                + "статус код", instanceId));
        int statusCode = given()
                .spec(requestSpec)
                .get("api/process-instance/" + instanceId)
                .then()
                .statusCode(status)
                .extract()
                .response()
                .getStatusCode();

        log.info("Process instance status code: " + statusCode);
        return statusCode;
    }

    public List<Instance> getProcessInstancesList() {
        log.info("Отримання списку экземплярів процесу");
        List<Instance> instanceList = given()
                .spec(requestSpec)
                .get("api/process-instance/")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response()
                .jsonPath()
                .getList("", Instance.class);

        log.info("Got process instances list: " + instanceList);
        return instanceList;
    }

    public int getProcessInstanceCount() {
        log.info("Отримання кількості экземплярів процесу");
        int count = given()
                .spec(requestSpec)
                .get("api/history/process-instance/count/")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("$", not(empty()))
                .extract()
                .response()
                .jsonPath()
                .get("count");

        log.info("Process instance count: " + count);
        return count;
    }

    public Instance getProcessInstanceById(String id) {
        log.info(new ParameterizedMessage("Отримання экземпляру інстансу за id : {}", id));
        Instance instance = given()
                .spec(requestSpec)
                .get("api/process-instance/" + id)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("$", not(empty()))
                .extract()
                .response()
                .jsonPath()
                .getObject("", Instance.class);

        log.info("Get process instance: " + instance + " by Id: " + id);
        return instance;
    }

    /**
     * will be replaced with History Management API (https://jiraeu.epam.com/browse/MDTUDDM-10595)
     */
    @Deprecated
    public InstanceHistory getProcessInstanceHistoryById(String id) {
        log.info(new ParameterizedMessage("Отримання історії экземпляру інстансу за id : {}", id));
        InstanceHistory history = given()
                .spec(requestSpec)
                .get("api/history/process-instance/" + id)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("$", not(empty()))
                .extract()
                .response()
                .jsonPath()
                .getObject("", InstanceHistory.class);

        log.info("Process instance history: " + history + " by Id: " + id);
        return history;
    }

    public String getProcessInstanceStateById(String id) {
        log.info(new ParameterizedMessage("Отримання статусу экземпляру процесу за id: {}", id));
        String state = given()
                .spec(requestSpec)
                .get("api/history/process-instance/" + id)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response()
                .jsonPath()
                .getObject("", InstanceHistory.class)
                .getState();

        log.info("Process instance: " + id + " has state: " + state);
        return state;
    }

    private List<InstanceHistory> getProcessInstancesHistoryByParams(Map<String, ?> queryParams) {
        log.info(new ParameterizedMessage("Отримання списку экземплярів історії процесу за параметрами: {}",
                queryParams));
        List<InstanceHistory> instanceList = given()
                .spec(requestSpec)
                .queryParams(queryParams)
                .get("api/history/process-instance/")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response()
                .jsonPath()
                .getList("", InstanceHistory.class);

        log.info("Got process instances history by params:" + queryParams + " list: " + instanceList);
        return instanceList;
    }

    private List<Instance> getProcessInstancesByParams(Map<String, ?> queryParams) {
        log.info(new ParameterizedMessage("Отримання списку экземплярів процесу за параметрами: {}", queryParams));
        List<Instance> instanceList = given()
                .spec(requestSpec)
                .queryParams(queryParams)
                .get("api/process-instance/")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response()
                .jsonPath()
                .getList("", Instance.class);

        log.info("Got process instances by params:" + queryParams + " list: " + instanceList);
        return instanceList;
    }
}