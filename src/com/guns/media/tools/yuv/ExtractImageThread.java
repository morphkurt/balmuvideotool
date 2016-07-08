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

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class ExtractImageThread implements Runnable {

    private Frame f1;

    private String filename;
    private int frame;

    RandomAccessFile bos;

    public ExtractImageThread(Frame f1, int frame, String filename) {
        this.f1 = f1;

        this.frame = frame;
        this.filename=filename;

    }

    @Override
    public void run() {
        BufferedImage image = new BufferedImage(f1.getW(), f1.getH(), BufferedImage.TYPE_INT_RGB);
        
        
        for (int i = 0; i < f1.getW(); i++) {
            for (int j = 0; j < f1.getH(); j++) {
                            //int a = yourmatrix[i][j];
                //          System.out.println("Y:" + f.Y(i, j) + "U:" + f.U(i, j) + "V:" + f.V(i, j));
                //        System.out.println("R:" + f.R(i, j) + "G:" + f.G(i, j) + "B:" + f.B(i, j));
                Color newColor = new Color(f1.R(i, j), f1.G(i, j), f1.B(i, j));
                //      System.out.println(newColor.getRGB());
                image.setRGB(i, j, newColor.getRGB());

            }
        }
        File output = new File(String.format(filename, frame));
        try {
            ImageIO.write(image, "png", output);
        } catch (IOException ex) {
            Logger.getLogger(ExtractImageThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
