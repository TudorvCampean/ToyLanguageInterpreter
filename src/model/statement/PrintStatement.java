package model.statement;

import exception.MyException;
import model.adts.IMyDictionary;
import model.expression.IExpression;
import model.type.IType;

public record PrintStatement(IExpression expression) implements IStatement {
    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        state.getOut().add(expression.evaluate(state.getSymTable(), state.getHeap()));
        return null;
    }

    @Override
    public IMyDictionary<String, IType> typecheck(IMyDictionary<String, IType> typeEnv) throws MyException {
        expression.typecheck(typeEnv);
        return typeEnv;
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
