package model.statement;


import exception.MyException;
import exception.TypeMissMatchException;
import model.adts.IMyDictionary;
import model.expression.IExpression;
import model.type.BooleanType;
import model.type.IType;
import model.value.IValue;
import model.value.BooleanValue;

public record IfStatement(IExpression condition, IStatement thenBranch, IStatement elseBranch) implements IStatement {

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        IValue result = condition.evaluate(state.getSymTable(), state.getHeap());
        if (result instanceof BooleanValue(boolean value)) {
            if (value) {
                state.getExeStack().push(thenBranch);
            } else {
                state.getExeStack().push(elseBranch);
            }
        } else {
            throw new TypeMissMatchException("Condition expression does not evaluate to a boolean.");
        }
        return null;
    }

    @Override
    public IMyDictionary<String, IType> typecheck(IMyDictionary<String, IType> typeEnv) throws MyException {
        IType typeExpr = condition.typecheck(typeEnv);
        if (!typeExpr.equals(new BooleanType())){
            throw new TypeMissMatchException("The condition of IF does not have the type boolean");
        }
        thenBranch.typecheck(typeEnv.copy());
        elseBranch.typecheck(typeEnv.copy());
        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new IfStatement(condition.deepCopy(), thenBranch.deepCopy(), elseBranch.deepCopy());
    }

    @Override
    public String toString() {
        return "if(" + condition.toString() + ") then {" + thenBranch.toString() + "} else {" + elseBranch.toString() + "}";
    }
}
