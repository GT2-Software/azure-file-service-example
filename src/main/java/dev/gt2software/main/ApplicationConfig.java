package dev.gt2software.main;

import java.util.Set;

import jakarta.ws.rs.core.Application;

import org.glassfish.jersey.media.multipart.MultiPartFeature;

@jakarta.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        resources.add(MultiPartFeature.class);
        addRestResourceClasses(resources);
        return resources;
    }

    private void addRestResourceClasses(Set<Class<?>> resources) {

        resources.add(dev.gt2software.main.controllers.blob.AzureBlobController.class);
        resources.add(dev.gt2software.main.controllers.queue.AzureQueueController.class);
    }
}
