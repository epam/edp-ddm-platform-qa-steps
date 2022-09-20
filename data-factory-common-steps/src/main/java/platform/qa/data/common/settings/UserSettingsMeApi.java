package platform.qa.data.common.settings;

import io.restassured.response.Response;
import platform.qa.entities.Service;
import platform.qa.entities.User;

import static io.restassured.RestAssured.given;


public class UserSettingsMeApi extends UserSettingsApi {

    public static String SETTINGS_ME = "api/settings/me";

    public UserSettingsMeApi(Service service, User userAccessToProcess) {
        super(service, userAccessToProcess);
    }

    public Response getSettingsMe() {
        return given()
                .spec(requestSpec)
                .basePath(SETTINGS_ME)
                .get();
    }

}
