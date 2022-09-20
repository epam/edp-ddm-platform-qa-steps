package platform.qa.data.common.settings;

import io.restassured.response.Response;
import platform.qa.entities.Service;
import platform.qa.entities.User;
import platform.qa.pojo.common.settings.request.Email;

import static io.restassured.RestAssured.given;


public class UserSettingsValidationEmailApi extends UserSettingsApi {

    public static String VALIDATE = "api/settings/me/channels/email/validate";

    public UserSettingsValidationEmailApi(Service service, User userAccessToProcess) {
        super(service, userAccessToProcess);
    }

    public Response validate(Email email) {
        return given()
                .spec(requestSpec)
                .basePath(VALIDATE)
                .body(email)
                .post();
    }

}
