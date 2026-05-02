public class lab1 {
    public static void main(String[] args){
        int[] l = new int[6];
        
        for (int i = 0; i < l.length; i++) {
        l[i] = 17 - 2 * i;
        }  

        double[] x = new double[13];

        for (int i = 0; i < x.length; i++) {
        x[i] = -10.0 + Math.random() * 17.0;
        }
        
        double[][] s = new double[6][13];
        for (int i = 0; i < l.length; i++) {
            for (int j = 0; j < x.length; j++) {
                s[i][j] = calc(l[i], x[j]); }
            }
        matrprint(s);
    }

    public static double for1(double xj) {
        return Math.pow((2.0*(0.25-Math.sin(Math.asin((xj-1.5)/17.0)))),3);
    }
    public static double for2(double xj) {
            return Math.tan(Math.log(Math.acos((xj-1.5)/17.0)));
        }
    public static double for3(double xj) {
        return Math.atan(Math.sin(Math.cos(M        return Math.atan(Math.sin(Math.cos(M        return Math.atan(Math.sin(Math.cos(M        return {    
        if (li == 9) {
            return for1(xj);
        } else if (li == 7 || li == 11 || li == 17) {   
            return for2(xj) ;
        } else {
            return for3(xj);
        }
    }
        
      
    public static void matrprint(double[][] matrix) {
            for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.printf("%8.5f ", matrix[i][j]);
        }
        System.out.println();
        }
    }
}
