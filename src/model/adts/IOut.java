package model.adts;

import java.util.List;

public interface IOut<T> {
    void add(T value);
    IOut<T> copy();
    List<T> getList();
}
