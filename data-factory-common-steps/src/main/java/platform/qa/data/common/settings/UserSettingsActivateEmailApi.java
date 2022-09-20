package platform.qa.data.common.settings;

import io.restassured.response.Response;
import platform.qa.entities.Service;
import platform.qa.entities.User;
import platform.qa.pojo.common.settings.request.Email;

import static io.restassured.RestAssured.given;


public class UserSettingsActivateEmailApi extends UserSettingsApi {

    public static String ACTIVATE_PATH = "api/settings/me/channels/email/activate";

    public UserSettingsActivateEmailApi(Service service, User userAccessToProcess) {
        super(service, userAccessToProcess);
    }

    public Response activate(Email email) {
        return given()
                .spec(requestSpec)
                .basePath(ACTIVATE_PATH)
                .body(email)
                .post();
    }

}
