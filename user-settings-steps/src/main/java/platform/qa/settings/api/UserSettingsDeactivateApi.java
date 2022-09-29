package platform.qa.settings.api;

import static io.restassured.RestAssured.given;

import io.restassured.response.Response;
import platform.qa.entities.Service;
import platform.qa.entities.User;
import platform.qa.settings.api.spec.UserSettingsSpecification;
import platform.qa.settings.enumerations.ChannelType;
import platform.qa.settings.pojo.request.DeactivationReason;


public class UserSettingsDeactivateApi extends UserSettingsSpecification {

    public String deactivate = "/api/settings/me/channels/%s/deactivate";

    public UserSettingsDeactivateApi(Service service, User user) {
        super(service, user);
    }

    public Response deactivate(DeactivationReason deactivationReason, ChannelType channelType) {
        return given()
                .spec(requestSpec)
                .basePath(String.format(deactivate, channelType.getType()))
                .body(deactivationReason)
                .post();
    }

}
