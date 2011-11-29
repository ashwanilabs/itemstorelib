package com.itemstore.types;

/**
 * Class ItemStoreException
 * @author Ashwani Priyedarshi
 */
public class ItemStoreException extends Exception {

    private static final long serialVersionUID = 1L;
    private String message;

    public ItemStoreException(String message) {
        super(message);
        this.message = message;
    }

    public ItemStoreException(String message, String param1) {
        super(message.replaceFirst("@1", param1));
        this.message = message.replaceFirst("1", param1);
    }

    public ItemStoreException(String message, String param1, String param2) {
        super(message.replaceFirst("@1", param1).replaceFirst("@2", param2));
        this.message = message.replaceFirst("@1", param1).replaceFirst("@2", param2);
    }

    public ItemStoreException(String message, String param1, String param2, String param3) {
        super(message.replaceFirst("@1", param1).replaceFirst("@2", param2).replaceFirst("@3", param3));
        this.message = message.replaceFirst("@1", param1).replaceFirst("@2", param2).replaceFirst("@3", param3);
    }

    @Override
    public String toString() {
        return "ItemStoreException{" + "message=" + message + '}';
    }
}
