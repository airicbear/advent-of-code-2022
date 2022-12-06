import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

public class Puzzle06 {
    public static void main(String[] args) {
        if (args.length > 0) {
            System.out.println(PacketProcessor.findFirstPacket(args[0]));
            System.out.println(MessageProcessor.findFirstMessage(args[0]));
        } else {
            System.err.println("ERROR: Filename not provided.\nUSAGE: java Puzzle06 [filename].");
        }
    }
}

class MessageProcessor {
    private static final int SIZE_MESSAGE_MARKER = 14;

    public static int findFirstMessage(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            Deque<Character> buffer = new ArrayDeque<>();

            int count = 1;
            int value;
            while ((value = reader.read()) != -1) {
                if (ProcessorUtils.validBufferSize(SIZE_MESSAGE_MARKER, buffer)) {
                    if (ProcessorUtils.validPacketMarker(buffer, (char) value)) {
                        return count;
                    }
                    buffer.removeFirst();
                }
                buffer.addLast((char) value);
                count++;
            }

            reader.close();

            return count;
        } catch (IOException e) {
            System.err.println("ERROR: File \"" + filename + "\" not found.");
            return -1;
        }
    }
}

class PacketProcessor {
    private static final int SIZE_PACKET_MARKER = 4;

    public static int findFirstPacket(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            Deque<Character> buffer = new ArrayDeque<>();

            int count = 1;
            int value;
            while ((value = reader.read()) != -1) {
                if (ProcessorUtils.validBufferSize(SIZE_PACKET_MARKER, buffer)) {
                    if (ProcessorUtils.validPacketMarker(buffer, (char) value)) {
                        return count;
                    }
                    buffer.removeFirst();
                }
                buffer.addLast((char) value);
                count++;
            }

            reader.close();

            return count;
        } catch (IOException e) {
            System.err.println("ERROR: File \"" + filename + "\" not found.");
            return -1;
        }
    }
}

class ProcessorUtils {
    public static boolean validBufferSize(int markerSize, Deque<Character> buffer) {
        return buffer.size() % (markerSize - 1) == 0 && !buffer.isEmpty();
    }

    public static boolean validPacketMarker(Deque<Character> buffer, char value) {
        return !hasDuplicate(buffer) && !buffer.contains((char) value);
    }

    private static boolean hasDuplicate(Deque<Character> buffer) {
        List<Character> list = buffer.stream().collect(Collectors.toList());
        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(i) == list.get(j)) {
                    return true;
                }
            }
        }
        return false;
    }
}