package platform.qa.steps;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import platform.qa.api.FormSchemaProviderApi;
import platform.qa.entities.Service;
import platform.qa.entity.ErrorMessageResponse;

import java.util.Collections;
import java.util.Map;


public class FormSchemaProviderSteps {

    public static final String FORM_SCHEMA_NOT_FOUND = "FORM_SCHEMA_NOT_FOUND";
    private final FormSchemaProviderApi schemaProviderApi;

    public FormSchemaProviderSteps(Service service) {
        schemaProviderApi = new FormSchemaProviderApi(service);
    }

    public boolean isFormWasFoundByName(String formName) {
        return formName.equals(getFormByName(formName).get("name"));
    }

    public Map getFormByName(String formName) {
        Response response = schemaProviderApi.getSearchFormByName(formName);
        int statusCode = response.statusCode();

        if (HttpStatus.SC_OK == statusCode) {
            return response.body().as(Map.class);
        } else if (isTypeErrorFormSchemaNotFound(response)) {
            return Collections.emptyMap();
        } else {
            throw new IllegalStateException(response.prettyPrint());
        }
    }

    private boolean isTypeErrorFormSchemaNotFound(Response response) {
        return HttpStatus.SC_NOT_FOUND == response.statusCode() &&
                FORM_SCHEMA_NOT_FOUND.equals(response.body().as(ErrorMessageResponse.class).getCode());
    }

}
