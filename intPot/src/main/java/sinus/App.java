package sinus;

import java.awt.Point;

public class App 
{
    public static int period = 4; 
    public static double c = 2 * Math.PI / period;

    public static double[] approx(double[] points, int scale, double x, double y) {

        double[] min = {Double.POSITIVE_INFINITY, -1, -1};

        for (double xVar = x; xVar < x + Math.pow(10, -(scale - 1)); xVar += Math.pow(10, -scale)) {
            for (double yVar = y; xVar < y + Math.pow(10, -(scale - 1)); yVar += Math.pow(10, -scale)) {
                
                double left = (points[2] - points[0]) / (points[1] - points[0]);
                double right = (Math.sin(xVar * c * 3 + yVar) - Math.sin(xVar * c * 1 + yVar)) / (Math.sin(xVar * c * 2 + yVar) - Math.sin(xVar * c * 1 + yVar));

                double val = left - right;

                if ( val < min[0] ) {
                    min[0] = val;
                    min[1] = xVal;
                    min[2] = yVal;
                }
            }
        }

        return min;
    }

    public static void main( String[] args )
    {
        double[] points = getPoints();
        double[] min = {0, 0, 0};

        for (int i = 0; i < 10; i++) {
            min = approx(points, i, min[1], min[2]);
        }

        double b = (points[1] - points[0]) / (Math.sin(min[1] * c * 2 + min[2]) - Math.sin(min[1] * c * 1 + min[2]));
        double a = points[0] - b * Math.sin(min[1] * c * 1 + min[2]);

        System.out.println(a+" + "+b+" * ( "+min[1]*c+" * x + "+min[2]+" )");
    }
}
