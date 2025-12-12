package model.statement;


import exception.MyException;
import exception.NotDefinedException;
import exception.TypeMissMatchException;
import model.adts.IHeap;
import model.expression.IExpression;
import model.adts.IMyDictionary;
import model.type.IType;
import model.value.IValue;

public record AssignmentStatement(String variableName, IExpression expression) implements IStatement {

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        IMyDictionary<String, IValue> symbolTable = state.getSymTable();
        IHeap heap = state.getHeap();
        if (!symbolTable.isDefined(variableName)) {
            throw new NotDefinedException("Variable " + variableName + " is not defined.");
        }
        IValue value = expression.evaluate(symbolTable,heap); // Aceasta este valoarea NOUĂ

        IValue currentValue = symbolTable.getValue(variableName); // Aceasta este valoarea VECHE

        IType variableType = currentValue.getType();
        if (!value.getType().equals(variableType)) {
            throw new TypeMissMatchException("Type mismatch: cannot assign " + value.getType() + " to variable " + variableName + " of type " + variableType);
        }
        symbolTable.update(variableName, value);
        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new AssignmentStatement(variableName, expression.deepCopy());
    }

    @Override
    public String toString() {
        return variableName + " = " + expression.toString();
    }
}
