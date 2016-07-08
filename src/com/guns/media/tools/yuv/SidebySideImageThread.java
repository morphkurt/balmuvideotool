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
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SidebySideImageThread implements Runnable {

    private byte[] s1;
    private byte[] s2;
    private int frame;
    private int w;
    private int h;
    private boolean file = true;

    private RandomAccessFile bos;
    private PrintStream bw;

    public SidebySideImageThread(byte[] s1, byte[] s2, int frame, int w, int h, RandomAccessFile bos) {
        this.s1 = s1;
        this.s2 = s2;
        this.frame = frame;
        this.w = w;
        this.h = h;
        this.bos = bos;

    }

    public SidebySideImageThread(byte[] s1, byte[] s2, int frame, int w, int h, PrintStream bw) {
        this.s1 = s1;
        this.s2 = s2;
        this.frame = frame;
        this.w = w;
        this.h = h;
        this.bw = bw;
        this.file = false;
    }

    @Override
    public void run() {

        double total = 0;

        byte[] out = new byte[s1.length];
        for (int k = 0; k < w * h; k++) {

            int pixelValue_2 = s1[k] & 0xFF;

            int pixelValue_1 = s2[k] & 0xFF;

            out[k] = (byte) pixelValue_1;

            if ((k % w) > (w / 2)) {
                out[k] = (byte) (s1[(k / w) * w + (k % (w / 2))] & 0xFF);
            }

            //  total = total+square(frame2[i][j] - frame1[i][j]);
        }
        int c_w = w / 2;
        int c_h = h / 2;

        for (int k = w * h; k < (w * h) + (w * h / 4); k++) {

            int pixelValue_2 = s1[k] & 0xFF;

            int pixelValue_1 = s2[k] & 0xFF;

            out[k] = (byte) pixelValue_1;

            if ((k % c_w) > (c_w / 2)) {
                out[k] = (byte) (s1[(k / c_w) * c_w + (k % (c_w / 2))] & 0xFF);
            }

            //  total = total+square(frame2[i][j] - frame1[i][j]);
        }

        for (int k = (w * h) + (w * h / 4); k < (w * h) + (w * h / 2); k++) {

            int pixelValue_2 = s1[k] & 0xFF;

            int pixelValue_1 = s2[k] & 0xFF;

            out[k] = (byte) pixelValue_1;

            if ((k % c_w) > (c_w / 2)) {
                out[k] = (byte) (s1[(k / c_w) * c_w + (k % (c_w / 2))] & 0xFF);
            }

            //  total = total+square(frame2[i][j] - frame1[i][j]);
        }
        long seek = (long) (w * h + (w * h) / 2) * (long) frame;
        if (file) {
            StorageIO.write(bos, out, seek);
        } else {
            try {
                bw.write(out);
            } catch (IOException ex) {
                Logger.getLogger(SidebySideImageThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    // System.out.println(total);

    private int getX(int k) {
        return k % w;

    }

}
