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

/**
 *
 * @author damitha
 */
public class StorageIO {
    
    public static synchronized void write(RandomAccessFile raf,byte[] out, long seek){
        try {
            raf.seek(seek );
            
            
            raf.write(out);
        } catch (IOException ex) {
            Logger.getLogger(StorageIO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
