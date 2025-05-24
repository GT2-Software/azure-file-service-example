package dev.gt2software.main.controllers.fileshare;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.JSONObject;

import com.azure.storage.file.share.models.ShareStorageException;

import dev.gt2software.main.Constants;
import dev.gt2software.main.models.ResponseModel;
import dev.gt2software.main.services.v1.AzureFileShareService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import com.azure.storage.file.share.ShareClient;
import com.azure.storage.file.share.ShareClientBuilder;
import com.azure.storage.file.share.ShareFileClient;
import com.azure.storage.file.share.ShareFileClientBuilder;

@Path("fileshare")
public class AzureFileShareController {

    private final ResponseModel responseModel = ResponseModel.getInstance();
    private final AzureFileShareService fileShareService = AzureFileShareService.getInstance();

    @POST
    @Path("binary/upload")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadBinaryFile(
            @FormDataParam("fileName") String fileName,
            @FormDataParam("shareName") String shareName,
            @FormDataParam("path") String path,
            @FormDataParam("file") InputStream fileInputStream,
            @FormDataParam("file") FormDataContentDisposition fileMetaData) {
        responseModel
                .setResponse(fileShareService.uploadBinaryFileFromFormDataParam(fileName,
                        shareName, path,
                        fileInputStream, fileMetaData));
        return responseModel.getResponse();
    }

}