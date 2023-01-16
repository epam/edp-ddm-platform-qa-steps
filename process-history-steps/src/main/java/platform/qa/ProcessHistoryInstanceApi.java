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

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.extern.log4j.Log4j2;
import platform.qa.entities.HistoryProcessInstance;
import platform.qa.entities.Service;
import platform.qa.entities.User;

import java.util.List;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.message.ParameterizedMessage;

@Log4j2
public class ProcessHistoryInstanceApi {
    private final RequestSpecification requestSpec;

    public ProcessHistoryInstanceApi(Service processHistory, User user) {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setConfig(
                        config()
                                .logConfig(logConfig()
                                        .enableLoggingOfRequestAndResponseIfValidationFails()
                                        .enablePrettyPrinting(Boolean.TRUE)))
                .setBaseUri(processHistory.getUrl() + "api/history")
                .addParams(Map.of("limit", "100", "sort", "desc(endTime)"))
                .setContentType(ContentType.JSON)
                .addHeader("X-Access-Token", user.getToken())
                .addHeader("X-XSRF-TOKEN", "Token")
                .addHeader("Cookie", "XSRF-TOKEN=Token");

        requestSpec = requestSpecBuilder.build();
    }

    public List<HistoryProcessInstance> getHistoryProcessInstancesList() {
        log.info("Отримання переліку екземплярів процесу");
        List<HistoryProcessInstance> instanceList = given()
                .spec(requestSpec)
                .get("/process-instances/")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response()
                .jsonPath()
                .getList("", HistoryProcessInstance.class);

        log.info("Got history process instances list: " + instanceList);
        return instanceList;
    }

    public HistoryProcessInstance getHistoryProcessInstanceByProcessInstanceId(String processInstanceId) {
        log.info(new ParameterizedMessage("Отримання екземпляру процесу за ідентифікатором: {}",
                processInstanceId));
        return getHistoryProcessInstancesByParams(Map.of("processInstanceId", processInstanceId)).get(0);
    }

    public List<HistoryProcessInstance> getHistoryProcessInstancesByProcessDefinitionId(String processDefinitionId) {
        log.info(new ParameterizedMessage("Отримання переліку екземплярів процесу за ідентифікатором процесу: "
                + "{}", processDefinitionId));
        return getHistoryProcessInstancesByParams(Map.of("processDefinitionId", processDefinitionId));
    }

    public List<HistoryProcessInstance> getHistoryProcessInstancesBySuperProcessInstanceId(String superProcessInstanceId) {
        log.info(new ParameterizedMessage("Отримання переліку екземплярів корінного процесу за ідентифікатором: "
                + "{}", superProcessInstanceId));
        return getHistoryProcessInstancesByParams(Map.of("superProcessInstanceId", superProcessInstanceId));
    }

    public List<HistoryProcessInstance> getHistoryProcessInstancesByProcessDefinitionKey(String processDefinitionKey) {
        log.info(new ParameterizedMessage("Отримання переліку екземплярів процесу за ключем процесу: "
                + "{}", processDefinitionKey));
        return getHistoryProcessInstancesByParams(Map.of("processDefinitionKey", processDefinitionKey));
    }

    public List<HistoryProcessInstance> getHistoryProcessInstancesByProcessDefinitionName(String processDefinitionName) {
        log.info(new ParameterizedMessage("Отримання переліку екземплярів процесу за назвою процесу: "
                + "{}", processDefinitionName));
        return getHistoryProcessInstancesByParams(Map.of("processDefinitionName", processDefinitionName));
    }

    public List<HistoryProcessInstance> getHistoryProcessInstancesByBusinessKey(String businessKey) {
        log.info(new ParameterizedMessage("Отримання переліку екземплярів процесу за бізнес ключем: {}",
                businessKey));
        return getHistoryProcessInstancesByParams(Map.of("businessKey", businessKey));
    }

    private List<HistoryProcessInstance> getHistoryProcessInstancesByParams(Map<String, ?> queryParams) {
        log.info(new ParameterizedMessage("Отримання списку екземплярів процесу за параметрами: {}",
                queryParams));
        List<HistoryProcessInstance> instanceList = given()
                .spec(requestSpec)
                .queryParams(queryParams)
                .get("/process-instances/")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response()
                .jsonPath()
                .getList("", HistoryProcessInstance.class);

        log.info("Got history process instances by params: " + queryParams + " list: " + instanceList);
        return instanceList;
    }
}
