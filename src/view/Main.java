package view;

import controller.Controller;
import model.adts.*;
import model.expression.*;
import model.statement.*;
import model.type.BooleanType;
import model.type.IntegerType;
import model.type.RefType;
import model.type.StringType;
import model.value.BooleanValue;
import model.value.IValue;
import model.value.IntegerValue;
import model.value.StringValue;
import repository.IRepo;
import repository.Repo;

public class Main {
    public static void main(String[] args) {

        IExecutionStack<IStatement> exeStack = new StackExecutionStack<>();
        IMyDictionary<String, IValue> symTable = new MapSymbolTable<>();
        IOut<IValue> out = new ListOut<>();
        IMyDictionary<StringValue, java.io.BufferedReader> fileTable = new MapSymbolTable<>();
        IHeap heap = new MyHeap();

        IStatement example1 = getExample1();
        ProgramState state1 = new ProgramState(exeStack, symTable, out, fileTable,heap,example1);
        IRepo repo1 = new Repo("files/log1.txt", state1);
        Controller controller1 = new Controller(repo1);

        IStatement example2 = getExample2();
        ProgramState state2 = new ProgramState(new StackExecutionStack<>(), new MapSymbolTable<>(), new ListOut<>(), new MapSymbolTable<>(),new MyHeap(), example2);
        IRepo repo2 = new Repo("files/log2.txt", state2);
        Controller controller2 = new Controller(repo2);

        IStatement example3 = getExample3();
        ProgramState state3 = new ProgramState(new StackExecutionStack<>(), new MapSymbolTable
<>(), new ListOut<>(), new MapSymbolTable<>(),new MyHeap(), example3);
        IRepo repo3 = new Repo("files/log3.txt", state3);
        Controller controller3 = new Controller(repo3);

        IStatement example4 = getExample4_FileOperations();
        ProgramState state4 = new ProgramState(new StackExecutionStack<>(), new MapSymbolTable
<>(), new ListOut<>(), new MapSymbolTable<>(),new MyHeap(), example4);
        IRepo repo4 = new Repo("files/log4.txt", state4);
        Controller controller4 = new Controller(repo4);

        IStatement example5 = getExample5_Heap();
        ProgramState state5 = new ProgramState(new StackExecutionStack<>(), new MapSymbolTable<>(), new ListOut<>(), new MapSymbolTable<>(),new MyHeap(), example5);
        IRepo repo5 = new Repo("files/log5.txt", state5);
        Controller controller5 = new Controller(repo5);

        IStatement example6 = getExample6_While();
        ProgramState state6 = new ProgramState(new StackExecutionStack<>(), new MapSymbolTable<>(), new ListOut<>(), new MapSymbolTable<>(),new MyHeap(), example6);
        IRepo repo6 = new Repo("files/log6.txt", state6);
        Controller controller6 = new Controller(repo6);

        IStatement example7 = getExample7_Fork();
        ProgramState state7 = new ProgramState(new StackExecutionStack<>(), new MapSymbolTable<>(), new ListOut<>(), new MapSymbolTable<>(),new MyHeap(), example7);
        IRepo repo7 = new Repo("files/log7.txt", state7);
        Controller controller7 = new Controller(repo7);


        TextMenu menu = new TextMenu();
        menu.addCommand(new RunExampleCommand("1", "Execute example 1", controller1));
        menu.addCommand(new RunExampleCommand("2", "Execute example 2", controller2));
        menu.addCommand(new RunExampleCommand("3", "Execute example 3", controller3));
        menu.addCommand(new RunExampleCommand("4", "Execute example 4", controller4));
        menu.addCommand(new RunExampleCommand("5", "Execute example 5", controller5));
        menu.addCommand(new RunExampleCommand("6", "Execute example 6", controller6));
        menu.addCommand(new RunExampleCommand("7", "Execute example 7", controller7));
        menu.addCommand(new ExitCommand("0", "Exit"));
        menu.show();
    }
    private static IStatement getExample1() {
        // int v; v=2; Print(v)
        return new CompoundStatement(new VariableDeclarationStatement(new IntegerType(), "v"), new CompoundStatement(
                new AssignmentStatement("v", new ConstantExpression(new IntegerValue(2))),
                new PrintStatement(new VariableExpression("v"))
        )
        );
    }
    private static IStatement getExample2() {
        // int a; int b; a=2+3*5; b=a+1; Print(b)
        return new CompoundStatement(
                new VariableDeclarationStatement(new IntegerType(), "a"),
                new CompoundStatement(
                        new VariableDeclarationStatement(new IntegerType(), "b"),
                        new CompoundStatement(
                                new AssignmentStatement("a", new ArithmeticExpression("+",
                                        new ConstantExpression(new IntegerValue(2)),
                                        new ArithmeticExpression("*",
                                                new ConstantExpression(new IntegerValue(3)),
                                                new ConstantExpression(new IntegerValue(5))
                                        )
                                )),
                                new CompoundStatement(
                                        new AssignmentStatement("b", new ArithmeticExpression("+",
                                                new VariableExpression("a"),
                                                new ConstantExpression(new IntegerValue(1))
                                        )),
                                        new PrintStatement(new VariableExpression("b"))
                                )
                        )
                )
        );
    }
    private static IStatement getExample3() {
        // bool a; int v; a=true; (If a Then v=2 Else v=3); Print(v)
        return new CompoundStatement(
                new VariableDeclarationStatement(new BooleanType(), "a"),
                new CompoundStatement(
                        new VariableDeclarationStatement(new IntegerType(), "v"),
                        new CompoundStatement(
                                new AssignmentStatement("a", new ConstantExpression(new BooleanValue(true))),
                                new CompoundStatement(
                                        new IfStatement(
                                                new VariableExpression("a"),
                                                new AssignmentStatement("v", new ConstantExpression(new IntegerValue(2))),
                                                new AssignmentStatement("v", new ConstantExpression(new IntegerValue(3)))
                                        ),
                                        new PrintStatement(new VariableExpression("v"))
                                )
                        )
                )
        );
    }
    private static IStatement getExample4_FileOperations() {
        /*
        string varf;
        varf="test.in";
        openRFile(varf);
        int varc;
        readFile(varf,varc);print(varc);
        readFile(varf,varc);print(varc);
        closeRFile(varf)
    */
        return new CompoundStatement(
                // string varf;
                new VariableDeclarationStatement(new StringType(), "varf"),
                new CompoundStatement(
                        // varf="test.in";
                        new AssignmentStatement("varf", new ConstantExpression(new StringValue("test.in"))),
                        new CompoundStatement(
                                // openRFile(varf);
                                new OpenRFileStatement(new VariableExpression("varf")),
                                new CompoundStatement(
                                        // int varc;
                                        new VariableDeclarationStatement(new IntegerType(), "varc"),
                                        new CompoundStatement(
                                                // readFile(varf,varc);
                                                new ReadFileStatement(new VariableExpression("varf"), "varc"),
                                                new CompoundStatement(
                                                        // print(varc);
                                                        new PrintStatement(new VariableExpression("varc")),
                                                        new CompoundStatement(
                                                                // readFile(varf,varc);
                                                                new ReadFileStatement(new VariableExpression("varf"), "varc"),
                                                                new CompoundStatement(
                                                                        // print(varc);
                                                                        new PrintStatement(new VariableExpression("varc")),
                                                                        // closeRFile(varf)
                                                                        new CloseRFileStatement(new VariableExpression("varf"))
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
    }
    private static IStatement getExample5_Heap() {
        // Ref int v; new(v,20); print(rH(v)); wH(v,30); print(rH(v)+5);

        return new CompoundStatement(
                // Ref int v;
                new VariableDeclarationStatement(new RefType(new IntegerType()), "v"),
                new CompoundStatement(
                        // new(v, 20);
                        new NewStatement("v", new ConstantExpression(new IntegerValue(20))),
                        new CompoundStatement(
                                // print(rH(v));
                                new PrintStatement(new ReadHeapExpression(new VariableExpression("v"))),
                                new CompoundStatement(
                                        // wH(v, 30);
                                        new WriteHeapStatement("v", new ConstantExpression(new IntegerValue(30))),
                                        // print(rH(v) + 5);
                                        new PrintStatement(
                                                new ArithmeticExpression("+",
                                                        new ReadHeapExpression(new VariableExpression("v")),
                                                        new ConstantExpression(new IntegerValue(5))
                                                )
                                        )
                                )
                        )
                )
        );
    }
    private static IStatement getExample6_While() {
        // int v; v=4; while (v>0) {print(v); v=v-1;} print(v)
        return new CompoundStatement(
                new VariableDeclarationStatement(new IntegerType(), "v"),
                new CompoundStatement(
                        new AssignmentStatement("v", new ConstantExpression(new IntegerValue(4))),
                        new CompoundStatement(
                                new WhileStatement(
                                        // Condiția: v > 0
                                        new RelationalExpression(">", new VariableExpression("v"), new ConstantExpression(new IntegerValue(0))),
                                        // Corpul buclei: print(v); v=v-1
                                        new CompoundStatement(
                                                new PrintStatement(new VariableExpression("v")),
                                                new AssignmentStatement("v",
                                                        new ArithmeticExpression("-",
                                                                new VariableExpression("v"),
                                                                new ConstantExpression(new IntegerValue(1))
                                                        )
                                                )
                                        )
                                ),
                                // După while: print(v)
                                new PrintStatement(new VariableExpression("v"))
                        )
                )
        );
    }
    private static IStatement getExample7_Fork() {
        /*
         * int v; Ref int a; v=10; new(a,22);
         * fork(wH(a,30); v=32; print(v); print(rH(a)));
         * print(v); print(rH(a))
         */
        return new CompoundStatement(
                new VariableDeclarationStatement(new IntegerType(), "v"),
                new CompoundStatement(
                        new VariableDeclarationStatement(new RefType(new IntegerType()), "a"),
                        new CompoundStatement(
                                new AssignmentStatement("v", new ConstantExpression(new IntegerValue(10))),
                                new CompoundStatement(
                                        new NewStatement("a", new ConstantExpression(new IntegerValue(22))),
                                        new CompoundStatement(
                                                // --- FORK STATEMENT ---
                                                new ForkStatement(new CompoundStatement(
                                                        new WriteHeapStatement("a", new ConstantExpression(new IntegerValue(30))),
                                                        new CompoundStatement(
                                                                new AssignmentStatement("v", new ConstantExpression(new IntegerValue(32))),
                                                                new CompoundStatement(
                                                                        new PrintStatement(new VariableExpression("v")),
                                                                        new PrintStatement(new ReadHeapExpression(new VariableExpression("a")))
                                                                )
                                                        )
                                                )),
                                                // --- AFTER FORK (Main Thread) ---
                                                new CompoundStatement(
                                                        new PrintStatement(new VariableExpression("v")),
                                                        new PrintStatement(new ReadHeapExpression(new VariableExpression("a")))
                                                )
                                        )
                                )
                        )
                )
        );
    }
}
