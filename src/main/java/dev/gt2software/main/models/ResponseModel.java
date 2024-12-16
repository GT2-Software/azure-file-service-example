package dev.gt2software.main.models;

import java.util.Date;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author giovani.meza
 */
public class ResponseModel {

    // private static Logger _logger = Log.getInstance(ResponseModel.class);
    protected int code;
    protected JSONObject responseBody;
    protected Request request;
    protected Status status;

    private ResponseModel() {
        // this.responseBody = new JSONObject();
    }

    public static ResponseModel getInstance() {
        return new ResponseModel();
    }

    /**
     * @param body the body to set. Must include the code and the result
     * @return
     */
    public void setResponse(JSONObject body) {
        this.responseBody = body;
        this.code = body.getInt("code");
    }

    /**
     * Selecciona un estado para el retorno de la respuesta al cliente con el
     * formato de {@code com.microsoft.azure.functions.HttpStatus}
     * 
     * @author giovani.meza
     */
    private Status statusSelector(int code) {
        switch (code) {
            case 200:
                status = Status.OK;
                break;
            case 201:
                status = Status.CREATED;
                break;
            case 202:
                status = Status.ACCEPTED;
                break;
            case 205:
                status = Status.RESET_CONTENT;
                break;
            case 206:
                status = Status.PARTIAL_CONTENT;
                break;
            case 204:
                status = Status.NO_CONTENT;
                break;
            case 304:
                status = Status.NOT_MODIFIED;
                break;
            case 400:
                status = Status.BAD_REQUEST;
                break;
            case 401:
                status = Status.UNAUTHORIZED;
                break;
            case 403:
                status = Status.FORBIDDEN;
                break;
            case 404:
                status = Status.NOT_FOUND;
                break;
            case 409:
                status = Status.CONFLICT;
                break;
            case 500:
                status = Status.INTERNAL_SERVER_ERROR;
                break;
            case 501:
                status = Status.NOT_IMPLEMENTED;
                break;
            case 503:
                status = Status.SERVICE_UNAVAILABLE;
                break;
            case 504:
                status = Status.GATEWAY_TIMEOUT;
                break;
            case 505:
                status = Status.HTTP_VERSION_NOT_SUPPORTED;
                break;
            case 506:
                status = Status.INTERNAL_SERVER_ERROR;
            default:
                status = Status.BAD_REQUEST;
        }
        return status;
    }

    private String codeSelector(int code) {
        String result;
        switch (code) {
            case 500:
                result = "Error de servidor";
                break;
            case 504:
                result = "Error de conectividad a los sistemas externos";
                break;
            case 400:
                result = "Datos invalidos";
                break;
            case 401:
                result = "Encabezado vacio o encabezado invalido";
                break;
            case 403:
                result = "Forbidden";
                break;
            case 404:
                result = "No se encontro";
                break;
            case 201:
                result = "Creado";
                break;
            case 409:
                result = "Hay un conflicto";
                break;
            case 304:
                result = "No modificado";
                break;
            case 204:
                result = "No Content";
                break;
            case 506:
                result = "No aceptable";
                break;
            case 200:
                result = "Exitoso";
                break;
            case 202:
                result = "Modificado";
                break;
            case 205:
                result = "Reset Content";
                break;
            default:
                result = "Codigo no controlado";
        }
        return result;
    }

    /**
     * @author giovani.meza
     * @return La respuesta en formato de
     *         {@code javax.ws.rs.Response}
     */
    public Response getResponse(Request req) {
        this.request = req;
        try {
            return Response.status(statusSelector(code))
                    .header("Content-Type", "application/json")
                    .header("X-Frame-Options", "DENY")
                    .header("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE")
                    .header("Allow", "GET, POST, PUT, PATCH, DELETE")
                    .entity(responseBody.has("result") ? responseBody.toString(2)
                            : (responseBody.put("result", this.codeSelector(code)).toString(2)))
                    .build();
        } catch (JSONException e) {
            // _logger.error("Error de respuesta: " + e.getMessage());
            return Response.status(statusSelector(code))
                    .header("Content-Type", "application/json")
                    .header("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE")
                    .header("Allow", "GET, POST, PUT, PATCH, DELETE")
                    .entity(responseBody.has("result") ? responseBody.toString(2)
                            : (responseBody.put("result", this.codeSelector(code)).toString(2)))
                    .build();
        }
    }

