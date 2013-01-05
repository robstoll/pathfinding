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
package ch.tutteli.pathfinding.utils;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author Robert Stoll <rstoll@tutteli.ch>
 */
public class ImageHelper
{
    public static void setPoint(BufferedImage image,int x,int y,int factor, Color color){
        for(int i=0;i<factor;++i){
            for(int j=0;j<factor;++j){
                image.setRGB(x*factor+i, y*factor+j, color.getRGB());
            }
        }
    }
}
