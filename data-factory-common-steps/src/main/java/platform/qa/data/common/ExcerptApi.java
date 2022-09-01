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

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import platform.qa.entities.Ceph;
import platform.qa.entities.IEntity;
import platform.qa.entities.Service;
import platform.qa.rest.RestApiClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import org.apache.logging.log4j.message.ParameterizedMessage;

/**
 * API to work with excerpts
 */
@Log4j2
public class ExcerptApi {
    private Service excerptService;
    private SignatureSteps signatureSteps;

    private static final String EXCERPTS = "excerpts";
    private static final String EXCERPTS_STATUS = "/status";


    public ExcerptApi(Service excerptService, Service digitalSignOps, Ceph signatureCeph) {
        this.excerptService = excerptService;
        signatureSteps = new SignatureSteps(excerptService, digitalSignOps, signatureCeph);
    }

    public String createExport(IEntity payload) {
        log.info("Виклик API створення витягу");
        String signatureId = signatureSteps.signRequest(payload);

        String excerptIdentifier = new RestApiClient(excerptService, signatureId)
                .postNegative(payload, EXCERPTS)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getString("excerptIdentifier");

        return excerptIdentifier;
    }

    public String getStatusOfExport(String id) {
        log.info(new ParameterizedMessage("Виклик API для отримання статуси за витягом {}", id));
        String status = new RestApiClient(excerptService)
                .get(EXCERPTS + "/" + id + EXCERPTS_STATUS)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getString("status");

        return status;
    }

    @SneakyThrows
    public File getExport(String id) {
        log.info(new ParameterizedMessage("Виклик API для завантаження витягу з id {}", id));
        byte[] bytes = new RestApiClient(excerptService)
                .get(id, EXCERPTS + "/")
                .then()
                .statusCode(200)
                .extract()
                .asByteArray();

        File file = new File("target/report.pdf");
        file.createNewFile();
        OutputStream outStream = new FileOutputStream(file);
        outStream.write(bytes);
        outStream.close();

        return file;
    }
}
