// Aleksandra Chesiak - 3
import java.util.Scanner;

public class Source {
    private static Scanner in = new Scanner( System.in );

    public static void main(String[] args) {
        int numOfSets = 0;
        int n; // wiersze
        int m; // kolumny

        numOfSets = in.nextInt();
        int licznik = 0;

        while ( licznik < numOfSets ) {
            n = in.nextInt();
            m = in.nextInt();

            int[][] tab = new int [ n ][ m ];
            for ( int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    if ( in.hasNext() )
                    tab [ i ][ j ] = in.nextInt();
                }
            }
            Sub2DTable result = maximalSum2D ( tab );
            // wypisujemy
            if ( result.empty ) {
                System.out.println("empty");
            }
            else{
                System.out.println("[" +result.upperBorder+".." +
                        result.bottomBorder+ ","+ result.letf+
                        ".."+result.right + "]");
                System.out.println("max_sum=" + result.sum );
            }

            System.out.println("My Kadane: ");
            Kadane2D out = Kadane2D.maxSum2D ( tab );
            out.print();

            licznik++;
        }
    }

    private static Sub2DTable maximalSum2D(int[][] A) {
        Sub2DTable maxSubtable = new Sub2DTable();
        maxSubtable.empty = true;
        int numOfElements = 0; // musi byc najmniejsza

        int rows = A.length;
        int columns = A[ 0 ].length;
        boolean first = true;

        int[] temporarySums = new int[rows];

        for (int L = 0; L < columns ; L++) {
            // od nowa szukamy
            for (int i = 0; i < rows ; i++) {
                temporarySums[ i ] = 0;
            }
            for (int R = L; R < columns ; R++) {
                for (int i = 0; i < rows ; i++) {
                    // updating temporary table
                    temporarySums[ i ] += A [ i ][ R ];
                }
                KadaneTable maxArray = maximalSubarray( temporarySums );
                if ( !maxArray.empty ) {
                    maxSubtable.empty = false;
                }
                if ( !maxSubtable.empty ) {
                    // nie jest pusta
                    numOfElements = ( R - L + 1) * ( maxArray.end - maxArray.beg + 1 );
                    if( first ) {
                        maxSubtable.sum = maxArray.sum;
                        maxSubtable.letf = L;
                        maxSubtable.right = R;
                        maxSubtable.upperBorder = maxArray.beg;
                        maxSubtable.bottomBorder = maxArray.end;

                        maxSubtable.howManyElements = numOfElements;
                        first = false;
                    }
                    if ( maxArray.sum > maxSubtable.sum ) {
                        maxSubtable.sum = maxArray.sum;

                        maxSubtable.letf = L;
                        maxSubtable.right = R;
                        maxSubtable.upperBorder = maxArray.beg;
                        maxSubtable.bottomBorder = maxArray.end;

                        maxSubtable.howManyElements = numOfElements;
                    }
                    else {
                        if ( maxArray.sum == maxSubtable.sum ) {
                            if ( maxSubtable.howManyElements > numOfElements ) {
                                maxSubtable.letf = L;
                                maxSubtable.right = R;
                                maxSubtable.upperBorder = maxArray.beg;
                                maxSubtable.bottomBorder = maxArray.end;

                                maxSubtable.howManyElements = numOfElements;
                            }
                            else {
                                if ( maxSubtable.upperBorder > maxArray.beg && maxSubtable.howManyElements == numOfElements ) {
                                    maxSubtable.letf = L;
                                    maxSubtable.right = R;
                                    maxSubtable.upperBorder = maxArray.beg;
                                    maxSubtable.bottomBorder = maxArray.end;

                                    maxSubtable.howManyElements = numOfElements;
                                }
                                if ( maxSubtable.upperBorder == maxArray.beg && maxSubtable.bottomBorder > maxArray.end && maxSubtable.howManyElements == numOfElements ) {
                                    maxSubtable.letf = L;
                                    maxSubtable.right = R;
                                    maxSubtable.upperBorder = maxArray.beg;
                                    maxSubtable.bottomBorder = maxArray.end;

                                    maxSubtable.howManyElements = numOfElements;
                                }
                            }
                        }
                    }
                }
            }
        }
        return maxSubtable;
    }

    private static KadaneTable maximalSubarray(int[] array ) {
        int resultBeg = -1;
        int resultEnd = -1;
        int tempMax = 0;
        int currBest = 0;
        int currStart = 0;

        boolean empty = true;
        boolean zero = false;
        int numOfIndex = -1;

        int currNumofElem = 0;
        int bestNumofElem = 0;

        for (int i = 0; i < array.length ; i++) {
            if ( array[ i ] > 0 ) {
                empty = false;
            }
            if ( array [ i ] == 0 ) {
                zero = true;
                if ( empty && numOfIndex == -1 ) {
                    numOfIndex = i;
                }
            }
        }

        if (!empty) {
            // wszystko
            for (int i = 0; i < array.length ; i++) {
                tempMax += array[ i ];

                if ( tempMax <= 0 ) {
                    tempMax = 0;
                    currStart = i + 1;
                }

                if ( tempMax > currBest) {
                    resultBeg = currStart;
                    resultEnd = i;
                    currBest = tempMax;
                    bestNumofElem = resultEnd - resultBeg + 1;
                }
                if ( tempMax == currBest ) {
                    currNumofElem = i - currStart + 1;

                    if ( currNumofElem < bestNumofElem ) {
                        resultBeg = currStart;
                        resultEnd = i;
                        currBest = tempMax;
                        bestNumofElem = resultEnd - resultBeg + 1;
                    }
                }
            }
            return  new KadaneTable(currBest, resultBeg, resultEnd, empty);
        }
        else {
            // pusta lub tylko zero
            if ( zero ) {
                return new KadaneTable( 0, numOfIndex, numOfIndex, false); // same zera i ujemne
            }
            else {
                return new KadaneTable( 0, 0, 0, true); // pusta
            }
        }
    }

    private static class KadaneTable {
        int sum;
        int beg;
        int end;

        boolean empty;

        KadaneTable(int s, int b, int e, boolean empty) {
            this.sum = s;
            this.beg = b;
            this.end = e;
            this.empty = empty;
        }
    }

    private static class Sub2DTable {
        int sum;
        int letf;
        int right;

        int upperBorder;
        int bottomBorder;

        boolean empty;
        int howManyElements;
    }
}
/*
test.in
14
6 3
1 6 -5
1 1 -10
-50 -50 -50
0 0 0
-5 0 8
-10 -10 1
3 3
-1 -2 0
9 -10 9
-1 1 1
2 3
0 0 4
0 0 -10
2 5
0 0 4 -10 4
0 -1 0 2 0
2 5
 1 1 -2 -1 0
 1 1 -2 -1 4
2 5
 10 1 -2 -1 -100
 1 -11 -2 -1 40
2 5
 0 -1 -1 4 0
 2 2 0 0 0
2 4
 -2 -3 -1 -2
 -1 -1 -1 -5
2 1
0
0
3 6
0 0 0 -2 1 1
0 1 1 -2 1 1
0 1 1 -2 0 0
4 8
1 -10 -3 5 -14 -2 13 -3
18 -2 -6 -8 2 -5 14 1
9 -2 9 -1 -1 10 -50 2
1 -13 15 -7 8 -2 2 -6
2 2
-1 0
0 -1
1 4
1 1 -2 2
1 1
5
*
* test.out
[4..5,2..2]
max_sum=9
[1..2,2..2]
max_sum=10
[0..0,2..2]
max_sum=4
[0..0,2..2]
max_sum=4
[1..1,4..4]
max_sum=4
[1..1,4..4]
max_sum=40
[0..1,0..3]
max_sum=6
empty
[0..0,0..0]
max_sum=0
[0..1,4..5]
max_sum=4
[2..3,2..5]
max_sum=31
[0..0,1..1]
max_sum=0
[0..0,3..3]
max_sum=2
[0..0,0..0]
max_sum=5
*
*
*/