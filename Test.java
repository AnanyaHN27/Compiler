package compiler;

public class Test {
    public static void main(String[] args) {
        StringBuilder lol = new StringBuilder();
        lol.append("120.2");
        String hi = lol.toString();
        Double lol1 = Double.valueOf(hi) + 2;
        System.out.println(lol1);
    }


}
