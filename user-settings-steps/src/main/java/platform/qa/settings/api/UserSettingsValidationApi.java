package platform.qa.settings.api;

import static io.restassured.RestAssured.given;

import io.restassured.response.Response;
import platform.qa.entities.IEntity;
import platform.qa.entities.Service;
import platform.qa.entities.User;
import platform.qa.settings.api.spec.UserSettingsSpecification;
import platform.qa.settings.enumerations.ChannelType;


public class UserSettingsValidationApi extends UserSettingsSpecification {

    public static String VALIDATE_PATH = "/api/settings/me/channels/%s/validate";

    public UserSettingsValidationApi(Service service, User user) {
        super(service, user);
    }

    public <T extends IEntity> Response validate(ChannelType channelType, T channelEntity) {
        return given()
                .spec(requestSpec)
                .basePath(String.format(VALIDATE_PATH, channelType.getType()))
                .body(channelEntity)
                .post();
    }

}
