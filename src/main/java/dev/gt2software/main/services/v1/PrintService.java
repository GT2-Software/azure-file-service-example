package dev.gt2software.main.services.v1;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.json.JSONObject;

import dev.gt2software.main.Constants;
import dev.gt2software.main.services.v2.LogService;
import io.fabric8.kubernetes.api.model.Context;
import io.fabric8.kubernetes.api.model.NamedContext;
import io.fabric8.kubernetes.api.model.NamedContextBuilder;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;

public class PrintService {

        private static Logger _logger = LogManager.getLogger(PrintService.class);
        // private static Logger _logger = LogService.getAppEventInstance();

        public PrintService() {
        }

        public JSONObject printInformation() {
                _logger.info("[info]Hello, World!");
                _logger.debug("[debug]Hello, World!");
                _logger.warn("[warn]Hello, World!");
                _logger.trace("[trace]Hello, World!");
                _logger.error("[error]Hello, World!");
                /*
                 * NamedContext context = new NamedContextBuilder()
                 * .withName("gt2-apps-cluster-01")
                 * .build();
                 */
                /*
                 * Config config = new ConfigBuilder()
                 * .withMasterUrl(Constants.MASTER_URL)
                 * .withCaCertData(Constants.CA_CERT_DATA)
                 * .withClientCertData(Constants.CLIENT_CERT_DATA)
                 * .withClientKeyData(Constants.CLIENT_KEY_DATA)
                 * .withContexts(context)
                 * .build();
                 */
                /*
                 * KubernetesClient client = new KubernetesClientBuilder()
                 * .withConfig(config)
                 * .build();
                 */
                /*
                 * StringBuilder sb = new StringBuilder();
                 * client.getKubernetesVersion();
                 * client.nodes().list().getItems().forEach(node -> {
                 * _logger.info("Node: {}", node.getMetadata().getName());
                 * sb.append("{Node: " + node.getMetadata().getName() + "}").append("\n");
                 * });
                 */
                // Pod pod = client.pods().inNamespace("default").list().getItems().get(0);

                /*
                 * client.apps().deployments().list().getItems().forEach(deployment -> {
                 * // _logger.info("Deployment: {}", deployment.getMetadata().getName());
                 * sb.append("{Deployment Name: " + deployment.getMetadata().getName() +
                 * "}").append("\n");
                 * sb.append("{Deployment namespace: " + deployment.getMetadata().getNamespace()
                 * + "}").append("\n");
                 * sb.append("Deployment creation timestamp: " +
                 * deployment.getMetadata().getCreationTimestamp() + "}")
                 * .append("\n");
                 * });
                 */

                return new JSONObject()
                                .put("code", 200)
                                // .put("kluster", "{" + sb.toString() + "}")
                                .put("NodeName", System.getenv("NODE_NAME"))
                                .put("PodName", System.getenv("POD_NAME"))
                                .put("PodIP", System.getenv("POD_IP"))
                                .put("message", "Hello, World!");
        }
}
