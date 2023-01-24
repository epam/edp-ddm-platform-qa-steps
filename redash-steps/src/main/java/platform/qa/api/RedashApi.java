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

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.extern.log4j.Log4j2;
import platform.qa.clients.RedashClient;
import platform.qa.entities.IEntity;
import platform.qa.entities.Service;
import platform.qa.pojo.redash.CreateDashboardRequest;
import platform.qa.pojo.redash.CreateRoleRequest;
import platform.qa.pojo.redash.CreateTextboxRequest;
import platform.qa.pojo.redash.PublishDashboardRequest;
import platform.qa.pojo.redash.RedashUser;
import platform.qa.pojo.redash.RedashUserPassword;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.apache.logging.log4j.message.ParameterizedMessage;
import com.google.common.collect.Maps;

@Log4j2
public class RedashApi {
    private RedashClient redashClient;
    private Service redash;

    public RedashApi(Service service) {
        redashClient = new RedashClient(service);
        redash = service;
    }

    public RedashApi(Service service, RedashUser user) {
        redashClient = new RedashClient(service, user);
        redash = service;
    }

    public static final String DASHBOARD_URL = "/dashboards";
    public static final String DATA_SOURCE_URL = "/data_sources";
    public static final String GROUPS_URL = "/groups";
    private static final String QUERIES_URL = "/queries";
    private static final String QUERY_RESULT_URL = "/queries/%s/results";
    private static final String REFRESH_QUERY_URL = "/queries/%s/refresh";
    public static final String WIDGETS_URL = "/widgets";
    public static final String CREATE_USER_URL = "/users?no_invite";
    public static final String REFRESH_USER_TOKEN_URL = "/users/%s/regenerate_api_key";
    public static final String GET_ALL_USERS = "users?order=-created_at";
    public static final String ADD_MEMBER_TO_GROUP_URL = "/groups/%s/members";
    private static final String JOBS_URL = "jobs/";
    private static final String QUERY_RESULTS_URL = "query_results/";
    private static final String DATA_SOURCE_SCHEMA = "data_sources/%s/schema";
    private static final String JOB_STATUS_SUCCESS = "3";

    @SuppressWarnings("unchecked")
    public Map<String, Integer> getDashboardsList() {
        log.info("Отримання переліку DASHBOARD");
        List<Map<String, Object>> list = (List<Map<String, Object>>) redashClient.getRequest(DASHBOARD_URL, "results");
        Map<String, Integer> resultMap = Maps.newHashMap();
        list.forEach(a -> resultMap.put((String) a.get("name"), (Integer) a.get("id")));
        return resultMap;
    }

    @SuppressWarnings("unchecked")
    public List<String> getDataSourcesList() {
        log.info("Отримання переліку DATA_SOURCE");
        return (List<String>) redashClient.getRequest(DATA_SOURCE_URL, "name");

    }

    @SuppressWarnings("unchecked")
    public List<String> getGroupsList() {
        log.info("Отримання переліку GROUPS");
        return (List<String>) redashClient.getRequest(GROUPS_URL, "name");
    }

    @SuppressWarnings("unchecked")
    public Map<String, Integer> getQueriesList() {
        log.info("Отримання переліку QUERIES");
        List<Map<String, Object>> list = (List<Map<String, Object>>) redashClient.getRequest(QUERIES_URL, "results");
        Map<String, Integer> resultMap = Maps.newHashMap();
        list.forEach(a -> resultMap.put((String) a.get("name"), (Integer) a.get("id")));

        return resultMap;
    }

    public Response getDashboardByName(String name) {
        log.info(new ParameterizedMessage("Отримання дашборду за його назвою {}", name));
        var list = getDashboardsList();
        return RestAssured.given(redashClient.getRequestSpecification(redash))
                .get(DASHBOARD_URL + "/" + list.get(name));
    }

    public Response getDashboardById(Integer id) {
        log.info(new ParameterizedMessage("Отримання дашборду за його id {}", id));
        return RestAssured.given(redashClient.getRequestSpecification(redash)).get(DASHBOARD_URL + "/" + id);
    }

    private void waitForJobCompletion(String jobId) {
        log.info(new ParameterizedMessage("Очікування виконання запиту {}", jobId));
        await()
                .pollInterval(5, TimeUnit.SECONDS)
                .atMost(10, TimeUnit.MINUTES)
                .ignoreExceptions()
                .pollInSameThread()
                .untilAsserted(() -> {
                    String jobStatus = (String) redashClient.getRequest(JOBS_URL + "/" + jobId, "job.status");
                    //   ConnectionConfig connectionConfig = new ConnectionConfig();
                    //   connectionConfig.closeIdleConnectionsAfterEachResponse();
                    assertThat(jobStatus).as("Job Status:").isEqualTo(JOB_STATUS_SUCCESS);
                });
    }

    public int getQueryIdByDashboardName(String dashboardName) {
        log.info(new ParameterizedMessage("Отримання query id за назвою дашборду {}", dashboardName));
        return (int) ((HashMap) ((HashMap) ((ArrayList) getDashboardByName(dashboardName).jsonPath().get("widgets")).get(0)).get("visualization")).get("id");
    }

