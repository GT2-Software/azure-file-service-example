package dev.gt2software.main.services.v1;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.LogManager;
import org.json.JSONObject;
import dev.gt2software.main.Constants;
import io.fabric8.kubernetes.api.model.NamedContext;
import io.fabric8.kubernetes.api.model.NamedContextBuilder;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;

public class PrintService {

        private static Logger _logger = LogManager.getLogger(PrintService.class);
        Marker marker = createMarker();

        private Marker createMarker() {
                switch (Constants.DEPLOYMENT_ARCH) {
                        case "Container":
                                return MarkerManager.getMarker("Container");
                        case "K8S":
                                return MarkerManager.getMarker("K8S");
                        default:
                                return MarkerManager.getMarker("Unknown");
                }

        }

        public PrintService() {
        }

        public JSONObject printInformation() {
                _logger.info("[info]Hello, World!");
                _logger.debug("[debug]Hello, World!");
                _logger.warn("[warn]Hello, World!");
                _logger.trace("[trace]Hello, World!");
                _logger.error("[error]Hello, World!");
                return new JSONObject()
                                .put("code", 200)
                                .put("NodeName", System.getenv("NODE_NAME"))
                                .put("PodName", System.getenv("POD_NAME"))
                                .put("PodIP", System.getenv("POD_IP"))
                                .put("message", "Hello, World!");
        }

        public JSONObject echo() {
                _logger.info(marker, "Hello, World!");
                return new JSONObject()
                                .put("code", 200)
                                .put("nodeName", System.getenv("NODE_NAME"))
                                .put("podName", System.getenv("POD_NAME"))
                                .put("podIP", System.getenv("POD_IP"))
                                .put("message", "Hello, World!");
        }

        public JSONObject echoClient() {
                try {
                        JSONObject response = new JSONObject();
                        NamedContext context = new NamedContextBuilder()
                                        .withName("gt2-apps-cluster-01")
                                        .build();

                        Config config = new ConfigBuilder()
                                        .withMasterUrl(Constants.MASTER_URL)
                                        .withCaCertData(Constants.CA_CERT_DATA)
                                        .withClientCertData(Constants.CLIENT_CERT_DATA)
                                        .withClientKeyData(Constants.CLIENT_KEY_DATA)
                                        .withContexts(context)
                                        .build();

                        KubernetesClient client = new KubernetesClientBuilder()
                                        .withConfig(config)
                                        .build();
                        response.put("kubernetesVersion", client.getKubernetesVersion());
                        client.nodes()
                                        .list()
                                        .getItems()
                                        .forEach(node -> {
                                                response.accumulate("nodes",
                                                                new JSONObject()
                                                                                .put("nodeName", node.getMetadata()
                                                                                                .getName())
                                                                                .put("nodeCreationTimestamp", node
                                                                                                .getMetadata()
                                                                                                .getCreationTimestamp())
                                                                                .put("nodeLabels",
                                                                                                node.getMetadata()
                                                                                                                .getLabels())
                                                                                .put("nodeAnnotations", node
                                                                                                .getMetadata()
                                                                                                .getAnnotations())
                                                                                .put("nodeStatus", node.getStatus())
                                                                                .put("nodeSpec", node.getSpec())
                                                                                .put("nodeProviderID",
                                                                                                node.getSpec().getProviderID())
                                                                                .put("nodeAddresses",
                                                                                                node.getStatus().getAddresses())
                                                                                .put("nodeConditions", node.getStatus()
                                                                                                .getConditions())
                                                                                .put("nodeCapacity",
                                                                                                node.getStatus().getCapacity())
                                                                                .put("nodeAllocatable", node.getStatus()
                                                                                                .getAllocatable())
                                                                                .put("nodeImages", node.getStatus()
                                                                                                .getImages())
                                                                                .put("nodeInfo", node.getStatus()
                                                                                                .getNodeInfo())
                                                                                .put("nodePhase", node.getStatus()
                                                                                                .getPhase()));
                                        });
                        client.apps()
                                        .deployments()
                                        .list()
                                        .getItems()
                                        .forEach(deployment -> {
                                                response.accumulate("deployments",
                                                                new JSONObject()
                                                                                .put("name", deployment.getMetadata()
                                                                                                .getName())
                                                                                .put("namespace",
                                                                                                deployment.getMetadata()
                                                                                                                .getNamespace())
                                                                                .put("creationTimestamp", deployment
                                                                                                .getMetadata()
                                                                                                .getCreationTimestamp()));
                                        });
                        _logger.debug(marker, "K8s client information", client);
                        response.put("code", 200);
                        return response;
                } catch (Exception e) {
                        _logger.error(marker, "Error getting cluster or pod information", e);
                        return new JSONObject()
                                        .put("code", 500)
                                        .put("error", e.getMessage());
                }

        }
}
