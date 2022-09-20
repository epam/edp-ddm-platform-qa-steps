package platform.qa.data.common.settings;


import io.restassured.response.Response;
import platform.qa.entities.Service;
import platform.qa.entities.User;

import static io.restassured.RestAssured.given;


public class UserSettingsByUserIdApiApi extends UserSettingsApi {

    public static String SETTINGS_ME = "api/settings/";

    public UserSettingsByUserIdApiApi(Service service, User userAccessToProcess) {
        super(service, userAccessToProcess);
    }

    public Response getSettingsByUserId(String keycloakUserId) {
        return given()
                .spec(requestSpec)
                .basePath(SETTINGS_ME + "{userId}")
                .pathParam("userId", keycloakUserId)
                .get();
    }

}
