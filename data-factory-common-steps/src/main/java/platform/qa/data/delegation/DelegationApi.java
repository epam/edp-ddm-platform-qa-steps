package platform.qa.data.delegation;

import lombok.extern.log4j.Log4j2;
import platform.qa.data.common.SignatureSteps;
import platform.qa.entities.Redis;
import platform.qa.entities.Service;
import platform.qa.pojo.delegation.DelegationAuthorizedPerson;
import platform.qa.pojo.delegation.DelegationLicense;
import platform.qa.pojo.delegation.DelegationOrganization;
import platform.qa.rest.RestApiClient;

import org.apache.logging.log4j.message.ParameterizedMessage;

@Log4j2
public class DelegationApi {

    private final String DELEGATION_ORG_URL = "delegation-organization/";
    private final String DELEGATION_LICENSE_URL = "delegation-license/";
    private final String DELEGATION_AUTH_PERSON_URL = "delegation-authorized-person/";

    private SignatureSteps signatureSteps;
    private Service dataFactory;

    public DelegationApi(Service dataFactory, Service digitalSignOps, Redis signatureRedis) {
        this.dataFactory = dataFactory;
        signatureSteps = new SignatureSteps(dataFactory, digitalSignOps, signatureRedis);
    }

    public DelegationOrganization getOrganization(String orgId) {
        log.info(new ParameterizedMessage("Отримання інформації по організації за id: {}", orgId));
        return new RestApiClient(dataFactory)
                .get(orgId, DELEGATION_ORG_URL)
                .then()
                .statusCode(200)
                .extract()
                .as(DelegationOrganization.class);
    }

    public String createOrganization(DelegationOrganization organization) {
        log.info(new ParameterizedMessage("Створення організації та перевірка наявності у головній БД: {}",
                organization));
        String id = signatureSteps.signRequest(organization);

        return new RestApiClient(dataFactory, id)
                .post(organization, DELEGATION_ORG_URL)
                .then()
                .statusCode(201)
                .extract()
                .jsonPath()
                .get("id");
    }

    public void deleteOrganization(String orgId) {
        log.info(new ParameterizedMessage("Видалення інформації по організації за id: {}", orgId));
        String signatureId = signatureSteps.signDeleteRequest(orgId);

        new RestApiClient(dataFactory, signatureId).delete(orgId, DELEGATION_ORG_URL);
    }

    public DelegationLicense getLicense(String licenseId) {
        log.info(new ParameterizedMessage("Отримання інформації по ліцензії за id: {}", licenseId));
        return new RestApiClient(dataFactory)
                .get(licenseId, DELEGATION_LICENSE_URL)
                .then()
                .statusCode(200)
                .extract()
                .as(DelegationLicense.class);
    }

    public String createLicense(DelegationLicense license) {
        log.info(new ParameterizedMessage("Створення ліцензії та перевірка наявності у головній БД: {}", license));
        String id = signatureSteps.signRequest(license);

        return new RestApiClient(dataFactory, id)
                .post(license, DELEGATION_LICENSE_URL)
                .then()
                .statusCode(201)
                .extract()
                .jsonPath()
                .get("id");
    }

    public void deleteLicense(String licenseId) {
        log.info(new ParameterizedMessage("Видалення інформації по ліцензії за id: {}", licenseId));
        String signatureId = signatureSteps.signDeleteRequest(licenseId);

        new RestApiClient(dataFactory, signatureId).delete(licenseId, DELEGATION_LICENSE_URL);
    }

    public DelegationAuthorizedPerson getAuthPerson(String personId) {
        log.info(new ParameterizedMessage("Отримання інформації по уповноваженню користувача за id: {}", personId));
        return new RestApiClient(dataFactory)
                .get(personId, DELEGATION_AUTH_PERSON_URL)
                .then()
                .statusCode(200)
                .extract()
                .as(DelegationAuthorizedPerson.class);
    }

    public String createAuthPerson(DelegationAuthorizedPerson person) {
        log.info(new ParameterizedMessage("Створення уповноваження користувача та перевірка наявності у головній БД: "
                + "{}", person));
        String id = signatureSteps.signRequest(person);

        return new RestApiClient(dataFactory, id)
                .post(person, DELEGATION_AUTH_PERSON_URL)
                .then()
                .statusCode(201)
                .extract()
                .jsonPath()
                .get("id");
    }

    public void deleteAuthPerson(String personId) {
        log.info(new ParameterizedMessage("Видалення інформації по уповноваженому користувачу за id: {}", personId));
        String signatureId = signatureSteps.signDeleteRequest(personId);

        new RestApiClient(dataFactory, signatureId).delete(personId, DELEGATION_AUTH_PERSON_URL);
    }
}
