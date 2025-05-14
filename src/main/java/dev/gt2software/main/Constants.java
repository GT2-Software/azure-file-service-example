package dev.gt2software.main;

public class Constants {
    public static final String DEPLOYMENT_ARCH = System.getenv("DEPLOYMENT_ARCH");
    public static int conteo = 0;
    public static String AZ_ST_ACC_CONN_STRING = System.getenv("AZ_ST_ACC_CONN_STRING");
    public static String AZ_ST_ACC_NAME = System.getenv("AZ_ST_ACC_NAME");
    public static String CA_CERT_DATA = System.getenv("CA_CERT_DATA");
    public static String CLIENT_CERT_DATA = System.getenv("CLIENT_CERT_DATA");
    public static String CLIENT_KEY_DATA = System.getenv("CLIENT_KEY_DATA");
    public static String MASTER_URL = System.getenv("MASTER_URL");
    public static String AzureQueueEnv = System.getenv("AZURE_QUEUE_ENV");
    public static String AzureQueueEndpoint = System.getenv("AZURE_QUEUE_ENDPOINT");
}
