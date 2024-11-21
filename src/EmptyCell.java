import java.util.ArrayList;

public class EmptyCell {
    public ArrayList<Integer> possibleNumbers2;
    public int possibleNumbers;
    public int cellID;
    public int nextValuePointer = 0;

    public EmptyCell(int cellID, int possibleNumbers) {
        this.cellID = cellID;
        this.possibleNumbers = possibleNumbers;
    }

    public EmptyCell(int cellID, ArrayList<Integer> possibleNumbers2) {
        this.cellID = cellID;
        this.possibleNumbers2 = possibleNumbers2;
        possibleNumbers = possibleNumbers2.size();
    }

    public int GetPossibleNumbers() {
        return possibleNumbers;
    }
}
