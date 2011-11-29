package com.itemstore.types;

/**
 * Class ItemStoreExceptions defines the system expections
 * @author Ashwani Priyedarshi
 */
class ItemStoreExceptions {

    public static final String ITEM_NOT_FOUND = "Item not found Item: @1 Bucket: @2 UserId: @3";
    public static final String ITEM_ALREADY_EXISTS = "Requested Item Already Exists Item: @1 Bucket: @2 UserId: @3";
    public static final String BUCKET_NOT_FOUND = "Bucket not found Bucket: @1 UserId: @2";
    public static final String BUCKET_ALREADY_EXISTS = "Requested Bucket Already Exists Bucket: @1 UserId: @2";
    public static final String ITEMSTORE_SERVER_ERROR = "ItemStore Server Error";
    public static final String VERTEX_NOT_ADDED = "Vertex couldn't be added to the graph";
    public static final String VERTEX_NOT_DELETED = "Vertex couldn't be deleted from the graph";
    public static final String EDGE_NOT_ADDED = "Edge couldn't be added to the graph";
    public static final String EDGE_NOT_DELETED = "Edge couldn't be deleted from the graph";
    public static final String VERTEX_NOT_FOUND = "Vertex not found";
    public static final String EDGE_NOT_FOUND = "Edge not found";
    public static final String ELEMENT_NOT_ADDED = "Element couldn't be added to the item";
    public static final String ELEMENT_NOT_DELETED = "Element couldn't be deleted from the item";
}
