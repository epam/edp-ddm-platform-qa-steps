package platform.qa.registry.management.steps.api;

import platform.qa.entities.Service;

public abstract class BaseStep {
    protected final Service service;

    protected static final String FORM_NAME = "{formName}";
    protected static final String BP_NAME = "{bpName}";
    protected static final String ID = "{id}";

    public BaseStep(Service service) {
        this.service = service;
    }
}
