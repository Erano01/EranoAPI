package me.erano.com.api.cluster;

/** The network's state couldn't be reached or read. */
public class ClusterException extends Exception {

    private static final long serialVersionUID = 1L;

    public ClusterException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClusterException(String message) {
        super(message);
    }
}
