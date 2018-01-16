package com.github.mstawowiak.commons.rest;

public interface Service {

    default String getName() {
        return this.getClass().getName();
    }
}
