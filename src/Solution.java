import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

class Row{
    private final int x0;
    private final int x1;
    private final int y0;
    private final int y1;

    public Row(int x0, int y0, int x1, int y1) {
        this.x0 = x0;
        this.x1 = x1;
        this.y0 = y0;
        this.y1 = y1;
    }

    public int getX0() {
        return x0;
    }

    public int getX1() {
        return x1;
    }

    public int getY0() {
        return y0;
    }

    public int getY1() {
        return y1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Row row = (Row) o;
        return x0 == row.x0 && x1 == row.x1 && y0 == row.y0 && y1 == row.y1;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x0, x1, y0, y1);
    }
}

public class Solution {
    private static int counter;

    public static int calculateSpace(int[][] rectangles) {
        counter = 0;
        int commonSpace = 0;
        final List<Row> list = new ArrayList<>();

        int length = rectangles.length;

        Arrays.stream(rectangles)
                .forEach(small -> {
                    Arrays.stream(rectangles)
                            .filter(rectangle -> !rectangle.equals(small))
                            .takeWhile(large -> (small[0] < large[0] || small[1] < large[1] || small[2] > large[2] || small[3] > large[3]))
                            .forEach(row -> counter++);

                    if (counter == length - 1)
                        list.add(new Row(small[0], small[1], small[2], small[3]));
                    counter = 0;
                });

        int listSize = list.size();

        for (int i = 0; i < listSize - 1; i++)
            for (int j = i + 1; j < listSize; j++)
                if ((list.get(j).getY0() >= list.get(i).getY0() && list.get(j).getY0() <= list.get(i).getY1())
                        || (list.get(j).getY1() >= list.get(i).getY0() && list.get(j).getY1() <= list.get(i).getY1())) {
                    if ((list.get(j).getX0() >= list.get(i).getX0() && list.get(j).getX0() <= list.get(i).getX1())
                            || (list.get(j).getX1() >= list.get(i).getX0() && list.get(j).getX1() <= list.get(i).getX1()))
                        commonSpace += calculateCommonSpace(list.get(j), list.get(i));
                }

        return listSize == 0 ? 0 : list.stream().mapToInt(rectangle -> (rectangle.getX1() - rectangle.getX0()) * (rectangle.getY1() - rectangle.getY0())).sum() - commonSpace;
    }

    private static int calculateCommonSpace(Row rowJ, Row rowI){
        int commonSpace;

        if (rowJ.getY0() >= rowI.getY0() && rowJ.getY1() <= rowI.getY1()) {
            if (rowJ.getX0() >= rowI.getX0() && rowJ.getX1() <= rowI.getX1())
                commonSpace = (rowJ.getX1() - rowJ.getX0()) * (rowJ.getY1() - rowJ.getY0());
            else if (rowJ.getX0() >= rowI.getX0())
                commonSpace = (rowI.getX1() - rowJ.getX0()) * (rowJ.getY1() - rowJ.getY0());
            else commonSpace = (rowJ.getX1() - rowI.getX0()) * (rowJ.getY1() - rowJ.getY0());
        }else if (rowJ.getY0() >= rowI.getY0()){
            if (rowJ.getX0() >= rowI.getX0() && rowJ.getX1() <= rowI.getX1())
                commonSpace = (rowJ.getX1() - rowJ.getX0()) * (rowI.getY1() - rowJ.getY0());
            else if (rowJ.getX0() >= rowI.getX0())
                commonSpace = (rowI.getX1() - rowJ.getX0()) * (rowI.getY1() - rowJ.getY0());
            else commonSpace = (rowJ.getX1() - rowI.getX0()) * (rowI.getY1() - rowJ.getY0());
        }else {
            if (rowJ.getX0() >= rowI.getX0() && rowJ.getX1() <= rowI.getX1())
                commonSpace = (rowJ.getX1() - rowJ.getX0()) * (rowJ.getY1() - rowI.getY0());
            else if (rowJ.getX0() >= rowI.getX0())
                commonSpace = (rowI.getX1() - rowJ.getX0()) * (rowJ.getY1() - rowI.getY0());
            else commonSpace = (rowJ.getX1() - rowI.getX0()) * (rowJ.getY1() - rowI.getY0());
        }

        return commonSpace;
    }
    public static void main(String[] args) {
        int[][] a = {{3,3,8,5}, {6,3,8,9}, {11,6,14,12}};

        System.out.println(Solution.calculateSpace(a));
    }
}
