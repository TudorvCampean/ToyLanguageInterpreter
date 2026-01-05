package model.adts;

import java.util.ArrayList;
import java.util.List;

public class ListOut<T> implements IOut<T>{

    private final List<T> outputList;
    public ListOut() {
        this.outputList = new ArrayList<>();
    }

    @Override
    public void add(T value) {
        outputList.add(value);
    }

    @Override
    public IOut<T> copy() {
        ListOut<T> newListOut = new ListOut<>();
        for (T value : outputList) {
            newListOut.add(value);
        }
        return newListOut;
    }

    @Override
    public List<T> getList() {
        return this.outputList;
    }

    @Override
    public String toString() {
        return outputList.toString();
    }

}
