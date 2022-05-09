//ALEX LEMA - ASSIGNMENT #8
//DATE: 4/25/2022
//A brief description of this code: in this assignment is about random password and brute force 
//program will generates random passwords and then tries to guess them
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class generatePass {
    public <string, Stringt> double geneRanPass(Stringt stringSet) {
        String randomCase;
        randomCase= (String) stringSet;
        String randomPassword = "";
        int guesses = 0;
        // recording start time for each case
        long start = System.currentTimeMillis();
        // password not found yet
        boolean found = false;
        // generates a random 4 characters
        for (int i = 0; i < 4; i++) {
            randomPassword += randomCase.charAt((int) (Math.random() * ((String) stringSet).length())); }
        double seconds =0;
        try {
            File file = new File("OutputFileAssignment8.txt");
            FileWriter myWriter = new FileWriter(file, true);
            BufferedWriter br = new BufferedWriter(myWriter);
            br.write("\nGenerated password is: " + randomPassword);
            br.write("\nGenerated password in hex is: " + toHex(randomPassword));
            System.out.println("\nGenerated password is: " + randomPassword);
            System.out.println("Generated password in hex is: " + toHex(randomPassword));
            //The program will first generate a password with four random capital letters.
            //It will then try to guess the password using bruit force,
            //that is generating every possible combination of four capital letters
            //and checking to see if it is the correct one.  When the password is correctly guessed,
            // the program is to output, to a file, the password, the number of guesses made before success,
            // and the time the program took to guess the password.
            for (int a = 0; a < ((String) stringSet).length() && !found; a++) {
                for (int b = 0; b < randomCase.length() && !found; b++) {
                    for (int c = 0; c < randomCase.length() && !found; c++) {
                        for (int d = 0; d < randomCase.length() && !found; d++) {
                            // combining characters at indices a,b,c and d to create
                            // a guess
                            String pass = "" + randomCase.charAt(a) + randomCase.charAt(b) + randomCase.charAt(c) + randomCase.charAt(d);
                            // checking if guess is true
                            if (randomPassword.equals(pass)) {
                                // found. loop will not execute next time
                                found = true;
                            }
                            guesses++;
                        }
                    }
                }
            }
            ///-------------truck time-------//
            long end = System.currentTimeMillis();
            seconds = (end - start) / 1000.0;
            //-------------display guesses and time--------------//
            br.write("\nFound the password after " + guesses + " guesses.");
            System.out.println("Found the password after " + guesses + " guesses.");
            System.out.printf("It took %.6f seconds to find the password.\n", seconds);
            br.write("\nIt took "+String.format("%.6f", seconds)+" seconds to find the password.\n");
            br.close();
        }
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }//end catch
        return seconds;
    }

    private String toHex(String randomPassword) {
        // creating an empty string
        String resultHex = "";
        // looping through each char in str
        for (int i = 0; i < randomPassword.length(); i++) {
            // converting ASCII value of current char to hex and appending to result
            String hex = Integer.toHexString(randomPassword.charAt(i));
            resultHex += hex;
        }
        return resultHex;
    }
}

//////////////*****MAIN Assigment8*****/////////////////////////////////
public class Assignment8 {

    public static void main(String[] args) {
        //----Function To Erase Data In File Each Time It Is Excecute---//
        try {
            FileWriter myWriter = new FileWriter("OutputFileAssignment8.txt");
            myWriter.close();
        }//end try
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }//end catch

        //----Function To Write Data In File Each Time It Is Excecute---//
        try {
            File file = new File("OutputFileAssignment8.txt");
            FileWriter myWriter = new FileWriter(file, true);
            BufferedWriter brm = new BufferedWriter(myWriter);
            brm.write("Alex Lema\n" + "ICS 462 Assignment #8\nApril 25, 2022\n");
            System.out.println("Alex Lema\n" + "ICS 462 Assignment #8\nApril 25, 2022");
            brm.close();
        }//end try
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }//end catch

        //----String for case 1----//
        String charSet1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        //----String for case 2----//
        String charSet2 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789:;><=?@{}\\^_`";

        generatePass GP = new generatePass();//call generatePass CLASS

        //----call generatePass CLASS for case 1----//
        double startTotal = GP.geneRanPass(charSet1);
        //----call generatePass CLASS for case 2----//
        double endTotal =  GP.geneRanPass(charSet2);

        double seconds2 = (endTotal / startTotal) ;
        System.out.printf("\nIt took %.2f times as long with the expanded character set to guess the password.\n", seconds2);

        try {
            File file = new File("OutputFileAssignment8.txt");
            FileWriter myWriter = new FileWriter(file, true);
            BufferedWriter br = new BufferedWriter(myWriter);
            br.write("\nIt took "+String.format("%.2f", seconds2)+" times as long with the expanded character set to guess the password.\n");
            br.close();
        }//end try
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }//end catch
    }
}



