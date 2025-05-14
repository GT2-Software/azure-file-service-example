package dev.gt2software.main.services.v2;

import java.time.OffsetDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.queue.QueueClient;
import com.azure.storage.queue.QueueClientBuilder;
import com.azure.storage.queue.QueueServiceClient;
import com.azure.storage.queue.QueueServiceClientBuilder;

import dev.gt2software.main.Constants;

public class AzureQueueService {
    private final Logger logger = LogManager.getLogger(AzureQueueService.class);

    private QueueClient getClient(String queueName) {
        switch (Constants.AzureQueueEnv) {
            case "local":
                logger.debug("Using local Azure Storage Queue");
                QueueServiceClient queueServiceClient = new QueueServiceClientBuilder()
                        .connectionString(Constants.AZ_ST_ACC_CONN_STRING)
                        .buildClient();
                queueServiceClient.getQueueClient(queueName);
                return queueServiceClient.getQueueClient(queueName);
            default:
                logger.debug("Using Azure Storage Queue");
                return new QueueClientBuilder()
                        .endpoint(Constants.AzureQueueEndpoint)
                        .queueName(queueName)
                        .credential(new DefaultAzureCredentialBuilder().build())
                        .buildClient();
        }
    }

    public JSONObject createQueue(String content) {
        JSONObject queueRequest = new JSONObject(content);
        String queueName = queueRequest.getString("queueName");
        QueueClient queueClient = getClient(queueName);
        logger.debug("Creating queue: {}", queueName);
        queueClient.createIfNotExists();
        return new JSONObject()
                .put("status", "success")
                .put("code", 200)
                .put("message", "Queue created successfully")
                .put("queueName", queueName);
    }

    public JSONObject sendMessage(String content) {
        JSONObject queueRequest = new JSONObject(content);
        String queueName = queueRequest.getString("queueName");
        String message = queueRequest.getString("message");
        QueueClient queueClient = getClient(queueName);
        logger.debug("Sending message to queue: {}", message);
        queueClient.sendMessage(message)
                .setExpirationTime(OffsetDateTime.now().plusDays(7));
        logger.info("Message sent to queue: {}", queueName);
        return new JSONObject()
                .put("status", "success")
                .put("code", 200)
                .put("message", "Message sent successfully")
                .put("queueName", queueName);
    }

    public void receiveMessage(String queueName) {
        QueueClient queueClient = getClient(queueName);
        logger.debug("Receiving message from queue: {}", queueName);
        int messageCount = queueClient.getProperties().getApproximateMessagesCount();
        logger.debug("Total messages in queue: {}", messageCount);
        logger.info("Iterating over messages in queue: {}", queueName);
        int splitter = Math.round(queueClient.getProperties().getApproximateMessagesCount() / 10);
        for (int i = 0; i < messageCount; i++) {
            queueClient.receiveMessages(10)
                    .forEach(message -> {
                        logger.info("Message: {}", message.getBody().toString());
                        queueClient.deleteMessage(message.getMessageId(), message.getPopReceipt());
                    });
        }
    }

    public JSONObject getMessageResult(String content) {
        JSONObject queueRequest = new JSONObject(content);
        String queueName = queueRequest.getString("queueName");
        receiveMessage(queueName);
        return new JSONObject()
                .put("status", "success")
                .put("code", 200)
                .put("message", "Message received successfully")
                .put("queueName", queueName);
    }
}
