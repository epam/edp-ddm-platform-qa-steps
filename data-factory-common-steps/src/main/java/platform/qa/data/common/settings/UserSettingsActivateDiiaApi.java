package platform.qa.data.common.settings;

import io.restassured.response.Response;
import platform.qa.entities.Service;
import platform.qa.entities.User;

import static io.restassured.RestAssured.given;


public class UserSettingsActivateDiiaApi extends UserSettingsApi {

    public static String ACTIVATE_PATH = "/api/settings/me/channels/diia/activate";

    public UserSettingsActivateDiiaApi(Service service, User userAccessToProcess) {
        super(service, userAccessToProcess);
    }

    public Response activate() {
        return given()
                .spec(requestSpec)
                .basePath(ACTIVATE_PATH)
                .post();
    }

}
