//ALEX LEMA - ICS 462 Operating Systems
//ASSIGNMENT #5
//DATE: 3/28/2022
//A brief description of this code: in this assignment is about Paging
//This assignment is divided in 4 classes: Main, FIFO, LRU, & Optimal
//page-reference string is to be 20 entries long
//page numbers range from 0 to 9
//page frames goes from 1 to 7
// the requirement is to compute the page fault for each of these frame numbers
//we are going to use the algorithms presented in chapter 8 of your text
//The purpose of this work was to define the algorithm that realizes the best performance of the system. A good page
//replacement algorithm can reduce the page faults, when the program is executing, reduce the number of I/O, and then
//increase the system’s efficiency effectively.
import java.io.FileWriter;
import java.io.IOException;
import java.io.*;

//////////////*****FIFO class******////////////////////////////////////////
//FIFO means First In, First Out,
//i.e., consider (in this case delete) elements strictly in arrival order
class FIFO {
    //Simple and easy to implement.
    //Low overhead.
    public int fifo(int pageFrameSize, int referencePageString, int[] referenceSring)
    {
        int i, frame[] = new int[pageFrameSize];
        for(i=0;i<pageFrameSize;i++){frame[i] = -1;}
        int index=0, loc, faults=0;

        for(i=0;i<referencePageString;i++)
        {
            if(isPresent(frame, pageFrameSize, referenceSring[i])!=-1){}
            else{
                loc = isEmpty(frame, pageFrameSize);
                if(loc!=-1)
                {
                    frame[loc] = referenceSring[i];
                    index = (index+1)%pageFrameSize;
                    faults++;
                }
                else
                {
                    frame[index] = referenceSring[i];
                    index = (index+1)%pageFrameSize;
                    faults++;
                }
            }

        }
        System.out.println("\n\n   FIFO had "+faults+" page faults");
        return faults;
    }

    // return index of empty cell
    public static int isEmpty(int []a, int n){

        int i;
        for(i=0;i<n;i++)
            if(a[i]==-1)
                return i;
        return -1;
    }

    //return index if present
    public static int isPresent(int []a, int n, int key)
    {
        for(int i=0;i<n;i++)
            if(a[i]==key)
                return i;
        return -1;
    }

}
//////////////*****LRU class******////////////////////////////////////////
//LRU is Least Recently Used, the cache element that hasn't been used the longest time is evicted
// (on the hunch that it won't be needed soon)
class LRU {
    public int lru(int pageFrameSize, int referencePageString, int[] referenceSring)
    {
        int i, j;
        int frame[] = new int[pageFrameSize];
        int distance[] = new int[pageFrameSize];
        for(i=0;i<pageFrameSize;i++)
            frame[i] = -1;
        for(i=0;i<pageFrameSize;i++)
            distance[i] = 0;

        int loc;
        int faults=0;

        for(i=0;i<referencePageString;i++)
        {
            loc = ispresent(frame, pageFrameSize, referenceSring[i]);
            if(loc!=-1)
            {
                // page present in frames
                for(j=0;j<pageFrameSize;j++)
                    distance[j]++;
                distance[loc]=0;
            }
            else{
                // page fault
                loc = isempty(frame, pageFrameSize);
                if(loc!=-1)
                {
                    // empty frame is available
                    frame[loc] = referenceSring[i];
                    faults++;
                    for(j=0;j<pageFrameSize;j++)
                        distance[j]++;
                    distance[loc]=0;
                }
                else
                {
                    loc = maxdis(distance, pageFrameSize);
                    frame[loc] = referenceSring[i];
                    faults++;
                    for(j=0;j<pageFrameSize;j++)
                        distance[j]++;
                    distance[loc]=0;
                }
            }

        }

        System.out.println("   LRU had "+faults+" page faults");
        return faults;

    }
   // keeps track of page usage over a short period of time with index

    // return index of empty cell(cell having value -1) else return -1
    public static int isempty(int []a, int n){

        int i;
        for(i=0;i<n;i++)
            if(a[i]==-1)
                return i;
        return -1;
    }

    // search the page number in array, return index if present or -1 otherwise
    public static int ispresent(int []a, int n, int key)
    {
        for(int i=0;i<n;i++)
            if(a[i]==key)
                return i;
        return -1;
    }

    // Return index of maximum value from input array
    public static int maxdis(int []a, int n)
    {
        int k=0;
        for(int i=1;i<n;i++)
        {
            if(a[k]<a[i])
                k=i;
        }
        return k;
    }

}


//////////////*****Optimal class******////////////////////////////////////////
//This idea consists in pages, which have been heavily
//used in the last few instructions, which will probably be used again in the next few. Pages that aren’t used for a long time
//may stay in this state again.
class Optimal {

