import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

public class SudokuSolver {

    public enum Type {
        BRUTE_FORCE,
        CONSTRAINT_PROPAGATION,
    }

    private boolean solved = false;
    private boolean backtracking = false;
    private JTextField[] cells;
    private String[] initialValues = new String[81];
    private HashMap <Integer, Boolean> hints = new HashMap<>();
    private int[][] grid = {
            {0, 1, 2, 9, 10, 11, 18, 19, 20},
            {3, 4, 5, 12, 13, 14, 21, 22, 23},
            {6, 7 ,8, 15, 16, 17, 24, 25, 26},

            {27, 28, 29, 36, 37, 38, 45, 46, 47},
            {30, 31, 32, 39, 40, 41, 48, 49, 50},
            {33, 34, 35, 42, 43, 44, 51, 52, 53},

            {54, 55, 56, 63, 64, 65, 72, 73, 74},
            {57, 58, 59, 66, 67, 68, 75, 76, 77},
            {60, 61, 62, 69, 70, 71, 78, 79, 80},
    };

    public SudokuSolver(JTextField[] cells) {
        this.cells = cells;
        for(int i = 0; i < 81; i++)
        {
            initialValues[i] = cells[i].getText();
        }
    }

    public void reset(){
        solved = true;
        for (int i = 0; i < initialValues.length; i++) {
            cells[i].setText(initialValues[i]);
        }
    }

    public void clear(){
        if(solved){
            for (int i = 0; i < cells.length; i++) {
                cells[i].setText("");
            }

        }
    }

    public void Start(Type type) {
        //if(!isValidSudoku()) throw new RuntimeException("Invalid sudoku");
        for (int i = 0; i < initialValues.length; i++) {
            initialValues[i] = cells[i].getText();
        }
        solved = false;
        hints.clear();
        for (int i = 0; i < cells.length; i++) {
            if(GetCellValue(i) != 0){
                hints.put(i, true);
            }
        }
        switch(type){
            case BRUTE_FORCE:
                BruteForce();
                break;
            case CONSTRAINT_PROPAGATION:
                ConstraintPropagation();
                break;
        }
    }

    private boolean CanPlaceNumber(int cellID, int number){
        if(ColumnHasNumber(cellID, number)) return false;
        if(RowHasNumber(cellID, number)) return false;
        if(!IsUniqueInBox(cellID, number)) return false;
        return true;
    }

    private int GetCellValue(int id) {
        JTextField label = cells[id];
        int value;
        try {
            value = Integer.parseInt(label.getText());
        } catch (NumberFormatException e) {
            value = 0;
        }
        return value;
    }

    private void SetCellValue(int id, int value){
        cells[id].setText(Integer.toString(value));
    }

    private void ClearCellValue(int id){
        cells[id].setText("");
    }

    private boolean IsUniqueInBox(int cellID, int number){
        int boxID = GetBoxNumber(cellID);
        for (int i = 0; i < grid[boxID].length; i++) {
            int value = GetCellValue(grid[boxID][i]);
            if(value == number){
                return false;
            }
        }
        return true;
    }

    private int GetBoxNumber(int cellID){
        int columnBoxID = (cellID % 9) / 3;
        int RowBoxID = (cellID / 9) / 3;
        return columnBoxID + RowBoxID * 3;
    }

    private boolean ColumnHasNumber(int cellID, int number) {
        int pos = cellID % 9;

        for (int i = 0; i < 9; i++) {
            if(GetCellValue(pos) == number) return true;
            pos += 9;
        }

        return false;
    }

    private boolean RowHasNumber(int cellID, int number) {
        int pos = cellID / 9;
        pos *= 9;

        for (int i = 0; i < 9; i++) {
            if(GetCellValue(pos) == number) return true;
            pos++;
        }
        return false;
    }

    private boolean IsHint(int cellID){
        return hints.getOrDefault(cellID, false);
    }

    private int GetPossibleNumbers(int cellID){
        int count = 0;

        for (int i = 1; i <= 9; i++) {
            if(CanPlaceNumber(cellID, i)){
                count++;
            }
        }
        return count;
    }

