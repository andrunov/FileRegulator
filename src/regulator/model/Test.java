package regulator.model;

import java.util.Random;

public class Test {
    public static void main(String[] args) {
        /*
        int size = 9;
        int dimension = Integer.valueOf(size).toString().length();
        System.out.printf("Size: %s dimension: %s\n", size, dimension);
        size = 95;
        dimension = Integer.valueOf(size).toString().length();
        System.out.printf("Size: %s dimension: %s\n", size, dimension);
        size = 951;
        dimension = Integer.valueOf(size).toString().length();
        System.out.printf("Size: %s dimension: %s\n", size, dimension);
        size = 9535;
        dimension = Integer.valueOf(size).toString().length();
        System.out.printf("Size: %s dimension: %s\n", size, dimension);
         */

        Random random = new Random();
        System.out.println(random.nextInt(Integer.MAX_VALUE));
        System.out.println(random.nextInt(Integer.MAX_VALUE));
        System.out.println(random.nextInt(Integer.MAX_VALUE));
        System.out.println(random.nextInt(Integer.MAX_VALUE));
        System.out.println(random.nextInt(Integer.MAX_VALUE));

    }
}
