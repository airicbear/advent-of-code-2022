import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Puzzle09 {
    public static void main(String[] args) {
        if (args.length > 0) {
            Instructions instructions = Instructions.from(args[0]);
            Rope rope = Rope.from(0, 4, 0, 4);
            State state = State.from(6, 5, 0, 4, rope);
            state.run(instructions);
            System.out.println(state.markSum());
        } else {
            System.err.println("ERROR: Filename not provided.\nUSAGE: java Puzzle09 [filename].");
        }
    }
}

class Mark {
    private int x;
    private int y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public static Mark from(int x, int y) {
        Mark mark = new Mark();
        mark.x = x;
        mark.y = y;
        return mark;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Mark)) {
            return false;
        }
        return ((Mark) obj).getX() == getX() && ((Mark) obj).getY() == getY();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + x;
        hash = 31 * hash + y;
        return hash;
    }
}

class State {
    private int sizeX;
    private int sizeY;
    private int startX;
    private int startY;
    private Rope rope;
    private Set<Mark> marks;

    public void run(Instructions instructions) {
        for (Instruction instruction : instructions.getInstructions()) {
            run(instruction);
        }
    }

    private void run(Instruction instruction) {
        rope.run(instruction, this);
    }

    public void mark(int x, int y) {
        Mark mark = Mark.from(x, y);
        marks.add(mark);
    }

    public int markSum() {
        int sum = 0;
        for (int i = 0; i < marks.size(); i++) {
            sum++;
        }
        return sum;
    }

    public static State from(int sizeX, int sizeY, int startX, int startY, Rope rope) {
        State state = new State();
        state.marks = new HashSet<>();
        state.sizeX = sizeX;
        state.sizeY = sizeY;
        state.startX = startX;
        state.startY = startY;
        state.rope = rope;
        return state;
    }

    private boolean isMarked(int x, int y) {
        for (Mark mark : marks) {
            if (mark.getX() == x && mark.getY() == y) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                if (startX == x && startY == y) {
                    sb.append('s');
                } else if (isMarked(x, y)) {
                    sb.append('#');
                } else if (rope.getHeadX() == x && rope.getHeadY() == y) {
                    sb.append('H');
                } else if (rope.getTailX() == x && rope.getTailY() == y) {
                    sb.append('T');
                } else {
                    sb.append('.');
                }
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}

class Rope {
    private int headX;
    private int headY;
    private int tailX;
    private int tailY;

    private void mark(State state) {
        state.mark(tailX, tailY);
    }

    public void run(Instruction instruction, State state) {
        mark(state);
        switch (instruction.getDirection()) {
            case UP:
                for (int i = 0; i < instruction.getMagnitude(); i++) {
                    headY--;
                    if (distance(tailY, headY) > 1) {
                        if (tailX != headX) {
                            tailX = headX;
                        }
                        tailY--;
                    }
                    mark(state);
                }
                break;
            case DOWN:
                for (int i = 0; i < instruction.getMagnitude(); i++) {
                    headY++;
                    if (distance(tailY, headY) > 1) {
                        if (tailX != headX) {
                            tailX = headX;
                        }
                        tailY++;
                        mark(state);
                    }
                }
                break;
            case LEFT:
                for (int i = 0; i < instruction.getMagnitude(); i++) {
                    headX--;
                    if (distance(tailX, headX) > 1) {
                        if (tailY != headY) {
                            tailY = headY;
                        }
                        tailX--;
                        mark(state);
                    }
                }
                break;
            default:
                for (int i = 0; i < instruction.getMagnitude(); i++) {
                    headX++;
                    if (distance(tailX, headX) > 1) {
                        if (tailY != headY) {
                            tailY = headY;
                        }
                        tailX++;
                        mark(state);
                    }
                }
                break;
        }
    }

    private int distance(int x1, int x2) {
        return Math.abs(x1 - x2);
    }

    public int getHeadX() {
        return headX;
    }

    public int getHeadY() {
        return headY;
    }

    public int getTailX() {
        return tailX;
    }

    public int getTailY() {
        return tailY;
    }

    public static Rope from(int headX, int headY, int tailX, int tailY) {
        Rope rope = new Rope();
        rope.headX = headX;
        rope.headY = headY;
        rope.tailX = tailX;
        rope.tailY = tailY;
        return rope;
    }
}

class Instructions {
    private List<Instruction> instructions;

    public List<Instruction> getInstructions() {
        return instructions;
    }

    public static Instructions from(String filename) {
        Instructions instructions = new Instructions();
        instructions.instructions = new ArrayList<>();

        try {
            Scanner s = new Scanner(new File(filename));

            while (s.hasNextLine()) {
                String line = s.nextLine();
                Instruction instruction = Instruction.from(line);
                instructions.instructions.add(instruction);
            }

            s.close();
        } catch (FileNotFoundException e) {
            System.err.println("ERROR: File \"" + filename + "\" not found.");
        }

        return instructions;
    }

    @Override
    public String toString() {
        return instructions.toString();
    }
}

class Instruction {
    private Direction direction;
    private int magnitude;

    public Direction getDirection() {
        return direction;
    }

    public int getMagnitude() {
        return magnitude;
    }

    public static Instruction from(String string) {
        String[] tokens = string.split(" ");

        assert (tokens.length == 2);

        Direction direction = Direction.from(tokens[0].charAt(0));
        int magnitude = Integer.parseInt(tokens[1]);

        return from(direction, magnitude);
    }

    public static Instruction from(Direction direction, int magnitude) {
        Instruction instruction = new Instruction();
        instruction.direction = direction;
        instruction.magnitude = magnitude;
        return instruction;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(direction);
        sb.append(' ');
        sb.append(magnitude);
        return sb.toString();
    }
}

enum Direction {
    UP, DOWN, LEFT, RIGHT;

    public static Direction from(char c) {
        switch (c) {
            case 'U':
                return UP;
            case 'D':
                return DOWN;
            case 'L':
                return LEFT;
            default:
                return RIGHT;
        }
    }
}