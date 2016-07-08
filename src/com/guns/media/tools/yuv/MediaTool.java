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

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import static java.lang.System.exit;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 *
 * @author damitha
 */
public class MediaTool {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        try {
            Options options = getOptions();
            CommandLine cmd = null;
            int offset = 0;
            CommandLineParser parser = new GnuParser();
            cmd = parser.parse(options, args);
            int frame = new Integer(cmd.getOptionValue("f"));

            if (cmd.hasOption("help")) {
                printHelp(options);
                exit(1);
            }

            if (cmd.hasOption("offset")) {
                offset = new Integer(cmd.getOptionValue("offset"));
            }
            

            //  int scale = new Integer(args[2]);
            BufferedInputStream if1 = new BufferedInputStream(new FileInputStream(cmd.getOptionValue("if1")));
            BufferedInputStream if2 = new BufferedInputStream(new FileInputStream(cmd.getOptionValue("if2")));

            DataStorage.create(new Integer(cmd.getOptionValue("f")));

            int width = new Integer(cmd.getOptionValue("w"));
            int height = new Integer(cmd.getOptionValue("h"));
            LookUp.initSSYUV(width, height);
            //  int[][] frame1 = new int[width][height];
            //  int[][] frame2 = new int[width][height];
            int nRead;
            int fRead;
            byte[] data = new byte[width * height + ((width * height) / 2)];
            byte[] data1 = new byte[width * height + ((width * height) / 2)];
            int frames = 0;
            long start_ms = System.currentTimeMillis() / 1000L;
            long end_ms = start_ms;

            if (offset > 0) {
                if1.skip(((width * height + ((width * height) / 2)) * offset));
            } else if(offset < 0){
                if2.skip(((width * height + ((width * height) / 2)) * (-1*offset)));
            }

