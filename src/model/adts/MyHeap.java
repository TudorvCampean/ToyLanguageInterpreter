package model.adts;

import exception.HeapException;
import model.value.IValue;

import java.util.HashMap;
import java.util.Map;

public class MyHeap implements IHeap{

    private Map<Integer, IValue> map;
    private int freeLocation;

    public MyHeap() {
        this.map = new HashMap<>();
        this.freeLocation = 1;
    }

    @Override
    public int allocate(IValue value) {
        map.put(freeLocation, value);
        int allocatedAddress = freeLocation;
        freeLocation++;
        return allocatedAddress;
    }

    @Override
    public IValue get(int address) throws HeapException {
        if(!map.containsKey(address))
            throw new HeapException("The address " + address + " is not defined in the heap.");
        return map.get(address);
    }

    @Override
    public void update(int address, IValue value) throws HeapException {
        if(!map.containsKey(address))
            throw new HeapException("The address " + address + " is not defined in the heap.");
        map.put(address, value);
    }

    @Override
    public boolean isDefined(int address) {
        return map.containsKey(address);
    }

    @Override
    public Map<Integer, IValue> getContent() {
        return map;
    }

    @Override
    public void setContent(Map<Integer, IValue> newContent) {
        this.map = newContent;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, IValue> entry : map.entrySet()) {
            sb.append(entry.getKey())
                    .append(" -> ")
                    .append(entry.getValue())
                    .append("\n");
        }
        return sb.toString();
    }
}