    public int createDashboardWithTextbox(String dashboardName) {
        log.info(new ParameterizedMessage("Створення дашборду із назвою {}", dashboardName));
        CreateDashboardRequest createDashboardRequest = new CreateDashboardRequest(dashboardName);
        HashMap<String, Object> createDashboardResponse =
                (HashMap<String, Object>) redashClient.postRequest(DASHBOARD_URL, createDashboardRequest);
        Integer id = (Integer) createDashboardResponse.get("id");

        CreateTextboxRequest createTextboxRequest = CreateTextboxRequest.getRequest(dashboardName, id);
        redashClient.postRequest(WIDGETS_URL, createTextboxRequest);

        PublishDashboardRequest publishDashboardRequest = new PublishDashboardRequest(id, dashboardName, false);
        redashClient.postRequest(DASHBOARD_URL + "/" + id, publishDashboardRequest);
        return id;
    }

    public void deleteDashboardWithDashboardId(String dashboardName, Integer dashboardId) {
        log.info(new ParameterizedMessage("Видалення дашборду із назвою {}", dashboardName));
        redashClient.deleteRequest(DASHBOARD_URL + "/" + dashboardId);
    }

    public String executeQuery(String id, IEntity entity) {
        log.info(new ParameterizedMessage("Виконання query {} та {}", id, entity));
        String jobId = (String) redashClient.getRequest(String.format(QUERY_RESULT_URL, id), "job.id");
        waitForJobCompletion(jobId);
        String queryResultId = (String) redashClient.getRequest(JOBS_URL + "/" + jobId, "job.query_result_id");
        return (String) redashClient.getRequest(QUERY_RESULTS_URL + "/" + queryResultId, "jquery_result.data");
    }

    public HashMap executeQueryOnRedash(String queryId, Map<String, String> params, IEntity payload) {
        log.info(new ParameterizedMessage("Виконання query {} та {}", queryId, payload));
        //refresh query
        redashClient.emptyPostRequest(String.format(REFRESH_QUERY_URL, queryId), params);
        //call api to execute query
        return (HashMap) redashClient.postRequest(String.format(QUERY_RESULT_URL, queryId), payload);
    }

    public RedashUser registerUser(RedashUser redashUser) {
        log.info("Створення користувача в redash");
        //if user doesn't exist we create it
        if (!isUserExists(redashUser)) {
            var response = (HashMap) redashClient.postRequest(CREATE_USER_URL, redashUser);
            String invitationLink = (String) response.get("invite_link");
            redashClient.postRequestToSpecificUrl(invitationLink, new RedashUserPassword("12342342353453"));
        }

        //Get list of all users
        ArrayList<HashMap> users = (ArrayList<HashMap>) redashClient.getRequest(GET_ALL_USERS, "results");
        int userId = (int) users
                .stream()
                .filter(x -> x.get("name").equals(redashUser.getName()))
                .findFirst()
                .get()
                .get("id");

        redashUser.setId(userId);

        //Refresh access token
        HashMap token = (HashMap) redashClient.postRequest(String.format(REFRESH_USER_TOKEN_URL, userId),
                new RedashUserPassword());
        redashUser.setToken((String) token.get("api_key"));

        return redashUser;
    }

    public void assignRoleToUser(RedashUser redashUser, List<String> roles) {
        log.info("Додання користувачу ролi в redash");
        var groups = (ArrayList<HashMap>) redashClient.getRequest(GROUPS_URL, "");
        //find roles which exists in Redash and get their ids
        var roleIds = groups
                .stream()
                .filter(x -> roles.contains(x.get("name")))
                .map(x -> x.get("id"))
                .collect(Collectors.toList());

        CreateRoleRequest request = new CreateRoleRequest(redashUser.getId());
        roleIds.forEach(roleId -> redashClient.postRequest(String.format(ADD_MEMBER_TO_GROUP_URL, roleId), request));
    }

    public Map<String, Integer> getDataSourceToId() {
        log.info("Отримання id усіх джерел даних");
        List<Map<String, Object>> list = (List<Map<String, Object>>) redashClient.getRequest(DATA_SOURCE_URL, "");
        Map<String, Integer> result = new HashMap<>();

        list.forEach(entity -> result.put(entity.get("name").toString(), Integer.valueOf(entity.get("id").toString())));

        return result;
    }

    public List<String> getSchemaNameForDataSource(Integer dataSourceId) {
        log.info(new ParameterizedMessage("Отримання схем якi доступні для джерела даних з id {}", dataSourceId));
        List<String> result = new ArrayList<>();
        List<Map<String, Object>> list = (List<Map<String, Object>>) redashClient.getRequest(
                String.format(DATA_SOURCE_SCHEMA, dataSourceId),
                "schema"
        );

        if (list != null) {
            list.forEach(x -> result.add(x.get("name").toString()));
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    private boolean isUserExists(RedashUser user) {
        //Get list of all users
        ArrayList<HashMap> users = (ArrayList<HashMap>) redashClient.getRequest(GET_ALL_USERS, "results");
        return users
                .stream()
                .anyMatch(x -> x.get("name").equals(user.getName()));
    }
}
