package dev.gt2software.main.controllers.queue;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import dev.gt2software.main.models.ResponseModel;
import dev.gt2software.main.services.v2.AzureQueueService;

@Path("queue")
public class AzureQueueController {
    private final ResponseModel responseModel = ResponseModel.getInstance();
    private final AzureQueueService azureQueueService = new AzureQueueService();

    @POST
    @Path("get")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getQueue(String jsonContent) {
        responseModel.setResponse(azureQueueService.getMessageResult(jsonContent));
        return responseModel.getResponse();
    }

    @POST
    @Path("create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createQueue(String jsonContent) {
        responseModel.setResponse(azureQueueService.createQueue(jsonContent));
        return responseModel.getResponse();
    }

    @POST
    @Path("send")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendQueue(String jsonContent) {
        responseModel.setResponse(azureQueueService.sendMessage(jsonContent));
        return responseModel.getResponse();
    }
}
