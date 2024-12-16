package dev.gt2software.main.controllers;

import java.util.Base64;
import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobItem;

import dev.gt2software.main.Constants;
import dev.gt2software.main.models.ResponseModel;
import dev.gt2software.main.services.v1.BlobService;

@Path("blob")
public class AzureBlobController {

    private final ResponseModel responseModel = ResponseModel.getInstance();
    private final BlobService blobService = new BlobService();

    public AzureBlobController() {
    }

    @GET
    @Path("download/local")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response downloadFileLocally(@QueryParam("fileName") String fileName,
            @QueryParam("containerName") String containerName, @QueryParam("localPath") Optional<String> localPath) {
        responseModel.setResponse(blobService.DownloadFile(fileName, containerName, localPath));
        return responseModel.getResponse();
    }

    @GET
    @Path("download/client")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response downloadFileToClient(@QueryParam("fileName") String fileName,
            @QueryParam("containerName") String containerName) {
        responseModel.setResponse(blobService.DownloadFileToClient(fileName, containerName));
        return responseModel.getResponse();
    }

    @PUT
    @Path("write/file/text")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response writeToBlobTextFile(@QueryParam("fileName") String fileName,
            @QueryParam("containerName") String containerName) {
        responseModel.setResponse(blobService.WriteToBlobTextFile(fileName, containerName));
        return responseModel.getResponse();
    }

    @Path("upload")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String uploadBlob(String content) {
        JSONObject json = new JSONObject(content);
        String fileB64 = json.getString("fileB64");
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(Constants.AZ_ST_ACC_CONN_STRING)
                .buildClient();
        // get binary data from fileB64
        byte[] data = Base64.getDecoder().decode(fileB64);
        BinaryData binaryData = BinaryData.fromBytes(data);

        blobServiceClient.getBlobContainerClient("contenedor-01")
                .getBlobClient("path/to/file/file-uploaded.txt").upload(binaryData, true);
        return "check blob";
    }

    @Path("upload/esim/qr")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response uploadBlobEsim(String content) {
        responseModel.setResponse(blobService.uploadImageFromBase64(content));
        return responseModel.getResponse();
    }

    @Path("getBlobs")
    @GET
    public String listBlobs() {
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(
                        "DefaultEndpointsProtocol=https;AccountName=staccportals01;AccountKey=H6It4V/JoIWJUrpUMQ4/hWvd1w+RMmy88KiQEWe2QUU1CQwT8oDfw5iyf62h5/fpEODjEozrLqYe+ASt0rE57A==;EndpointSuffix=core.windows.net")
                .buildClient();
        for (BlobItem blobItem : blobServiceClient.getBlobContainerClient("contenedor-01").listBlobs()) {
            System.out.println("\t" + blobItem.getName());
        }
        return "check console";
    }

    @Path("deleteBlob")
    @DELETE
    public String deleteBlob(@QueryParam("blobName") String blobName) {
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(
                        "DefaultEndpointsProtocol=https;AccountName=staccportals01;AccountKey=H6It4V/JoIWJUrpUMQ4/hWvd1w+RMmy88KiQEWe2QUU1CQwT8oDfw5iyf62h5/fpEODjEozrLqYe+ASt0rE57A==;EndpointSuffix=core.windows.net")
                .buildClient();
        blobServiceClient.getBlobContainerClient("contenedor-01").getBlobClient(blobName).deleteIfExists();
        for (BlobItem blobItem : blobServiceClient.getBlobContainerClient("contenedor-01").listBlobs()) {
            System.out.println("\t" + blobItem.getName());
        }
        return "check console";
    }

}
