package uni.project.proxmox.implementation;

import tools.jackson.databind.ObjectMapper;
import uni.project.proxmox.model.ProxmoxCredentials;
import uni.project.proxmox.model.VirtualEnvironmentConfiguration;
import uni.project.proxmox.enums.HttpRequestMethod;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

/**
 * The {@code ProxmoxConsumerFacade} class provides a simplified, high-level interface
 * for interacting directly with a Proxmox Virtual Environment (VE) server.
 *
 * @apiNote ProxmoxCredentials is use for providing the server data about the communication!
 * */

public class ProxmoxConsumerFacade {

    private final ProxmoxHttpClient client;

    private ObjectMapper mapper;

    private final String nodeName;

    /**
     * Constructs a new {@code ProxmoxConsumerFacade} with the required server configuration.
     *
     * @param baseUrl     The public or locally hosted URL from which the Proxmox server can be accessed.
     * @param nodeName    The server name in Proxmox.
     * @param client      The {@link HttpClient} configuration for establishing HTTPS requests with the Proxmox server.
     * @param credentials The authorization information about the API key.
     */
    public ProxmoxConsumerFacade(String baseUrl, String nodeName, HttpClient client, ProxmoxCredentials credentials) {

        this.client = new ProxmoxHttpClient(baseUrl, client, credentials);
        this.mapper = new ObjectMapper();

        this.nodeName = nodeName;
    }

    /**
     * {@code createVirtualEnvironment} is generating a new virtual environment into the proxmox server by using pre-configuration.
     * @param veConfig    The virtual environment configuration (CPU, GPU, RAM, OS ect.)
     * */

        public HttpResponse<String> createVirtualEnvironment(VirtualEnvironmentConfiguration veConfig) throws IOException,InterruptedException {

            String ApiPath = String.format("nodes/%s/qemu", nodeName);

                HttpResponse<String> response = client.request(HttpRequestMethod.POST,
                        ApiPath,
                        mapper.writeValueAsString(veConfig));

                return response;
        }

    /**
     * {@code createCloneVirtualEnvironment} creates a clone of an existing virtual machine or template
     * on the Proxmox server by using pre-configuration.
     *
     * @param sourceVirtualMachineId The ID of the existing virtual machine or template to clone from.
     * @param cloneConfig            The configuration for the new clone (e.g., newid, name, full).
     */
        public HttpResponse<String> createCloneVirtualEnvironment(int sourceVirtualMachineId, VirtualEnvironmentConfiguration cloneConfig) throws IOException, InterruptedException {

            String apiPath = String.format("nodes/%s/qemu/%d/clone", nodeName, sourceVirtualMachineId);

            HttpResponse<String> response = client.request(HttpRequestMethod.POST,
                    apiPath,
                    mapper.writeValueAsString(cloneConfig));

            return response;
        }

    /**
     * {@code startVirtualEnvironment} is booting up an existing virtual machine from the proxmox server.
     * @param virtualMachineId    The virtual environment identification.
     * */
        public HttpResponse<String> startVirtualEnvironment(int virtualMachineId) throws IOException, InterruptedException {
            String ApiPath = String.format("nodes/%s/qemu/%d/status/start",nodeName, virtualMachineId);

            HttpResponse<String> response = client.request(HttpRequestMethod.POST,
                    ApiPath,
                    null);

            return response;

    }

    /**
     * {@code stopVirtualEnvironment} is shutting down already booted up virtual machine.
     * @param virtualMachineId    The virtual environment identification.
     * */
        public HttpResponse<String> stopVirtualEnvironment(int virtualMachineId) throws IOException, InterruptedException {
            String ApiPath = String.format("nodes/%s/qemu/%d/status/stop",nodeName, virtualMachineId);

            HttpResponse<String> response = client.request(HttpRequestMethod.POST,
                    ApiPath,
                    null);

            return response;
        }

    /**
     * {@code showVirtualEnvironmentStats} is showing a real live data of a virtual machine which is provided from the proxmox server.
     * @param virtualMachineId    The virtual environment identification.
     * */

        public HttpResponse<String> showVirtualEnvironmentStats(int virtualMachineId) throws IOException, InterruptedException {

            String ApiPath = String.format("nodes/%s/qemu/%d/status/current",nodeName, virtualMachineId);

            HttpResponse<String> response = client.request(HttpRequestMethod.GET,
                    ApiPath,
                    null);

            return response;
        }
    /**
     * {@code getVncProxyTicket} is showing a real live data of a virtual machine which is provided from the proxmox server.
     * @param virtualMachineId     The virtual environment identification.
     * @param upgradeCommunication Upgrading the HTTPS into a WSS communication
     *
     * @apiNote {@code upgradeCommunication} should be true if the communication protocol has to upgrade into a websocket. <br><br>
     *                                       For general purposes is recommented to stay false.
     * */
        public HttpResponse<String> getVncProxyTicket(int virtualMachineId, boolean upgradeCommunication) throws IOException, InterruptedException {

            String ApiPath = String.format("nodes/%s/qemu/%d/vncproxy",nodeName, virtualMachineId);

            HttpResponse<String> response = client.request(HttpRequestMethod.POST,
                    ApiPath,
                    "{\"websocket\": "+upgradeCommunication+"}");

            return response;
        }


    /**
     * {@code removeVirtualEnvironment} deletes an existing virtual machine from the Proxmox server.
     *
     * @param virtualMachineId The virtual environment identification.
     */
        public HttpResponse<String> removeVirtualEnvironment(int virtualMachineId) throws IOException, InterruptedException {
            String apiPath = String.format("nodes/%s/qemu/%d", nodeName, virtualMachineId);

            HttpResponse<String> response = client.request(HttpRequestMethod.DELETE,
                    apiPath,
                    null);

            return response;
        }

}
