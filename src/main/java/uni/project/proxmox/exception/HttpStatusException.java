package uni.project.proxmox.exception;

/**
 * The {@code HttpStatusException} class is a runtime exception thrown when
 * an HTTP response with an error status code (typically 400 or greater)
 * is returned by the Proxmox server during a request.
 */
public class HttpStatusException extends RuntimeException {

    /**
     * Constructs a new {@code HttpStatusException} with the specified message.
     *
     * @param message the message should describe the HTTP error status code and server response
     */
    public HttpStatusException(String message) {
        super(message);
    }
}
