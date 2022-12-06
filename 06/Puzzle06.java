import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

public class Puzzle06 {
    public static void main(String[] args) {
        if (args.length > 0) {
            SignalReader signalReader = SignalReader.from(args[0]);
            System.out.println(signalReader.getCountUntilFirstPacket());
        } else {
            System.err.println("ERROR: Filename not provided.\nUSAGE: java Puzzle06 [filename].");
        }
    }
}

class SignalReader {
    private int countUntilFirstPacket;

    public int getCountUntilFirstPacket() {
        return countUntilFirstPacket;
    }

    public static SignalReader from(String filename) {
        SignalReader signalReader = new SignalReader();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            signalReader.countUntilFirstPacket = signalReader.processUntilFirstPacket(reader);
        } catch (IOException e) {
            System.err.println("ERROR: File \"" + filename + "\" not found.");
        }
        return signalReader;
    }

    private int processUntilFirstPacket(BufferedReader reader) throws IOException {
        Deque<Character> buffer = new ArrayDeque<>();

        int count = 0;
        char value;
        while ((value = (char) reader.read()) != -1) {
            if (buffer.size() % 3 == 0 && !buffer.isEmpty()) {
                if (!hasDuplicate(buffer) && !buffer.contains(value)) {
                    return count + 1;
                }
                buffer.removeFirst();
            }
            buffer.addLast(value);
            count++;
        }

        reader.close();

        return count;
    }

    private static boolean hasDuplicate(Deque<Character> buffer) {
        Deque<Character> copy = new ArrayDeque<>(buffer);
        char c1 = copy.removeFirst();
        char c2 = copy.peekFirst();
        char c3 = copy.peekLast();
        return c1 == c2 || c1 == c3 || c2 == c3;
    }
}