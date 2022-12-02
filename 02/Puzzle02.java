import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Puzzle02 {
    private static final String ERROR_ARGUMENT_NOT_FOUND = "ERROR: Filename not provided.\nUSAGE: java Puzzle01 [filename].";

    public static void main(String[] args) {
        
        if (args.length > 0) {
            StrategyListReader strategyListReader = new StrategyListReader(args[0]);
            System.out.println(strategyListReader.getStrategyScore());
        } else {
            System.err.println(ERROR_ARGUMENT_NOT_FOUND);
        }
    }
}

class StrategyListReader {
    private StrategyList strategyList;

    public StrategyListReader(String filename) {
        File file = new File(filename);
        strategyList = new StrategyList();

        try {
            Scanner s = new Scanner(file);

            while (s.hasNextLine()) {
                String line = s.nextLine();
                String[] tokens = line.split(" ");

                Move opponent = Move.parse(tokens[0]);
                Result result = Result.parse(tokens[1]);
                Move you = Match.calculateMove(opponent, result);
                Match match = new Match(opponent, you);

                strategyList.addMatch(match);
            }

            s.close();
        } catch (FileNotFoundException e) {
            System.err.println("ERROR: File \"" + filename + "\" not found.");
        }
    }

    public int getStrategyScore() {
        return strategyList.getTotalScore();
    }
}

class StrategyList {
    private List<Match> matches;

    public StrategyList() {
        this.matches = new ArrayList<>();
    }

    public void addMatch(Match match) {
        matches.add(match);
    }

    private List<Result> getResults() {
        List<Result> results = new ArrayList<>();
        for (Match match : matches) {
            results.add(match.judge());
        }
        return results;
    }

    public int getTotalScore() {
        List<Result> results = getResults();
        int total = 0;
        for (int i = 0; i < matches.size(); i++) {
            int resultScore = results.get(i).getValue();
            int moveScore = matches.get(i).getB().getWeight();
            total += resultScore + moveScore;
        }
        return total;
    }
}

class Match {
    private Move a;
    private Move b;

    public Match(Move a, Move b) {
        this.a = a;
        this.b = b;
    }

    public Result judge() {
        if (a == b) {
            return Result.DRAW;
        }
        if ((a == Move.ROCK && b == Move.PAPER)
            || (a == Move.PAPER && b == Move.SCISSORS)
            || (a == Move.SCISSORS && b == Move.ROCK)) {
            return Result.WIN;
        }
        return Result.LOSE;
    }

    public Move getB() {
        return b;
    }

    public static Move calculateMove(Move opponent, Result result) {
        switch (result) {
            case LOSE:
                switch (opponent) {
                    case ROCK:
                        return Move.SCISSORS;
                    case PAPER:
                        return Move.ROCK;
                    default:
                        return Move.PAPER;
                }
            case WIN:
                switch (opponent) {
                    case ROCK:
                        return Move.PAPER;
                    case PAPER:
                        return Move.SCISSORS;
                    default:
                        return Move.ROCK;
                }
            default:
                return opponent;
        }
    }
}

enum Result {
    LOSE(0), DRAW(3), WIN(6);

    private int value;

    Result(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Result parse(String s) {
        switch (s) {
            case "X":
                return LOSE;
            case "Y":
                return DRAW;
            case "Z":
                return WIN;
            default:
                return DRAW;
        }
    }
}

enum Move {
    ROCK(1), PAPER(2), SCISSORS(3);

    private int weight;

    Move(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    public static Move parse(String s) {
        switch (s) {
            case "A":
                return ROCK;
            case "B":
                return PAPER;
            case "C":
                return SCISSORS;
            default:
                return ROCK;
        }
    }
}