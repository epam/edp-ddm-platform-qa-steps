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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import io.restassured.response.Response;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import platform.qa.data.entities.SignatureDeleteId;
import platform.qa.entities.Redis;
import platform.qa.entities.Service;
import platform.qa.entities.Signature;
import platform.qa.redis.JedisClient;
import platform.qa.rest.RestApiClient;

import java.util.List;
import java.util.UUID;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Steps to work with signatures
 */
@Log4j2
public class SignatureSteps {
    private JedisClient jedisClient;
    private Service digitalOps;
    private List<Redis> signatureRedis;
    private String token;

    public SignatureSteps(Service digitalOps, List<Redis> signatureRedisConfiguration) {
        jedisClient = new JedisClient(signatureRedisConfiguration);
        this.digitalOps = digitalOps;
        this.signatureRedis = signatureRedisConfiguration;
    }

    public SignatureSteps(Service dataFactory, Service digitalSignOps, List<Redis> signatureRedis) {
        this.signatureRedis = signatureRedis;
        this.jedisClient = new JedisClient(this.signatureRedis);
        this.digitalOps = digitalSignOps;
        this.token = dataFactory.getUser().getToken();
    }

    @SneakyThrows
    public <T> String signRequest(T payload) {
        log.info("Підписання payload для запиту");
        ObjectMapper objectMapper = new ObjectMapper();

        Signature signature = new Signature(objectMapper.writeValueAsString(payload));
        String signatureValue = getSignatureValue(signature);
        String signatureKey = UUID.randomUUID().toString();
        String signatureKeyPrefix = "bpm-form-submissions:".concat(signatureKey);

        jedisClient.hset(signatureKeyPrefix, "id", signatureKey);
        jedisClient.hset(signatureKeyPrefix, "signature", signatureValue);
        jedisClient.close();
        return signatureKey;
    }

    @SneakyThrows
    public <T> String signRequestPayloads(T payload) {
        log.info("Підписання payload для даних тіла повідомлення міжпроцесної взаємодії");
        ObjectMapper objectMapper = new ObjectMapper();

        Signature signature = new Signature(objectMapper.writeValueAsString(payload));
        String signatureValue = getSignatureValue(signature);
        String signatureKey = UUID.randomUUID().toString();
        String signatureKeyPrefix = "bpm-form-payloads:".concat(signatureKey);

        jedisClient.hset(signatureKeyPrefix, "id", signatureKey);
        jedisClient.hset(signatureKeyPrefix, "signature", signatureValue);
        jedisClient.close();
        return signatureKey;
    }

    @SneakyThrows
    public String signDeleteRequest(String id) {
        log.info("Підписання payload для delete запиту");

        ObjectMapper objectMapper = new ObjectMapper();
        Signature signature = new Signature(objectMapper.writeValueAsString(new SignatureDeleteId(id)));

        String signatureValue = getSignatureValue(signature);
        String signatureKey = UUID.randomUUID().toString();
        String signatureKeyPrefix = "bpm-form-submissions:".concat(signatureKey);

        jedisClient.hset(signatureKeyPrefix, "id", signatureKey);
        jedisClient.hset(signatureKeyPrefix, "signature", signatureValue);
        jedisClient.close();
        return signatureKey;
    }

    private String getSignatureValue(Signature signature) {
        RestApiClient restApiClient = new RestApiClient(digitalOps.getUrl(), token, UUID.randomUUID().toString());
        Response response = restApiClient.postNegative(signature, "api/eseal/sign");
        assertThat(response.body().asString()).contains("signature");

        return response.jsonPath().getString("signature");
    }
}