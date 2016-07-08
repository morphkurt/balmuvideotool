# Welcome to the Balamu wiki!

"Balamu" is [Sinhala](https://en.wikipedia.org/wiki/Sinhalese_language) slang, with a meaning of watching something. Balamu Video Tool is video comparison tool written purely in Java which can be ported to any JVM supported hardware.

# Features

## Ability compare the PSNR of frames in YUV format.   
   
Example usage

~~~bash
java -jar blamuvideotool.jar -if reference_image1.yuv -if2 reference_image2.yuv -psnr -w 1920 -h 1080 -f number_of_frames

#sample output

Frame Rate :100
frame: 0 PSNR: 19.8874122167576
frame: 1 PSNR: 19.205333886936934
frame: 2 PSNR: 19.806076227030488
frame: 3 PSNR: 19.8556327492563
frame: 4 PSNR: 19.80019089642385
frame: 5 PSNR: 19.750525891169858
frame: 6 PSNR: 19.664501676580237
frame: 7 PSNR: 19.702549780543357
frame: 8 PSNR: 19.70894806516521
frame: 9 PSNR: 19.664567746258722
frame: 10 PSNR: 19.683623044180436
frame: 11 PSNR: 19.642064789768646
frame: 12 PSNR: 19.52758549355057
~~~


## Ability side by side comparison of two YUV file.

This will create a split yuv frame of the two different YUV files. This will help to visually comapre the two different images. 

Example command

~~~bash
java -jar blamuvideotool.jar -if reference_image1.yuv -if2 reference_image2.yuv -ss -w 1920 -h 1080 -f number_of_frames -o new_yuv_file.yuv
~~~

## Ability to extract certain frame of the YUV file

This help to extract certain frame from the YUV file.

Example command

~~~bash
java -jar blamuvideotool.jar -if reference_image1.yuv -if2 reference_image2.yuv -image -w 1920 -h 1080 -f number_of_frames -o file_name
~~~

## Ability to offset the frames

Ability to offset certain frames from the yuv file


Example command

~~~bash
java -jar blamuvideotool.jar -if reference_image1.yuv -if2 reference_image2.yuv -psnr -w 1920 -h 1080 -f number_of_frames -offset 100
~~~