    public int optimal(int pageFrameSize, int referencePageString, int[] referenceSring)
    {
        int i, j;
        int frame[] = new int[pageFrameSize];
        int distance[] = new int[pageFrameSize];
        for(i=0;i<pageFrameSize;i++)
            frame[i] = -1;
        for(i=0;i<pageFrameSize;i++)
            distance[i] = 0;

        int loc;
        int faults=0;

        for(i=0;i<referencePageString;i++)
        {
            loc = ispresent(frame, pageFrameSize, referenceSring[i]);
            if(loc!=-1)
            {
                // page present in frames
                for(j=0;j<pageFrameSize;j++)
                    distance[j] = occurence(referenceSring, referencePageString, i, frame[j]);
            }
            else{
                // page fault
                loc = isempty(frame, pageFrameSize);
                if(loc!=-1)
                {
                    // empty frame is available
                    frame[loc] = referenceSring[i];
                    faults++;
                    for(j=0;j<pageFrameSize;j++)
                        distance[j] = occurence(referenceSring, referencePageString, i, frame[j]);
                }
                else
                {
                    loc = maxdis(distance, pageFrameSize);
                    frame[loc] = referenceSring[i];
                    faults++;
                    for(j=0;j<pageFrameSize;j++)
                        distance[j] = occurence(referenceSring, referencePageString, i, frame[j]);
                }
            }

        }
        System.out.println("   Optimal had "+faults+" page faults");
        return faults;
    }

    public static int isempty(int []a, int n){

        int i;
        for(i=0;i<n;i++)
            if(a[i]==-1)
                return i;
        return -1;
    }

    // search the page number in array
    public static int ispresent(int []a, int n, int key)
    {
        for(int i=0;i<n;i++)
            if(a[i]==key)
                return i;
        return -1;
    }

    // Return index
    public static int maxdis(int []a, int n)
    {
        int k=0;
        for(int i=1;i<n;i++)
        {
            if(a[k]<a[i])
                k=i;
        }
        return k;
    }

    public static int occurence(int []a, int n, int start, int key)
    {
        for(int i=start+1;i<n;i++)
            if(a[i]==key)
                return i-start;
        return 10000;
    }

}

//////////////*****MAIN******////////////////////////////////////////
public class assignment5 {

    public static void main(String[] args) {
        try {
            File file = new File("OutputFileAssignment5.txt");
            FileWriter myWriter = new FileWriter(file, true);
            BufferedWriter br = new BufferedWriter(myWriter);
            br.write("\nAlex Lema\n" +
                    "ICS 462 Assignment #5\nMarch 28, 2022\n");
            System.out.println("\nAlex Lema\n" +
                    "ICS 462 Assignment #5\nMarch 28, 2022\n");
            br.close();
        }//end try
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }//end catch

        //pageFrameSize = 7
        //referencePageString = 20
        int i, k, z,x;//page frame size & reference page string size

        for (z = 1; z <= 3; z++) {//z is used to the 3 array that we need to display

            for (k = 1; k <= 7; k++) //for FIFO, LRU, Optimal
            {
                int[] referenceString = numbers(z);// it will return the array needed

                System.out.print("\nFor " + k + " page frames, and using string page reference string: ");
                for (i = 0; i < 19; i++) {
                    System.out.print(referenceString[i] + ",");
                }//print string
                System.out.print(referenceString[i]);
                try {
                    File file = new File("OutputFileAssignment5.txt");
                    FileWriter myWriter = new FileWriter(file, true);
                    BufferedWriter br = new BufferedWriter(myWriter);
                    br.write("\nFor " + k + " page frames, and using string page reference string: ");
                    for (x = 0; x < 19; x++)//WRITE string
                    {
                        br.write(referenceString[x] + ",");
                    }
                    br.write(referenceString[19]+"");

                    FIFO f = new FIFO();//call FIFO CLASS
                    br.write("\n\n   FIFO had " + f.fifo(k, 20, referenceString) + " page faults");

                    LRU l = new LRU();//call LRU CLASS
                    br.write("\n   LRU had " + l.lru(k, 20, referenceString) + " page faults");

                    Optimal o = new Optimal();//call Optimal CLASS
                    br.write("\n   Optimal had " + o.optimal(k, 20, referenceString) + " page faults\n");

                    br.close();
                }//end try
                catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }//end catch
            }//end for k
        }//end for z
    }//end main
    //this is used to run the 3 array automatically
    private static int[] numbers(int z) {
        int[] arrayHelper = new int[0];
        int[] refString1 = {4,1,8,7,3,6,6,2,7,6,7,8,8,1,1,7,4,8,8,8};
        int[] refString2 = {0,7,0,1,2,0,8,9,0,3,0,4,5,6,7,0,8,9,1,2};
        int[] refString3 = {7,0,1,2,0,3,0,4,2,3,0,3,2,1,2,0,1,7,0,1};
        if (z==1){arrayHelper= refString1; }
        if (z==2){arrayHelper= refString2; }
        if (z==3){arrayHelper= refString3; }
        return arrayHelper;
    }
}