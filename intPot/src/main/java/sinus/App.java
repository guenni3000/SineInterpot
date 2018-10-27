package sinus;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;

public class App 
{
    public static JFrame frame;
    public static int period = 4, count = 10, index = 1; 
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

    public static double[] getPoints(int n, int exp)
    {
        BigInteger base = BigInteger.valueOf(10).pow(exp);

        double[] points = new double[200];

        for (int i = 0; i < 200; i++) 
        {
            int res = base.modPow(BigInteger.valueOf(i+1), BigInteger.valueOf(n)).intValue();
            points[i] = res;
        }

        return points;
    }

    public static double getA( int n )
    {
        double[] points = getPoints(n, 4);
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

    public static BufferedImage plotPoints(double[] points) {

        BufferedImage img = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
        g.setColor(Color.BLACK);

        double fact = 1;

        g.drawLine(0, img.getHeight() - 20 - (int)fact, img.getWidth(), img.getHeight() - 20 - (int)fact);

        for (int i = 0; i < points.length; i++) {
            System.out.println(i+", "+points[i]);
            g.fillRect(30 + i * 30, img.getHeight() - 20 - (int)(points[i] *fact), 10, 10);
        }

        return img;
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

    public static void checkPrimes() {
        
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

    public static String dft(double[] points) {

        double[] guessIm = new double[points.length];
        double[] guessRe = new double[points.length];

        for (int i = 0; i < points.length; i++) {
            
            double sumIm = 0;
            double sumRe = 0;

            for (int u = 0; u < points.length; u++) {
                sumIm -= Math.sin(i * u * 2 * Math.PI / points.length) * points[u];
                sumRe += Math.cos(i * u * 2 * Math.PI / points.length) * points[u];
            }

            guessIm[i] = sumIm;
            guessRe[i] = sumRe;
            
        }

        String out = "";
        out += guessRe[0] / points.length;

        for (int i = 1; i < points.length; i++) {
             out += " + " + guessRe[i] / points.length + " * cos(" + i + " * 2 * pi / " + points.length + " * x) - " + guessIm[i] / points.length + " * sin(" + i + " * 2 * pi / " + points.length + " * x)";
        }

        return out;
    }

    public static void showMod(final int p, final int q) {  
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(1600, 600);

        frame.addKeyListener(new KeyListener(){
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == ' ') {
                    frame.getGraphics().drawImage(plotPoints(getPoints(p * q, index)), 0, 0, null);
                } else if(e.getKeyChar() == 'a') { 
                    index--;
                    System.out.println(index);
                } else if(e.getKeyChar() == 'd') {
                    index++;
                    System.out.println(index);
                }
            }

            public void keyPressed(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
            }
        });
    }

    public static double[] getPeriod(double[] vals) {
        int per = 0;
        double start = 1;

        for (int i = 0; i < vals.length; i++) {
            per++;
            if (start == vals[i]) break;
        }

        double[] out = new double[per];
        out[0] = 1;

        for (int i = 0; i < per-1; i++) {
            out[i+1] = vals[i];
        }

        return out;
    }

    public static void  main(String[] args) {
        double[] points = new double[10];

        for (int i = 0; i < points.length; i++) {
            points[i] = 1 + Math.sin(i + 2);
        }

        double[] in = {1, 4, 16};

        System.out.println(dft(getPeriod(getPoints(77, 4))));
    }
}
