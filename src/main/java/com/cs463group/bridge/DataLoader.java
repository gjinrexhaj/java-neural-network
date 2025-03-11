package com.cs463group.bridge;

// TODO: Implement a dataLoader in bridge that loads data in a parametrized fashion

/**
 *  DataLoader.java
 *  Created on 3/11/2025
 *  Defines an adaptive and flexible data loader, which reads and formats data from
 *  a csv or plaintext file such that the backend can interact with it.
 */

public class DataLoader {

    // draft: MAKE BELOW CODE ADAPTIVE - use num of input datapoint and num of output neurons as parameters for
    // data size and handling
     /*
        Scanner scan;
        File file = new File("test-data/weight-height-gender/Source-Data.txt");
        try {
            scan = new Scanner(file);
            scan.useDelimiter("[,\\n]");
            int i = 0;

            while (scan.hasNext()) {
                i++;
                String token = scan.next();
                if (i == 1) {
                    // data add 1 and next
                    String nextToken = scan.next();
                    data.add(Arrays.asList(Double.parseDouble(token), Double.parseDouble(nextToken)));
                    System.out.print(i + ": ");
                    System.out.println(token + ", " + nextToken);
                } else if (i == 2) {
                    // do nothing
                    System.out.print(i + ": ");
                    answers.add(Double.parseDouble(token));
                    System.out.println(token);
                    i = 0;
                }
            }

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
         */
}
