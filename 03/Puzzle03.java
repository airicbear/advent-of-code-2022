import java.io.FileNotFoundException;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Puzzle03 {
    public static void main(String[] args) {
        if (args.length > 0) {
            RucksackList rucksackList = RucksackList.parse(args[0]);
            System.out.println(rucksackList.sumCommonPriorities());
            System.out.println(rucksackList.sumGroupPriorities(3));
        } else {
            System.err.println("ERROR: Filename not provided.\nUSAGE: java Puzzle03 [filename].");
        }
    }
}

class RucksackList {
    private List<Rucksack> rucksacks;

    private RucksackList() {
        rucksacks = new ArrayList<>();
    }

    public void add(Rucksack rucksack) {
        rucksacks.add(rucksack);
    }

    public static RucksackList parse(String filename) {
        RucksackList rucksackList = new RucksackList();

        try {
            Scanner s = new Scanner(new File(filename));

            while (s.hasNextLine()) {
                String line = s.nextLine();
                Rucksack rucksack = Rucksack.parse(line);
                rucksackList.add(rucksack);
            }
        } catch (FileNotFoundException e) {
            System.err.println("ERROR: File \"" + filename + "\" not found.");
        }

        return rucksackList;
    }

    public int sumCommonPriorities() {
        int total = 0;
        for (Rucksack rucksack : rucksacks) {
            total += rucksack.findCommonItem();
        }
        return total;
    }

    public List<Group> groups(int n) {
        List<Group> groups = new ArrayList<>();
        for (int i = 0; i < rucksacks.size(); i += n) {
            Group group = Group.create();
            for (int j = 0; j < n; j++) {
                group.add(rucksacks.get(i + j));
            }
            groups.add(group);
        }
        return groups;
    }

    public int sumGroupPriorities(int n) {
        int total = 0;
        for (Group group : groups(n)) {
            total += group.findBadge().getPriority();
        }
        return total;
    }
}

class Group {
    private List<Rucksack> rucksacks;

    public static Group create() {
        Group group = new Group();
        group.rucksacks = new ArrayList<>();
        return group;
    }

    public void add(Rucksack rucksack) {
        rucksacks.add(rucksack);
    }

    public Item findBadge() {
        Set<Item> initialSet = rucksacks.get(0).itemSet();
        for (int i = 1; i < rucksacks.size(); i++) {
            initialSet.retainAll(rucksacks.get(i).itemSet());
        }
        return initialSet.iterator().next();
    }
}

class Rucksack {
    private Compartment left;
    private Compartment right;

    public static Rucksack parse(String s) {
        assert (s.length() % 2 == 1);

        Rucksack rucksack = new Rucksack();
        String left = s.substring(0, s.length() / 2);
        String right = s.substring(s.length() / 2, s.length());
        rucksack.left = Compartment.parse(left);
        rucksack.right = Compartment.parse(right);
        return rucksack;
    }

    public int findCommonItem() {
        Compartment intersection = Compartment.intersection(left, right);
        Item commonItem = intersection.itemIterator().next();
        return commonItem.getPriority();
    }

    public Set<Item> itemSet() {
        Set<Item> leftSet = new HashSet<>(left.getItems());
        Set<Item> rightSet = new HashSet<>(right.getItems());
        leftSet.addAll(rightSet);
        return leftSet;
    }
}

class Compartment {
    private List<Item> items;

    public Iterator<Item> itemIterator() {
        return items.iterator();
    }

    public List<Item> getItems() {
        return items;
    }

    public static Compartment intersection(Compartment left, Compartment right) {
        Set<Item> leftSet = new HashSet<>(left.items);
        Set<Item> rightSet = new HashSet<>(right.items);
        leftSet.retainAll(rightSet);
        return parse(leftSet);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (Item item : items) {
            sb.append(item.getValue());
        }
        sb.append(']');
        return sb.toString();
    }

    public static Compartment parse(String s) {
        Compartment compartment = new Compartment();

        compartment.items = new ArrayList<>();
        for (char c : s.toCharArray()) {
            compartment.add(Item.parse(c));
        }

        return compartment;
    }

    private static Compartment parse(Set<Item> itemSet) {
        Compartment compartment = new Compartment();

        compartment.items = new ArrayList<>();
        for (Item item : itemSet) {
            compartment.add(item);
        }

        return compartment;
    }

    private void add(Item item) {
        if (items != null) {
            items.add(item);
        }
    }
}

class Item {
    private char value;

    public static Item parse(char c) {
        Item item = new Item();
        item.value = c;
        return item;
    }

    public char getValue() {
        return value;
    }

    public int getPriority() {
        if (isLowercase()) {
            return value - 96;
        } else {
            return value - 38;
        }
    }

    @Override
    public boolean equals(Object obj) {
        return getPriority() == ((Item) obj).getPriority();
    }

    @Override
    public int hashCode() {
        return getPriority();
    }

    private boolean isLowercase() {
        return value > 96 && value < 123;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(value);
        sb.append('(');
        sb.append(getPriority());
        sb.append(')');
        return sb.toString();
    }
}