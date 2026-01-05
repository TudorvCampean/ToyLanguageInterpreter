package model.statement;

import exception.MyException;
import model.adts.*;
import model.value.IValue;
import model.value.StringValue;

import java.io.BufferedReader;

public class ProgramState{
    private IExecutionStack<IStatement> exeStack;
    private IMyDictionary<String, IValue> symTable;
    private IOut<IValue> out;
    private IMyDictionary<StringValue, BufferedReader> fileTable;
    private IHeap heap;

    private IStatement originalProgram;

    private int id;
    private static int globalID = 0;

    public synchronized static int getNewID() {
        globalID++;
        return globalID;
    }

    public ProgramState(IExecutionStack<IStatement> stk,
                        IMyDictionary<String, IValue> symtbl,
                        IOut<IValue> ot,
                        IMyDictionary<StringValue, BufferedReader> fileTbl,
                        IHeap heap,
                        IStatement prg) {
        this.exeStack = stk;
        this.symTable = symtbl;
        this.out = ot;
        this.fileTable = fileTbl;
        this.heap = heap;
        this.id = getNewID();
        this.originalProgram = prg.deepCopy();


        // Logica din constructor: adaugă programul pe stivă
        stk.push(prg);
    }
    public Boolean isNotCompleted() {
        return !exeStack.isEmpty();
    }

    public ProgramState oneStep() throws MyException {
        if (exeStack.isEmpty()) {
            throw new MyException("Program state stack is empty");
        }
        IStatement currentStatement = exeStack.pop();
        return currentStatement.execute(this);
    }

    public IMyDictionary<StringValue, BufferedReader> getFileTable() {
        return fileTable;
    }

    public void setFileTable(IMyDictionary<StringValue, BufferedReader> fileTable) {
        this.fileTable = fileTable;
    }

    public IExecutionStack<IStatement> getExeStack() {
        return exeStack;
    }

    public void setExeStack(IExecutionStack<IStatement> exeStack) {
        this.exeStack = exeStack;
    }

    public IMyDictionary<String, IValue> getSymTable() {
        return symTable;
    }

    public void setSymTable(IMyDictionary<String, IValue> symTable) {
        this.symTable = symTable;
    }

    public IOut<IValue> getOut() {
        return out;
    }

    public void setOut(IOut<IValue> out) {
        this.out = out;
    }

    public IStatement getOriginalProgram() {
        return originalProgram;
    }

    public void setOriginalProgram(IStatement originalProgram) {
        this.originalProgram = originalProgram;
    }

    public IHeap getHeap() {
        return heap;
    }

    public void setHeap(IHeap heap) {
        this.heap = heap;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        // Folosim StringBuilder pentru o concatenare eficientă
        StringBuilder sb = new StringBuilder();

        sb.append("ID: ").append(id).append("\n");

        sb.append("ExeStack:\n");
        sb.append(exeStack.toString());

        sb.append("SymTable:\n");
        sb.append(symTable.toString());

        sb.append("Out:\n");
        sb.append(out.toString());
        sb.append("\n");

        sb.append("FileTable:\n");
        for (StringValue key : fileTable.getContent().keySet()) {
            sb.append(key.toString()).append("\n");
        }

        sb.append("Heap:\n");
        sb.append(heap.toString());

        sb.append("-----------------------\n");
        return sb.toString();
    }

    public ProgramState deepCopy() {
        return new ProgramState(
                new StackExecutionStack<>(),
                this.symTable.copy(),
                this.out,
                this.fileTable,
                this.heap,
                this.originalProgram.deepCopy()
        );
    }
}
