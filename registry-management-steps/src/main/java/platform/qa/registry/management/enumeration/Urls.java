package platform.qa.registry.management.enumeration;

public enum Urls {
    //Version candidate forms
    GET_VERSION_CANDIDATES("/versions/candidates"),
    CREATE_VERSION_CANDIDATE("/versions/candidates"),
    GET_VERSION_CANDIDATES_BY_ID("/versions/candidates/{id}"),
    GET_FORM_LIST_FOR_SPECIFIC_VERSION("/versions/candidates/{id}/forms"),
    GET_FORM_CONTENT_FOR_SPECIFIC_VERSION("/versions/candidates/{id}/forms/{formName}"),
    CREATE_FORM_CONTENT_FOR_SPECIFIC_VERSION("/versions/candidates/{id}/forms/{formName}"),
    UPDATE_FORM_FOR_SPECIFIC_VERSION("versions/candidates/{id}/forms/{formName}"),
    DELETE_FORM_FOR_SPECIFIC_VERSION("versions/candidates/{id}/forms/{formName}"),



    //Master forms
    GET_MASTER_VERSIONS("/versions/master/"),
    GET_FORM_LIST_FOR_MASTER_VERSION("/versions/master/forms"),
    GET_FORM_CONTENT_FOR_MASTER_VERSION("/versions/master/forms/{formName}"),
    CREATE_FORM_FOR_MASTER_VERSION("/versions/master/forms/{formName}"),
    UPDATE_FORM_FOR_MASTER_VERSION("/versions/master/forms/{formName}"),
    DELETE_FORM_FOR_MASTER_VERSION("/versions/master/forms/{formName}"),


    //Master business processes
    GET_BP_LIST_FROM_MATER_VERSION("/versions/master/business-processes"),
    GET_BP_CONTENT_FROM_MASTER_VERSION("/versions/master/business-processes/{bpName}"),
    CREATE_BP_FOR_MASTER_VERSION("/versions/master/business-processes/{bpName}"),
    UPDATE_BP_FOR_MASTER_VERSION("/versions/master/business-processes/{bpName}"),
    DELETE_BP_FOR_MASTER_VERSION("/versions/master/business-processes/{bpName}");

    private String url;

    Urls(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
