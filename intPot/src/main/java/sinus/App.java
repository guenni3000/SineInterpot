package sinus;

import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;

public class App 
{
    public static int period = 4, count = 10; 
    public static double c = 2 * Math.PI / period;

    public static double[] approx(double[] points, int scale, double[] min) {

        double x = min[1], y = min[2];

        for (double xVar = x; xVar < x + Math.pow(10, -(scale - 1)); xVar += Math.pow(10, -scale)) {
            for (double yVar = y; yVar < y + Math.pow(10, -(scale - 1)); yVar += Math.pow(10, -scale)) {
                
                double left = (points[2] - points[0]) / (points[1] - points[0]);
                double right = (Math.sin(xVar * 3 + yVar) - Math.sin(xVar * 1 + yVar)) / (Math.sin(xVar * 2 + yVar) - Math.sin(xVar * 1 + yVar));

                double val = Math.abs(left - right);

                if ( val < min[0] ) {
                    min[0] = val;
                    min[1] = xVar;
                    min[2] = yVar;
                }
            }
        }

        return min;
    }

    public static int getPrime(int index) {
    
        int i = 0;
        int x = 0;

        while (i < index) {
            x++;

            boolean isPrime = true;
            for (int u = 2; u <= Math.sqrt(x); u++) {
                if (x % u == 0){
                    isPrime = false;
                    break;
                }
            }

            if (isPrime) {
                i++;
            }
        }

        return x;
    } 

    public static double[] getPoints(int n)
    {
        int exp = 4;
        int base = ((int)Math.pow(10, exp) % n);

        double[] points = new double[100];

        for (int i = 0; i < 100; i++) 
        {
            int res = (int)Math.pow(base, i+1) % n;
            points[i] = res;
        }

        return points;
    }

    public static double getA( int n )
    {
        double[] points = getPoints(n);
        double[] min = {Double.POSITIVE_INFINITY, 0, 0};

        for (int i = 0; i < 10; i++) {
            min = approx(points, i, min);
            min[0] = Double.POSITIVE_INFINITY;
        }

        c = min[1];

        double b = (points[1] - points[0]) / (Math.sin(c * 2 + min[2]) - Math.sin(c * 1 + min[2]));
        double a = points[0] - b * Math.sin(c * 1 + min[2]);

        System.out.println(n+",  "+a+" + "+b+" * sin( "+c+" * x + "+min[2]+" )");
        return b;
    }

    public static Double[][] sort(ArrayList<Double[]> comp) {
        Double[][] out = new Double[comp.size()][2];

        for (int i = 0; i < out.length; i++) {
            Double[] min = {0.0, Double.POSITIVE_INFINITY};
            for (int u = 0; u < comp.size(); u++) {
                if (comp.get(u)[1] < min[1]) {
                    min = comp.get(u);
                }
            }
            comp.remove(min);
            System.out.println(min[0]+", "+min[1]);
            out[i] = min;
        }
        return out;
    }

    public static void main(String[] args) {
        
        ArrayList<Double[]> list = new ArrayList<>(); 
        ArrayList<Integer> pass = new ArrayList<>();

        for (int x = 2; x <= count; x++) {
            for (int y = 2; y <= count; y++) {
                if (!pass.contains(x*y)) {
                    pass.add(x*y);
                    Double[] temp = {getPrime(x)*getPrime(y)*1.0, getA(getPrime(x)*getPrime(y))};
                    list.add(temp);
                }  
            }
        }
        Double[][] arr = sort(list);

        try {
            BufferedWriter stream = new BufferedWriter(new FileWriter(new File("C:/Users/Julian/Desktop/out.txt")));

            for (int i = 0; i < arr.length; i++) {
                stream.write(arr[i][0]+", "+arr[i][1]+"\n");
            }
            stream.close();
        } catch (Exception e) {
            System.out.println("Couldn't open file!");
        }
        
    }
}
