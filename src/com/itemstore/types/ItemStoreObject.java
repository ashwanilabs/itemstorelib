package com.itemstore.types;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class ItemStoreObject is a parent class which is extended by all the
 * CRDT data types implemented in the system
 * @author Ashwani Priyedarshi
 */
public class ItemStoreObject implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public String itemnm;
    public String bucketnm;
    public String userid;
    public String type;
    public Serializable itemObj;
    public transient boolean atSource;
    public transient Log log;

    public ItemStoreObject(String itemnm, String bucketnm, String userid, String type, Serializable itemObj, boolean atSource, Log log) {
        this.itemnm = itemnm;
        this.bucketnm = bucketnm;
        this.userid = userid;
        this.type = type;
        this.itemObj = itemObj;
        this.atSource = atSource;
        this.log = log;
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        atSource = true;
        log = new Log("PUT", new ArrayList<Operation>());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ItemStoreObject other = (ItemStoreObject) obj;
        if ((this.itemnm == null) ? (other.itemnm != null) : !this.itemnm.equals(other.itemnm)) {
            return false;
        }
        if ((this.bucketnm == null) ? (other.bucketnm != null) : !this.bucketnm.equals(other.bucketnm)) {
            return false;
        }
        if ((this.userid == null) ? (other.userid != null) : !this.userid.equals(other.userid)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.itemnm != null ? this.itemnm.hashCode() : 0);
        hash = 53 * hash + (this.bucketnm != null ? this.bucketnm.hashCode() : 0);
        hash = 53 * hash + (this.userid != null ? this.userid.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "ItemStoreObject{" + "itemnm=" + itemnm + "bucketnm=" + bucketnm + "userid=" + userid + '}';
    }
}
