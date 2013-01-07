/*
 * Copyright 2012 Robert Stoll <rstoll@tutteli.ch>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package ch.tutteli.pathfinding.speedtests;

import ch.tutteli.pathfinding.DStar;
import java.util.concurrent.CountDownLatch;

/**
 *
 * @author Robert Stoll <rstoll@tutteli.ch>
 */
public class TestGreatMapParallel
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        SpeedTestHelper helper = new SpeedTestHelper();
        //somehow the first two runs need longer
        helper.run();
        helper.run();
      
        for (int i = 1; i <= 100; ++i) {
            CountDownLatch startSignal = new CountDownLatch(1);
            CountDownLatch doneSignal = new CountDownLatch(i);
            for (int j = 0; j < i; ++j) {
                new Thread(new Tester(startSignal, doneSignal, helper)).start();
            }
            Thread.sleep(50);
            long startTime = System.nanoTime();
            startSignal.countDown();
            doneSignal.await();
            long timeUsed = System.nanoTime() - startTime;
            System.out.println(i + "\t" + timeUsed);

        }
    }

    private static class Tester implements Runnable
    {

        CountDownLatch doneSignal;
        CountDownLatch startSignal;
        SpeedTestHelper helper;

        Tester(CountDownLatch theStartSignal, CountDownLatch theDoneSignal, SpeedTestHelper aHelper) {
            startSignal = theStartSignal;
            doneSignal = theDoneSignal;
            helper = aHelper;
        }

        @Override
        public void run() {
            try {
                startSignal.await();
                helper.run();
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            } finally {
                doneSignal.countDown();
            }

        }
    }
}
