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
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static platform.qa.constants.Constants.COOKIE_HEADER_NAME;
import static platform.qa.constants.Constants.COOKIE_HEADER_VALUE;
import static platform.qa.constants.Constants.XSRF_HEADER_NAME;
import static platform.qa.constants.Constants.XSRF_HEADER_VALUE;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.extern.log4j.Log4j2;
import platform.qa.entities.Service;
import platform.qa.entities.Task;
import platform.qa.entities.TaskHistory;

import java.util.List;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.message.ParameterizedMessage;

/**
 * Implement abilities to manipulate tasks in user task management
 */
@Log4j2
public class TaskApi {

    private final RequestSpecification requestSpec;

    public TaskApi(Service bpms) {

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

    public List<Task> getTasksInstances() {
        log.info("Отримання переліку задач на виконання");
        List<Task> taskList = given()
                .spec(requestSpec)
                .get("api/task/")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response()
                .jsonPath()
                .getList("", Task.class);
        log.info("Tasks list has size: " + taskList.size());
        return taskList;
    }

    public int getTaskCountByProcessInstanceId(String id) {
        log.info(new ParameterizedMessage("Отримання кількості задач за processInstanceId: {}", id));
        int count = given()
                .spec(requestSpec)
                .queryParam("processInstanceId", id)
                .get("api/task/count")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response()
                .jsonPath()
                .get("count");
        log.info("Got tasks count: " + count + " by processInstanceId" + id);
        return count;
    }

    public int getTaskCountByProcessDefinitionId(String id) {
        log.info(new ParameterizedMessage("Отримання кількості задач за processDefinitionId: {}", id));
        int count = given()
                .spec(requestSpec)
                .queryParam("processDefinitionId", id)
                .get("api/task/count")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response()
                .jsonPath()
                .get("count");
        log.info("Got tasks count: " + count + " by processDefinitionId: " + id);
        return count;
    }

    public List<Task> getTasksByProcessInstanceId(String id) {
        log.info(new ParameterizedMessage("Отримання переліку задач за processInstanceId: {}", id));
        List<Task> taskList = given()
                .spec(requestSpec)
                .queryParam("processInstanceId", id)
                .get("api/task")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response()
                .jsonPath()
                .getList("", Task.class);
        log.info("Tasks list by processInstanceId: " + id + "  has size: " + taskList.size());
        return taskList;
    }

    public List<Task> getTasksByProcessDefinitionId(String id) {
        log.info(new ParameterizedMessage("Отримання переліку задач за processDefinitionId: {}", id));
        List<Task> taskList = given()
                .spec(requestSpec)
                .queryParam("processDefinitionId", id)
                .get("api/task")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response()
                .jsonPath()
                .getList("", Task.class);
        log.info("Tasks list by processDefinitionId: +" + id + " has size: " + taskList.size());
        return taskList;
    }

    public List<Task> getTasksByProcessDefinitionName(String name) {
        log.info(new ParameterizedMessage("Отримання переліку задач за processDefinitionName: {}", name));
        return getTasksByParams(Map.of("processDefinitionName", name));
    }

    /**
     * will be replaced with History Management API (https://jiraeu.epam.com/browse/MDTUDDM-10595)
     */
    @Deprecated
    public List<TaskHistory> getTasksHistoryByDefinitionId(String id) {
        log.info(new ParameterizedMessage("Отримання переліку задач з історії за processDefinitionId: {}", id));
        List<TaskHistory> taskList = given()
                .spec(requestSpec)
                .queryParam("processDefinitionId", id)
                .get("api/history/task")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response()
                .jsonPath()
                .getList("", TaskHistory.class);
        log.info("Tasks history list by processDefinitionId: " + id + " has size: " + taskList.size());
        return taskList;
    }

    public void setTaskAssignee(String taskId, String userId) {
        log.info(new ParameterizedMessage("Встановлення assignee userId: {} для задачі: {}", userId, taskId));
        int statusCode = given()
                .spec(requestSpec)
                .pathParam("taskId", taskId)
                .body(Map.of("userId", userId))
                .when()
                .post("api/task/{taskId}/assignee")
                .then()
                .statusCode(SC_NO_CONTENT)
                .extract()
                .statusCode();
        log.info("Встановлення assignee: " + userId + " для задачі: " + taskId + "Status code: " + statusCode);
    }

    public void claimTaskById(String id, String userId) {
        log.info(new ParameterizedMessage("Обрання задачі id: {} до виконання юзером: {}", id, userId));
        int statusCode = given()
                .spec(requestSpec)
                .contentType(ContentType.JSON)
                .when()
                .body(Map.of("userId", userId))
                .post("api/task/" + id + "/claim")
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT)
                .extract()
                .statusCode();
        log.info("Task with id: " + id + " was claimed by user " + userId + " . Status code " + statusCode);
    }

