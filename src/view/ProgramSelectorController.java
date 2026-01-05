package view;

import controller.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import exception.*;
import javafx.stage.Stage;
import model.adts.*;
import model.expression.*;
import model.statement.*;
import model.type.*;
import model.value.*;
import repository.IRepo;
import repository.Repo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProgramSelectorController {

    @FXML
    private ListView<IStatement> programsListView;

    @FXML
    private Button runButton;

    @FXML
    public void initialize() {
        List <IStatement> programs = getAllExamples();
        ObservableList<IStatement> observablePrograms = FXCollections.observableArrayList(programs);
        programsListView.setItems(observablePrograms);
        programsListView.getSelectionModel().selectFirst();
    }

    @FXML
    void handleRunButton(ActionEvent event) {
        IStatement selectedStmt = programsListView.getSelectionModel().getSelectedItem();

        if (selectedStmt == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error encountered!");
            alert.setContentText("No statement selected!");
            alert.showAndWait();
            return;
        }

        try {
            selectedStmt.typecheck(new MyDictionary<>());

            ProgramState programState = new ProgramState(
                    new StackExecutionStack<>(),
                    new MyDictionary<>(),
                    new ListOut<>(),
                    new MyDictionary<>(),
                    new MyHeap(),
                    selectedStmt
            );

            IRepo repo = new Repo("log.txt",programState);
            Controller controller = new Controller(repo);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
            Parent root = loader.load();

            // Obținem controller-ul ferestrei principale pentru a-i pasa datele
            MainWindowController mainWindowController = loader.getController();
            mainWindowController.setController(controller);

            // Creăm o nouă scenă și o afișăm
            Stage stage = new Stage();
            stage.setTitle("Main Window");
            stage.setScene(new Scene(root));

            // Setăm comportamentul la închidere (să închidă tot thread-ul JavaFX)
            stage.show();
        } catch (MyException e) {
            // EROARE DE VALIDARE (Type Check)
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Type Validation Error");
            alert.setHeaderText("The selected program failed the type check!");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch (IOException e) {
            // EROARE DE ÎNCĂRCARE FXML
            e.printStackTrace();
        }
    }

    private List<IStatement> getAllExamples() {
        List<IStatement> programs = new ArrayList<>();
        programs.add(getExample1());
        programs.add(getExample2());
        programs.add(getExample3());
        programs.add(getExample4_FileOperations());
        programs.add(getExample5_Heap());
        programs.add(getExample6_While());
        programs.add(getExample7_Fork());
        programs.add(getExample8_FailTypeCheck());
        return programs;
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

    private static IStatement getExample8_FailTypeCheck() {
        // int v; v=true; Print(v)
        return new CompoundStatement(new VariableDeclarationStatement(new IntegerType(), "v"), new CompoundStatement(
                new AssignmentStatement("v", new ConstantExpression(new BooleanValue(true))),
                new PrintStatement(new VariableExpression("v"))
        )
        );
    }

}