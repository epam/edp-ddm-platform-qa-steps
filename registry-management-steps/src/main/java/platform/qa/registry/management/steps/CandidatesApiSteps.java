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

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.http.HttpStatus;
import platform.qa.entities.Service;
import platform.qa.registry.management.dto.request.CreateVersionCandidateRequest;
import platform.qa.registry.management.dto.response.CreatedVersionResponse;
import platform.qa.registry.management.dto.response.VersionCandidateInfoResponse;
import platform.qa.registry.management.dto.response.VersionInfo;
import platform.qa.registry.management.enumeration.Urls;
import platform.qa.registry.management.steps.api.BaseStep;
import platform.qa.rest.client.impl.RestClientProxy;

import java.util.HashMap;
import java.util.List;

public class CandidatesApiSteps extends BaseStep {

    public CandidatesApiSteps(Service service) {
        super(service);
    }

    public List<VersionInfo> getCandidatesList() {
        return new RestClientProxy(service)
                .positiveRequest()
                .get(Urls.GET_VERSION_CANDIDATES.getUrl(),
                        new HashMap<>(),
                        new TypeReference<List<VersionInfo>>() {}.getType(),
                        HttpStatus.SC_OK
                );
    }

    public CreatedVersionResponse createVersionCandidate(CreateVersionCandidateRequest request) {
        return new RestClientProxy(service)
                .positiveRequest()
                .post(Urls.CREATE_VERSION_CANDIDATE.getUrl(),
                        null,
                        request,
                        new TypeReference<CreatedVersionResponse>() {}.getType(),
                        HttpStatus.SC_CREATED
                );
    }

    public VersionCandidateInfoResponse getVersionCandidateInfo(String id) {
        return new RestClientProxy(service)
                .positiveRequest()
                .get(Urls.GET_VERSION_CANDIDATES_BY_ID.getUrl().replace(ID, id),
                        null,
                        new TypeReference<VersionCandidateInfoResponse>() {}.getType(),
                        HttpStatus.SC_OK
                );
    }
}
