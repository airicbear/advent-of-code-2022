import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

class Puzzle01 {
    private static final String ERROR_ARGUMENT_NOT_FOUND = "ERROR: Filename not provided.\nUSAGE: java Puzzle01 [filename].";

    public static void main(String[] args) {
        if (args.length > 0) {
            ElfListReader elfListReader = new ElfListReader(args[0]);
            System.out.println(elfListReader.getElfList().elfWithMostCalories());
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
                    elfList.lastElf().addFood(food);
                }
            }

            s.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ElfList getElfList() {
        return elfList;
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

    public Elf lastElf() {
        if (elves.size() > 0) {
            return elves.get(elves.size() - 1);
        } else {
            return null;
        }
    }

    public int elfWithMostCalories() {
        int maxCalories = 0;

        for (int i = 0; i < elves.size(); i++) {
            Elf elf = elves.get(i);

            if (elf.getTotalCaloriesOfSupplies() > maxCalories) {
                maxCalories = elf.getTotalCaloriesOfSupplies();
            }
        }

        return maxCalories;
    }
}

class Elf {
    private List<Food> supplies;

    public Elf() {
        this.supplies = new ArrayList<>();
    }

    public int getTotalCaloriesOfSupplies() {
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