            if (cmd.hasOption("psnr")) {
                ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
                while ((nRead = if1.read(data)) != -1 && ((fRead = if2.read(data1)) != -1) && frames < frame) {
                    byte[] data_out = data.clone();
                    byte[] data1_out = data1.clone();

                    PSNRCalculatorThread wt = new PSNRCalculatorThread(data_out, data1_out, frames, width, height);
                    executor.execute(wt);
                    frames++;

                }
                executor.shutdown();
                end_ms = System.currentTimeMillis() / 1000L;

                System.out.println("Frame Rate :" + frames / ((end_ms - start_ms)));
                for (int i = 0; i < frames; i++) {
                    System.out.println("frame: " + i + " PSNR: " + 10 * Math.log10((255 * 255) / (DataStorage.getFrame(i) / (width * height))));

                }
            }
            if (cmd.hasOption("sub")) {

                RandomAccessFile raf = new RandomAccessFile(cmd.getOptionValue("o"), "rw");

                ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
                while ((nRead = if1.read(data)) != -1 && ((fRead = if2.read(data1)) != -1)) {
                    byte[] data_out = data.clone();
                    byte[] data1_out = data1.clone();

                    ImageSubstractThread wt = new ImageSubstractThread(data_out, data1_out, frames, width, height, raf);
                    //wt.run();
                    executor.execute(wt);

                    frames++;

                }
                executor.shutdown();
                end_ms = System.currentTimeMillis() / 1000L;
                System.out.println("Frame Rate :" + frames / ((end_ms - start_ms)));

                raf.close();
            }
            if (cmd.hasOption("ss") && !cmd.getOptionValue("o").matches("-")) {
                
                

                RandomAccessFile raf = new RandomAccessFile(cmd.getOptionValue("o"), "rw");
                
               

               // RandomAccessFile ra =  new RandomAccessFile(cmd.getOptionValue("o"), "rw");

               // MappedByteBuffer raf = new RandomAccessFile(cmd.getOptionValue("o"), "rw").getChannel().map(FileChannel.MapMode.READ_WRITE, 0, ((width*height)+(width*height/2))*frame);
                   ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
                while ((nRead = if1.read(data)) != -1 && ((fRead = if2.read(data1)) != -1) && frames < frame) {
                    byte[] data_out = data.clone();
                    byte[] data1_out = data1.clone();

                    SidebySideImageThread wt = new SidebySideImageThread(data_out, data1_out, frames, width, height, raf);
                    // MPSidebySideImageThread wt = new MPSidebySideImageThread(data_out, data1_out, frames, width, height, ra);
                    frames++;
                   // wt.run();

                    executor.execute(wt);
                }
                executor.shutdown();
                end_ms = System.currentTimeMillis() / 1000L;
               
                while(!executor.isTerminated()){
                    
                }
               
                raf.close();
            }
            if (cmd.hasOption("ss") && cmd.getOptionValue("o").matches("-")) {
                
                

              
                
                PrintStream stdout = new PrintStream(System.out);

               // RandomAccessFile ra =  new RandomAccessFile(cmd.getOptionValue("o"), "rw");

               // MappedByteBuffer raf = new RandomAccessFile(cmd.getOptionValue("o"), "rw").getChannel().map(FileChannel.MapMode.READ_WRITE, 0, ((width*height)+(width*height/2))*frame);
                  // ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
                while ((nRead = if1.read(data)) != -1 && ((fRead = if2.read(data1)) != -1) && frames < frame) {
                    byte[] data_out = data.clone();
                    byte[] data1_out = data1.clone();

                    SidebySideImageThread wt = new SidebySideImageThread(data_out, data1_out, frames, width, height, stdout);
                    // MPSidebySideImageThread wt = new MPSidebySideImageThread(data_out, data1_out, frames, width, height, ra);
                    frames++;
                   // wt.run();

                    wt.run();
                }
              
                end_ms = System.currentTimeMillis() / 1000L;
                System.out.println("Frame Rate :" + frames / ((end_ms - start_ms)));
               
               
                stdout.close();
            }
            if (cmd.hasOption("image")) {

                System.setProperty("java.awt.headless", "true");
                //ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
                ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
                while ((nRead = if1.read(data)) != -1 && ((fRead = if2.read(data1)) != -1)) {

                    if (frames == frame) {
                        byte[] data_out = data.clone();
                        byte[] data1_out = data1.clone();

                        Frame f1 = new Frame(data_out, width, height, Frame.YUV420);
                        Frame f2 = new Frame(data1_out, width, height, Frame.YUV420);
                        //       System.out.println(cmd.getOptionValue("o"));
                        ExtractImageThread wt = new ExtractImageThread(f1, frames, cmd.getOptionValue("if1") + "frame1-" + cmd.getOptionValue("o"));
                        ExtractImageThread wt1 = new ExtractImageThread(f2, frames, cmd.getOptionValue("if2") + "frame2-" + cmd.getOptionValue("o"));
                        //   executor.execute(wt);
                        executor.execute(wt);
                        executor.execute(wt1);
                    }
                    frames++;

                }
                executor.shutdown();
                //  executor.shutdown();
                end_ms = System.currentTimeMillis() / 1000L;
                System.out.println("Frame Rate :" + frames / ((end_ms - start_ms)));
            }

        } catch (ParseException ex) {
            Logger.getLogger(MediaTool.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static Options getOptions() {
        Options options = new Options();
        options.addOption("help", false, "print help");
        options.addOption("psnr", false, "option");
        options.addOption("sub", false, "option");
        options.addOption("image", false, "option");
        options.addOption("ss", false, "option");
        options.addOption("if1", true, "input filename1");
        options.addOption("if2", true, "input filename1");
        options.addOption("w", true, "width");
        options.addOption("h", true, "height");
        options.addOption("o", true, "Output File Name");
        options.addOption("pfmt", true, "Pixel Format");
        options.addOption("f", true, "Number of Frames");
        options.addOption("offset", true, "Offset Frame (This is the offset for the second input file");
     //   options.addOption("reverse", true, "Offset Frame (This is the offset for the second input file");
        //    options.addOption("overide", true, "overide channel specific configurationx");
        return options;
    }

    private static void printHelp(Options op) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("HeadendProvisioningTool", op);

    }
}
