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
package ch.tutteli.dstar;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Robert Stoll <rstoll@tutteli.ch>
 */
public class ActualWorld
{
    Map<String, Cost> actualEnterCosts = new HashMap<>();

    public Cost getActualEnterCost(int x, int y) {
        return actualEnterCosts.containsKey(x + "," + y) ? actualEnterCosts.get(x + "," + y) :  null;
    }

    public void setActualEnterCost(int x, int y, Cost enterCost) {
        actualEnterCosts.put(x + "," + y, enterCost);
    }
        
}
