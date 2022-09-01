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

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import lombok.extern.log4j.Log4j2;
import platform.qa.entities.Ceph;
import platform.qa.entities.Service;
import platform.qa.entities.Subject;
import platform.qa.pojo.common.SubjectProfile;
import platform.qa.rest.RestApiClient;

import org.apache.logging.log4j.message.ParameterizedMessage;

/**
 * Api to work with subjects
 */
@Log4j2
public class SubjectApi {
    private final String USER_SUBJECT_SETTINGS_URL = "subject-settings/";
    private final String USER_SUBJECT_URL = "subject/";
    private SignatureSteps signatureSteps;
    private Service dataFactory;


    public SubjectApi(Service dataFactory, Service digitalSignOps, Ceph signatureCeph) {
        this.dataFactory = dataFactory;
        signatureSteps = new SignatureSteps(dataFactory, digitalSignOps, signatureCeph);
    }

    @Deprecated
    public String createUserSubject(Subject subject) {
        log.info(new ParameterizedMessage("Створення налаштувань кабінету людини user subject {}", subject));
        String id = signatureSteps.signRequest(subject);

        return new RestApiClient(dataFactory, id)
                .post(subject, USER_SUBJECT_URL)
                .then()
                .statusCode(201)
                .extract()
                .jsonPath()
                .get("id");
    }

    public String createSubject(Subject subject) {
        log.info(new ParameterizedMessage("Створення суб'єкту користувача user {}", subject));
        String id = signatureSteps.signRequest(subject);

        String subjectId = new RestApiClient(dataFactory, id).post(subject, USER_SUBJECT_URL)
                .then().statusCode(201).extract().jsonPath().getString("id");
        subject.setSubjectId(subjectId);
        return subjectId;
    }

    public ExtractableResponse<Response> tryToCreateSubject(Subject subject) {
        log.info(new ParameterizedMessage("Створення суб'єкту користувача user {}", subject));
        String id = signatureSteps.signRequest(subject);

        return new RestApiClient(dataFactory, id).postNegative(subject, USER_SUBJECT_URL)
                .then().extract();
    }

    public String createUserSubjectSettingsProfile(SubjectProfile subjectProfile) {
        log.info(new ParameterizedMessage("Створення налаштувань кабінету людини user subjectProfile {}",
                subjectProfile.getSubjectId()));
        String id = signatureSteps.signRequest(subjectProfile);

        return new RestApiClient(dataFactory, id)
                .post(subjectProfile, USER_SUBJECT_SETTINGS_URL)
                .then()
                .statusCode(201)
                .extract()
                .jsonPath()
                .get("id");
    }

    public Subject getUserSubject(String subjectId) {
        log.info(new ParameterizedMessage("Створення налаштувань кабінету людини user subject {}", subjectId));
        return new RestApiClient(dataFactory).get(subjectId, USER_SUBJECT_URL)
                .then().statusCode(200).extract().as(Subject.class);
    }

    public SubjectProfile getUserSubjectSettingsProfile(String profileId) {
        log.info(new ParameterizedMessage("Створення налаштувань кабінету людини user subjectProfile {}", profileId));
        return new RestApiClient(dataFactory).get(profileId, USER_SUBJECT_SETTINGS_URL)
                .then().statusCode(200).extract().as(SubjectProfile.class);
    }

    public void deleteUserSubjectSettingsProfile(String profileId) {
        log.info(new ParameterizedMessage("Видалення налаштувань кабінету людини user subjectProfile {}", profileId));
        String id = signatureSteps.signDeleteRequest(profileId);

        new RestApiClient(dataFactory, id).delete(profileId, USER_SUBJECT_SETTINGS_URL);
    }

    public void deleteUserSubject(String subjectId) {
        log.info(new ParameterizedMessage("Видалення налаштувань кабінету людини user subject {}", subjectId));
        String id = signatureSteps.signDeleteRequest(subjectId);

        new RestApiClient(dataFactory, id).delete(subjectId, USER_SUBJECT_URL);
    }

    public Subject createSubjectIfNotExisted(Subject subject) {
        log.info(new ParameterizedMessage("Створення суб'єкту користувача якщо від відсутній у БД user {}",
                subject.getSubjectName()));
        String id = "";
        var response = tryToCreateSubject(subject);
        if (response.statusCode() == 201) {
            id = response.jsonPath().getString("id");
            subject.setSubjectId(id);
            return subject;
        }

        if (response.statusCode() == 409) {
            Subject searchData = new SubjectSearchConditions(dataFactory)
                    .subjectEqual(subject.getSubjectType(), subject.getSubjectCode());
            id = searchData.getSubjectId();
            subject.setSubjectId(id);
            return subject;
        }
        return null;
    }

}
