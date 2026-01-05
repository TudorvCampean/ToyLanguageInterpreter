package model.statement;

import exception.FileException;
import exception.MyException;
import exception.NotDefinedException;
import exception.TypeMissMatchException;
import model.adts.IHeap;
import model.expression.IExpression;
import model.adts.IMyDictionary;
import model.type.IType;
import model.type.IntegerType;
import model.type.StringType;
import model.value.IValue;
import model.value.IntegerValue;
import model.value.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public record ReadFileStatement(IExpression expression, String varName) implements IStatement {

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        IMyDictionary<String, IValue> symTable = state.getSymTable();
        IMyDictionary<StringValue, BufferedReader> fileTable = state.getFileTable();
        IHeap heap = state.getHeap();

        if (!symTable.isDefined(varName)) {
            throw new NotDefinedException("Error: Variable '" + varName + "' is not defined in SymTable.");
        }

        IValue expValue = null;
        try {
            expValue = symTable.getValue(varName);
        } catch (MyException e) {
            throw new RuntimeException(e);
        }
        if (!expValue.getType().equals(new IntegerType())) {
            throw new NotDefinedException("Error: Variable '" + varName + "' is not of type Integer.");
        }
        IValue evaluatedValue = expression.evaluate(symTable,heap);
        if (!evaluatedValue.getType().equals(new StringType())) {
            throw new TypeMissMatchException("Error: Expression does not evaluate to a String.");
        }

        StringValue fileNameValue = (StringValue) evaluatedValue;
        if (!fileTable.isDefined(fileNameValue)) {
            throw new FileException("Error: File '" + fileNameValue.value() + "' is not opened.");
        }

        BufferedReader bufferedReader = fileTable.getValue(fileNameValue);
        String line;
        try {
            line = bufferedReader.readLine();
        } catch (IOException e) {
            throw new FileException("Error reading from file: " + fileNameValue.value());
        }
        IntegerValue intValue;
        if (line == null) {
            intValue = new IntegerValue(0);
        } else {
            try {
                intValue = new IntegerValue(Integer.parseInt(line));
            } catch (NumberFormatException e) {
                throw new FileException("Error: The line read from file is not a valid integer.");
            }
        }
        symTable.update(varName, intValue);
        return null;
    }

    @Override
    public IMyDictionary<String, IType> typecheck(IMyDictionary<String, IType> typeEnv) throws MyException {
        IType typeVar = typeEnv.getValue(varName);
        IType typeExpr = expression.typecheck(typeEnv);

        if (typeVar.equals(new IntegerType())) {
            if (typeExpr.equals(new StringType())) {
                return typeEnv;
            } else {
                throw new TypeMissMatchException("ReadFileStatement: expression is not of type String.");
            }
        } else {
            throw new TypeMissMatchException("ReadFileStatement: variable " + varName + " is not of type Integer.");
        }
    }

    @Override
    public IStatement deepCopy() {
        return new ReadFileStatement(expression.deepCopy(), varName);
    }

    @Override
    public String toString() {
        return "readFile(" + expression.toString() + ", " + varName + ")";
    }
}
