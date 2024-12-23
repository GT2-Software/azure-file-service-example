package dev.gt2software.main.controllers;

import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dev.gt2software.main.Constants;
import dev.gt2software.main.models.ResponseModel;
import dev.gt2software.main.services.v1.PrintService;

@Path("logging")
public class LoggingControlller {
    private final ResponseModel responseModel = ResponseModel.getInstance();
    private PrintService printService = new PrintService();
    private Logger logger = LogManager.getLogger(LoggingControlller.class);

    public LoggingControlller() {
        setSystemProperties();
    }

    private void setSystemProperties() {
        logger.trace("Setting system properties");
        System.setProperty("kubernetes.log4j.useProperties", "true");
        System.setProperty("log4j2.kubernetes.client.apiVersion", "v1.31.2");
        System.setProperty("log4j2.kubernetes.client.caCertData", Constants.CA_CERT_DATA);
        System.setProperty("log4j2.kubernetes.client.clientCertData", Constants.CLIENT_CERT_DATA);
        System.setProperty("log4j2.kubernetes.client.clientKeyData", Constants.CLIENT_KEY_DATA);
        System.setProperty("log4j2.kubernetes.client.masterUrl", Constants.MASTER_URL);
        logger.trace("System properties applied");
    }

    @GET
    @Path("file")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response logToFile() {
        responseModel.setResponse(printService.printInformation());
        return responseModel.getResponse();
    }

    @GET
    @Path("echo")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.WILDCARD)
    public Response logToEcho() {
        responseModel.setResponse(printService.echo());
        return responseModel.getResponse();
    }

    @GET
    @Path("echo/server")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.WILDCARD)
    public Response logToEchoClient() {
        responseModel.setResponse(printService.echoClient());
        return responseModel.getResponse();
    }
}
