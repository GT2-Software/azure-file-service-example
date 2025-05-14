package dev.gt2software.main.services.v1;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.core.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.azure.core.util.BinaryData;
import com.azure.core.util.Context;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.options.BlobDownloadToFileOptions;
import com.azure.storage.blob.specialized.AppendBlobClient;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import dev.gt2software.main.Constants;
import dev.gt2software.main.services.v2.LogService;
import dev.gt2software.main.services.v2.LogToBlobService;

public class BlobService {
    // private Logger logger = LogToBlobService.getInstance();
    private static final Logger _logger = LogService.getAppEventInstance();

    public BlobService() {
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

    /**
     * Metodo que descarga un archivo de un contenedor de Azure Blob Storage
     * 
     * @param fileName
     * @param containerName
     * @param localPath
     * @return
     */
    public JSONObject DownloadFile(String fileName, String containerName, Optional<String> localPath) {
        try {
            if (isInvalidFilename(fileName)) {
                throw new JSONException("Invalid filename. Must not contain '..' or '\\' characters");
            }
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .connectionString(Constants.AZ_ST_ACC_CONN_STRING)
                    .buildClient();
            // saves file to local path if no path to download is provided
            BlobDownloadToFileOptions blobDownloadToFileOptions = new BlobDownloadToFileOptions(
                    localPath.orElse(String.format("./%s", fileName)));
            blobServiceClient.getBlobContainerClient(containerName)
                    .getBlobClient(fileName)
                    .downloadToFileWithResponse(blobDownloadToFileOptions, Duration.ofSeconds(30), Context.NONE);
            return new JSONObject()
                    .put("message", String.format("Check file downloaded: %s", fileName))
                    .put("code", 200);
        } catch (UncheckedIOException e) {
            return new JSONObject()
                    .put("message", e.getMessage())
                    .put("code", 500);
        } catch (JSONException e) {
            return new JSONObject()
                    .put("message", e.getMessage())
                    .put("code", 400);
        }
    }

    public JSONObject DownloadFileToClient(String fileName, String containerName) {
        try {
            if (isInvalidFilename(fileName)) {
                throw new JSONException("Invalid filename. Must not contain '..' or '\\' characters");
            }
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .connectionString(Constants.AZ_ST_ACC_CONN_STRING)
                    .buildClient();
            // saves file to local path if and then deletes it
            BlobDownloadToFileOptions blobDownloadToFileOptions = new BlobDownloadToFileOptions(
                    String.format("./%s", fileName));
            blobServiceClient.getBlobContainerClient(containerName)
                    .getBlobClient(fileName)
                    .downloadToFileWithResponse(blobDownloadToFileOptions, Duration.ofSeconds(30), Context.NONE);
            return new JSONObject()
                    .put("message", String.format("Check file downloaded: %s", fileName))
                    .put("code", 200);
        } catch (UncheckedIOException e) {
            return new JSONObject()
                    .put("message", e.getMessage())
                    .put("code", 500);
        } catch (JSONException e) {
            return new JSONObject()
                    .put("message", e.getMessage())
                    .put("code", 400);
        }
    }

    public JSONObject WriteToBlobTextFile(String fileName, String containerName) {
        try {
            if (isInvalidFilename(fileName)) {
                throw new JSONException("Invalid filename. Must not contain '..' or '\\' characters");
            }
            BlobContainerClient blobContainerClient = new BlobServiceClientBuilder()
                    .connectionString(Constants.AZ_ST_ACC_CONN_STRING)
                    .buildClient()
                    .getBlobContainerClient(containerName);
            // logger.info("Creando archivo en Azure Blob Storage");
            LogToBlobService.info("Creando archivo en Azure Blob Storage");
            AppendBlobClient appendBlobClient = blobContainerClient.getBlobClient(fileName).getAppendBlobClient();
            appendBlobClient.createIfNotExists();
            // appendBlobClient.appendBlock(new ByteArrayInputStream("Hello,
            // World!".getBytes()), 0);
            // BlockBlobClient blockClient = blobContainerClient.getBlobClient(fileName)
            // .getBlockBlobClient();
            try (ByteArrayInputStream dataStream = new ByteArrayInputStream("Hello, World!".getBytes())) {
                // blockClient.upload(dataStream, dataStream.available(), false);
                appendBlobClient.appendBlock(dataStream, dataStream.available());
            } catch (IOException ex) {
                ex.printStackTrace();
                throw new UncheckedIOException(ex);
            }
            return new JSONObject()
                    .put("message", String.format("Check file downloaded: %s", fileName))
                    .put("code", 200);
        } catch (UncheckedIOException e) {
            return new JSONObject()
                    .put("message", e.getMessage())
                    .put("code", 500);
        } catch (JSONException e) {
            return new JSONObject()
                    .put("message", e.getMessage())
                    .put("code", 400);
        }
    }

    public JSONObject WriteToBlobTextFile(String fileName, String containerName, String message) {
        try {
            if (isInvalidFilename(fileName)) {
                throw new JSONException("Invalid filename. Must not contain '..' or '\\' characters");
            }
            BlobContainerClient blobContainerClient = new BlobServiceClientBuilder()
                    .connectionString(Constants.AZ_ST_ACC_CONN_STRING)
                    .buildClient()
                    .getBlobContainerClient(containerName);

            AppendBlobClient appendBlobClient = blobContainerClient.getBlobClient(fileName).getAppendBlobClient();
            appendBlobClient.createIfNotExists();
            // appendBlobClient.appendBlock(new ByteArrayInputStream("Hello,
            // World!".getBytes()), 0);
            // BlockBlobClient blockClient = blobContainerClient.getBlobClient(fileName)
            // .getBlockBlobClient();
            try (ByteArrayInputStream dataStream = new ByteArrayInputStream(message.getBytes())) {
                // blockClient.upload(dataStream, dataStream.available(), false);
                appendBlobClient.appendBlock(dataStream, dataStream.available());

            } catch (IOException ex) {
                ex.printStackTrace();
                throw new UncheckedIOException(ex);
            }
            return new JSONObject()
                    .put("message", String.format("Check file downloaded: %s", fileName))
                    .put("code", 200);
        } catch (UncheckedIOException e) {
            return new JSONObject()
                    .put("message", e.getMessage())
                    .put("code", 500);
        } catch (JSONException e) {
            return new JSONObject()
                    .put("message", e.getMessage())
                    .put("code", 400);
        }
    }

    public JSONObject uploadImageFromBase64(String base64QRCode) {
        try {

            if (base64QRCode == null || base64QRCode.isEmpty()) {
                throw new JSONException("El texto base64 no puede ser nulo o vacío.");
            }

            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .connectionString(Constants.AZ_ST_ACC_CONN_STRING)
                    .buildClient();

            // get binary data from fileB64
            byte[] data = Base64.getDecoder().decode(base64QRCode);
            // byte[] data = Base64.getDecoder().decode(fileB64);
            BinaryData binaryData = BinaryData.fromBytes(data);

            // se debe reemplazar el nombre del archivo por el que se desee crear por el
            // numero de orden
            // el contenedor $web se especializa en contenido estatico
            blobServiceClient.getBlobContainerClient("$web")
                    .getBlobClient(String.format("esim/images/%s.jpg", "orderId"))
                    .upload(binaryData, true);

            return new JSONObject()
                    .put("message", String.format("%s/esim/images/%s.jpg", "https://", "orderId"))
                    .put("code", 200);
        } catch (JSONException e) {
            return new JSONObject()
                    .put("message", e.getMessage())
                    .put("code", 400);
        }
    }

    public JSONObject uploadBinaryFileFromFormDataParam(String fileName, String containerName,
            InputStream fileInputStream) {
        try {
            // I need to convert the InpurtStream to a BinaryData
            // and then upload it to the blob
            if (isInvalidFilename(fileName)) {
                throw new JSONException("Invalid filename. Must not contain '..' or '\\' characters");
            }
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .connectionString(Constants.AZ_ST_ACC_CONN_STRING)
                    .buildClient();

            blobServiceClient.getBlobContainerClient(containerName)
                    .getBlobClient(fileName)
                    .upload(fileInputStream);
            // .upload(BinaryData.fromStream(fileInputStream), true);

        } catch (Exception e) {
            _logger.error("Error al guardar el archivo: {}", e);
            return new JSONObject()
                    .put("message", "Error al guardar el archivo")
                    .put("code", 500);
        }
        return new JSONObject()
                .put("message", String.format("Check file downloaded: %s", fileName))
                .put("code", 200);
    }
}
