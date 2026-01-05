package model.adts;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class StackExecutionStack<T> implements IExecutionStack<T> {

    private final List<T> stack = new LinkedList<>();

    @Override
    public void push(T statement) {
        stack.addFirst(statement);
    }

    @Override
    public T pop() {
        if(stack.isEmpty()) {
            throw new RuntimeException("Execution stack is empty!");
        }
       return stack.removeFirst();
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public IExecutionStack<T> copy() {
        StackExecutionStack<T> newStack = new StackExecutionStack<>();
        LinkedList<T> linkedList = (LinkedList<T>) this.stack;
        var iterator = linkedList.descendingIterator();
        while (iterator.hasNext()) {
            newStack.push(iterator.next());
        }
        return newStack;
    }

    @Override
    public String toString() {
        // Afișează stiva de sus în jos
        StringBuilder sb = new StringBuilder();
        for (T elem : stack) { // LinkedList iterează de la primul (sus) la ultimul (jos)
            sb.append(elem.toString()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public List<T> getReverse() {
        return new ArrayList<>(stack);
    }
}
