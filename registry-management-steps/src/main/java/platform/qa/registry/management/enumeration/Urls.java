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
    GET_FORM_CONTENT_FOR_MASTER_VERSION("/versions/master/forms/{formName}");

    private String url;

    Urls(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
