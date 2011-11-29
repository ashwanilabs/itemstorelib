package com.itemstore.types;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class ItemStoreUtil implements the methods to access the system
 * @author Ashwani Priyedarshi
 */
public class ItemStoreUtil {

    public static final String serverURL = "http://server2:8080/itemstoreR/resources/buckets/";
    public static String serverName = "";

    /**
     * Method getBuckets gets the list of buckets in the system
     * @param userid
     * @return XML string of the list of buckets
     * @throws ItemStoreException
     */
    public static String getBuckets(String userid) throws ItemStoreException {
        String xml = "";
        try {
            URL getUrl = new URL(getServerLoc() + ";user=" + userid);
            HttpURLConnection.setFollowRedirects(true);
            HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/xml");
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new ItemStoreException(ItemStoreExceptions.ITEMSTORE_SERVER_ERROR);
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = reader.readLine();
            while (line != null) {
                xml += line;
                line = reader.readLine();
            }
            connection.disconnect();
        } catch (MalformedURLException ex) {
            Logger.getLogger(ItemStoreUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ConnectException ex) {
            serverName = "";
        } catch (IOException ex) {
            Logger.getLogger(ItemStoreUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return xml;
    }

    /**
     * Method createBucket creates a new bucket in the system
     * @param bucketnm
     * @param userid
     * @throws ItemStoreException
     */
    public static void createBucket(String bucketnm, String userid) throws ItemStoreException {
        try {
            URL postUrl = new URL(getServerLoc() + bucketnm + ";user=" + userid);
            HttpURLConnection.setFollowRedirects(true);
            HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
            connection.setRequestMethod("POST");
            if (connection.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
                throw new ItemStoreException(ItemStoreExceptions.BUCKET_ALREADY_EXISTS, bucketnm, userid);
            }
            if (serverName.isEmpty()) {
                serverName = connection.getURL().getHost();
            }
            connection.disconnect();
        } catch (MalformedURLException ex) {
            Logger.getLogger(ItemStoreUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ConnectException ex) {
            serverName = "";
        } catch (IOException ex) {
            Logger.getLogger(ItemStoreUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Method deleteBucket deletes an existing bucket in the system
     * @param bucketnm
     * @param userid
     * @throws ItemStoreException
     */
    public static void deleteBucket(String bucketnm, String userid) throws ItemStoreException {
        try {
            URL deleteUrl = new URL(getServerLoc() + bucketnm + ";user=" + userid);
            HttpURLConnection.setFollowRedirects(true);
            HttpURLConnection connection = (HttpURLConnection) deleteUrl.openConnection();
            connection.setRequestMethod("DELETE");
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new ItemStoreException(ItemStoreExceptions.BUCKET_NOT_FOUND, bucketnm, userid);
            }
            if (serverName.isEmpty()) {
                serverName = connection.getURL().getHost();
            }
            connection.disconnect();
        } catch (MalformedURLException ex) {
            Logger.getLogger(ItemStoreUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ConnectException ex) {
            serverName = "";
        } catch (IOException ex) {
            Logger.getLogger(ItemStoreUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Methos getItems get the list of items stored in a bucket
     * @param bucketnm
     * @param userid
     * @return XML string of the list of items stored in a bucket
     * @throws ItemStoreException
     */
    public static String getItems(String bucketnm, String userid) throws ItemStoreException {
        String xml = "";
        try {
            URL getUrl = new URL(getServerLoc() + bucketnm + ";user=" + userid);
            HttpURLConnection.setFollowRedirects(true);
            HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/xml");
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new ItemStoreException(ItemStoreExceptions.ITEMSTORE_SERVER_ERROR);
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = reader.readLine();
            while (line != null) {
                xml += line;
                line = reader.readLine();
            }
            if (serverName.isEmpty()) {
                serverName = connection.getURL().getHost();
            }
            connection.disconnect();
        } catch (MalformedURLException ex) {
            Logger.getLogger(ItemStoreUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ConnectException ex) {
            serverName = "";
        } catch (IOException ex) {
            Logger.getLogger(ItemStoreUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return xml;
    }

    /**
     * Method getObjects get all the items stored in a bucket
     * @param bucketnm
     * @param userid
     * @return Array of objects
     * @throws ItemStoreException
     */
    public static Serializable[] getObjects(String bucketnm, String userid) throws ItemStoreException {
        Serializable[] itemObjArr = null;
        try {
            URL getUrl = new URL(getServerLoc() + bucketnm + ";user=" + userid + ";type=o");
            HttpURLConnection.setFollowRedirects(true);
            HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/x-java-serialized-object");
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new ItemStoreException(ItemStoreExceptions.ITEMSTORE_SERVER_ERROR);
            }
            ObjectInputStream ois = new ObjectInputStream(connection.getInputStream());
            itemObjArr = (Serializable[]) ois.readObject();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new ItemStoreException(ItemStoreExceptions.ITEM_NOT_FOUND);
            }
            if (serverName.isEmpty()) {
                serverName = connection.getURL().getHost();
            }
            connection.disconnect();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ItemStoreUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(ItemStoreUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ConnectException ex) {
            serverName = "";
        } catch (IOException ex) {
            Logger.getLogger(ItemStoreUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return itemObjArr;
    }

    /**
     * Method getItem get the requested item from the system
     * @param itemnm
     * @param bucketnm
     * @param userid
     * @param type
     * @return ItemStoreObject
     * @throws ItemStoreException
     */
    public static ItemStoreObject getItem(String itemnm, String bucketnm, String userid, String type) throws ItemStoreException {
        ItemStoreObject itemObj = null;
        try {
            URL getUrl = new URL(getServerLoc() + bucketnm + "/" + itemnm + ";type=" + type + ";user=" + userid);
            HttpURLConnection.setFollowRedirects(true);
            HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
            connection.setRequestMethod("GET");
            ObjectInputStream ois = new ObjectInputStream(connection.getInputStream());
            itemObj = (ItemStoreObject) ois.readObject();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw (new ItemStoreException(ItemStoreExceptions.ITEM_NOT_FOUND, itemnm, bucketnm, userid));
            }
            if (serverName.isEmpty()) {
                serverName = connection.getURL().getHost();
            }
            connection.disconnect();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ItemStoreUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(ItemStoreUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ConnectException ex) {
            serverName = "";
        } catch (IOException ex) {
            Logger.getLogger(ItemStoreUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return itemObj;
    }

    /**
     * Method commitItem commits the updates to an oject to the system
     * @param Obj
     * @throws ItemStoreException
     */
    public static void commitItem(Serializable Obj) throws ItemStoreException {
        ItemStoreObject itemObj = (ItemStoreObject) Obj;
        String method = itemObj.log.getMethod();
        try {
            URL url = new URL(getServerLoc() + itemObj.bucketnm + "/" + itemObj.itemnm + ";type=" + itemObj.type + ";user=" + itemObj.userid);
            HttpURLConnection.setFollowRedirects(true);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-java-serialized-object");
            OutputStream os = connection.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            if (method.equals("POST")) {
                oos.writeObject(itemObj);
                oos.flush();
                if (connection.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
                    System.out.println("ItemStoreUtil::commitItem::server" + serverName + "::" + method + "::" + connection.getResponseCode());
                    throw new ItemStoreException(ItemStoreExceptions.ITEM_ALREADY_EXISTS, itemObj.itemnm, itemObj.bucketnm, itemObj.userid);
                }
            } else if (method.equals("PUT")) {
                oos.writeObject(itemObj.log);
                oos.flush();
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    System.out.println("ItemStoreUtil::commitItem::server" + serverName + "::" + method + "::" + connection.getResponseCode());
                    throw new ItemStoreException(ItemStoreExceptions.ITEM_ALREADY_EXISTS, itemObj.itemnm, itemObj.bucketnm, itemObj.userid);
                }
            }
            if (serverName.isEmpty()) {
                serverName = connection.getURL().getHost();
            }
            connection.disconnect();
            itemObj.log.setMethod("PUT");
            itemObj.log.getOps().clear();

        } catch (MalformedURLException ex) {
            Logger.getLogger(ItemStoreUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ConnectException ex) {
            serverName = "";
        } catch (IOException ex) {
            Logger.getLogger(ItemStoreUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Method deleteItem deletes an item stored on the system
     * @param itemObj
     * @throws ItemStoreException
     */
    public static void deleteItem(String itemnm, String bucketnm, String userid, String type) throws ItemStoreException {
        try {
            URL deleteUrl = new URL(getServerLoc() + bucketnm + "/" + itemnm + ";type=" + type + ";user=" + userid);
            HttpURLConnection.setFollowRedirects(true);
            HttpURLConnection connection = (HttpURLConnection) deleteUrl.openConnection();
            connection.setRequestMethod("DELETE");
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.out.println("ItemStoreUtil::commitItem::DELETE::" + connection.getResponseCode());
                throw new ItemStoreException(ItemStoreExceptions.ITEM_NOT_FOUND, itemnm, bucketnm, userid);
            }
            if (serverName.isEmpty()) {
                serverName = connection.getURL().getHost();
            }
            connection.disconnect();
        } catch (MalformedURLException ex) {
            Logger.getLogger(ItemStoreUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ConnectException ex) {
            serverName = "";
        } catch (IOException ex) {
            Logger.getLogger(ItemStoreUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Method deleteItem deletes an item stored on the system
     * @param itemObj
     * @throws ItemStoreException
     */
    public static void deleteItem(ItemStoreObject itemObj) throws ItemStoreException {
        try {
            URL deleteUrl = new URL(getServerLoc() + itemObj.bucketnm + "/" + itemObj.itemnm + ";type=" + itemObj.type + ";user=" + itemObj.userid);
            HttpURLConnection.setFollowRedirects(true);
            HttpURLConnection connection = (HttpURLConnection) deleteUrl.openConnection();
            connection.setRequestMethod("DELETE");
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.out.println("ItemStoreUtil::commitItem::DELETE::" + connection.getResponseCode());
                throw new ItemStoreException(ItemStoreExceptions.ITEM_NOT_FOUND, itemObj.itemnm, itemObj.bucketnm, itemObj.userid);
            }
            if (serverName.isEmpty()) {
                serverName = connection.getURL().getHost();
            }
            connection.disconnect();
        } catch (MalformedURLException ex) {
            Logger.getLogger(ItemStoreUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ConnectException ex) {
            serverName = "";
        } catch (IOException ex) {
            Logger.getLogger(ItemStoreUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static String getServerLoc() {
        if (serverName.isEmpty()) {
//            String[] servers = {"server1", "server3", "server4", "server5", "server6", "server7", "server8", "server9", "server10"};
//            Random r = new Random();
//            int i = r.nextInt(9);
//            serverName = servers[i];
            return serverURL;
        } else {
            return serverURL.replaceFirst("server2:", serverName+":").replaceFirst("itemstoreR", "itemstore");
        }
    }
}
