package model.adts;

import java.util.List;

public interface IExecutionStack<T> {
    void push(T statement);
    T pop();
    boolean isEmpty();
    IExecutionStack<T> copy();
    List<T> getReverse();
}
