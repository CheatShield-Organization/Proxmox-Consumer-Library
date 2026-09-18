package uni.project.proxmox.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uni.project.proxmox.model.ProxmoxCredentials;
import uni.project.proxmox.enums.HttpRequestMethod;
import uni.project.proxmox.exception.HttpMethodNotSupported;
import uni.project.proxmox.exception.HttpStatusException;
import uni.project.proxmox.exception.ProxmoxTokenException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

/**
 * The {@code ProxmoxHttpClient} class is used for making HTTPS requests to the Proxmox server.
 * It builds up the necessary requirements for the HTTPS request (such as headers, authorization keys,
 * and request bodies) and executes it asynchronously, blocking for the response.
 *
 * @apiNote This client handles standard HTTP methods (GET, POST, PUT, DELETE) and validates
 * the HTTP response status codes, throwing exceptions when errors occur.
 */
public class ProxmoxHttpClient {

    private static final Logger log = LoggerFactory.getLogger(ProxmoxHttpClient.class);

    private final HttpClient client;

    private final String authorizationKey;

    private final String baseUrl;


    /**
     * Constructs a new {@code ProxmoxHttpClient} with the specified base URL, underlying HTTP client,
     * and Proxmox credentials used for API token generation.
     *
     * @param baseUrl     the base URL of the Proxmox server
     * @param client      the {@link HttpClient} instance used to send the requests
     * @param credentials the {@link ProxmoxCredentials} containing the Token ID and Secret for authentication
     */
    public ProxmoxHttpClient(String baseUrl, HttpClient client, ProxmoxCredentials credentials){

        this.baseUrl = baseUrl;

        this.client = client;

        this.authorizationKey = "PVEAPIToken="+credentials.getTokenId()+"="+credentials.getSecret();

    }
    /**
     * Sends an HTTPS request to the Proxmox server based on the specified HTTP method, API path, and JSON body.
     *
     * <p>The request method builds up the requirements for the HTTPS request and executes it.
     * From the return type, we get the status and content of the response.</p>
     *
     * @param method   the HTTP method request (e.g., GET, POST, PUT, DELETE) represented by {@link HttpRequestMethod}
     * @param path     the API endpoint path/branch to which we establish the request (e.g., "/nodes")
     * @param jsonBody the JSON body required for the request; if null or empty, it defaults to '{}' in JSON format
     * @return an {@link HttpResponse} containing the server's string response body and status code
     * @throws IOException           if an I/O error occurs when sending or receiving
     * @throws InterruptedException  if the operation is interrupted while waiting for the response
     * @throws ProxmoxTokenException if the authorization key is missing or invalid
     * @throws HttpMethodNotSupported if the provided HTTP method is not supported by the client
     * @throws HttpStatusException   if the server returns an HTTP error status code (400 or greater)
     */
    public HttpResponse<String> request(HttpRequestMethod method, String path, String jsonBody) throws IOException, InterruptedException {
        // jsonBody always should be in json format even if the body is empty or null
        if(jsonBody == null || jsonBody.isEmpty())
            jsonBody = "{}";

        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(jsonBody);

            log.info("Checking the json body requirements!");

            log.info("Start the HTTPS configuration!");
            HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + path))
                    .header("Content-Type", "application/json");

                    log.info("Checking the API Key requirements!");
                    if(authorizationKey==null || authorizationKey.isEmpty()){

                        log.error("Failed to insert the authorization key into the header configuration!");
                        throw new ProxmoxTokenException("Authorization Key not provided");

                    }

                    httpRequestBuilder.header("Authorization", authorizationKey);

            switch (method) {
                case GET:
                    httpRequestBuilder.GET();
                    break;
                case POST:
                    httpRequestBuilder.POST(bodyPublisher);
                    break;
                case PUT:
                    httpRequestBuilder.PUT(bodyPublisher);
                    break;
                case DELETE:
                    httpRequestBuilder.DELETE();
                    break;
                default:
                    throw new HttpMethodNotSupported("This method " + method.name() + " is not supported");

            }
            log.info("The HTTPS configuration has been successfully completed!");

            HttpRequest httpRequest = httpRequestBuilder.build();

            CompletableFuture<HttpResponse<String>> httpResponse = client.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString());

            log.info("Waiting for HTTPS response from server!");
            HttpResponse<String> response = httpResponse.join();

             if(response.statusCode() >= 400){
                 log.error("The HTTPS configuration returned an invalid status code!");
                throw new HttpStatusException("The request returned HTTP error code : \n Status: " + response.statusCode() +"\n Message provided from the server:\n"+response.body());
             }



            return response;


    }



}
