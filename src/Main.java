//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {

        JFrame frame = new JFrame();
        SudokuPanel panel = new SudokuPanel();
        frame.add(panel.GetPanel(), BorderLayout.CENTER);
        SetupWindow(frame);
        SudokuSolver solver = new SudokuSolver(panel.GetCells());

        SudokuSolver.Type type = SudokuSolver.Type.BRUTE_FORCE;
        if(args.length > 0){
            switch(args[0]){
                case "-b":
                    type = SudokuSolver.Type.BRUTE_FORCE;
                    break;
                case "-c":
                    type = SudokuSolver.Type.CONSTRAINT_PROPAGATION;
                    break;
            }
        }
        solver.Start(type);
    }

    private static void SetupWindow(JFrame frame){
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }
}