package model.statement;

import exception.MyException;
import model.adts.IExecutionStack;
import model.adts.IMyDictionary;
import model.adts.StackExecutionStack;
import model.statement.ProgramState;
import model.type.IType;
import model.value.IValue;

public record ForkStatement(IStatement statement) implements IStatement {
    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        IExecutionStack<IStatement> newExeStack = new StackExecutionStack<>();
        IMyDictionary<String, IValue> newSymTable = state.getSymTable().copy();

        return new ProgramState(
                newExeStack,
                newSymTable,
                state.getOut(),
                state.getFileTable(),
                state.getHeap(),
                statement
        );
    }

    @Override
    public IMyDictionary<String, IType> typecheck(IMyDictionary<String, IType> typeEnv) throws MyException {
        statement.typecheck(typeEnv.copy());
        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new ForkStatement(statement.deepCopy());
    }

    @Override
    public String toString() {
        return "fork(" + statement.toString() + ")";
    }
}
