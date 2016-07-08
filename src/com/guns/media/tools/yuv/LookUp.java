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
public class LookUp {

    public static int[] SSY;
    public static int[] SSU;
    public static int[] SSV;
    
    
    
    public static void initSSYUV(int w, int h) {
        SSY = new int[w * h];
        for (int i = 0; i < SSY.length; i++) {
            int z = SSY[i];

            SSY[i] = (i / w) * w + (i % (w / 2));

        }
        SSU = new int[((w * h) / 4)];
        int c_w = w / 2;
        int c_h = h / 2;
        for (int i = 0; i < SSU.length; i++) {

            SSU[i] = ((i + (w * h)) / c_w) * c_w + ((i + (w * h)) % (c_w / 2));
        }

        SSV = new int[((w * h) / 4)];

        for (int i = 0; i < SSV.length; i++) {
  
            SSV[i] = ((i + (w * h)+(w * h)/4) / c_w) * c_w + ((i + (w * h)+(w * h)/4) % (c_w / 2));
        }

        //  total = total+square(frame2[i][j] - frame1[i][j]);
    }
}
