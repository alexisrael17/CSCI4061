//ALEX LEMA - ICS 462 Operating Systems
//ASSIGNMENT #2
//DATE: 1/31/2022

//A brief description of this code =
//White producer thread that consist of a loop that writes the number of loops to a value from 0 to 4 (0<=i<5).
//This code contains a variable that it shares with the consuming thread that is initialized as int value=100.
//At each pass through the loop, before the producer writes to the shared variable, it does a random one to three second wait. 
//The loop must execute five times.
//The consumer process consists of a loop that reads the variable it shares with the producer five times and calculates the sum of the values it has read. 
//At each pass through the loop before reading the shared variable, it does a random one to three second wait.
//When the loop completes, the program should write the sum to a file such as OutputFileAssignment2.

import java.io.*;
public class Assignment2 {
    static int value = 100;
    //shared variable for the producer and  consumer as the assignment required
    public static void main(String[] args) {

//threat(The producer process/thread)
        Thread producer = new Thread(new Runnable() {
            public void run() {
//a loop that writes the loop count (a value from 0 to 4)
                for (int i = 0; i < 5; i++) {
// it does a random wait of from one to three seconds
// (compute a new random wait value on each pass through the loop).
                    try {//random wait of from one to three seconds
                        Thread.sleep((long) (Math.random() * 2001) + 1000);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    value = i; //assigning a value from the loop
                } //end of for loop
            }//end of public void run()
        }
        );//end of Thread producer

// creating the consumer threat
// The consumer process consists of a loop that reads the variable
// it shares with the producer five times and computes the sum of the values it has read.
        Thread consumer = new Thread(new Runnable() {
            int sum = 0;
            public void run() {
                for (int i = 0; i < 5; i++) {
                    try {//it does a random wait of from one to three seconds
                        Thread.sleep((long) (Math.random() * 2001) + 1000);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    sum += value;//add value shared to the sum
                }//end for loop

//to display or debug the output
                System.out.println("Alex Lema\n" +
                        "ICS 462 Assignment #2");
                System.out.println("Sum is : " + sum);

// storing the sum to OutputFileAssignment2.txt
// When the loop finishes, the program is to write the sum into a file.

                try {
                    FileWriter myWriter = new FileWriter("OutputFileAssignment2.txt");
                    myWriter.write("Alex Lema\n" +
                            "ICS 462 Assignment #2\n");
                    myWriter.write("Sum is :"+sum);
                    myWriter.close();
                    System.out.println("Successfully wrote to the file.");
                }//end try
                catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }//end catch
            }
        });//end of Thread consumer

        producer.start(); //staring producer thread
        consumer.start(); //starting consumer thread
    }//end of public static void main
}//end of the Assignment 2