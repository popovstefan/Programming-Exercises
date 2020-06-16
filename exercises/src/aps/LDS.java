package aps;
import java.util.Scanner;


public class LDS {


    private static int najdolgaOpagackaSekvenca(int[] a) {
        int result = 1;
        // Vasiot kod tuka
        for (int i = 0; i < a.length - 1; i++) {
            int currIter = 1, smallest = a[i];
            for (int j = i; j < a.length; j++) {
                if (a[j] < smallest) {
                    smallest = a[j];
                    currIter++;
                }
            }
            if (currIter > result)
                result = currIter;
        }
        return result;
    }

    public static void main(String[] args) {
        Scanner stdin = new Scanner(System.in);

        int n = stdin.nextInt();
        int a[] = new int[n];
        for (int i = 0; i < a.length; i++) {
            a[i] = stdin.nextInt();
        }
        System.out.println(najdolgaOpagackaSekvenca(a));
    }


}
