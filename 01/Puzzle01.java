import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

class Puzzle01 {
    private static final String ERROR_ARGUMENT_NOT_FOUND = "ERROR: Filename not provided.\nUSAGE: java Puzzle01 [filename].";

    public static void main(String[] args) {
        if (args.length > 0) {
            ElfListReader elfListReader = new ElfListReader(args[0]);
            System.out.println(elfListReader.getTotalCaloriesOfElfWithMostCalories());
            System.out.println(elfListReader.getTotalCaloriesOfTopThreeElvesWithMostCalories());
        } else {
            System.err.println(ERROR_ARGUMENT_NOT_FOUND);
        }
    }
}

class ElfListReader {
    private ElfList elfList;

    public ElfListReader(String filename) {
        File file = new File(filename);
        elfList = new ElfList();

        try {
            Scanner s = new Scanner(file);

            Elf elf = new Elf();
            elfList.add(elf);

            while (s.hasNextLine()) {
                String line = s.nextLine();

                if (line.isEmpty()) {
                    elfList.addNewElf();
                } else {
                    Food food = new Food(Integer.parseInt(line));
                    elfList.getLastElf().addFood(food);
                }
            }

            s.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public int getTotalCaloriesOfElfWithMostCalories() {
        return elfList.getMaxElf().getCalories();
    }
    
    public int getTotalCaloriesOfTopThreeElvesWithMostCalories() {
        List<Elf> maxElves = elfList.getMaxElves(3);

        int total = 0;
        for (int i = 0; i < 3; i++) {
            total += maxElves.get(i).getCalories();
        }

        return total;
    }
}

class ElfList {
    private List<Elf> elves;

    public ElfList() {
        elves = new ArrayList<>();
    }

    public void add(Elf elf) {
        elves.add(elf);
    }

    public void addNewElf() {
        elves.add(new Elf());
    }

    public int getSize() {
        return elves.size();
    }

    public Elf getElf(int index) {
        return elves.get(index);
    }

    public Elf getLastElf() {
        if (getSize() > 0) {
            return getElf(elves.size() - 1);
        } else {
            return null;
        }
    }

    public List<Elf> sortedElves() {
        List<Elf> sorted = new ArrayList<>(elves);
        sorted.sort(Comparator.comparing(Elf::getCalories).reversed());
        return sorted;
    }
    
    public List<Elf> getMaxElves(int n) {
        if (n < 1) {
            return new ArrayList<>();
        } else if (n > getSize()) {
            n = getSize();
        }

        List<Elf> maxElves = new ArrayList<>();
        List<Elf> sorted = sortedElves();

        for (int i = 0; i < n; i++) {
            maxElves.add(sorted.get(i));
        }

        return maxElves;
    }

    public Elf getMaxElf() {
        List<Elf> maxElves = getMaxElves(1);

        if (maxElves.isEmpty()) {
            return null;
        } else {
            return maxElves.get(0);
        }
    }
}

class Elf {
    private List<Food> supplies;

    public Elf() {
        this.supplies = new ArrayList<>();
    }

    public int getCalories() {
        int total = 0;

        for (int i = 0; i < supplies.size(); i++) {
            total += supplies.get(i).getCalories();
        }

        return total;
    }

    public void addFood(Food food) {
        this.supplies.add(food);
    }
}

class Food {
    private int calories;

    public Food(int calories) {
        this.calories = calories;
    }

    public int getCalories() {
        return calories;
    }
}