    private ArrayList<Integer> GetPossibleNumbersList(int cellID){
        ArrayList<Integer> numbers = new ArrayList<>();

        for (int i = 1; i <= 9; i++) {
            if(CanPlaceNumber(cellID, i)){
                numbers.add(i);
            }
        }
        return numbers;
    }

    private boolean isValidSudoku(){
        for (int i = 0; i < cells.length; i++) {
            if(cells[i].getText().equals("")) continue;
            int value = Integer.parseInt(cells[i].getText());
            if(!IsUniqueInBox(i, value) ||
                RowHasNumber(i, value) ||
                ColumnHasNumber(i, value)){
                return false;
            }
        }
        return true;
    }

    // TODO: fix function breaking if there is more than one valid solution
    private void ConstraintPropagation_v2(){
        ArrayList<EmptyCell> emptyCells = new ArrayList<>();
        for (int i = 0; i < cells.length; i++) {
            if(!IsHint(i)){
                emptyCells.add(new EmptyCell(i, GetPossibleNumbersList(i)));
            }
        }
        emptyCells.sort(Comparator.comparing(EmptyCell::GetPossibleNumbers));

        int pos = 0;
        while(!solved){
            EmptyCell emptyCell = emptyCells.get(pos);
            boolean placed = false;

            if(emptyCell.possibleNumbers2.size() == emptyCell.nextValuePointer){
                emptyCell.nextValuePointer = 0;
                if(pos > 0){
                    ClearCellValue(emptyCell.cellID);
                    pos--;
                    continue;
                }
            }
            for (int i = 0; i < emptyCell.possibleNumbers2.size(); i++) {
                if(emptyCell.nextValuePointer + i == emptyCell.possibleNumbers2.size()){
                    break;
                }
                int value = emptyCell.possibleNumbers2.get(emptyCell.nextValuePointer + i);
                if(CanPlaceNumber(emptyCell.cellID, value)){
                    SetCellValue(emptyCell.cellID, value);
                    emptyCell.nextValuePointer = i + 1;
                    placed = true;
                    backtracking = false;
                    pos++;
                    if(pos == emptyCells.size()) solved = true;
                    break;
                }
            }
            if(!placed){
                ClearCellValue(emptyCell.cellID);
                emptyCell.nextValuePointer = 0;
                backtracking = true;
                pos--;
            }
        }
    }


    private void ConstraintPropagation(){
        ArrayList<EmptyCell> emptyCells = new ArrayList<>();
        for (int i = 0; i < cells.length; i++) {
            if(!IsHint(i)){
                emptyCells.add(new EmptyCell(i, GetPossibleNumbers(i)));
            }
        }
        emptyCells.sort(Comparator.comparing(EmptyCell::GetPossibleNumbers));

        int pos = 0;
        while(!solved){
            EmptyCell emptyCell = emptyCells.get(pos);
            boolean placed = false;

            int value = 1;
            if(backtracking){
                value = GetCellValue(emptyCell.cellID);
            }

            for (int i = value; i <= 9; i++) {
                if(CanPlaceNumber(emptyCell.cellID, i)){
                    SetCellValue(emptyCell.cellID, i);
                    backtracking = false;
                    placed = true;
                    if(++pos >= emptyCells.size()) solved = true;
                    break;
                }
            }
            if(!placed){
                ClearCellValue(emptyCell.cellID);
                pos--;
                backtracking = true;
            }
        }
    }

    private void BruteForce(){
        int currentCell = 0;
        while(!solved){
            if(currentCell >= cells.length){
                solved = true;
                return;
            }

            if(IsHint(currentCell)){
                if(backtracking){
                    if(currentCell == 0){
                        backtracking = false;
                    } else{
                        currentCell--;
                    }
                } else{
                    currentCell++;
                }
                continue;
            }

            int value = 1;
            if(backtracking){
                value = GetCellValue(currentCell);
            }

            boolean placed = false;
            for (int number = value; number <= 9; number++) {
                if(CanPlaceNumber(currentCell, number)){
                    SetCellValue(currentCell, number);
                    placed = true;
                    backtracking = false;
                    currentCell++;
                    break;
                }
            }
            if(!placed){
                ClearCellValue(currentCell);
                backtracking = true;
                currentCell--;
            }
        }
    }
}