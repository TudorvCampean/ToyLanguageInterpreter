package model.statement;

import exception.MyException;
import model.expression.IExpression;

public record PrintStatement(IExpression expression) implements IStatement {
    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        state.getOut().add(expression.evaluate(state.getSymTable(), state.getHeap()));
        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new PrintStatement(expression.deepCopy());
    }

    @Override
    public String toString() {
        return "print(" + expression.toString() + ")";
    }
}
