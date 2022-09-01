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

package platform.qa.data.common;

import io.restassured.response.ValidatableResponse;
import platform.qa.entities.Ceph;
import platform.qa.entities.Service;
import platform.qa.pojo.common.Settings;
import platform.qa.rest.RestApiClient;

/**
 * Api to manipulate user settings
 */
public class UserSettingsApi {
    private final String USER_SETTINGS_URL = "settings/";
    private RestApiClient client;
    private SignatureSteps signatureSteps;
    private Service dataFactory;

    public UserSettingsApi(Service dataFactory, Service digitalSignOps, Ceph signatureCeph) {
        this.dataFactory = dataFactory;
        signatureSteps = new SignatureSteps(dataFactory, digitalSignOps, signatureCeph);
    }

    public ValidatableResponse getUserSettings() {
        return new RestApiClient(dataFactory).get(USER_SETTINGS_URL)
                .then().statusCode(200);
    }

    public String putUserSettings(Settings settings) {
        String id = signatureSteps.signRequest(settings);

        return new RestApiClient(dataFactory, id).put(settings, USER_SETTINGS_URL).body().jsonPath().getString(
                "settings_id");
    }
}
