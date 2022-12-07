import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Puzzle07 {
    public static void main(String[] args) {
        if (args.length > 0) {
            CommandLine commandLine = CommandLine.from(args[0]);
            System.out.println(commandLine.getSumDirectorySizes(100000));
        } else {
            System.err.println("ERROR: Filename not provided.\nUSAGE: java Puzzle07 [filename].");
        }
    }
}

class CommandLine {
    private Directory rootDirectory;
    private Deque<Directory> workingDirectory;

    public int getSumDirectorySizes(int max) {
        Deque<Directory> dirs = new ArrayDeque<>();
        dirs.add(rootDirectory);

        int total = 0;
        while (!dirs.isEmpty()) {
            Directory dir = dirs.pop();
            Iterator<Directory> iter = dir.getSubdirectories().iterator();
            while (iter.hasNext()) {
                Directory next = iter.next();
                int size = next.getSize();
                if (size <= max) {
                    total += size;
                }
                dirs.add(next);
            }
        }
        return total;
    }

    public static CommandLine from(String filename) {
        CommandLine commandLine = new CommandLine();
        commandLine.workingDirectory = new ArrayDeque<>();

        try {
            Scanner s = new Scanner(new File(filename));

            Deque<MyFile> contents = new ArrayDeque<>();

            while (s.hasNextLine()) {
                String line = s.nextLine();

                if (line.startsWith("$")) {
                    if (!commandLine.workingDirectory.isEmpty()) {
                        commandLine.workingDirectory.peekLast().add(contents);
                    }
                    contents.clear();
                    commandLine.run(line);
                } else {
                    if (line.startsWith("dir")) {
                        contents.add(Directory.from(line.split(" ")[1]));
                    } else {
                        contents.add(MyFile.from(line));
                    }
                }
            }

            commandLine.workingDirectory.peekLast().add(contents);

            s.close();
        } catch (FileNotFoundException e) {
            System.err.println("ERROR: File \"" + filename + "\" not found.");
        }

        return commandLine;
    }

    public void run(String command) {
        String[] tokens = command.split(" ");

        if (tokens.length <= 1) {
            return;
        }

        if (tokens[1].equals("cd")) {
            String dirname = tokens[2];
            Directory nextDirectory;

            if (rootDirectory == null) {
                rootDirectory = Directory.from(dirname);
                workingDirectory.add(rootDirectory);
            } else if (dirname.equals("..") && workingDirectory.size() > 1) {
                workingDirectory.removeLast();
            } else if ((nextDirectory = (Directory) workingDirectory.peekLast().findFile(dirname)) != null) {
                workingDirectory.add(nextDirectory);
            }
        }
    }
}

class MyFile {
    protected int size;
    protected String name;

    public int getSize() {
        return size;
    }

    public String getName() {
        return name;
    }

    public static MyFile from(String string) {
        String[] tokens = string.split(" ");

        assert (tokens.length == 2);

        return from(Integer.parseInt(tokens[0]), tokens[1]);
    }

    public static MyFile from(int size, String name) {
        MyFile myFile = new MyFile();
        myFile.size = size;
        myFile.name = name;
        return myFile;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        sb.append(name);
        sb.append(')');
        return sb.toString();
    }
}

class Directory extends MyFile {
    private List<MyFile> files;

    public List<Directory> getSubdirectories() {
        List<Directory> subdirectories = new ArrayList<>();
        for (MyFile file : getFiles()) {
            if (file instanceof Directory) {
                subdirectories.add((Directory) file);
            }
        }
        return subdirectories;
    }

    public List<MyFile> getFiles() {
        return files;
    }

    public int getSize() {
        int total = 0;
        for (MyFile file : getFiles()) {
            total += file.getSize();
        }
        return total;
    }

    public static Directory from(String name) {
        Directory directory = new Directory();
        directory.files = new ArrayList<>();
        directory.name = name;
        return directory;
    }

    public MyFile findFile(String target) {
        for (MyFile file : getFiles()) {
            if (file.name.equals(target)) {
                return file;
            }
        }
        return null;
    }

    public void add(Deque<MyFile> contents) {
        while (!contents.isEmpty()) {
            files.add(contents.pop());
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(DIR ");
        sb.append(name);
        sb.append(" { ");
        for (MyFile file : files) {
            sb.append(file);
            sb.append(' ');
        }
        sb.append('}');
        sb.append(')');
        return sb.toString();
    }
}