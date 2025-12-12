package model.expression;

import exception.MyException;
import model.adts.IHeap;
import model.value.IValue;
import model.adts.IMyDictionary;

public interface IExpression {
    IValue evaluate(IMyDictionary<String,IValue> symTable, IHeap heap) throws MyException;
    IExpression deepCopy();
}
