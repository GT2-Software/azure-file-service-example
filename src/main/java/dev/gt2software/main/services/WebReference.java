package dev.gt2software.main.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;

import org.json.JSONException;
import org.json.JSONObject;

public class WebReference {

    private static void trustAllHosts() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509ExtendedTrustManager() {
                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
                                throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
                                throws CertificateException {
                        }

                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] xcs, String string,
                                Socket socket) throws CertificateException {

                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] xcs, String string,
                                Socket socket) throws CertificateException {

                        }

                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] xcs, String string,
                                SSLEngine ssle) throws CertificateException {

                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] xcs, String string,
                                SSLEngine ssle) throws CertificateException {

                        }

                    }
            };

            SSLContext sc = SSLContext.getInstance("TLSv1.2");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            // System.out.println(e.getMessage());
        }
    }

    public JSONObject consumirRESTAuthConHeaderDePais(String uri, String tipoRequest, String tipoValor,
            String parametrosJSON, String authHeader, String country) {
        // Log.debug("[debug][Consumiendo endpoint rest]: " + uri, LOGGER);
        // Log.debug("[debug][request]: " + parametrosJSON, LOGGER);
        try {
            trustAllHosts();
            URL url = new URL(uri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(tipoRequest);
            connection.setRequestProperty("Content-Type", tipoValor);
            connection.setRequestProperty("Authorization", authHeader);
            connection.setRequestProperty("country", country);
            if (parametrosJSON != null) { // Si trae parámetros JSON
                connection.setDoOutput(true); // Debe ir seteado, de lo contrario no deja enviar el stream
                // de datos
                OutputStream os = connection.getOutputStream();
                String json = parametrosJSON;
                // Enviamos los parámetros
                os.write(json.getBytes());
            }
            InputStream inputStream;
            if (connection.getResponseCode() == 200) {
                inputStream = connection.getInputStream();
            } else {
                inputStream = connection.getErrorStream();
            }
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String read;

            while ((read = br.readLine()) != null) {
                sb.append(read);
            }
            connection.disconnect();

            // Log.debug("[debug][response]: " + sb.toString(), LOGGER);
            return new JSONObject(sb.toString());
        } catch (IOException | JSONException ex) {
            // Log.error("Consumption REST error: " + uri + " - " + ex.getMessage(),
            // LOGGER);
            return null;
        }
    }

    public JSONObject consumirURLConGrantType(String uri, String tipoRequest, String tipoValor, String authHeader,
            String grantType) {
        // Log.debug("[debug][Consumiendo endpoint rest]: " + uri, LOGGER);
        // Log.debug("[debug][request]: " + grantType, LOGGER);
        try {
            trustAllHosts();
            URL url = new URL(uri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(tipoRequest);
            connection.setRequestProperty("Content-Type", tipoValor);
            connection.setRequestProperty("Authorization", authHeader);
            if (grantType != null) {
                connection.setDoOutput(true);
                String requestBody = "grant_type=" + URLEncoder.encode(grantType, "UTF-8");
                OutputStream os = connection.getOutputStream();
                os.write(requestBody.getBytes());
            }
            InputStream inputStream;
            if (connection.getResponseCode() == 200) {
                inputStream = connection.getInputStream();
            } else {
                inputStream = connection.getErrorStream();
            }
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String read;

            while ((read = br.readLine()) != null) {
                sb.append(read);
            }
            connection.disconnect();

            // Log.debug("[debug][response]: " + sb.toString(), LOGGER);
            return new JSONObject(sb.toString());
        } catch (IOException | JSONException ex) {
            // Log.error("Consumption REST error: " + uri + " - " + ex.getMessage(),
            // LOGGER);
            return null;
        }
    }
}
