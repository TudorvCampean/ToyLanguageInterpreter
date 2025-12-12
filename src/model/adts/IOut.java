package model.adts;

public interface IOut<T> {
    void add(T value);
    IOut<T> copy();
}
