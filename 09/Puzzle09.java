import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Puzzle09 {
    public static void main(String[] args) {
        if (args.length > 0) {
            Instructions instructions = Instructions.from(args[0]);
            Rope rope = Rope.from(0, 4);
            State state = State.from(6, 5, 0, 4, rope);
            state.run(instructions);
            System.out.println(state.markSum());

            LongRope longRope = LongRope.from(12, 16, 10);
            LongRopeState state2 = LongRopeState.from(26, 22, 12, 16, longRope);
            state2.run(instructions);
            System.out.println(state2.markSum());
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Mark(x=");
        sb.append(x);
        sb.append(",y=");
        sb.append(y);
        sb.append(')');
        return sb.toString();
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

class LongRopeState {
    private int sizeX;
    private int sizeY;
    private int startX;
    private int startY;
    private LongRope longRope;
    private Set<Mark> marks;

    public Set<Mark> marks() {
        return marks;
    }

    public LongRope getLongRope() {
        return longRope;
    }

    public void run(Instructions instructions) {
        for (Instruction instruction : instructions.getInstructions()) {
            run(instruction);
        }
    }

    public void run(Instruction instruction) {
        longRope.run(instruction, this);
    }

    public void mark(int x, int y) {
        Mark mark = Mark.from(x, y);
        marks.add(mark);
    }

    public int markSum() {
        return marks.size();
    }

    public static LongRopeState from(int sizeX, int sizeY, int startX, int startY, LongRope longRope) {
        LongRopeState state = new LongRopeState();
        state.marks = new HashSet<>();
        state.sizeX = sizeX;
        state.sizeY = sizeY;
        state.startX = startX;
        state.startY = startY;
        state.longRope = longRope;
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

    public Knot findKnot(int x, int y) {
        for (Knot knot : longRope.getKnots()) {
            if (knot.getX() == x && knot.getY() == y) {
                return knot;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                Knot knot = findKnot(x, y);
                if (knot != null) {
                    sb.append(knot.getLabel());
                } else if (startX == x && startY == y) {
                    sb.append('s');
                } else if (isMarked(x, y)) {
                    sb.append('#');
                } else {
                    sb.append('.');
                }
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}

class LongRope {
    private Deque<Knot> knots;
    private int length;

    public int getLength() {
        return length;
    }

    public Deque<Knot> getKnots() {
        return knots;
    }

    public Knot head() {
        return knots.peek();
    }

    public Knot tail() {
        return knots.peekLast();
    }

    public static LongRope from(int x, int y, int length) {
        LongRope longRope = new LongRope();
        longRope.knots = new ArrayDeque<>();
        longRope.length = length;

        for (int i = 0; i < length; i++) {
            char label;
            if (i == length - 1) {
                label = 'H';
            } else {
                label = Integer.toString(length - i - 1).charAt(0);
            }
            Knot knot = Knot.from(label, x, y);
            longRope.knots.push(knot);
        }

        return longRope;
    }

    public void run(Instruction instruction, LongRopeState state) {
        Iterator<Knot> iter;
        Knot head;
        for (int i = 0; i < instruction.getMagnitude(); i++) {
            iter = knots.iterator();
            head = iter.next();
            Knot next = null;
            int j = 0;
            while (iter.hasNext() || next != null) {
                if (next != null) {
                    if (isAdjacent(head, next)) {
                        break;
                    }

                    if (j == 1) {
                        if (instruction.isHorizontal() && next.getY() != head.getY()) {
                            next.setY(head.getY());
                        } else if (instruction.isVertical() && next.getX() != head.getX()) {
                            next.setX(head.getX());
                        }
                    }
                    if (head.getX() > next.getX()) {
                        next.moveOnce(Direction.RIGHT);
                    } else if (head.getX() < next.getX()) {
                        next.moveOnce(Direction.LEFT);
                    }
                    if (head.getY() > next.getY()) {
                        next.moveOnce(Direction.DOWN);
                    } else if (head.getY() < next.getY()) {
                        next.moveOnce(Direction.UP);
                    }

                    head = next;
                } else if (next == null) {
                    head.moveOnce(instruction.getDirection());
                }
                j++;

                if (!iter.hasNext()) {
                    break;
                }
                next = iter.next();
            }
            state.mark(tail().getX(), tail().getY());
        }

    }

    private boolean isAdjacent(Knot k1, Knot k2) {
        return isHorizontallyAdjacent(k1, k2) && isVerticallyAdjacent(k1, k2);
    }

    private boolean isHorizontallyAdjacent(Knot k1, Knot k2) {
        return Math.abs(k1.getX() - k2.getX()) <= 1;
    }

    private boolean isVerticallyAdjacent(Knot k1, Knot k2) {
        return Math.abs(k1.getY() - k2.getY()) <= 1;
    }

    @Override
    public String toString() {
        return knots.toString();
    }
}

class Knot {
    private char label;
    private int x;
    private int y;

    public char getLabel() {
        return label;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void moveOnce(Direction direction) {
        switch (direction) {
            case UP:
                y--;
                break;
            case DOWN:
                y++;
                break;
            case LEFT:
                x--;
                break;
            default:
                x++;
                break;
        }
    }

    public static Knot from(char label, int x, int y) {
        Knot knot = new Knot();
        knot.label = label;
        knot.x = x;
        knot.y = y;
        return knot;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Knot(label=");
        sb.append(label);
        sb.append(",x=");
        sb.append(x);
        sb.append(",y=");
        sb.append(y);
        sb.append(')');
        return sb.toString();
    }
}

class Rope {
    private int headX;
    private int headY;
    private int tailX;
    private int tailY;

    public void setHead(int x, int y) {
        headX = x;
        headY = y;
    }

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

    public static Rope from(int x, int y) {
        Rope rope = new Rope();
        rope.headX = x;
        rope.headY = y;
        rope.tailX = x;
        rope.tailY = y;
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

    public boolean isVertical() {
        return direction.isVertical();
    }

    public boolean isUp() {
        return direction == Direction.UP;
    }

    public boolean isDown() {
        return direction == Direction.DOWN;
    }

    public boolean isLeft() {
        return direction == Direction.LEFT;
    }

    public boolean isRight() {
        return direction == Direction.RIGHT;
    }

    public boolean isHorizontal() {
        return direction.isHorizontal();
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

    public boolean isVertical() {
        return this == UP || this == DOWN;
    }

    public boolean isHorizontal() {
        return this == LEFT || this == RIGHT;
    }

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