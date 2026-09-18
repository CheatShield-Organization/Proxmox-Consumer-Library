package uni.project.proxmox.exception;


/**
 * The {@code HttpMethodNotSupported} exception is a runtime exception thrown
 * when an unsupported HTTP method is provided during a request operation.
 */
public class HttpMethodNotSupported extends RuntimeException {


    /**
     * Constructs a new {@code HttpMethodNotSupported} exception with the specified message.
     *
     * @param message the message should explain which method is unsupported
     */
    public HttpMethodNotSupported(String message) {
        super(message);
    }
}
