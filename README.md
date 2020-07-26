# JavaSetganography

## Summary
JavaSteganography is a tool for hiding messages inside PNG images and reading those messages. As the name implies, the tool is meant for use only with PNG files. The program works by first extracting the RGB pixel values of the image; then processing (flipping or reading) the least significant bit of each pixel in the PNG (by order of color channel); and finally, if writing, saving the modified image. 

## Compilation
Although not included, the program is in the form of a jar file made from the contents of the src directory using InteliJ. This, however, can be acheived by following the instructions in [Baeldung, sections 1 and 3.2](https://www.baeldung.com/java-create-jar), with JavaMessageHider as the main class.

## Instruciton Templates
When executing the program, users must provide at least 2 (and at most 4) arguments. The first of these indicates whether we are writing a message or reading one. Arguments ```-w``` and ```-write``` are used for writing messages and ```-r``` and ```-read``` are used for reading messages from PNG image files. The second argument is the path to the image in which to perform the write or read operation. 

If we are performing a read operation, there can be at most 3 arguments instead of 4; this third argument must specify a text file in which to store the message extracted from argument 2 (otherwise, the message is simply displayed on the screen without being saved). If we are performing a write, then argument 3 specifies either a destination for our new message-laced image (if we don't want to override the existing one) or a text file containing the message to write. If there are 4 arguments, the third must be the image path and the fourth must be the text file described in the previous sentence.

Here are some helpful example use cases; we will assume that the "deliverable" was created as ```steg.jar```:
- ```java -jar steg.jar -w img.png```: Write a user-specified message into the image img.png, overiding img.png
- ```java -jar steg.jar -write img.png img2.png```: Write a user-specified message into the image img.png, saving the result in img2.png
- ```java -jar steg.jar -w img.png mssg.txt```: Write the text in mssg.txt into the image img.png
- ```java -jar steg.jar -w img.png img2.png mssg.txt```: Write the text in mssg.txt into the image img.png

- ```java -jar steg.jar -r img.png```: Read message hidden in img.png, printing it onto screen
- ```java -jar steg.jar -read img.png secret.txt```: Read message hidden in img.png, printing it onto screen and storing result in secret.txt

## Important Notes
From what I can tell, this tool does not make a "complete copy" of an image when hiding a message. Although the resulting picture looks almost exactly like the original, tests have shown that some metadata present in the original may be abscent in the copy.
