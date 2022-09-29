package platform.qa.registry.management.steps;

import platform.qa.entities.Service;
import platform.qa.registry.management.dto.request.form.Form;
import platform.qa.registry.management.dto.response.FormInfo;
import platform.qa.registry.management.enumeration.Urls;
import platform.qa.registry.management.steps.api.BaseStep;
import platform.qa.rest.client.impl.RestClientProxy;

import java.util.List;
import org.apache.http.HttpStatus;
import com.fasterxml.jackson.core.type.TypeReference;


public class MasterFormsApiSteps extends BaseStep {

    public MasterFormsApiSteps(Service service) {
        super(service);
    }

    public List<FormInfo> getFormListFromMasterVersion() {
        return new RestClientProxy(service)
                .positiveRequest()
                .get(Urls.GET_FORM_LIST_FOR_MASTER_VERSION.getUrl(),
                        null,
                        new TypeReference<List<FormInfo>>() {}.getType(),
                        HttpStatus.SC_OK
                );
    }

    public Form getFormContentFromMasterVersion(String formName) {
        return new RestClientProxy(service)
                .positiveRequest()
                .get(Urls.GET_FORM_CONTENT_FOR_MASTER_VERSION.getUrl().replace(FORM_NAME, formName),
                        null,
                        new TypeReference<Form>() {}.getType(),
                        HttpStatus.SC_OK
                );
    }
}