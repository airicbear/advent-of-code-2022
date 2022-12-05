import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Puzzle05 {
    private static final String ERROR_ARG_LENGTH = "ERROR: Filename not provided.\nUSAGE: java Puzzle05 [filename].";

    public static void main(String[] args) {
        if (args.length > 0) {
            CrateProcedure crateProcedure = CrateProcedure.from(args[0]);
            System.out.println(
                    crateProcedure.getTopCrates().stream()
                            .map(Object::toString)
                            .collect(Collectors.joining("")));
        } else {
            System.err.println(ERROR_ARG_LENGTH);
        }
    }
}

class CrateProcedure {
    private Crates crates;
    private Instructions instructions;

    public Crates getCrates() {
        return crates;
    }

    public Instructions getInstructions() {
        return instructions;
    }

    public List<Crate> getTopCrates() {
        List<Crate> topCrates = new ArrayList<>();
        for (int i = 0; i < crates.size(); i++) {
            int crateId = i + 1;
            topCrates.add(crates.peek(crateId));
        }
        return topCrates;
    }

    public static CrateProcedure from(String filename) {
        CrateProcedure crateProcedure = new CrateProcedure();

        try {
            Scanner s = new Scanner(new File(filename));

            crateProcedure.crates = Crates.from(s);
            s.nextLine();
            crateProcedure.instructions = Instructions.from(s);

            for (Instruction instruction : crateProcedure.instructions.getList()) {
                crateProcedure.crates.runInstruction9001(instruction);
            }

            s.close();
        } catch (FileNotFoundException e) {
            System.err.println("File \"" + filename + "\" not found.");
        }

        return crateProcedure;
    }
}

class Crates {
    private Map<Integer, Deque<Crate>> map;

    public void runInstruction9000(Instruction instruction) {
        for (int i = 0; i < instruction.getAmount(); i++) {
            push(instruction.getTo(), pop(instruction.getFrom()));
        }
    }

    public void runInstruction9001(Instruction instruction) {
        Deque<Crate> buffer = new ArrayDeque<>();
        for (int i = 0; i < instruction.getAmount(); i++) {
            buffer.push(pop(instruction.getFrom()));
        }
        for (int i = 0; i < instruction.getAmount(); i++) {
            push(instruction.getTo(), buffer.pop());
        }
    }

    public static Crates from(Scanner s) {
        Crates crates = new Crates();
        crates.map = new HashMap<>();

        while (s.hasNextLine()) {
            String line = s.nextLine();
            int numCrates = (line.length() + 1) / 4 + 1;

            if (line.isEmpty() || line.contains("1")) {
                break;
            }

            for (int i = 0; i < numCrates - 1; i++) {
                int crateIndex = i * 4 + 1;
                int crateId = i + 1;
                if (line.charAt(crateIndex) != ' ') {
                    Crate crate = Crate.from(line.charAt(crateIndex));
                    crates.map.putIfAbsent(crateId, new ArrayDeque<>());
                    crates.map.get(crateId).addLast(crate);
                }
            }
        }

        return crates;
    }

    public Crate peek(int id) {
        assert (map.containsKey(id)) : "There is no crate with ID \"" + id + "\".";

        return map.get(id).peek();
    }

    public int size() {
        return map.size();
    }

    private Crate pop(int id) {
        return map.get(id).pop();
    }

    private void push(int id, Crate crate) {
        map.get(id).push(crate);
    }

    @Override
    public String toString() {
        return map.toString();
    }
}

class Crate {
    private char value;

    public char getValue() {
        return value;
    }

    public static Crate from(String string) {
        assert (string.length() == 1);

        return from(string.charAt(0));
    }

    public static Crate from(char value) {
        Crate crate = new Crate();
        crate.value = value;
        return crate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(value);
        return sb.toString();
    }
}

class Instructions {
    private List<Instruction> list;

    public List<Instruction> getList() {
        return list;
    }

    public static Instructions from(Scanner s) {
        Instructions instructions = new Instructions();
        instructions.list = new ArrayList<>();

        while (s.hasNextLine()) {
            String line = s.nextLine();

            Instruction instruction = Instruction.from(line);
            instructions.list.add(instruction);
        }

        return instructions;
    }

    @Override
    public String toString() {
        return list.toString();
    }
}

class Instruction {
    private int amount;
    private int from;
    private int to;

    public int getAmount() {
        return amount;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public static Instruction from(String string) {
        String[] tokens = string.split(" ");

        assert (tokens.length == 6) : "Invalid string: \"" + string + "\"";

        int amount = Integer.parseInt(tokens[1]);
        int from = Integer.parseInt(tokens[3]);
        int to = Integer.parseInt(tokens[5]);
        return from(amount, from, to);
    }

    public static Instruction from(int amount, int from, int to) {
        Instruction instruction = new Instruction();
        instruction.amount = amount;
        instruction.from = from;
        instruction.to = to;
        return instruction;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("move ");
        sb.append(amount);
        sb.append(" from ");
        sb.append(from);
        sb.append(" to ");
        sb.append(to);
        return sb.toString();
    }
}