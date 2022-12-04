package graphs;

public final class GraphUtility {

    private GraphUtility(){

    }

    public static final double DEFAULT_EPS = 1E-6;

    public static boolean isCloseEnough(double a, double b, double eps){
        double diff = a-b;
        return Math.abs(diff) < eps;
    }

    public static boolean isCloseEnough(double a, double b){
        return isCloseEnough(a,b, DEFAULT_EPS);
    }



}
