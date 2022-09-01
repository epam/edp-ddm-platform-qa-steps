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
import platform.qa.entities.HistoryTask;
import platform.qa.entities.Service;
import platform.qa.entities.User;

import java.util.List;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.message.ParameterizedMessage;

@Log4j2
public class ProcessHistoryTaskApi {
    private final RequestSpecification requestSpec;

    public ProcessHistoryTaskApi(Service processHistory, User user) {
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

    public List<HistoryTask> getHistoryTasksInstances() {
        log.info("Отримання переліку задач процесу");
        List<HistoryTask> historyTaskList = given()
                .spec(requestSpec)
                .get("/tasks/")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response()
                .jsonPath()
                .getList("", HistoryTask.class);
        log.info("Tasks list has size: " + historyTaskList.size());
        return historyTaskList;
    }

    public List<HistoryTask> getHistoryTasksByProcessInstanceId(String processInstanceId) {
        log.info(new ParameterizedMessage("Отримання задач за ідентифікатором екземпляру процесу: "
                + "{processInstanceId}", processInstanceId));
        return getHistoryTasksByParams(Map.of("processInstanceId", processInstanceId));
    }

    public List<HistoryTask> getHistoryTasksByTaskDefinitionName(String taskDefinitionName) {
        log.info(new ParameterizedMessage("Отримання задач за назвою: {taskDefinitionName}", taskDefinitionName));
        return getHistoryTasksByParams(Map.of("taskDefinitionName", taskDefinitionName));
    }

    public List<HistoryTask> getHistoryTasksByProcessDefinitionId(String processDefinitionId) {
        log.info(new ParameterizedMessage("Отримання задач за ідентифікатором процесу: {processDefinitionId}",
                processDefinitionId));
        return getHistoryTasksByParams(Map.of("processDefinitionId", processDefinitionId));
    }

    public List<HistoryTask> getHistoryTasksByProcessDefinitionName(String processDefinitionName) {
        log.info(new ParameterizedMessage("Отримання задач за назвою процесу: {processDefinitionName}",
                processDefinitionName));
        return getHistoryTasksByParams(Map.of("processDefinitionName", processDefinitionName));
    }

    public List<HistoryTask> getHistoryTasksByProcessDefinitionKey(String processDefinitionKey) {
        log.info(new ParameterizedMessage("Отримання задач за ключем процесу: {processDefinitionKey}",
                processDefinitionKey));
        return getHistoryTasksByParams(Map.of("processDefinitionKey", processDefinitionKey));
    }

    public List<HistoryTask> getHistoryTasksByRootProcessInstanceId(String rootProcessInstanceId) {
        log.info(new ParameterizedMessage("Отримання задач за ідентифікатором корінного екземпляру процесу: "
                + "{rootProcessInstanceId}", rootProcessInstanceId));
        return getHistoryTasksByParams(Map.of("rootProcessInstanceId", rootProcessInstanceId));
    }

    public List<HistoryTask> getHistoryTasksByAssignee(String assignee) {
        log.info(new ParameterizedMessage("Отримання задач за виконавцем: {assignee}", assignee));
        return getHistoryTasksByParams(Map.of("assignee", assignee));
    }

    private List<HistoryTask> getHistoryTasksByParams(Map<String, ?> queryParams) {
        log.info(new ParameterizedMessage("Отримання списку задач за параметрами: {queryParams}", queryParams));
        List<HistoryTask> historyTaskList = given()
                .spec(requestSpec)
                .queryParams(queryParams)
                .get("/tasks/")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response()
                .jsonPath()
                .getList("", HistoryTask.class);

        log.info("Got history tasks by params: " + queryParams + " list: " + historyTaskList);
        return historyTaskList;
    }
}
