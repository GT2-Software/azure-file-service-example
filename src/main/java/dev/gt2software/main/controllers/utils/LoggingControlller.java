package dev.gt2software.main.controllers.utils;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dev.gt2software.main.models.ResponseModel;
import dev.gt2software.main.services.v1.PrintService;

@Path("version")
public class LoggingControlller {
    private final ResponseModel responseModel = ResponseModel.getInstance();
    private PrintService printService = new PrintService();
    private Logger logger = LogManager.getLogger(LoggingControlller.class);

    public LoggingControlller() {
    }

    @GET
    // @Path("echo")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.WILDCARD)
    public Response logToEchoClient() {
        responseModel.setResponse(printService.echoClient());
        return responseModel.getResponse();
    }
}
