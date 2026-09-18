import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uni.project.proxmox.implementation.ProxmoxConsumerFacade;
import uni.project.proxmox.model.ProxmoxCredentials;

import java.net.http.HttpClient;

public class TestProxmoxConsumer {


    private ProxmoxConsumerFacade  proxmoxConsumer;

    @Given("a connected client to the node {string}")
    public void a_connected_client_to_the_node(String nodeName) {





    }
    @When("the configuration for the virtual machine are including {int} cores and {int} RAM")
    public void the_configuration_for_the_virtual_machine_are_including_cores_and_ram(Integer int1, Integer int2) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
    @When("the user of that library makes request to the server")
    public void the_user_of_that_library_makes_request_to_the_server() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
    @Then("the server should return status of that request")
    public void the_server_should_return_status_of_that_request() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

}
