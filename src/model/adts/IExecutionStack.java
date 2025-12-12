package model.adts;

public interface IExecutionStack<T> {
    void push(T statement);
    T pop();
    boolean isEmpty();
    IExecutionStack<T> copy();
}
