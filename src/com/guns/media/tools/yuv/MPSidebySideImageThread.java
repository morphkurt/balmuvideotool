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
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MPSidebySideImageThread implements Runnable {

    private final byte[] s1;
    private final byte[] s2;
    private final int frame;
    private final int w;
    private final int h;


    private final RandomAccessFile raf;

    public MPSidebySideImageThread(byte[] s1, byte[] s2, int frame, int w, int h, RandomAccessFile raf) {
        this.s1 = s1;
        this.s2 = s2;
        this.frame = frame;
        this.w = w;
        this.h = h;
        this.raf = raf;
   

    }

    @Override
    public void run() {

        try {
            double total = 0;
            int wh = w * h;
            byte[] out = new byte[s1.length];
            for (int k = 0; k < wh; k++) {
                
                int pixelValue_1 = s2[k] & 0xFF;
                
                out[k] = (byte) pixelValue_1;
                
                if ((k % w) > (w / 2)) {
                    //   out[k] = (byte) (s1[(k / w) * w + (k % (w / 2))] & 0xFF);
                    out[k] = (byte) (s1[LookUp.SSY[k]] & 0xFF);
                    //out[k] = (byte) (s1[LookUp.getK(k)] & 0xFF);
                    
                }
                
                //  total = total+square(frame2[i][j] - frame1[i][j]);
            }
            int c_w = w / 2;
            int c_h = h / 2;
            
            for (int k = 0; k < (wh / 4); k++) {
                
                int pixelValue_1 = s2[k + wh] & 0xFF;
                
                out[k + wh] = (byte) pixelValue_1;
                
                if (((k + wh) % c_w) > (c_w / 2)) {
                    out[k + wh] = (byte) (s1[LookUp.SSU[k]] & 0xFF);
                    //    out[k] = (byte) (s1[(k / c_w) * c_w + (k % (c_w / 2))] & 0xFF);
                }
                
                //  total = total+square(frame2[i][j] - frame1[i][j]);
            }
            
            for (int k = 0; k < (wh / 4); k++) {
                
                int pixelValue_1 = s2[k + wh + wh / 4] & 0xFF;
                
                out[k + (wh) + wh / 4] = (byte) pixelValue_1;
                
                if (((k + (wh) + wh / 4) % c_w) > (c_w / 2)) {
                    out[k + (wh) + wh / 4] = (byte) (s1[LookUp.SSV[k]] & 0xFF);
                    //    out[k] = (byte) (s1[(k / c_w) * c_w + (k % (c_w / 2))] & 0xFF);
                }
                
                //  total = total+square(frame2[i][j] - frame1[i][j]);
            }
       
          
            MappedByteBuffer bos = raf.getChannel().map(FileChannel.MapMode.READ_WRITE, (wh + wh / 2) *  frame, (wh + wh / 2) *  (frame+1));
            
            // System.
          
            
            bos.put(out);
            
            bos.force();
            
            // System.out.println(total);
        } catch (IOException ex) {
            Logger.getLogger(MPSidebySideImageThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int getX(int k) {
        return k % w;

    }

}
