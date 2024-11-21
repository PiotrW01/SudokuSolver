import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.MatteBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.*;

public class SudokuPanel extends JPanel {

    private JPanel panel;
    private JTextField[] cells = new JTextField[81];
    private SudokuSolver solver;

    public SudokuPanel(){
        this.setLayout(new BorderLayout());
        panel = new JPanel();
        panel.setLayout(new GridLayout(9,9));
        for (int i = 0; i < 81; i++) {
            JTextField cell = new JTextField("");
            cell.setHighlighter(null);
            cell.setCaret(new DefaultCaret() {
                @Override
                public void paint(Graphics g) {

                }
            });
            cell.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            cell.setHorizontalAlignment(SwingConstants.CENTER);
            cell.setPreferredSize(new Dimension(50, 50));

            MatteBorder innerLeft = BorderFactory.createMatteBorder(1,1,1,2, Color.BLACK);
            MatteBorder innerRight = BorderFactory.createMatteBorder(1,1,1,2, Color.BLACK);
            Border inner2 = BorderFactory.createLineBorder(Color.BLACK, 1);
            int left = i + 1;
            int right = i - 1;
            if(left % 3 == 0 && left % 9 != 0){
                cell.setBorder(BorderFactory.createCompoundBorder(null, innerLeft));
            }
            else if(right % 3 == 0 && right % 9 != 0){
                cell.setBorder(BorderFactory.createCompoundBorder(null, innerRight));
            }
            else {
                cell.setBorder(BorderFactory.createCompoundBorder(null, inner2));
            }

            cell.addFocusListener(new FocusListener() {
                public void focusGained(FocusEvent e) {
                    CompoundBorder b = (CompoundBorder)cell.getBorder();
                    var targetBorder = BorderFactory.createLineBorder(Color.RED, 2);
                    cell.setBorder(BorderFactory.createCompoundBorder(targetBorder, b.getInsideBorder()));
                }

                @Override
                public void focusLost(FocusEvent e) {
                    CompoundBorder b = (CompoundBorder)cell.getBorder();
                    cell.setBorder(BorderFactory.createCompoundBorder(null, b.getInsideBorder()));
                }
            });
            cell.addKeyListener(new KeyAdapter() {

                @Override
                public void keyTyped(KeyEvent e) {
                    // 49 do 57
                    int ascii = (int)e.getKeyChar();
                    if(ascii == 8){
                        cell.setText("");
                        return;
                    }

                    if(ascii < 49 || ascii > 57){
                        e.consume();
                    } else {
                        cell.setText("");
                        cell.transferFocus();
                    }
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE && cell.getText().isEmpty()){
                        cell.transferFocusBackward();
                    }
                }
            });
            cell.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if(cell.hasFocus()) panel.grabFocus();
                }
            });
            this.panel.add(cell);
            this.panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            this.grabFocus();
            cells[i] = cell;
        }
        {
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
        solver = new SudokuSolver(cells);
        this.add(panel, BorderLayout.NORTH);
        this.add(new ButtonsPanel().getPanel(), BorderLayout.SOUTH);
    }

    private void SetValue(int row, int column, int value){
        cells[column - 1 + (row - 1) * 9].setText(String.valueOf(value));
    }


    private class ButtonsPanel {
        private JPanel panel;
        Thread thread;

        public ButtonsPanel() {
            panel = new JPanel();
            panel.setLayout(new FlowLayout(FlowLayout.CENTER));
            JButton buttonBF = new JButton("Brute Force");
            JButton buttonCP = new JButton("Constraint Propagation");
            JButton buttonReset = new JButton("Reset");
            JButton buttonClear = new JButton("Clear");
            panel.add(buttonBF);
            panel.add(buttonCP);
            panel.add(buttonReset);
            panel.add(buttonClear);
            buttonBF.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(thread != null) return;
                    thread = new Thread(() -> {
                        solver.Start(SudokuSolver.Type.BRUTE_FORCE);
                    });
                    thread.start();
                }
            });
            buttonCP.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(thread != null) return;
                    thread = new Thread(() -> {
                        solver.Start(SudokuSolver.Type.CONSTRAINT_PROPAGATION);
                    });
                    thread.start();
                }
            });
            buttonReset.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(thread != null){
                        try {
                            thread.join();
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                        thread = null;
                    }
                    solver.reset();
                }
            });
            buttonClear.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(thread != null) return;
                    solver.clear();
                }
            });
        }

        public JPanel getPanel() {
            return panel;
        }
    }
}
