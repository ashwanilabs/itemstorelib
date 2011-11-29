package com.itemstore.types;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Class ItemStoreSet is a CRDT implementation of Sets.
 * @author Ashwani Priyedarshi
 */
public class ItemStoreSet extends ItemStoreObject {

    private static final long serialVersionUID = 1L;
    //payload set A, set R
    private Map<String, Object> A;
    private Set<String> R;

    public ItemStoreSet(String itemnm, String bucketnm, String userid, Serializable itemObj) {
        super(itemnm, bucketnm, userid, "g", itemObj, true, new Log("POST", new ArrayList<Operation>()));
        A = new HashMap<String, Object>();
        R = new HashSet<String>();
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        atSource = true;
    }
    //query lookup (element e) : boolean b
    public boolean lookup(String key) {
        return A.containsKey(key) && !R.contains(key);
    }

    //update add (element e)
    public void add(String key, Serializable val) {
        Map<String, String> params = new HashMap<String, String>(1);
        params.put("key", key);
        log.getOps().add(new Operation("add", params, val));
        A.put(key, val);
    }

    //update remove (element e)
    public void remove(String key) {
        if (!atSource || lookup(key)) {
            if (A.containsKey(key)) {
                Map<String, String> params = new HashMap<String, String>(1);
                params.put("key", key);
                log.getOps().add(new Operation("remove", params, null));
                R.add(key);
            }
        }
    }

    public Object get(String key) {
        if (lookup(key)) {
            return A.get(key);
        } else {
            return null;
        }
    }

    //compare (S, T ) : boolean b
    public boolean compare(ItemStoreSet S, ItemStoreSet T) {
        return S.A.keySet().containsAll(T.A.keySet()) && S.A.keySet().containsAll(T.A.keySet());
    }

    //merge (S, T ) : payload U
    public void merge(ItemStoreSet S) {
        A.putAll(S.A);
        R.addAll(S.R);
    }
}