    public void completeTaskById(String id) {
        log.info(new ParameterizedMessage("Завершення виконання задачі id: {}", id));
        Map payload = Map.of("data", Map.of("firstName", Map.of("value", "name")));
        int statusCode = given()
                .spec(requestSpec)
                .pathParam("taskId", id)
                .body(payload)
                .when()
                .post("api/task/{taskId}/complete")
                .then()
                .statusCode(SC_NO_CONTENT)
                .extract()
                .statusCode();
        log.info("Task with id: " + id + " was completed. Status code: " + statusCode);
    }

    /**
     * will be replaced with History Management API (https://jiraeu.epam.com/browse/MDTUDDM-10595)
     */
    @Deprecated
    public List<TaskHistory> getTasksHistoryByProcessInstanceId(String id) {
        log.info(new ParameterizedMessage("Отримання переліку задач з історії за processInstanceId: {}", id));
        List<TaskHistory> taskList = given()
                .spec(requestSpec)
                .queryParam("processInstanceId", id)
                .get("api/history/task")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response()
                .jsonPath()
                .getList("", TaskHistory.class);
        log.info("Tasks history list by processInstanceId: " + id + " has size: " + taskList.size());
        return taskList;
    }

    /**
     * will be replaced with History Management API (https://jiraeu.epam.com/browse/MDTUDDM-10595)
     */
    @Deprecated
    public List<TaskHistory> getTasksHistory() {
        log.info("Отримання всіх завершених задач");
        List<TaskHistory> taskList = given()
                .spec(requestSpec)
                .get("api/history/task")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response()
                .jsonPath()
                .getList("", TaskHistory.class);
        log.info("All tasks history list has size: " + taskList.size());
        return taskList;
    }

    public Task getTaskById(String id) {
        log.info(new ParameterizedMessage("Отримання задачі за ідентифікатором: {}", id));
        Task task = given()
                .spec(requestSpec)
                .pathParam("id", id)
                .get("api/task/{id}")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response()
                .jsonPath()
                .getObject("", Task.class);
        log.info("Task data: " + task);
        return task;
    }

    public List<Task> getTasksByName(String name) {
        log.info(new ParameterizedMessage("Отримання задачі за ім'ям: {}", name));
        return getTasksByParams(Map.of("name", name));
    }

    public List<Task> getTasksByAssignee(String assignee) {
        log.info(new ParameterizedMessage("Отримання задачі за виконавцем: {}", assignee));
        return getTasksByParams(Map.of("assignee", assignee));
    }

    private List<Task> getTasksByParams(Map<String, ?> queryParams) {
        log.info(new ParameterizedMessage("Отримання списку задач за параметрами: {}", queryParams));
        List<Task> taskList = given()
                .spec(requestSpec)
                .queryParams(queryParams)
                .get("api/task/")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response()
                .jsonPath()
                .getList("", Task.class);

        log.info("Got tasks by params:" + queryParams + " list: " + taskList);
        return taskList;
    }
}