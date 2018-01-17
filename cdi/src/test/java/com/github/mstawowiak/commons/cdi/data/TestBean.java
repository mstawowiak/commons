package com.github.mstawowiak.commons.cdi.data;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TestBean {

    private String status;

    @PostConstruct
    public void init() {
        status = "INITIALIZED";
    }

    public String getStatus() {
        return status;
    }
}
