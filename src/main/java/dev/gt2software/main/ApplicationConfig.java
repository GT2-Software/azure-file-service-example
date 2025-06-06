package dev.gt2software.main;

import java.util.Set;

import javax.ws.rs.core.Application;

@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    private void addRestResourceClasses(Set<Class<?>> resources) {

        resources.add(dev.gt2software.main.controllers.AzureBlobController.class);
        resources.add(dev.gt2software.main.controllers.LoggingControlller.class);
    }
}