    /**
     * @author giovani.meza
     * @return La respuesta en formato de
     *         {@code javax.ws.rs.Response}
     */
    public Response getResponse() {
        try {
            return Response.status(statusSelector(code))
                    .header("Content-Type", "application/json")
                    .header("X-Frame-Options", "DENY")
                    .header("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE")
                    .header("Allow", "GET, POST, PUT, PATCH, DELETE")
                    .entity(responseBody.has("result") ? responseBody.toString(2)
                            : (responseBody.put("result", this.codeSelector(code)).toString(2)))
                    .build();
        } catch (JSONException e) {
            // _logger.error("Error de respuesta: " + e.getMessage());
            return Response.status(statusSelector(code))
                    .header("Content-Type", "application/json")
                    .header("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE")
                    .header("Allow", "GET, POST, PUT, PATCH, DELETE")
                    .entity(responseBody.has("result") ? responseBody.toString(2)
                            : (responseBody.put("result", this.codeSelector(code)).toString(2)))
                    .build();
        }
    }

    /**
     * @author giovani.meza
     * @return
     */
    /*
     * public HttpResponseMessage
     * getResponseWithCookiesFunction(HttpRequestMessage<Optional<String>> req,
     * String cookieValue) {
     * this.request = req;
     * String cookieName = "rjwt"; // Haciendo referencia a refreshToken
     * String cookiePath = "/"; // Path
     * String cookieDomain = CONSTANTS.CookieDomain; // Definido al momento de
     * despliegue
     * Integer cookieMaxAge = 3600; // 1 hora en segundos
     * Date cookieExpirationTime = Date.from(new
     * Date().toInstant().plusSeconds(3600)); // 1 hora
     * 
     * Cookie refreshToken = new Cookie(cookieName, cookieValue, cookiePath,
     * cookieDomain);
     * NewCookie browserCookie = new NewCookie(
     * refreshToken, // Cookie
     * "Token de refresco", // Comentario
     * cookieMaxAge, // Max-Age expira al cerrar sesión
     * cookieExpirationTime, //
     * true, // Secure, no guarda informacion confidencial
     * true // HTTP-Only ya que no se usa para configuraciones del navegador
     * );
     * return request
     * .createResponseBuilder(statusSelector(code))
     * .body(responseBody.toString(2))
     * .header("Content-Type", "application/json")
     * .header("X-Frame-Options", "DENY")
     * .header("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE")
     * .header("Allow", "GET, POST, PUT, PATCH, DELETE")
     * .header("Set-Cookie", browserCookie.toString() + ";SameSite=None;")
     * .build();
     * }
     */

