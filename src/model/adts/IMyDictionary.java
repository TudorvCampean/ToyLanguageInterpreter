package model.adts;

import exception.MyException;

import java.util.Map;


public interface IMyDictionary<K,V> {
    boolean isDefined(K key);
    void update(K key, V value);
    V getValue(K key) throws MyException;
    Map<K, V> getContent();
    void remove(K key);
    IMyDictionary<K, V> copy();
}
