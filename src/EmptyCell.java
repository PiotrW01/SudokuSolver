import java.util.ArrayList;

public class EmptyCell {
    public int possibleNumbers;
    public int cellID;

    public EmptyCell(int cellID, int possibleNumbers) {
        this.cellID = cellID;
        this.possibleNumbers = possibleNumbers;
    }

    public int GetPossibleNumbers() {
        return possibleNumbers;
    }

/*    public int GetPossibleNumbersLength(){
        return possibleNumbers.size();
    }

    public void ShiftNumber(){
        int number = possibleNumbers.get(0);
        possibleNumbers.remove(0);
        possibleNumbers.add(number);
    }

    public int GetNumber(){
        return possibleNumbers.get(0);
    }*/
}
