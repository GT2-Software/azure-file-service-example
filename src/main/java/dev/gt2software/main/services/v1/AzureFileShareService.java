package dev.gt2software.main.services.v1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.json.JSONException;
import org.json.JSONObject;

import com.azure.core.http.rest.Response;
import com.azure.core.util.Context;
import com.azure.storage.common.ParallelTransferOptions;
import com.azure.storage.file.share.ShareFileClient;
import com.azure.storage.file.share.ShareFileClientBuilder;
import com.azure.storage.file.share.models.ShareFileUploadInfo;
import com.azure.storage.file.share.models.ShareStorageException;

import dev.gt2software.main.Constants;

public class AzureFileShareService {
    private final Logger logger = LogManager.getLogger(AzureFileShareService.class);

    public static AzureFileShareService getInstance() {
        return new AzureFileShareService();
    }

    /**
     * Metodo que verifica si el nombre del archivo es invalido
     * 
     * @param filename
     * @return boolean
     */
    private boolean isInvalidFilename(String filename) {
        return filename.contains("..") || filename.contains("\\");
    }

    public JSONObject uploadBinaryFileFromFormDataParam(String fileName, String shareName, String path,
            InputStream fileInputStream, FormDataContentDisposition fileMetaData) {
        try {
            if (isInvalidFilename(fileMetaData.getFileName())) {
                logger.debug("Throw Invalid filename error: {}", fileMetaData.getFileName());
                throw new JSONException("Invalid filename. Must not contain '..' or '\\' characters");
            }
            logger.info("ShareName: {}", shareName);
            logger.info("Path: {}", path);
            logger.info(Constants.AZ_ST_ACC_NAME);
            String fileURL = String.format("https://%s.file.core.windows.net", Constants.AZ_ST_ACC_NAME);
            ShareFileClient fileClient = new ShareFileClientBuilder()
                    .connectionString(Constants.AZ_ST_ACC_CONN_STRING)
                    .endpoint(fileURL)
                    .shareName(shareName)
                    .resourcePath(path + "/" + fileName)
                    .buildFileClient();
            logger.info("Account name obtained: {}", fileClient.getAccountName());
            logger.info("Share name obtained: {}", fileClient.getShareName());
            logger.info("File exists: {}", fileClient.exists());
            fileClient.deleteIfExists();

            long chunkSize = 4 * 1024 * 1024L;
            ParallelTransferOptions parallelTransferOptions = new ParallelTransferOptions()
                    .setBlockSizeLong(chunkSize) // 4MB
                    .setMaxConcurrency(1); // 1 concurrent uploads

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[4];

            while ((nRead = fileInputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();
            byte[] targetArray = buffer.toByteArray();
            InputStream byteArrayInputStream = new ByteArrayInputStream(targetArray);

            logger.debug("File input stream: {}", targetArray.length);

            long fileSize = targetArray.length;
            logger.debug("File size: {}", fileSize);
            fileClient.create(fileSize);

            String fileNameM = fileMetaData.getFileName();
            logger.debug("File name: {}", fileNameM);

            if (fileSize > chunkSize) {
                for (int offset = 0; offset < targetArray.length; offset += chunkSize) {
                    try {
                        // the last chunk size is smaller than the others
                        chunkSize = Math.min(targetArray.length - offset, chunkSize);

                        // select the chunk in the byte array
                        byte[] subArray = Arrays.copyOfRange(targetArray, offset, (int) (offset + chunkSize));

                        // upload the chunk
                        fileClient.uploadWithResponse(
                                new ByteArrayInputStream(subArray), chunkSize, (long) offset,
                                null, Context.NONE);
                        logger.debug("Chunk {} uploaded of size {}", offset, chunkSize);
                    } catch (RuntimeException e) {
                        logger.error("Failed to upload the file", e);
                        if (Boolean.TRUE.equals(fileClient.exists())) {
                            fileClient.delete();
                        }
                        throw e;
                    }
                }
            } else {
                ShareFileUploadInfo response = fileClient.upload(byteArrayInputStream,
                        fileSize,
                        parallelTransferOptions);
                logger.debug("File {} uploaded {}", fileName, response.getETag());
            }

            // Return success response
            return new JSONObject()
                    .put("code", 200)
                    .put("status", "success")
                    .put("message", "File uploaded successfully");
        } catch (ShareStorageException | IOException e) {
            logger.error("Server error: {}", e);
            return new JSONObject()
                    .put("code", 500)
                    .put("status", "error")
                    .put("message", "Server error");
        } catch (JSONException e) {
            logger.error("Client error: {}", 3);
            return new JSONObject()
                    .put("code", 400)
                    .put("status", "error")
                    .put("message", "Client error");
        }
    }

}
