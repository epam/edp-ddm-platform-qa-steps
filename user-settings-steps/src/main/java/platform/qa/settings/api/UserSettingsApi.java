package platform.qa.settings.api;

import io.restassured.response.Response;
import platform.qa.entities.Service;
import platform.qa.entities.User;
import platform.qa.settings.api.spec.UserSettingsSpecification;

import static io.restassured.RestAssured.given;


public class UserSettingsApi extends UserSettingsSpecification {
    public static String SETTINGS = "/api/settings/";

    public UserSettingsApi(Service service, User user) {
        super(service, user);
    }

    public Response getAllCurrentUserSettings() {
        return given()
                .spec(requestSpec)
                .basePath(SETTINGS + "me")
                .get();
    }

    public Response getSettingsByUserId(String keycloakUserId) {
        return given()
                .spec(requestSpec)
                .basePath(SETTINGS + "{userId}")
                .pathParam("userId", keycloakUserId)
                .get();
    }

}
