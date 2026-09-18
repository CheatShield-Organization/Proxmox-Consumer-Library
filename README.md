# Cheat Shield - Proxmox VE Java Consumer Library

A Java API client and proxy wrapper for Proxmox VE, developed as part of the Cheat Shield project.
---

## Core Features

* **Virtual Machine Lifecycle Management**
    * **Creation**: Spin up new VMs dynamically using payload configuration maps.
    * **Cloning**: Duplicate existing virtual environments efficiently.
    * **Power Control**: Execute remote start and stop commands.
* **Live Monitoring & Telemetry**
    * Fetch real-time performance and resource statistics (`/status/current`) for running virtual environments.
* **VNC Console Access**
    * Generate secure VNC proxy tickets for remote console interactions.
* **Custom SSL Management**
    * Support for programmatic installation and uploads of custom SSL certificates to Proxmox nodes.
* **Clean Resource Deletion**
    * Execute standard VM removals cleanly without extraneous or unexpected query parameters.

---

## Security & In-Memory SSL Caching

Unlike traditional implementations that blindly bypass security or repeatedly parse certificate files from disk, this library implements a secure **in-memory caching architecture**:
* **`ProxmoxSslContextImpl`**: Loads the Proxmox `.pem` or `.crt` certificate file **strictly once** upon initialization.
* **In-Memory Reuse**: Caches the underlying `KeyStore` and `SSLContext` in memory across the application or test lifecycle.

---

## Supported Proxmox API Endpoints

| Method | Endpoint Path | Description |
| :--- | :--- | :--- |
| **POST** | `/nodes/{node}/qemu` | Create a new virtual machine |
| **POST** | `/nodes/{node}/qemu/{vmid}/clone` | Clone an existing virtual machine |
| **POST** | `/nodes/{node}/qemu/{vmid}/status/start` | Start a virtual machine |
| **POST** | `/nodes/{node}/qemu/{vmid}/status/stop` | Stop a virtual machine |
| **GET** | `/nodes/{node}/qemu/{vmid}/status/current` | Retrieve live VM statistics |
| **POST** | `/nodes/{node}/qemu/{vmid}/vncproxy` | Generate a VNC proxy ticket |
| **DELETE**| `/nodes/{node}/qemu/{vmid}` | Remove a virtual machine |

---

## Tech Stack

* **Language**: Java
* **JSON Mapping**: Jackson
* **Testing**: Cucumber BDD Framework

---

## Quick Start Example

```java
File certFile = new File("path-to-your-certification");

//Making a imports a ssl certification and creates a new SSLContext instance from the certification
ProxmoxSslContextImpl sslConfig = new ProxmoxSslContextImpl(certFile);

HttpClient secureClient = HttpClient.newBuilder()
        .sslContext(sslConfig.getSslContext())
        .build();

//Set up credentials and facade
ProxmoxCredentials credentials = new ProxmoxCredentials("root@pam!api-token", "your-uuid-token");
ProxmoxConsumerFacade proxmoxFacade = new ProxmoxConsumerFacade("https://your-server-address:8006/api2/json", "node-name", secureClient, credentials);

//Interact with the Proxmox API
int virtualMachineId = 120;
bool enableWebsocketProtocol = true;
proxmoxFacade.getVncProxyTicket(virtualMachineId, enableWebsocketProtocol);
