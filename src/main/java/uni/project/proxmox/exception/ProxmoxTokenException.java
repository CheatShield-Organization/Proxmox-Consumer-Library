package uni.project.proxmox.exception;


/**
 * The {@code ProxmoxTokenException} class is a runtime exception thrown when
 * there is an issue with the Proxmox API token.
 */
public class ProxmoxTokenException extends RuntimeException {
    /**
     * Constructs a new {@code ProxmoxTokenException} with the specified message.
     *
     * @param message the message should explain the specific token-related error
     */
    public ProxmoxTokenException(String message) {
        super(message);
    }
}
