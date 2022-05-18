import java.io.*;
import java.util.*;

public class Task3 {

    public static void sortByColumn(Double[][] coords, int column) {
        Arrays.sort(coords, new Comparator<Double[]>() {

            @Override
            public int compare(final Double[] a, final Double[] b) {
                if (a[column] != null) {
                    if (b[column] != null) {
                        if (a[column] > b[column]) {
                            return(1);
                        } else {
                            return(-1);
                        }
                    }
                }
                return(0);
            }
        });
    }

    public static Double distance(Double x1, Double y1, Double x2, Double y2) {
        if ((x1 == null) || (y1 == null) || (x2 == null) || (y2 == null)) { 
            return(Double.POSITIVE_INFINITY);
        }
        return(Math.sqrt(((x1 -x2)*(x1 - x2)) + ((y1 - y2)*(y1 - y2))));
    }

    public static String round(Double num, int sigfigs) {
        String numString = Double.toString(num);
        if (numString.length() <= sigfigs) {
            return(numString);
        }
        String resultString = "";
        String exponential = "";
        Boolean hasExponential = false;
        Boolean notZeroYet = false;
        int figureCounter = 0;
        int count = 0;
        for (int i = 0; i < numString.length(); i++) {
            if ((numString.charAt(i) != '0') && (numString.charAt(i) != '.')) {
                notZeroYet = true;
            }
            if ((numString.charAt(i) != '.') && notZeroYet) {
                figureCounter = figureCounter + 1;
            }
            resultString = resultString + String.valueOf(numString.charAt(i));
            if (figureCounter > sigfigs-2) {
                break;
            }
            count = i;
        }
        for (int j = 0; j < numString.length(); j++) {
            if ((numString.charAt(j) == 'E') || (numString.charAt(j) == 'e')) {
                hasExponential = true;
            }
            if (hasExponential) {
                exponential = exponential + String.valueOf(numString.charAt(j)).toLowerCase();
            }
        }
        int lastDigit = Integer.parseInt(String.valueOf(numString.charAt(count+2)));
        if (numString.charAt(sigfigs+1) != 'E') {
            int determinant = Integer.parseInt(String.valueOf(numString.charAt(count+3)));
            if (determinant >= 5) {
                lastDigit = lastDigit + 1;
            }
        }
        resultString = resultString + String.valueOf(lastDigit);
        resultString = resultString + exponential; 
        return(resultString);
    }

