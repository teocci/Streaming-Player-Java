package com.github.teocci.codesample.av.streaming;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2018-Jul-16
 */
public class ForkJoinTest
{
    private static double[] d;

    private static class ForkJoinTask extends RecursiveTask<Integer>
    {
        private int first;
        private int last;

        public ForkJoinTask(int first, int last)
        {
            this.first = first;
            this.last = last;
        }

        protected Integer compute()
        {
            int subCount;
            if (last - first < 10) {
                subCount = 0;
                for (int i = first; i <= last; i++) {
                    if (d[i] < 0.5)
                        subCount++;
                }
            } else {
                int mid = (first + last) >>> 1;
                ForkJoinTask left = new ForkJoinTask(first, mid);
                left.fork();
                ForkJoinTask right = new ForkJoinTask(mid + 1, last);
                right.fork();
                subCount = left.join();
                subCount += right.join();
            }
            return subCount;
        }
    }

    public static void main(String[] args)
    {
        d = createArrayOfRandomDoubles();
        int n = new ForkJoinPool().invoke(new ForkJoinTask(0, 9999999));
        System.out.println("Found " + n + " values");
    }

    private static double[] createArrayOfRandomDoubles()
    {
        double[] anArray = new double[10000000];

        double min = 0.0;  //  Set To Your Desired Min Value
        double max = 10.0; //    Set To Your Desired Max Value

        for (int i = 0; i < anArray.length; i++) {
            anArray[i] = ThreadLocalRandom.current().nextDouble(min, max);
        }
        return anArray;
    }
}
