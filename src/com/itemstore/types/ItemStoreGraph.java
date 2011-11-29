/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itemstore.types;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Class ItemStoreGraph is a CRDT implementation of Graphs.
 * @author Ashwani Priyedarshi
 */
public class ItemStoreGraph extends ItemStoreObject {

    private static final long serialVersionUID = 1L;
    //payload set VA, VR, EA, ER
    private Map<String, Object> VA;
    private Set<String> VR;
    private Map<String, Object> EA;
    private Set<String> ER;

    public ItemStoreGraph(String itemnm, String bucketnm, String userid, Serializable itemObj) {
        super(itemnm, bucketnm, userid, "g",itemObj, true, new Log("POST", new ArrayList<Operation>()));
        this.VA = new HashMap<String, Object>();
        this.VR = new HashSet<String>();
        this.EA = new HashMap<String, Object>();
        this.ER = new HashSet<String>();
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        atSource = true;
        log = new Log("PUT", new ArrayList<Operation>());
    }

    //lookup (vertex v) : boolean b
    public boolean verLookup(String v) {
        return VA.containsKey(v) && !VR.contains(v);
    }

    //lookup (edge (u, v)) : boolean b
    public boolean edgeLookup(String u, String v) {
        return verLookup(u) && verLookup(v) && EA.containsKey(u + ":" + v) && !ER.contains(u + ":" + v);
    }

    //update addVertex (vertex w)
    public void addVer(String w, Serializable obj) throws ItemStoreException {
        Map<String, String> params = new HashMap<String, String>(1);
        params.put("v", w);
        log.getOps().add(new Operation("addVer", params, obj));
        VA.put(w, obj);
    }

    //update addEdge (vertex u, vertex v)
    public void addEgde(String u, String v, Serializable obj) throws ItemStoreException {
        if (!atSource || (verLookup(u) && verLookup(v))) {
            Map<String, String> params = new HashMap<String, String>(2);
            params.put("u", u);
            params.put("v", v);
            log.getOps().add(new Operation("addEdge", params, obj));
            EA.put(u + ":" + v, obj);
        }
    }

    private boolean isIsolated(String w) {
        for (String e : EA.keySet()) {
            if (e.indexOf(":" + w) != -1 || e.indexOf(w + ":") != -1) {
                for (String er : ER) {
                    if (er.indexOf(":" + w) != -1 || er.indexOf(w + ":") != -1) {
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
    }

    //update removeVertex (vertex w)
    public void removeVer(String w) throws ItemStoreException {
        if (!atSource || (verLookup(w) && isIsolated(w))) {
            if (VA.containsKey(w)) {
                Map<String, String> params = new HashMap<String, String>(1);
                params.put("v", w);
                log.getOps().add(new Operation("removeVer", params, null));
                VR.add(w);
            }
        }
    }

    //update removeEdge (edge (u, v))
    public void removeEdge(String u, String v) throws ItemStoreException {
        if (!atSource || edgeLookup(u, v)) {
            if (EA.containsKey(u + ":" + v)) {
                Map<String, String> params = new HashMap<String, String>(2);
                params.put("u", u);
                params.put("v", v);
                log.getOps().add(new Operation("removeEdge", params, null));
                ER.add(u + ":" + v);
            }
        }
    }

    public Object getVer(String w) throws ItemStoreException {
        if (verLookup(w)) {
            return VA.get(w);
        } else {
            return null;
        }
    }

    public Object getEdge(String u, String v) throws ItemStoreException {
        if (edgeLookup(u, v)) {
            return EA.get(u + ":" + v);
        } else {
            return null;
        }
    }
}
