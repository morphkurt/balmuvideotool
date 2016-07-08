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
public class Frame {

    private byte[] yuvFrame;
    private int w;

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }
    private int h;
    public final static int YUV420 = 0;
    public final static int YUV422 = 1;
    public final static int YUV444 = 2;
    private int type;

    public Frame(byte[] yuvFrame, int w, int h, int type) {
        this.yuvFrame = yuvFrame;
        this.w = w;
        this.h = h;
        this.type = type;
    }

    public int Y(int x, int y) {
        return yuvFrame[x + (y * w)] & 0xFF;
    }

    public int U(int x, int y) {
        if (type == YUV420) {
            return yuvFrame[(w * h) + (x / 2) + ((y / 2) * w / 2)] & 0xFF;
        } else if (type == YUV422) {
            return 0;
        } else if (type == YUV444) {
            return yuvFrame[(w * h) + x + (y * w)] & 0xFF;
        } else {
            return 255;
        }
    }

    public int V(int x, int y) {
        if (type == YUV420) {
             return yuvFrame[(w * h) + (w * h) / 4 + (x / 2) + ((y / 2) * w / 2)] & 0xFF;
           // return yuvFrame[((w * h) + (w * h) / 4 + (x / 4) + ((y / 4) * w ))] & 0xFF;
        } else if (type == YUV422) {
            return 0;
        } else if (type == YUV444) {
            return yuvFrame[(w * h) * 2 + x + (y * w)] & 0xFF;
        } else {
            return 255;
        }
    }

    public int R(int x, int y) {
        return clamp((int) ((Y(x, y) - 16) * 1.164 + 1 * (0 * (U(x, y) - 128) + 1.793 * (V(x, y) - 128))));

    }

    public int G(int x, int y) {
        //- 0.395U - 0.581V
        return clamp((int) ((Y(x, y) - 16) * 1.164 - 1 * (0.213 * (U(x, y) - 128) - 0.533 * (V(x, y) - 128))));
    }

    public int B(int x, int y) {
        //Y + 2.032U
        return clamp((int) ((Y(x, y) - 16) * 1.164 + 1 * (2.112 * (U(x, y) - 128) + 0 * (V(x, y) - 128))));
    }

    private int clamp(int value) {
        return Math.max(0, Math.min(value, 255));
    }

}
