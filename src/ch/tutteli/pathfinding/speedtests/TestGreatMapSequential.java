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

/**
 *
 * @author Robert Stoll <rstoll@tutteli.ch>
 */
public class TestGreatMapSequential
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        SpeedTestHelper helper = new SpeedTestHelper();
        //somehow the first two runs need longer
        helper.run();
        helper.run();
      
        for (int i = 1; i <= 100; ++i) {
            long startTime = System.nanoTime();
            for (int j = 0; j < i; ++j) {
                 helper.run();
            }
            long timeUsed = System.nanoTime() - startTime;
            System.out.println(i + "\t" + timeUsed);
           
        }

    }
}
