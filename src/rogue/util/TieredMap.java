/*
 * The MIT License
 *
 * Copyright 2014 Rogue <Alice Q.>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package rogue.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Data structure for a Tiered Map - that is a map structure which uses
 * generations of different instances for data access. All implementations of
 * this must follow the following guidelines: 1) every child map must be a
 * subset of the union of all generations before it, 2) no two different
 * instances may appear in a family under the same key and 3) a single Object is
 * allowed to be under multiple keys over different generations, provided that
 * this does not conflict with existing keys, violating rule 2.
 *
 * This is an useful data structure for structures which require or would
 * benefit from global reference maps. For example, a list of IRC users on
 * different channels could all be children of a parent server-wide map
 *
 * @author Rogue <Alice Q.>
 * @param <K> the type of object to use as a key
 * @param <V> the type of object to store under specific keys
 */
public class TieredMap<K, V> implements Map<K, V> {

    // REFERENCE TO PARENT
    private TieredMap<K, V> parent;

    // DATA STORAGE
    private Map<K, V> data;

    // THE NUMBER OF GENERATIONS
    private int generation;

    // CREATION METHODS
    // - constructor (2)
    // - getChild
    // - getSibling
    // - getParent
    // - getRoot
    // - getNewRoot
    /**
     * Basic constructor which creates a new root map - that is, a map without a
     * parent. As a root map this map will contain more data than any of its
     * children
     */
    public TieredMap() {
        parent = null;
        data = new HashMap();
        generation = 0;
    }

    /**
     * Constructor to create a new root map based off of another TieredMap. The
     * new map will contain the same data as the source map but will itself be a
     * root with no other connections to the source
     *
     * @param source the map to copy
     */
    public TieredMap(TieredMap<K, V> source) {
        parent = null;
        data = new HashMap(source.data);
        generation = 0;
    }

    /**
     * Constructor to create a new root map based off of another map. The
     * created map will be a root map but contain the same data as the source
     * map.
     *
     * @param source the map to copy
     */
    public TieredMap(Map<K, V> source) {
        parent = null;
        data = new HashMap(source);
        generation = 0;
    }

    /**
     * Method to create a child map - a new TieredMap with this as its parent.
     *
     * @return a new empty TieredMap of the same type as this map with this as
     * its parent
     */
    public TieredMap<K, V> getChild() {
        TieredMap<K, V> map = new TieredMap();
        map.parent = this;
        map.generation = this.generation + 1;
        return map;
    }

    /**
     * Method to create a sibling map - a new TieredMap which shares the same
     * parent map as this map. As such, the parent map will contain data of both
     * this map and its sibling
     *
     * @return a new empty TieredMap of the same type as this map with the same
     * parent as this one
     * @throws UnsupportedOperationException when this has no parent
     */
    public TieredMap<K, V> getSibling() {
        if (parent == null) {
            throw new UnsupportedOperationException("May not create sibling of root map");
        }
        TieredMap<K, V> map = new TieredMap();
        map.parent = parent;
        map.generation = this.generation;
        return map;
    }

    /**
     * Method to retrieve the parent of this map
     *
     * @return the parent map of this instance, which may be null in the case of
     * a root map
     */
    public TieredMap<K, V> getParent() {
        return parent;
    }

    /**
     * Method to retrieve the root map of the family this map belongs to. This
     * is the highest order map in the family which contains the entirety of the
     * data in the family
     *
     * @return a TieredMap of the same type as this one of generation 0
     */
    public TieredMap<K, V> getRoot() {
        if (parent == null) {
            return this;
        } else {
            return parent.getRoot();
        }
    }

    /**
     * Method to create a new root map with no reference this map, initialized
     * with a copy of the data contained in the map
     *
     * @return a new TieredMap of the same type as this one with a copy of the
     * data contained in this one
     */
    public TieredMap<K, V> getNewRoot() {
        return new TieredMap(this);
    }

    // DATA METHODS
    // - isRoot
    // - getGeneration
    /**
     * Method to check if this map is a root map
     *
     * @return true when this has no parent :(
     */
    public boolean isRoot() {
        return parent == null;
    }

    /**
     * Method to check a map's generation in the family tree. In other words,
     * the distance between this map and the root
     *
     * @return the number of parents and grandparents this object has
     */
    public int getGeneration() {
        return generation;
    }

    // REDIRECTED OVERWRITTEN METHODS METHODS
    // - clear
    // - containsKey
    // - containsValue
    // - entrySet
    // - get
    // - hashCode
    // - isEmpty
    // - keySet
    // - size
    // - values
    @Override
    public void clear() {
        data.clear();
    }

    @Override
    public boolean containsKey(Object key) {
        return data.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return data.containsValue(value);
    }

    @Override
    public java.util.Set<Entry<K, V>> entrySet() {
        return data.entrySet();
    }

    @Override
    public V get(Object key) {
        return data.get(key);
    }

    @Override
    public int hashCode() {
        return data.hashCode();
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public java.util.Set<K> keySet() {
        return data.keySet();
    }

    @Override
    public java.util.Collection<V> values() {
        return data.values();
    }

    // CUSTOM OVERRIDEN MethoDS
    // - clone
    // - equals (2)
    // - put
    // - putAll
    // - remove
    // - toString
    /**
     * Method which returns a sibling of this map with the same data as this map
     *
     * @return a new TieredMap of the same type ass this one sharing the same
     * initial data and parent
     */
    @Override
    public TieredMap<K, V> clone() {
        TieredMap<K, V> map = new TieredMap();

        map.data = new HashMap(data);
        map.parent = this.parent;
        map.generation = this.generation;

        return map;
    }

    /**
     * Method to check equality between two TIeredMaps
     *
     * @param map the map to compare to
     * @return true if both maps share the same parent and same dataset
     */
    public boolean equals(TieredMap map) {
        return parent == map.parent && data.equals(map.data);
    }

    @Override
    public boolean equals(Object o) {
        return data.equals(o);
    }

    /**
     * Method to put a value in this map as well as all greater maps in the
     * hierarchy
     *
     * @param key the object to use as a key for storage
     * @param value the value to store
     * @return the root value being replaced, or null if none
     */
    @Override
    public V put(K key, V value) {
        if (parent == null) {
            return data.put(key, value);
        } else {
            data.put(key, value);
            return parent.put(key, value);  // RECURSION!
        }
    }

    /**
     * Copies all of the mappings from the specified map to this map as well as
     * all higher maps in the hierarchy. Please note that this will replace any
     * existing key-value pairs.
     *
     * @param map the source map to add from
     */
    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        data.putAll(map);
        if (parent != null) {
            parent.putAll(map);
        }
    }

    @Override
    public String toString() {
        return generation + ':' + data.toString();
    }
}
