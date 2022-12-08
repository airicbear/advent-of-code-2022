import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Puzzle08 {
    public static void main(String[] args) {
        if (args.length > 0) {
            Trees trees = Trees.from(args[0]);
            System.out.println(trees.getVisibleTrees().size());
            System.out.println(trees.maxScenicScore());
        } else {
            System.err.println("ERROR: Filename not provided.\nUSAGE: java Puzzle08 [filename].");
        }
    }
}

class Trees {
    private List<List<Tree>> trees;

    public List<List<Tree>> getTrees() {
        return trees;
    }

    public List<Tree> getVisibleTrees() {
        List<Tree> visible = new ArrayList<>();
        for (int i = 0; i < trees.size(); i++) {
            for (int j = 0; j < trees.get(i).size(); j++) {
                Tree tree = trees.get(i).get(j);
                if (isVisible(tree)) {
                    visible.add(tree);
                }
            }
        }
        return visible;
    }

    public static Trees from(String filename) {
        Trees trees = new Trees();
        trees.trees = new ArrayList<>();

        try {
            Scanner s = new Scanner(new File(filename));
            int row = 0;
            while (s.hasNextLine()) {
                String line = s.nextLine();

                trees.trees.add(parseTreeRow(line, row));
                row++;
            }

            s.close();
        } catch (FileNotFoundException e) {
            System.err.println("ERROR: File \"" + filename + "\" not found.");
        }

        return trees;
    }

    private boolean isVisible(Tree tree) {
        boolean c1 = isEdge(tree);
        boolean c2 = isVisibleVertical(tree);
        boolean c3 = isVisibleHorizontal(tree);
        return c1 || c2 || c3;
    }

    private boolean isVisibleVertical(Tree tree) {
        return isVisibleFromTop(tree) || isVisibleFromBottom(tree);
    }

    private boolean isVisibleHorizontal(Tree tree) {
        return isVisibleFromLeft(tree) || isVisibleFromRight(tree);
    }

    private boolean isEdge(Tree tree) {
        if (tree.getXPos() == 0
                || tree.getXPos() == trees.get(0).size() - 1
                || tree.getYPos() == 0
                || tree.getYPos() == trees.size() - 1) {
            return true;
        }
        return false;
    }

    private List<Tree> getRow(int row) {
        List<Tree> list = new ArrayList<>();
        for (int i = 0; i < trees.size(); i++) {
            list.add(trees.get(row).get(i));
        }
        return list;
    }

    private List<Tree> getColumn(int col) {
        List<Tree> list = new ArrayList<>();
        for (int i = 0; i < trees.get(0).size(); i++) {
            list.add(trees.get(i).get(col));
        }
        return list;
    }

    private boolean isVisibleFromTop(Tree tree) {
        List<Tree> column = getColumn(tree.getXPos());

        for (int i = 0; i < tree.getYPos(); i++) {
            if (column.get(i).getHeight() >= tree.getHeight()) {
                return false;
            }
        }
        return true;
    }

    private boolean isVisibleFromBottom(Tree tree) {
        List<Tree> column = getColumn(tree.getXPos());

        for (int i = tree.getYPos() + 1; i < trees.size(); i++) {
            if (column.get(i).getHeight() >= tree.getHeight()) {
                return false;
            }
        }
        return true;
    }

    private boolean isVisibleFromLeft(Tree tree) {
        List<Tree> row = getRow(tree.getYPos());

        for (int i = 0; i < tree.getXPos(); i++) {
            if (row.get(i).getHeight() >= tree.getHeight()) {
                return false;
            }
        }
        return true;
    }

    private boolean isVisibleFromRight(Tree tree) {
        List<Tree> row = getRow(tree.getYPos());

        for (int i = tree.getXPos() + 1; i < trees.get(0).size(); i++) {
            if (row.get(i).getHeight() >= tree.getHeight()) {
                return false;
            }
        }
        return true;
    }

    public int maxScenicScore() {
        int maxScenicScore = 0;
        for (int i = 0; i < trees.size(); i++) {
            for (int j = 0; j < trees.get(i).size(); j++) {
                Tree tree = trees.get(i).get(j);
                int scenicScore = scenicScore(tree);
                if (scenicScore > maxScenicScore) {
                    maxScenicScore = scenicScore;
                }
            }
        }
        return maxScenicScore;
    }

    private int scenicScore(Tree tree) {
        return viewingDistanceUp(tree)
                * viewingDistanceDown(tree)
                * viewingDistanceLeft(tree)
                * viewingDistanceRight(tree);
    }

    private int viewingDistanceUp(Tree tree) {
        List<Tree> column = getColumn(tree.getXPos());
        int dist = 0;

        for (int i = tree.getYPos() - 1; i >= 0; i--) {
            dist++;
            if (column.get(i).getHeight() >= tree.getHeight()) {
                break;
            }
        }

        return dist;
    }

    private int viewingDistanceDown(Tree tree) {
        List<Tree> column = getColumn(tree.getXPos());
        int dist = 0;

        for (int i = tree.getYPos() + 1; i < trees.size(); i++) {
            dist++;
            if (column.get(i).getHeight() >= tree.getHeight()) {
                break;
            }
        }

        return dist;
    }

    private int viewingDistanceLeft(Tree tree) {
        List<Tree> row = getRow(tree.getYPos());
        int dist = 0;

        for (int i = tree.getXPos() - 1; i >= 0; i--) {
            dist++;
            if (row.get(i).getHeight() >= tree.getHeight()) {
                break;
            }
        }

        return dist;
    }

    private int viewingDistanceRight(Tree tree) {
        List<Tree> row = getRow(tree.getYPos());
        int dist = 0;

        for (int i = tree.getXPos() + 1; i < trees.get(0).size(); i++) {
            dist++;
            if (row.get(i).getHeight() >= tree.getHeight()) {
                break;
            }
        }

        return dist;
    }

    private static List<Tree> parseTreeRow(String line, int row) {
        List<Tree> treeRow = new ArrayList<>();
        String[] tokens = line.split("");
        for (int i = 0; i < tokens.length; i++) {
            Tree tree = Tree.from(Integer.parseInt(tokens[i]), i, row);
            treeRow.add(tree);
        }
        return treeRow;
    }
}

class Tree {
    private int height;
    private int xPos;
    private int yPos;

    public int getHeight() {
        return height;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

    public static Tree from(int height, int xPos, int yPos) {
        Tree tree = new Tree();
        tree.height = height;
        tree.xPos = xPos;
        tree.yPos = yPos;
        return tree;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(height);
        sb.append("(xPos=");
        sb.append(xPos);
        sb.append(",yPos=");
        sb.append(yPos);
        sb.append(")");
        return sb.toString();
    }
}