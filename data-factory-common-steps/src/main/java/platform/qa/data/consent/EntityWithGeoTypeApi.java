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

package platform.qa.data.consent;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.message.ParameterizedMessage;
import platform.qa.data.common.SignatureSteps;
import platform.qa.entities.Ceph;
import platform.qa.entities.Service;
import platform.qa.pojo.consent.EntityAddressLocationResponse;
import platform.qa.pojo.consent.EntityGeoType;
import platform.qa.rest.RestApiClient;

import java.util.List;
import java.util.Map;

@Log4j2
public class EntityWithGeoTypeApi {
    private final String ENTITY_WITH_GEO_TYPE_URL =  "entity-with-geo-type/";
    private final String GET_ENTITY_WITH_ADDRESS_URL =  "get-entity-address-from-table-geo-type/";

    private final Service serviceToDataFactory;
    private final SignatureSteps signatureSteps;

    public EntityWithGeoTypeApi(Service dataFactory, Service digitalSignOps, Ceph signatureCeph) {
        this.serviceToDataFactory = dataFactory;
        this.signatureSteps = new SignatureSteps(dataFactory, digitalSignOps, signatureCeph);
    }

    public String createLocation(EntityGeoType location) {
        log.info(new ParameterizedMessage("Створення  та координат: {}", location));

        String signatureId = this.signatureSteps.signRequest(location);

        return new RestApiClient(serviceToDataFactory, signatureId)
                .post(location, ENTITY_WITH_GEO_TYPE_URL)
                .then().statusCode(201)
                .extract().jsonPath()
                .getString("id");
    }

    public void deleteLocationById(String locationId) {
        log.info(new ParameterizedMessage("DELETE location by id: {}", locationId));
        String signatureId = signatureSteps.signDeleteRequest(locationId);

        new RestApiClient(serviceToDataFactory, signatureId).delete(locationId, ENTITY_WITH_GEO_TYPE_URL);
    }

    public List<EntityAddressLocationResponse> getEntityAddressLocation(@NonNull Map<String, String> params) {
        return new RestApiClient(serviceToDataFactory)
                .sendGetWithParams(GET_ENTITY_WITH_ADDRESS_URL, params)
                .extract().body().as(new TypeReference<List<EntityAddressLocationResponse>>() {
                }.getType());
    }




}
