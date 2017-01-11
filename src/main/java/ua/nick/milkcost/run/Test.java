package ua.nick.milkcost.run;

public class Test {

    public static void main(String[] args) {

        int f = 0;
        int g = 1;
        int c;
        for (int i = 0; i < 15; i++) {
            System.out.println(f);
            c = f;
            //0112358132134
            f = f + g;
            //11235813213455
            g = c;
            //01123581321
        }


    }
}
