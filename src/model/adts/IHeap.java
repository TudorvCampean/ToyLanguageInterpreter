package model.adts;

import exception.HeapException;
import model.value.IValue;
import java.util.Map;

public interface IHeap {
    int allocate(IValue value);
    IValue get(int address) throws HeapException;
    void update(int address, IValue value) throws HeapException;
    boolean isDefined(int address);
    Map<Integer, IValue> getContent();
    void setContent(Map<Integer, IValue> newContent);
}
