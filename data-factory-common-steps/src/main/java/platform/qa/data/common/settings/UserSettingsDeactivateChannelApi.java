package platform.qa.data.common.settings;

import io.restassured.response.Response;

import platform.qa.entities.Service;
import platform.qa.entities.User;
import platform.qa.pojo.common.settings.ChannelType;
import platform.qa.pojo.common.settings.request.DeactivationReason;

import static io.restassured.RestAssured.given;


public class UserSettingsDeactivateChannelApi extends UserSettingsApi {

    public String deactivate = "api/settings/me/channels/%s/deactivate";

    public UserSettingsDeactivateChannelApi(Service service, User userAccessToProcess) {
        super(service, userAccessToProcess);
    }

    public Response deactivate(DeactivationReason deactivationReason, ChannelType type) {
        return given()
                .spec(requestSpec)
                .basePath(String.format(deactivate, type.getType()))
                .body(deactivationReason)
                .post();
    }

}
