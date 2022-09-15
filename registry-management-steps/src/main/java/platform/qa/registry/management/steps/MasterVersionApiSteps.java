package platform.qa.registry.management.steps;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.http.HttpStatus;
import platform.qa.entities.Service;
import platform.qa.registry.management.dto.response.FormInfo;
import platform.qa.registry.management.dto.response.MasterVersionInfoResponse;
import platform.qa.registry.management.enumeration.Urls;
import platform.qa.registry.management.steps.api.BaseStep;
import platform.qa.rest.client.impl.RestClientProxy;

import java.util.List;


public class MasterVersionApiSteps extends BaseStep {

    public MasterVersionApiSteps(Service service) {
        super(service);
    }

    public List<FormInfo> getFormListFromMaster() {
        return new RestClientProxy(service)
                .positiveRequest()
                .get(Urls.GET_FORM_LIST_FOR_MASTER_VERSION.getUrl(),
                        null,
                        new TypeReference<List<FormInfo>>() {}.getType(),
                        HttpStatus.SC_OK
                );
    }

    public MasterVersionInfoResponse getMasterVersionInfo() {
        return new RestClientProxy(service)
                .positiveRequest()
                .get(Urls.GET_MASTER_VERSIONS.getUrl(),
                        null,
                        new TypeReference<MasterVersionInfoResponse>() {}.getType(),
                        HttpStatus.SC_OK
                );
    }
}
