/*
 * Copyright 2016 Damitha Gunawardena damitha.gunawardena@gmail.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.guns.media.tools.yuv;

/**
 *
 * @author damitha
 */
public class DataStorage {
    private static double[] hm ;
    
    public static void create(int frames){
        hm = new double[frames];
    }
    
    public static void setFrame(double total,int frame){
        hm[frame] = total;
    }
    
    public static double getFrame(int frame){
        return hm[frame];
    }
    
   
    
    
}