    public static Double BaseCase(Double[][] coords) {
        Double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < coords.length; i++) {
            for (int j = i+1; j < coords.length; j++) {
                Double dist = distance(coords[i][0], coords[i][1], coords[j][0], coords[j][1]);
                if (dist < min) {
                    min = dist;
                }  
            }
        }
        return(min);
    }

    public static Double crossStripClosestPair(Double[][] left, Double[][] right, Double d) {

        Double min = d;

        for (int i = 0; i < left.length; i++) {
            for (int j = 0; j < right.length; j++) {
                if (left[i][1] == null) {
                    continue;
                }
                if (right[j][1] == null) {
                    continue;
                }
                if (Math.abs(left[i][1] - right[j][1]) < d) {
                    Double dist = distance(left[i][0], left[i][1], right[j][0], right[j][1]);
                    if (dist < min) {
                        min = dist;
                    }

                }
            }
        }

        return(min);

    }

    public static Double ClosestPair(Double[][] coords, Double[][] coordsSortedByY) {

        int size = coords.length;

        if (size < 4) {
            return(BaseCase(coords));
        }

        // calculates the median x value using the median index since the coordinates are already sorted by x
        Double medianX = coords[(size)/2][0];
        //System.out.println("Median = " + medianX);
 
        // instantiates arrays to store the points in both parts of the split cartesian plane
        Double[][] more = new Double[(size+1)/2][2];
        Double[][] less = new Double[(size+1)/2][2];

        // index counters to correctly fill up arrays
        int moreIndex = 0;
        int lessIndex = 0;

        // populates the arrays
        for (int j = 0; j < coordsSortedByY.length; j++) {
            if (coordsSortedByY[j][0] != null) {
                if (coordsSortedByY[j][0] >= medianX) {
                    more[moreIndex] = coordsSortedByY[j].clone();
                    //System.out.println("more[" + moreIndex + "] = " + more[moreIndex][0] + " " + more[moreIndex][1]);
                    moreIndex = moreIndex + 1;
                } else {
                    less[lessIndex] = coordsSortedByY[j].clone();
                    //System.out.println("less[" + lessIndex + "] = " + less[lessIndex][0] + " " + less[lessIndex][1]);
                    lessIndex = lessIndex + 1;
                }
            }
        }

        Double[][] moreSortedByX = new Double[(size+1)/2][2];
        Double[][] lessSortedByX = new Double[(size+1)/2][2];

        // index counters to correctly fill up arrays
        int moreSortedByXIndex = 0;
        int lessSortedByXIndex = 0;

        // populates the arrays
        for (int l = 0; l < coords.length; l++) {
            if (coords[l][0] != null) {
                if (coords[l][0] >= medianX) {
                    moreSortedByX[moreSortedByXIndex] = coords[l].clone();
                    //System.out.println("more[" + moreIndex + "] = " + more[moreIndex][0] + " " + more[moreIndex][1]);
                    moreSortedByXIndex = moreSortedByXIndex + 1;
                } else {
                    lessSortedByX[lessSortedByXIndex] = coords[l].clone();
                    //System.out.println("less[" + lessIndex + "] = " + less[lessIndex][0] + " " + less[lessIndex][1]);
                    lessSortedByXIndex = lessSortedByXIndex + 1;
                }
            }
        }

        // recursive call on both parts of the plane
        Double dl = ClosestPair(lessSortedByX, less);
        Double dr = ClosestPair(moreSortedByX, more); 

        // stores the min of dl and dr
        Double d = Double.POSITIVE_INFINITY;

        // sets d as the min of dl and dr
        if (dl > dr) {
            d = dr;
        } else {
            d = dl;
        }

        //System.out.println("d: " + d);

        Double[][] leftStrip = new Double[(size+1)/2][2];
        Double[][] rightStrip = new Double[(size+1)/2][2];
        int leftStripPointer = 0;
        int rightStripPointer = 0;

        // will look for points within d of the centre line on the left
        for (int k = 0; k < coordsSortedByY.length; k++) {
            if (coordsSortedByY[k][0] != null) {
                if (Math.abs(coordsSortedByY[k][0] - medianX) < d) {
                    if (coordsSortedByY[k][0] >= medianX) {
                        rightStrip[rightStripPointer] = coordsSortedByY[k].clone();
                        //System.out.println("Right: " + rightStrip[rightStripPointer][0] + " " + rightStrip[rightStripPointer][1]);
                        rightStripPointer = rightStripPointer + 1;
                    } else {
                        leftStrip[leftStripPointer] = coordsSortedByY[k].clone();
                        //System.out.println("Left: " + leftStrip[leftStripPointer][0] + " " + leftStrip[leftStripPointer][1]);
                        leftStripPointer = leftStripPointer + 1;
                    }
                }
            }
        }

        //System.out.println(Integer.toString(leftStripPointer) + " " + Integer.toString(rightStripPointer));

        return(crossStripClosestPair(leftStrip, rightStrip, d));

    }

    public static Double[][] generateInput(int n) {
        Double[] tempCoords = new Double[2];
        Double[][] output = new Double[n][2];
        for (int i = 0; i < n; i++) {
            tempCoords[0] = Math.random() * 1000;
            tempCoords[1] = Math.random() * 1000;
            output[i] = tempCoords.clone();
        }
        return(output);
    }

    public static void main(String args[]) throws IOException {

        // REMOVE AFTER TESTING!
        System.setIn(System.in);
        System.setOut(new PrintStream(System.out));

        // used to read from standard input
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        // used to write to standard input
        Writer writer = new OutputStreamWriter(System.out);

        // the first line of the input which represents how many lines will follow (number of points)
        String numberOfPointsString = reader.readLine();
        // initialise the number of points variable
        int numberOfPoints = 0;

        // input format error handling and number of points variable set
        if (numberOfPointsString == null) {
            System.out.println("Incorrectly formatted input\n");
            return;
        } else {
            try {
                numberOfPoints = Integer.parseInt(numberOfPointsString);
                if (numberOfPoints < 2) {
                    System.out.println("Incorrectly formatted input\n");
                    return;
                }
            } finally {
                //System.out.println("Success");
            }
        }

        // stores each line from the standard input as a string
        Double[][] coordinates = new Double[numberOfPoints][2];

        // reads each line of the input and populates the coordinateStrings array
        for (int i = 0; i < numberOfPoints; i++) {
            String inputLine = reader.readLine();
            // checks for empty input file
            if (inputLine == null) {
                System.out.println("Incorrectly formatted input\n");
                return;
            }
            // stores each coordinate in an array of size two in format {"x", "y"} where x and y are strings
            String[] coordsString = inputLine.split(" ");
            if (coordsString.length != 2) {
                System.out.println("Incorrectly formatted input\n");
            }
            // initiates array that will store each coordinate in an array of size two in format {x, y} where x and y are doubles
            Double[] coords = new Double[2];
            // populates the coords array
            for (int j = 0; j < coords.length; j++) {
                coords[j] = Double.parseDouble(coordsString[j]);
            }
            // adds point to array of coordinates
            coordinates[i] = coords.clone();
            //System.out.println(coords[0] + " " + coords[1]);
            //System.out.println("Point " + i + " is: " + coordinateStrings[i]);
        }

        Double[][] coordsSortedByY = coordinates.clone();
        sortByColumn(coordinates, 0);
        sortByColumn(coordsSortedByY, 1);
        Double output = ClosestPair(coordinates, coordsSortedByY);
        System.out.println(round(output, 9));

        /*
        String runtimes = "[";
        for (int k = 1; k < 8000; k++) {
            Double[][] testCoords = generateInput(k);
            Double[][] testCoordsSortedByY = testCoords.clone();
            sortByColumn(testCoords, 0);
            sortByColumn(testCoordsSortedByY, 1);
            long AverageRunTime = 0;
            for (int l = 0; l < 5; l++) {
                long StartTime = System.nanoTime();
                ClosestPair(testCoords, testCoordsSortedByY);
                long EndTime = System.nanoTime();
                long RunTime = EndTime - StartTime;
                AverageRunTime = AverageRunTime + RunTime;
            }
            AverageRunTime = AverageRunTime / 5;
            runtimes = runtimes + String.valueOf(AverageRunTime);
            if (k < 7999) {
                runtimes = runtimes + ", ";
            }
        }
        runtimes = runtimes + "]";
        System.out.println(runtimes);
        */

    }

}