    /**
     * Este metodo se usa exclusivamente para el inicio de sesion. Este manda
     * consito una cookie con el proposito de refrescar la sesion del usuairo.
     * 
     * @author giovani.meza
     * @param cookieValue El JSON Web Token de refresco en formato {@code String}
     *                    sin ninguna conversion
     * @return {@code javax.ws.rs.core.Response } - Modelo de respuesta
     */
    public Response getResponseWithCookies(String cookieValue) {
        String cookieName = "rjwt"; // Haciendo referencia a refreshToken
        String cookiePath = "/"; // Path
        String cookieDomain = "localhost"; // Definido al momento de despliegue
        Integer cookieMaxAge = 3600; // 1 hora en segundos
        Date cookieExpirationTime = Date.from(new Date().toInstant().plusSeconds(3600)); // 1 hora

        // Se recomienda la actualizacion a Jakarta EE para futuras implementaciones de
        // cookies mas seguras
        /*
         * Cookie refreshToken = new Cookie.Builder(cookieName)
         * .domain(cookieDomain)
         * .path(cookiePath)
         * .value(cookieValue)
         * .build();
         * 
         * NewCookie browserCookie = new NewCookie.Builder(refreshToken)
         * .expiry(cookieExpirationTime)
         * .maxAge(cookieMaxAge)
         * .httpOnly(true)
         * .sameSite(SameSite.STRICT) // Solo se puede utilizar con el mismo aplicativo
         * .build();
         */

        // Date cookieExpirationTime = Date.from(new
        // Date().toInstant().plusSeconds(3600)); // 1 hora

        Cookie refreshToken = new Cookie(cookieName, cookieValue, cookiePath,
                cookieDomain);
        NewCookie browserCookie = new NewCookie(
                refreshToken, // Cookie
                "Token de refresco", // Comentario
                cookieMaxAge, // Max-Age expira al cerrar sesión
                cookieExpirationTime, //
                true, // Secure, no guarda informacion confidencial
                true // HTTP-Only ya que no se usa para configuraciones del navegador
        );

        return Response.status(code)
                .entity((responseBody.has("result") ? responseBody
                        : (responseBody.put("result", this.codeSelector(code)))).toString())
                .cookie(browserCookie)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    /**
     * Este metodo envia los archivos en un <code>206 Partial Content</code> que
     * puede ser usado por el navegador en archivos compatibles, para hacer
     * streaming del ardhivo, en lugar de una descarga directa.
     * 
     * @author giovani.meza
     * @param file El archivo que sera enviado al cliente.
     * @return {@code javax.ws.rs.core.Reponse}
     */
    /*
     * public Response getResponseWithAttachmentV2(Attachment file) {
     * if (file == null) {
     * return Response.status(code)
     * .entity((responseBody.has("result") ? responseBody
     * : (responseBody.put("result", "No hay archivos para descargar")))
     * .toString())
     * .type(MediaType.APPLICATION_JSON)
     * .build();
     * }
     * try {
     * ResponseBuilder response = Response.status(Response.Status.PARTIAL_CONTENT);
     * response.entity((Object) file.getAttachmentFile().getBinaryStream());
     * response.header("Accept-Range", "bytes");
     * response.header("Content-Range", "bytes 0-0/" +
     * file.getAttachmentFile().length());
     * response.header("Content-Type", file.getAttachmentType());
     * return response.build();
     * } catch (Exception e) {
     * return Response.status(code)
     * .entity((responseBody.has("result") ? responseBody
     * : (responseBody.put("result", "Error de servidor")))
     * .toString())
     * .type(MediaType.APPLICATION_JSON)
     * .build();
     * }
     * }
     */

    /**
     * Este metodo envia un <code>200 Ok</code> para una descarga directa del
     * archivo
     * 
     * @author giovani.meza
     * @param file {@code java.sql.Blob} El archivo que se quiere enviar al cliente
     *             para su descarga
     * @return Contiene el formato de jax.ws.rs.Response con la entidad
     *         mostrando el archivo
     */
    /*
     * public Response getResponseWithAttachmentV3(Attachment file) {
     * if (file == null) {
     * return Response.status(code)
     * .entity((responseBody.has("result") ? responseBody
     * : (responseBody.put("result", "No hay archivos para descargar")))
     * .toString())
     * .type(MediaType.APPLICATION_JSON)
     * .build();
     * }
     * try {
     * CacheControl cache = new CacheControl();
     * cache.setNoCache(true);
     * cache.setMustRevalidate(true);
     * cache.setNoStore(true);
     * cache.setMaxAge(0);
     * ResponseBuilder response = Response.ok();
     * response.entity((Object) file.getAttachmentFile().getBinaryStream());
     * response.type(MediaType.APPLICATION_OCTET_STREAM);
     * response.cacheControl(cache);
     * response.header("Content-Disposition",
     * String.format("attachment;filename=%s", file.getAttachmentName()));
     * return response.build();
     * } catch (Exception e) {
     * return Response.status(code)
     * .entity((responseBody.has("result") ? responseBody
     * : (responseBody.put("result", "Error de servidor")))
     * .toString())
     * .type(MediaType.APPLICATION_JSON)
     * .build();
     * }
     * }
     */

    /**
     * Este metodo envia un <code>200 Ok</code> para una descarga directa del
     * archivo
     * 
     * @author giovani.meza
     * @param file {@code java.sql.Blob} El archivo que se quiere enviar al cliente
     *             para su descarga
     * @return Contiene el formato de jax.ws.rs.Response con la entidad
     *         mostrando el archivo
     */
    /*
     * public HttpResponseMessage
     * getResponseWithAttachmentV3Function(HttpRequestMessage<Optional<String>> req,
     * Attachment file) {
     * this.request = req;
     * if (file == null) {
     * return request.createResponseBuilder(statusSelector(400))
     * .header("Content-Type", "application/json")
     * .header("X-Frame-Options", "DENY")
     * .header("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE")
     * .header("Allow", "GET, POST, PUT, PATCH, DELETE")
     * .body((responseBody.has("result") ? responseBody
     * : (responseBody.put("result", "No hay archivos para descargar")))
     * .toString())
     * .build();
     * }
     * try {
     * CacheControl cache = new CacheControl();
     * cache.setNoCache(true);
     * cache.setMustRevalidate(true);
     * cache.setNoStore(true);
     * cache.setMaxAge(0);
     * 
     * return request.createResponseBuilder(statusSelector(200))
     * .header("Cache-Control", cache.toString())
     * .header("Content-Type", file.getAttachmentType())
     * .header("X-Frame-Options", "DENY")
     * .header("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE")
     * .header("Allow", "GET, POST, PUT, PATCH, DELETE")
     * .header("Content-Disposition", String.format("attachment;filename=%s",
     * file.getAttachmentName()))
     * .body(file.getFileByteArray())
     * .build();
     * 
     * } catch (Exception e) {
     * _logger.error("Error creando la respuesta:" + e.getMessage());
     * return request.createResponseBuilder(statusSelector(500))
     * .body((responseBody.has("result") ? responseBody
     * : (responseBody.put("result", "Error de servidor")))
     * .toString())
     * .header("Content-Type", MediaType.APPLICATION_JSON)
     * .header("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE")
     * .build();
     * }
     * }
     */

    /**
     * Este metodo envia los archivos en un <code>206 Partial Content</code> que
     * puede ser usado por el navegador en archivos compatibles, para hacer
     * streaming del ardhivo, en lugar de una descarga directa.
     * 
     * @author giovani.meza
     * @param file El archivo que sera enviado al cliente.
     * @return {@code javax.ws.rs.core.Reponse}
     */
    /*
     * public HttpResponseMessage
     * getResponseWithAttachmentV2Function(HttpRequestMessage<Optional<OutputStream>
     * > req,
     * Attachment file) {
     * // this.request = req;
     * if (file == null) {
     * return req.createResponseBuilder(statusSelector(400))
     * .header("Content-Type", "application/json")
     * .header("X-Frame-Options", "DENY")
     * .header("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE")
     * .header("Allow", "GET, POST, PUT, PATCH, DELETE")
     * .body((responseBody.has("result") ? responseBody
     * : (responseBody.put("result", "No hay archivos para descargar")))
     * .toString())
     * .build();
     * }
     * try {
     * return req.createResponseBuilder(statusSelector(206))
     * .header("Content-Type", "application/pdf")
     * .header("Accept-Range", "bytes")
     * .header("X-Frame-Options", "DENY")
     * .header("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE")
     * .header("Allow", "GET, POST, PUT, PATCH, DELETE")
     * .header("Content-Range", "bytes 0-0/" +
     * file.getAttachmentOutputStream().toString().length())
     * .body((Object) file.getAttachmentOutputStream().toString().getBytes())
     * // .body((Object) file.getFileInputStream().toString().getBytes())
     * .build();
     * 
     * } catch (Exception e) {
     * _logger.error("Error creando la respuesta:" + e.getMessage());
     * return req.createResponseBuilder(statusSelector(500))
     * .body((responseBody.has("result") ? responseBody
     * : (responseBody.put("result", "Error de servidor").put("error",
     * e.getMessage())))
     * .toString())
     * .header("Content-Type", MediaType.APPLICATION_JSON)
     * .header("X-Frame-Options", "DENY")
     * .header("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE")
     * .header("Allow", "GET, POST, PUT, PATCH, DELETE")
     * .build();
     * }
     * }
     */

    /**
     * Este metodo envia un <code>200 Ok</code> para una descarga directa del
     * archivo
     * 
     * @author giovani.meza
     * @param file {@code java.io.OutputStream} El archivo que se quiere enviar al
     *             cliente para su descarga
     * @return Contiene el formato de jax.ws.rs.Response con la entidad
     *         mostrando el archivo
     */
    /*
     * public Response getResponseWithAttachmentV4(Attachment file) {
     * if (file == null) {
     * return Response.status(code)
     * .entity((responseBody.has("result") ? responseBody
     * : (responseBody.put("result", "No hay archivos para descargar")))
     * .toString())
     * .type(MediaType.APPLICATION_JSON)
     * .build();
     * }
     * try {
     * CacheControl cache = new CacheControl();
     * cache.setNoCache(true);
     * cache.setMustRevalidate(true);
     * cache.setNoStore(true);
     * cache.setMaxAge(0);
     * ResponseBuilder response = Response.ok();
     * response.entity((Object)
     * file.getAttachmentOutputStream().toString().getBytes());
     * response.type(MediaType.APPLICATION_OCTET_STREAM);
     * response.cacheControl(cache);
     * response.header("Content-Disposition",
     * String.format("attachment;filename=%s", file.getAttachmentName()));
     * return response.build();
     * } catch (Exception e) {
     * return Response.status(code)
     * .entity((responseBody.has("result") ? responseBody
     * : (responseBody.put("result", "Error de servidor")))
     * .toString())
     * .type(MediaType.APPLICATION_JSON)
     * .build();
     * }
     * }
     */

    /**
     * Este metodo envia un <code>200 Ok</code> para una descarga directa del
     * archivo
     * 
     * @author giovani.meza
     * @param file {@code java.io.OutputStream} El archivo que se quiere enviar al
     *             cliente para su descarga
     * @return Contiene el formato de jax.ws.rs.Response con la entidad
     *         mostrando el archivo
     */
    /*
     * public HttpResponseMessage
     * getResponseWithAttachmentV4Function(HttpRequestMessage<Optional<String>>
     * request,
     * Attachment file) {
     * this.request = request;
     * CacheControl cache = new CacheControl();
     * cache.setNoCache(true);
     * cache.setMustRevalidate(true);
     * cache.setNoStore(true);
     * cache.setMaxAge(0);
     * if (file == null) {
     * return request.createResponseBuilder(statusSelector(400))
     * .body((responseBody.has("result") ? responseBody :
     * (responseBody.put("result", this.codeSelector(code))))
     * .toString())
     * .header("Content-Type", "application/json")
     * .header("Cache-Control", cache.toString())
     * .header("X-Frame-Options", "DENY")
     * .header("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE")
     * .header("Allow", "GET, POST, PUT, PATCH, DELETE")
     * .build();
     * }
     * try {
     * return request.createResponseBuilder(statusSelector(200))
     * .header("Content-Type", MediaType.APPLICATION_OCTET_STREAM)
     * .header("Content-Disposition", String.format("attachment;filename=%s",
     * file.getAttachmentName()))
     * .header("Cache-Control", cache.toString())
     * .header("X-Frame-Options", "DENY")
     * .header("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE")
     * .header("Allow", "GET, POST, PUT, PATCH, DELETE")
     * .body((Object) file.getAttachmentOutputStream().toString().getBytes())
     * .build();
     * } catch (Exception e) {
     * return request.createResponseBuilder(statusSelector(500))
     * .body((responseBody.has("result") ? responseBody :
     * (responseBody.put("result", "Error de servidor"))).toString())
     * .header("Content-Type", "application/json")
     * .header("X-Frame-Options", "DENY")
     * .header("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE")
     * .header("Allow", "GET, POST, PUT, PATCH, DELETE")
     * .build();
     * }
     * }
     */

}
