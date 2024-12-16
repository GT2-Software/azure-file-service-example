package dev.gt2software.main.controllers;

import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import dev.gt2software.main.models.ResponseModel;
import dev.gt2software.main.services.v1.PrintService;

@Path("logging")
public class LoggingControlller {
    private final ResponseModel responseModel = ResponseModel.getInstance();
    private PrintService printService = new PrintService();

    public LoggingControlller() {
    }

    @GET
    @Path("file")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response logToFile() {
        responseModel.setResponse(printService.printInformation());
        return responseModel.getResponse();
    }
}
