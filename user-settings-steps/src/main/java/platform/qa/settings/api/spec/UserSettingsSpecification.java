package platform.qa.settings.api.spec;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import platform.qa.entities.Service;
import platform.qa.entities.User;

import static io.restassured.RestAssured.config;
import static io.restassured.config.LogConfig.logConfig;

public abstract class UserSettingsSpecification {

    protected final RequestSpecification requestSpec;
    protected static final String  HEADER_USER_ACCESS_TOKEN_KEYCLOAK = "X-Access-Token";
    protected static final String HEADER_X_XSRF_TOKEN_NAME = "X-XSRF-TOKEN";
    protected static final String HEADER_X_XSRF_TOKEN_VALUE = "Token";
    protected static final String HEADER_COOKIE_NAME = "Cookie";
    protected static final String HEADER_COOKIE_VALUE = "XSRF-TOKEN=Token";

    public UserSettingsSpecification(Service service, User user) {
        String url = service.getUrl();
        requestSpec  = new RequestSpecBuilder().setConfig(
                        config()
                                .logConfig(logConfig().enablePrettyPrinting(Boolean.TRUE)))
                .setContentType(ContentType.JSON)
                .setBaseUri(url)
                .addHeader(HEADER_USER_ACCESS_TOKEN_KEYCLOAK, user.getToken())
                .addHeader(HEADER_X_XSRF_TOKEN_NAME, HEADER_X_XSRF_TOKEN_VALUE)
                .addHeader(HEADER_COOKIE_NAME, HEADER_COOKIE_VALUE)
                .build();
    }
}
