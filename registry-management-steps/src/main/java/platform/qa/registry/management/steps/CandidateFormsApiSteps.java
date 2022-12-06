/*
 * Copyright 2022 EPAM Systems.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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


public class CandidateFormsApiSteps extends BaseStep {

    public CandidateFormsApiSteps(Service service) {
        super(service);
    }

    public Form getFormContentByFormNameForVersionCandidate(String formName, String id) {
        return new RestClientProxy(service)
                .positiveRequest()
                .get(Urls.GET_FORM_CONTENT_FOR_SPECIFIC_VERSION.getUrl().replace(FORM_NAME, formName).replace(ID, id),
                        null,
                        new TypeReference<Form>() {}.getType(),
                        HttpStatus.SC_OK
                );
    }

    public Form createFormForVersionCandidateById(Form request, String id, String formName) {
        return new RestClientProxy(service)
                .positiveRequest()
                .post(Urls.CREATE_FORM_CONTENT_FOR_SPECIFIC_VERSION.getUrl().replace(ID, id).replace(FORM_NAME, formName),
                        null,
                        request,
                        new TypeReference<Form>() {}.getType(),
                        HttpStatus.SC_CREATED
                );
    }

    public Form updateFormContentByFormNameForVersionCandidate(Form request, String formName, String id) {
        return new RestClientProxy(service)
                .positiveRequest()
                .put(Urls.UPDATE_FORM_FOR_SPECIFIC_VERSION.getUrl().replace(FORM_NAME, formName).replace(ID, id),
                        null,
                        request,
                        new TypeReference<Form>() {}.getType(),
                        HttpStatus.SC_OK
                );
    }


    public void deleteFormFromVersionCandidate(String formName, String id) {
        new RestClientProxy(service)
                .positiveRequest()
                .delete(Urls.DELETE_FORM_FOR_SPECIFIC_VERSION.getUrl().replace(FORM_NAME, formName).replace(ID, id),
                        HttpStatus.SC_NO_CONTENT
                );
    }

    public List<FormInfo> getFormListFromVersionCandidate(String id) {
        return new RestClientProxy(service)
                .positiveRequest()
                .get(Urls.GET_FORM_LIST_FOR_SPECIFIC_VERSION.getUrl().replace(ID, id),
                        null,
                        new TypeReference<List<FormInfo>>() {}.getType(),
                        HttpStatus.SC_OK
                );
    }
}
