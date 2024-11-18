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
        solver.Start(SudokuSolver.Type.BRUTE_FORCE);
    }

    private static void SetupWindow(JFrame frame){
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }
}