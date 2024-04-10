package com.backend.challenge.constant;

/**
 * Util class for project constants
 */
public class ServerConstant {

    /**
     * Private constructor to avoid instantiation of utility class.
     */
    private ServerConstant() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Regex for the name of the user in the connection.
     */
    public static final String NAME_REGEX = "^HI, I AM [a-zA-Z0-9-]+";

    /**
     * Regex for the connection end trigger message.
     */
    public static final String TERMINATE_REGEX = "^BYE MATE!$";

    /**
     * Default error message.
     */
    public static final String ERROR_MESSAGE = "SORRY, I DID NOT UNDERSTAND THAT";

    /**
     * Message for when a node is not found.
     */
    public static final String NODE_NOT_FOUND = "ERROR: NODE NOT FOUND";

    /**
     * Message for when a node already exists.
     */
    public static final String NODE_ALREADY_EXISTS = "ERROR: NODE ALREADY EXISTS";

    /**
     * Node keyword.
     */
    public static final String NODE_CONST = "NODE";

    /**
     * Edge keyword.
     */
    public static final String EDGE_CONST = "EDGE";

    /**
     * ADD operation keyword.
     */
    public static final String ADD_OPERATION = "ADD";

    /**
     * REMOVE operation keyword.
     */
    public static final String REMOVE_OPERATION = "REMOVE";

    /**
     * Node added message.
     */
    public static final String NODE_ADDED = "NODE ADDED";

    /**
     * Node removed message.
     */
    public static final String NODE_REMOVED = "NODE REMOVED";

    /**
     * Edge added message.
     */
    public static final String EDGE_ADDED = "EDGE ADDED";

    /**
     * Edge removed message.
     */
    public static final String EDGE_REMOVED = "EDGE REMOVED";

}
