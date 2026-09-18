Feature: Proxmox virtual environment manager
  Background: Setup connection to the Proxmox server
    Given a connected client to the node "pve"



    Scenario: Create a brand new virtual machine
      When the configuration for the virtual machine are including 2 cores and 2096 RAM
      And the user of that library makes request to the server
      Then the server should return status of that request


