package aps.voved_algoritmi;

public class BruteForce {

    int INF = 1000000;

    double min(double a, double b) {
        if (a < b) {
            return a;
        } else {
            return b;
        }
    }

    double rastojanie(Point a, Point b) {
        return Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
    }

    double najmalo_rastojanie(Point p[], int n) {
        int i, j;
        double best = INF;

        for (i = 0; i < (n - 1); i++) {
            for (j = i + 1; j < n; j++) {
                best = min(best, rastojanie(p[i], p[j]));
            }
        }

        return best;
    }

    int dali_se_napagaat(int i1, int j1, int i2, int j2) {
        if (i1 == i2) // ista redica
        {
            return 1;
        }
        if (j1 == j2) // ista kolona
        {
            return 1;
        }
        if (Math.abs(i1 - i2) == Math.abs(j1 - j2)) // ista dijagonala
        {
            return 1;
        }
        return 0;
    }

    int broj_na_nacini() {
        int i1, j1, i2, j2;
        int rezultat = 0;

        for (i1 = 0; i1 < 8; i1++) {
            for (j1 = 0; j1 < 8; j1++) {
                for (i2 = 0; i2 < 8; i2++) {
                    for (j2 = 0; j2 < 8; j2++) {
                        if (dali_se_napagaat(i1, j1, i2, j2) == 0) {
                            rezultat++;
                        }
                    }
                }
            }
        }

        return rezultat;
    }
    
    int min_broj_moneti(int suma) {
        int m1, m2, m3, m4, m5;
        int tmp;
        int rezultat = INF;
        int br_paricki;

        for (m1 = 0; m1 <= (suma / 50); m1++) {
            for (m2 = 0; m2 <= (suma / 10); m2++) {
                for (m3 = 0; m3 <= (suma / 5); m3++) {
                    for (m4 = 0; m4 <= (suma / 2); m4++) {
                        for (m5 = 0; m5 <= (suma / 1); m5++) {
                            tmp = m1 * 50 + m2 * 10 + m3 * 5 + m4 * 2 + m5 * 1;
                            if (tmp == suma) {
                                br_paricki = m1 + m2 + m3 + m4 + m5;
                                if (br_paricki < rezultat) {
                                    rezultat = br_paricki;
                                }
                            }
                        }
                    }
                }
            }
        }

        return rezultat;
    }

    // printf("%d\n", min_broj_moneti(79));
    public static void main(String[] args) {

        BruteForce bf = new BruteForce();
        
        Point a[] = new Point[10];
            
        a[0] = new Point(4, 5.5);
        a[1] = new Point(1.2, 12.3);
        a[2] = new Point(7.9, 9.8);
        a[3] = new Point(4.3, 5.5);
        a[4] = new Point(-9.9, 8.2);
        a[5] = new Point(1.2, 3);
        a[6] = new Point(9, 2.4);
        a[7] = new Point(2, 9.2);
        a[8] = new Point(1, 5.6);
        a[9] = new Point(2, 7);
        
        System.out.println("Najmalo rastojanie: "+bf.najmalo_rastojanie(a, 10));
        System.out.println("Broj na nacini: "+bf.broj_na_nacini());
        System.out.println("Minimalen broj na moneti: " + bf.min_broj_moneti(79));
    }
    
}
