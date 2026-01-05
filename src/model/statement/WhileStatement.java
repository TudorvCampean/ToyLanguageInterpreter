package model.statement;

import exception.MyException;
import exception.TypeMissMatchException;
import model.adts.IHeap;
import model.adts.IMyDictionary;
import model.adts.IExecutionStack;
import model.expression.IExpression;
import model.type.BooleanType;
import model.type.IType;
import model.value.BooleanValue;
import model.value.IValue;

public record WhileStatement(IExpression expression,IStatement statement) implements IStatement {
    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        IMyDictionary<String,IValue> symTable=state.getSymTable();
        IHeap heap=state.getHeap();
        IExecutionStack<IStatement> exeStack=state.getExeStack();

        IValue conditionValue = expression.evaluate(symTable,heap);
        if (!conditionValue.getType().equals(new BooleanType())){
           throw new TypeMissMatchException("While condition is not a boolean.");
        }
        BooleanValue boolValue=(BooleanValue) conditionValue;
        if (boolValue.value()) {
            exeStack.push(this);
            exeStack.push(statement);
        }
        return null;
    }

    @Override
    public IMyDictionary<String, IType> typecheck(IMyDictionary<String, IType> typeEnv) throws MyException {
        IType typeExpr = expression.typecheck(typeEnv);
        if (!typeExpr.equals(new BooleanType())){
            throw new TypeMissMatchException("The condition of WHILE does not have the type boolean");
        }
        statement.typecheck(typeEnv.copy());
        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new WhileStatement(expression.deepCopy(),statement.deepCopy());
    }
    @Override
    public String toString() {
        return "while(" + expression.toString() + ") { " + statement.toString() + " }";
    }
}
