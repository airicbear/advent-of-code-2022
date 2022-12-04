import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Puzzle04 {
    public static void main(String[] args) {
        if (args.length > 0) {
            AssignmentList assignmentList = AssignmentList.from(args[0]);
            System.out.println(assignmentList.countFullyContainedAssignments());
        } else {
            System.err.println("ERROR: Filename not provided.\nUSAGE: java Puzzle04 [filename]");
        }
    }
}

class AssignmentList {
    private List<AssignmentPair> assignmentPairs;

    public int countFullyContainedAssignments() {
        int total = 0;
        for (AssignmentPair assignmentPair : assignmentPairs) {
            if (assignmentPair.compare()) {
                total++;
            }
        }
        return total;
    }

    public static AssignmentList from(String filename) {
        AssignmentList assignmentList = init();

        try {
            Scanner s = new Scanner(new File(filename));

            while (s.hasNextLine()) {
                String line = s.nextLine();
                AssignmentPair assignmentPair = AssignmentPair.from(line);
                assignmentList.add(assignmentPair);
            }

            s.close();
        } catch (FileNotFoundException e) {
            System.err.println("ERROR: File \"" + filename + "\" not found.");
        }

        return assignmentList;
    }

    private void add(AssignmentPair assignmentPair) {
        assignmentPairs.add(assignmentPair);
    }

    private static AssignmentList init() {
        AssignmentList assignmentList = new AssignmentList();
        assignmentList.assignmentPairs = new ArrayList<>();
        return assignmentList;
    }
}

class AssignmentPair {
    private Assignment first;
    private Assignment second;
    private static final String DELIM = ",";

    public static AssignmentPair from(String string) {
        String[] tokens = string.split(DELIM);

        assert (tokens.length == 2);

        AssignmentPair assignmentPair = new AssignmentPair();
        assignmentPair.first = Assignment.from(tokens[0]);
        assignmentPair.second = Assignment.from(tokens[1]);
        return assignmentPair;
    }

    public boolean compare() {
        return first.getIdRange().compare(second.getIdRange());
    }
}

class Assignment {
    private IdRange idRange;

    public IdRange getIdRange() {
        return idRange;
    }

    public static Assignment from(String string) {
        Assignment assignment = new Assignment();
        assignment.idRange = IdRange.from(string);
        return assignment;
    }
}

class IdRange {
    private IdNumber initialNumber;
    private IdNumber finalNumber;
    private final static String DELIM = "-";

    public boolean compare(IdRange other) {
        return compare(this, other) || compare(other, this);
    }

    private static boolean compare(IdRange r1, IdRange r2) {
        boolean initContained = (r1.initialNumber.getValue() >= r2.initialNumber.getValue()
                && r1.initialNumber.getValue() <= r2.finalNumber.getValue());
        boolean finalContained = (r1.finalNumber.getValue() >= r2.initialNumber.getValue()
                && r1.finalNumber.getValue() <= r2.finalNumber.getValue());
        return initContained || finalContained;
    }

    public static IdRange from(String string) {
        String[] tokens = string.split(DELIM);

        assert (tokens.length == 2);

        IdNumber initialNumber = IdNumber.from(tokens[0]);
        IdNumber finalNumber = IdNumber.from(tokens[1]);
        return from(initialNumber, finalNumber);
    }

    public static IdRange from(IdNumber initialNumber, IdNumber finalNumber) {
        IdRange idRange = new IdRange();
        idRange.initialNumber = initialNumber;
        idRange.finalNumber = finalNumber;
        return idRange;
    }
}

class IdNumber {
    private int value;

    public int getValue() {
        return value;
    }

    public static IdNumber from(String string) {
        assert (!string.isEmpty());

        int value = Integer.parseInt(string);
        return from(value);
    }

    public static IdNumber from(int value) {
        assert (value > 0);

        IdNumber idNumber = new IdNumber();
        idNumber.value = value;
        return idNumber;
    }
}