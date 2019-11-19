public class Kadane2D {
    int left;
    int right;
    int top;
    int bottom;
    int sum;

    Kadane2D () {
        left = 0;
        right = 0;
        top = 0;
        bottom = 0;
    }

    public void print () {
        System.out.println("[" + top +".." +
                bottom + ","+ left+
                ".."+ right + "]");
        System.out.println("max_sum=" + sum );

    }

    public static Kadane2D maxSum2D ( int [][] arr ) {
        Kadane2D out = new Kadane2D();

        int rows = arr.length;
        int columns = arr[ 0 ].length;
        int currMax = 0;

        int [] runningRowSum = new int[ rows ];
        info temp;

        for ( int L = 0; L < columns ; L++ ) {
            zeruj( runningRowSum );
            for ( int R = L; R < columns ; R++ ) {

                for (int i = 0; i < rows ; i++) {
                    runningRowSum [i] += arr [i] [R];
                }
                temp = classicKadane( runningRowSum );

                if ( temp.sum > currMax ) {
                    out.left = L;
                    out.right = R;
                    out.top = temp.kadaneTop;
                    out.bottom = temp.kadaneBottom;
                    out.sum = temp.sum;
                    currMax = temp.sum;
                }
            }
        }
        return out;
    }

    public static void zeruj ( int [] a ) {
        for (int i = 0; i < a.length ; i++) {
            a[ i ] = 0;
        }
    }

    public static info classicKadane ( int[] a ) {
        int beg = 0;
        int end = 0;
        int bestBeg = 0;

        int currentBest = 0;
        int max = 0;

        for (int i = 0; i < a.length ; i++) {
            currentBest += a [ i ];
            if ( currentBest < 0 ) {
                currentBest = 0;
                bestBeg = i + 1;
            }
            else  {
                if ( currentBest > max ) {
                    max = currentBest;
                    beg = bestBeg;
                    end = i;
                }
            }
        }
        return new info( beg, end, max );
    }


}

class info {
    int kadaneTop;
    int kadaneBottom;
    int sum;

    info ( int t, int b, int s ) {
        kadaneTop = t;
        kadaneBottom = b;
        sum = s;
    }

}
