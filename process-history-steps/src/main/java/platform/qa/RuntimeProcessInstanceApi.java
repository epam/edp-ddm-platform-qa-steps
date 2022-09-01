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
import platform.qa.entities.RuntimeProcessInstance;
import platform.qa.entities.Service;
import platform.qa.entities.User;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.message.ParameterizedMessage;

@Log4j2
public class RuntimeProcessInstanceApi {
    private final RequestSpecification requestSpec;

    public RuntimeProcessInstanceApi(Service processHistory, User user) {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setConfig(
                        config()
                                .logConfig(logConfig()
                                        .enableLoggingOfRequestAndResponseIfValidationFails()
                                        .enablePrettyPrinting(Boolean.TRUE)))
                .setBaseUri(processHistory.getUrl() + "api/runtime")
                .addParams(Map.of("limit", "100", "sort", "desc(endTime)"))
                .setContentType(ContentType.JSON)
                .addHeader("X-Access-Token", user.getToken())
                .addHeader("X-XSRF-TOKEN", "Token")
                .addHeader("Cookie", "XSRF-TOKEN=Token");

        requestSpec = requestSpecBuilder.build();
    }

    public List<RuntimeProcessInstance> getRuntimeProcessInstancesList() {
        log.info("Отримання переліку екземплярів процесу");
        List<RuntimeProcessInstance> instanceList = given()
                .spec(requestSpec)
                .get("/process-instances/")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response()
                .jsonPath()
                .getList("", RuntimeProcessInstance.class);

        log.info("Got runtime process instances list: " + instanceList);
        return instanceList;
    }

    public int getRuntimeProcessCount() {
        log.info("Отримання кількості екземплярів процесу");
        int processCount =
                given()
                        .spec(requestSpec)
                        .get("/process-instances/count")
                        .then()
                        .statusCode(HttpStatus.SC_OK)
                        .extract()
                        .jsonPath()
                        .get("count");

        log.info("Got runtime process instances count: " + processCount);
        return processCount;
    }

    public RuntimeProcessInstance getRuntimeProcessInstanceByProcessInstanceId(String processInstanceId) {
        log.info(new ParameterizedMessage("Отримання екземпляру процесу за ідентифікатором: {processInstanceId}", processInstanceId));
        var processes = getRuntimeProcessInstancesByParams(Map.of("processInstanceId", processInstanceId));
        return processes.stream().filter(proc -> processInstanceId.equals(proc.getProcessInstanceId())).collect(Collectors.toList()).get(0);
    }

    public List<RuntimeProcessInstance> getRuntimeProcessInstancesByProcessDefinitionId(String processDefinitionId) {
        log.info(new ParameterizedMessage("Отримання переліку екземплярів процесу за ідентифікатором процесу: "
                + "{processDefinitionId}", processDefinitionId));
        return getRuntimeProcessInstancesByParams(Map.of("processDefinitionId", processDefinitionId));
    }

    public List<RuntimeProcessInstance> getRuntimeProcessInstancesBySuperProcessInstanceId(String superProcessInstanceId) {
        log.info(new ParameterizedMessage("Отримання переліку екземплярів корінного процесу за ідентифікатором: "
                + "{superProcessInstanceId}", superProcessInstanceId));
        return getRuntimeProcessInstancesByParams(Map.of("superProcessInstanceId", superProcessInstanceId));
    }

    public List<RuntimeProcessInstance> getRuntimeProcessInstancesByProcessDefinitionKey(String processDefinitionKey) {
        log.info(new ParameterizedMessage("Отримання переліку екземплярів процесу за ключем процесу: "
                + "{processDefinitionKey}", processDefinitionKey));
        return getRuntimeProcessInstancesByParams(Map.of("processDefinitionKey", processDefinitionKey));
    }

    public List<RuntimeProcessInstance> getRuntimeProcessInstancesByProcessDefinitionName(String processDefinitionName) {
        log.info(new ParameterizedMessage("Отримання переліку екземплярів процесу за назвою процесу: "
                + "{processDefinitionName}", processDefinitionName));
        return getRuntimeProcessInstancesByParams(Map.of("processDefinitionName", processDefinitionName));
    }

    public List<RuntimeProcessInstance> getRuntimeProcessInstancesByBusinessKey(String businessKey) {
        log.info(new ParameterizedMessage("Отримання переліку екземплярів процесу за бізнес ключем: {businessKey}",
                businessKey));
        return getRuntimeProcessInstancesByParams(Map.of("businessKey", businessKey));
    }

    private List<RuntimeProcessInstance> getRuntimeProcessInstancesByParams(Map<String, ?> queryParams) {
        log.info(new ParameterizedMessage("Отримання списку екземплярів процесу за параметрами: {queryParams}", queryParams));
        List<RuntimeProcessInstance> instanceList = given()
                .spec(requestSpec)
                .queryParams(queryParams)
                .get("/process-instances/")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response()
                .jsonPath()
                .getList("", RuntimeProcessInstance.class);

        log.info("Got runtime process instances by params: " + queryParams + " list: " + instanceList);
        return instanceList;
    }
}
