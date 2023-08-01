package platform.qa.registry.management.steps;

import com.fasterxml.jackson.core.type.TypeReference;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import platform.qa.entities.Service;
import platform.qa.registry.management.dto.request.bp.BusinessProcess;
import platform.qa.registry.management.dto.request.bp.BusinessProcessData;
import platform.qa.registry.management.dto.request.form.Form;
import platform.qa.registry.management.dto.response.EntityInfo;
import platform.qa.registry.management.enumeration.Urls;
import platform.qa.registry.management.steps.api.BaseStep;
import platform.qa.rest.client.impl.RestClientProxy;

import java.util.List;
import java.util.Map;

public class MasterBusinessProcessApiSteps extends BaseStep {

    public MasterBusinessProcessApiSteps(Service service) {
        super(service);
    }

    public List<EntityInfo> getBusinessProcesses() {
        return new RestClientProxy(service)
                .positiveRequest()
                .get(Urls.GET_BP_LIST_FROM_MATER_VERSION.getUrl(),
                        null,
                        new TypeReference<List<EntityInfo>>() {
                        }.getType(),
                        HttpStatus.SC_OK
                );
    }

    public String getBusinessProcessContent(String bpName) {
        return new RestClientProxy(service)
                .positiveRequest()
                .get(Urls.GET_BP_CONTENT_FROM_MASTER_VERSION.getUrl().replace(BP_NAME, bpName),
                        null,
                        new TypeReference<String>() {
                        }.getType(),
                        HttpStatus.SC_OK
                );
    }

    public String createBpInMasterVersion(String content, String bpName) {
        return new RestClientProxy(service)
                .positiveRequest()
                .post(Urls.CREATE_BP_FOR_MASTER_VERSION.getUrl().replace(BP_NAME, bpName),
                        null,
                        content,
                        new TypeReference<String>() {
                        }.getType(),
                        HttpStatus.SC_CREATED
                );
    }

    public String updateBpInMasterVersion(String content, String bpName, Map<String,String> headers) {
        return new RestClientProxy(service)
                .positiveRequest(ContentType.XML)
                .put(Urls.UPDATE_BP_FOR_MASTER_VERSION.getUrl().replace(BP_NAME, bpName),
                        null,
                        content,
                        new TypeReference<String>() {
                        }.getType(),
                        HttpStatus.SC_OK,
                        headers
                );
    }

    public void deleteBusinessProcess(String bpName, Map<String,String> headers) {
        new RestClientProxy(service)
                .positiveRequest(ContentType.XML)
                .delete(Urls.DELETE_BP_FOR_MASTER_VERSION.getUrl().replace(BP_NAME, bpName),
                        HttpStatus.SC_NO_CONTENT,
                        headers
                );
    }
}
