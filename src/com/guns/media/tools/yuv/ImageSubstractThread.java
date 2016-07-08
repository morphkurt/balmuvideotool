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

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImageSubstractThread implements Runnable {

    private byte[] s1;
    private byte[] s2;
    private int frame;
    private int w;
    private int h;
    RandomAccessFile bos;

    public ImageSubstractThread(byte[] s1, byte[] s2, int frame, int w, int h, RandomAccessFile bos) {
        this.s1 = s1;
        this.s2 = s2;
        this.frame = frame;
        this.w = w;
        this.h = h;
        this.bos = bos;
    }

    @Override
    public void run() {
        try {
            double total = 0;

            byte[] out = new byte[s1.length];
            for (int k = 0; k < out.length; k++) {

                int pixelValue_2 = s1[k] & 0xFF;

                int pixelValue_1 = s2[k] & 0xFF;

                out[k] = (byte) Math.abs(pixelValue_1 - pixelValue_2);
                
                //  total = total+square(frame2[i][j] - frame1[i][j]);
            }
            bos.seek(w*h*frame);
            bos.write(out);

            // System.out.println(total);
        } catch (IOException ex) {
            Logger.getLogger(ImageSubstractThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
