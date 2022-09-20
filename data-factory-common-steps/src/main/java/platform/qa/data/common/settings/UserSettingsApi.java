package platform.qa.data.common.settings;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import platform.qa.entities.Service;
import platform.qa.entities.User;

import static io.restassured.RestAssured.config;
import static io.restassured.config.LogConfig.logConfig;

public class UserSettingsApi {

    protected final RequestSpecification requestSpec;
    protected static final String  HEADER_USER_ACCESS_TOKEN_KEYCLOAK = "X-Access-Token";
    protected static final String HEADER_X_XSRF_TOKEN_NAME = "X-XSRF-TOKEN";
    protected static final String HEADER_X_XSRF_TOKEN_VALUE = "Token";
    protected static final String HEADER_COOKIE_NAME = "Cookie";
    protected static final String HEADER_COOKIE_VALUE = "XSRF-TOKEN=Token";

    public UserSettingsApi(Service service, User userAccessToService) {
        String url = service.getUrl();
        requestSpec  = new RequestSpecBuilder().setConfig(
                        config()
                                .logConfig(logConfig().enablePrettyPrinting(Boolean.TRUE)))
                .setContentType(ContentType.JSON)
                .setBaseUri(url)
                .addHeader(HEADER_USER_ACCESS_TOKEN_KEYCLOAK, userAccessToService.getToken())
                .addHeader(HEADER_X_XSRF_TOKEN_NAME, HEADER_X_XSRF_TOKEN_VALUE)
                .addHeader(HEADER_COOKIE_NAME, HEADER_COOKIE_VALUE)
                .build();
    }

}
