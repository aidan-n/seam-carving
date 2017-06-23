# Seam-Carving 
   
##                Before                                            After
![Screenshot](docs/images/tower.jpg)                            ![Screenshot](docs/images/small.jpg)

  
  

---

This project uses the algs4 library from Princeton University, which can be downloaded as a .jar here:
http://algs4.cs.princeton.edu/code/

All other imported libraries are part of the Java Development Kit (JDK).

---

### To run this program:

Clone the repository onto your computer:
`$ git clone https://github.com/aidan-n/seam-carving.git`

Set up the algs4 library, from the link mentioned earlier in the readme. On that page, there are instructions on how to set up the library.
For linux users, note that `~` is the same as `/home/[User-Name]/`. If you are unsure of your shell version, you can find out by doing `$ echo $SHELL`.

Once done with that, you will be able to compile the two java files in your local repo.
`$ javac -d . SeamCarver.java`
`$ javac -d . Client.java`

Have the image you want to seamcarve in the project directory. In other words, have the image you want to resize in the same folder you have Client.java and SeamCarver.java in.

Run the program:
`$ java sc.Client`

Closing the image ends the program.





