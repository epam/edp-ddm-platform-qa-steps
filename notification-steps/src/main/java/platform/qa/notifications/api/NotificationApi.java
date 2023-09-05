package platform.qa.notifications.api;

import static io.restassured.RestAssured.given;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.log4j.Log4j2;
import platform.qa.entities.Service;
import platform.qa.entities.User;
import platform.qa.notifications.pojo.response.InboxNotification;

import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;

@Log4j2
public class NotificationApi {

    private final RequestSpecification requestSpec;

    public NotificationApi(Service service, User user) {
        requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(service.getUrl())
                .addHeader("X-Access-Token", user.getToken()).build();
    }

    public Response getNotifications(int offset, int limit) {
        return given()
                .spec(requestSpec)
                .queryParam("offset", offset)
                .queryParam("limit", limit)
                .get("/api/notifications/inbox");
    }

    public Response changeState(String idNotification) {
        return given()
                .spec(requestSpec)
                .basePath(String.format("/api/notifications/inbox/%s/ack", idNotification))
                .post();
    }

    public List<InboxNotification> getNotificationList(int offset, int limit) {
        return getNotifications(offset, limit)
                .as(new TypeReference<List<InboxNotification>>() {
                }.getType());
    }
}
