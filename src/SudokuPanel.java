import javax.swing.*;
import java.awt.*;

public class SudokuPanel {

    private JPanel panel;
    private JLabel[] cells = new JLabel[81];

    public SudokuPanel(){
        panel = new JPanel();
        panel.setLayout(new GridLayout(9,9));
        for (int i = 0; i < 81; i++) {
            JLabel label = new JLabel("");
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setPreferredSize(new Dimension(50,50));
            label.setBorder(BorderFactory.createLineBorder(Color.black));
            this.panel.add(label);
            cells[i] = label;
        }
        SetValue(1,1,9);
        SetValue(1,2,8);
        SetValue(1,3,5);
        SetValue(1,4,4);
        SetValue(1,6,1);
        SetValue(2,5,3);
        SetValue(3,1,1);
        SetValue(3,3,6);
        SetValue(4,4,5);
        SetValue(5,1,4);
        SetValue(5,3,2);
        SetValue(5,6,9);
        SetValue(5,9,3);
        SetValue(6,2,9);
        SetValue(6,5,6);
        SetValue(6,6,3);
        SetValue(6,7,4);
        SetValue(7,2,6);
        SetValue(7,5,1);
        SetValue(8,4,3);
        SetValue(8,6,6);
        SetValue(8,9,5);
        SetValue(9,1,2);
        SetValue(9,5,8);
        SetValue(9,9,1);
    }

    public JLabel[] GetCells(){
        return cells;
    }

    public JPanel GetPanel(){
        return panel;
    }

    public void SetValue(int cellID, int value){
        cells[cellID].setText(String.valueOf(value));
    }

    public void SetValue(int row, int column, int value){
        cells[column - 1 + (row - 1) * 9].setText(String.valueOf(value));
    }

}
