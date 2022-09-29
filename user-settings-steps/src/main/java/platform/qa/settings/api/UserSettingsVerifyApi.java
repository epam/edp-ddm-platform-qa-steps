package platform.qa.settings.api;

import static io.restassured.RestAssured.given;

import io.restassured.response.Response;
import platform.qa.entities.IEntity;
import platform.qa.entities.Service;
import platform.qa.entities.User;
import platform.qa.settings.api.spec.UserSettingsSpecification;
import platform.qa.settings.enumerations.ChannelType;

public class UserSettingsVerifyApi extends UserSettingsSpecification {
    public static String VERIFY_PATH = "/api/settings/me/channels/%s/verify";

    public UserSettingsVerifyApi(Service service, User user) {
        super(service, user);
    }

    public <T extends IEntity> Response verify(ChannelType channelType, T channelEntity) {
        return given()
                .spec(requestSpec)
                .basePath(String.format(VERIFY_PATH, channelType.getType()))
                .body(channelEntity)
                .post();
    }
}
