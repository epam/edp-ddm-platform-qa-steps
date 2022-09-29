package platform.qa.settings.api;

import static io.restassured.RestAssured.given;

import io.restassured.response.Response;
import platform.qa.entities.Service;
import platform.qa.settings.api.spec.UserSettingsSpecification;


public class UserSettingsApi extends UserSettingsSpecification {
    public static String SETTINGS = "/api/settings/";

    public UserSettingsApi(Service service) {
        super(service);
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
