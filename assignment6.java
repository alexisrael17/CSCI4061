//ALEX LEMA - ICS 462 Operating Systems
//ASSIGNMENT #6
//DATE: 4/11/2022
//A brief description of this code: in this assignment is about disk-scheduling
//This assignment is divided in 6 classes:
//a.	FCFS
//b.	SSTF
//c.	SCAN
//d.	C-SCAN
//e.	LOOK
//f.	C-LOOK


import java.io.*;
import java.io.FileWriter;// Import the FileWriter class
import java.io.IOException;// Import the IOException class to handle errors

////////-------------------CLASS FCFS------------------///////////////////
//the requests are addressed in the order they arrive in the disk queue
//The simplest form of a CPU scheduling algorithm
//Easy to program
//First come first served
//First Come First Serve (FCFS) is an operating system scheduling algorithm that automatically
// executes queued requests and processes in order of their arrival.
// It is the easiest and simplest CPU scheduling algorithm.
// In this type of algorithm, processes which requests the CPU first get the CPU allocation first/
class FCFS {
    public int fcfsScheduling(int position, int[] stringNumbers) {
        int totalMovements = 0;
        for (int i = 0; i < stringNumbers.length; i++)
        {
            if (position > stringNumbers[i]) {
                totalMovements += position - stringNumbers[i]; }
            else {totalMovements += stringNumbers[i] - position; }
            position = stringNumbers[i];
        }
        return totalMovements;
    }
}
////////-------------------CLASS SSTF------------------///////////////////
class Node {
    int distance = 0;
    boolean accessed = false;
}
//requests having shortest seek time are executed first.
//the seek time of every request is calculated in advance in the queue and
// then they are scheduled according to their calculated seek time
class SSTF {
    int sstfScheduling(int position, int[] stringNumbers)
    {
        int totalMovements = 0;
        Node differences[] = new Node[stringNumbers.length];
        for (int i = 0; i < differences.length; i++)
            differences[i] = new Node();

        for ( int i = 0; i < stringNumbers.length; i++) {
            calculateDifferenceSSTF(stringNumbers, position, differences);
            int index = findMinSSTF(differences);
            differences[index].accessed = true;
            totalMovements += differences[index].distance;// increase the total movement of head
            position = stringNumbers[index];// set new head position
        }
        return totalMovements;
    }

    private void calculateDifferenceSSTF(int queue[], int head, Node diff[])
    {
        for (int i = 0; i < diff.length; i++)
            diff[i].distance = Math.abs(queue[i] - head);
    }

    // find a track at minimum distance from head which is not accessed
    private int findMinSSTF(Node diff[])
    {
        int index = -1, minimum = Integer.MAX_VALUE;

        for (int i = 0; i < diff.length; i++) {
            if (!diff[i].accessed && minimum > diff[i].distance)
            {
                minimum = diff[i].distance;
                index = i;
            }
        }
        return index;
    }
}
////////-------------------CLASS SCAN------------------///////////////////
//The disk arm starts at one end of the disk,
// and moves toward the other end, servicing requests until it gets to the other end of the disk,
// where the head movement is reversed and servicing continues.
//Sometimes called the elevator algorithm.
class SCAN {
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    public int scanScheduling(int position, int[] stringNumbers) {
        int totalMovements = 0;
        int maxHead = 0;
        for (int i =0; i< stringNumbers.length; i++) {
            if ( stringNumbers[i] > maxHead) {
                maxHead = stringNumbers[i];
            }
        }
        if (maxHead > position) {
            totalMovements = position + maxHead;
        }
        else {
            totalMovements = position;
        }
        return totalMovements;
    }
}
////////-------------------CLASS C_SCAN------------------///////////////////
//the disk arm again scans the path that has been scanned,
// after reversing its direction. So, it may be possible that too many requests are
// waiting at the other end or there may be zero or few requests pending at the scanned area

class C_SCAN{

    public int c_scanScheduling(int n_cylinders, int position, int[] stringNumbers) {
        int totalMovements = 0;
        int maxHeadBeforPosition = 0;

        for (int i =0 ; i < stringNumbers.length; i++) {
            if ( stringNumbers[i] < position && stringNumbers[i] > maxHeadBeforPosition) {
                maxHeadBeforPosition = stringNumbers[i];
            }
        }
        totalMovements = n_cylinders-position + n_cylinders + maxHeadBeforPosition;
        return totalMovements;
    }


}
////////-------------------CLASS LOOK------------------///////////////////
//similar to the SCAN disk scheduling algorithm except for the difference that
//the disk arm in spite of going to the end of the disk goes only to the last request
// to be serviced in front of the head and then reverses its direction from there only
class LOOK {
    public int lookScheduling(int n_cylinders, int position, int[] stringNumbers) {
        int totalMovements = 0;
        int maxHead = 0;
        int minHead = n_cylinders;
        for (int i =0; i< stringNumbers.length; i++) {
            if ( stringNumbers[i] < minHead) {
                minHead = stringNumbers[i];
            }
            if (stringNumbers[i] > maxHead) {
                maxHead = stringNumbers[i];
            }
        }
        if (minHead < position && position < maxHead) {
            totalMovements = (position - minHead) + (maxHead - minHead);
        }
        else if (minHead > position) {
            totalMovements = maxHead - position;
        } else if (maxHead < position) {
            totalMovements = position - minHead;
        }
        return totalMovements;
    }

}
////////-------------------CLASS C_LOOK------------------///////////////////
//he disk arm in spite of going to the end goes only to the last request
// to be serviced in front of the head and then from there goes to the other end’s last request
//it also prevents the extra delay which occurred due to unnecessary traversal to the end of the disk
class C_LOOK {
    public int c_lookScheduling(int n_cylinders, int position, int[] stringNumbers) {
        int totalMovements = 0;
        int maxHeadBeforPosition = 0;
        int minHead = n_cylinders;
        int maxHead = 0;

        for (int i =0 ; i < stringNumbers.length; i++) {
            if ( stringNumbers[i] < position && stringNumbers[i] > maxHeadBeforPosition) {
                maxHeadBeforPosition = stringNumbers[i];
            }
            if (stringNumbers[i] < minHead) {
                minHead = stringNumbers[i];
            }
            if (stringNumbers[i] > maxHead) {
                maxHead = stringNumbers[i];
            }
        }
        totalMovements = maxHead-position + maxHead - minHead + maxHeadBeforPosition-minHead;
        return totalMovements;
    }
}


////////-------------------CLASS MAIN------------------///////////////////

public class assignment6 {

    public static void main(String[] args) throws IOException {
        try {
            File file = new File("OutputFileAssignment6.txt");
            FileWriter myWriter = new FileWriter(file, true);
            BufferedWriter brm = new BufferedWriter(myWriter);
            brm.write("\nAlex Lema\n" +
                    "ICS 462 Assignment #6\nApril 11, 2022\n");
            System.out.println("\nAlex Lema\n" +
                    "ICS 462 Assignment #6\nApril 11, 2022\n");
            brm.close();
        }//end try
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }//end catch

        int n_cylinders; //number of cylinders for your disk
        int position;//the cylinder number the disk position at the beginning of the simulation
        int []stringNumbers; //string of numbers representing the numbers of cylinders with I/O requests and service

        //disk-scheduling algorithms
        int aFCFS=0;
        int bSSTF=0;
        int cSCAN=0;
        int dC_SCAN=0;
        int eLOOK=0;
        int fC_LOOK=0;

        int setNumber=2; //number of sets of scheduling data
        String line;

        BufferedReader br = new BufferedReader(new FileReader("Asg6 Data.txt"));
        for (int j =0; j<setNumber; j++) { //loop for the number of set on the file Asg6 Data.txt
            //comand to read number of cylinders for your disk
            line = br.readLine();
            n_cylinders = Integer.parseInt(line.trim());
            //comand to read the cylinder number the disk position at the beginning of the simulation
            line = br.readLine();
            position = Integer.parseInt(line.trim());
            //comand to read the string of numbers representing the numbers of cylinders with I/O requests and service
            line = br.readLine();
            String []chunks = line.split(" ");
            stringNumbers = new int[chunks.length];

            for( int i = 0; i<chunks.length; i++) {
                stringNumbers[i] = Integer.parseInt(chunks[i].trim());
            }
            /////////-----calling 6 function----////////
            FCFS fcfs      =   new FCFS();
            aFCFS          =   fcfs.fcfsScheduling( position, stringNumbers);

            SSTF sstf      =   new SSTF();
            bSSTF          =   sstf.sstfScheduling( position, stringNumbers);

            SCAN scan      =   new SCAN();
            cSCAN          =   scan.scanScheduling( position, stringNumbers);

            C_SCAN c_scan  =   new C_SCAN();
            dC_SCAN        =   c_scan.c_scanScheduling(n_cylinders, position, stringNumbers);

            LOOK look      =   new LOOK();
            eLOOK          =   look.lookScheduling(n_cylinders, position, stringNumbers);

            C_LOOK c_look  =   new C_LOOK();
            fC_LOOK        =   c_look.c_lookScheduling(n_cylinders, position, stringNumbers);

            /////////----write in OutputFileAssignment6 file-----//////////////////
            try {
                File file = new File("OutputFileAssignment6.txt");
                FileWriter myWriter = new FileWriter(file, true);
                BufferedWriter brn = new BufferedWriter(myWriter);
                //white in file
                brn.write("\nFor FCFS, the total head movement was " + aFCFS   +" cylinders.");
                brn.write("\nFor SSTF, the total head movement was " + bSSTF   +" cylinders.");
                brn.write("\nFor SCAN, the total head movement was " + cSCAN   +" cylinders.");
                brn.write("\nFor CSCAN, the total head movement was " + dC_SCAN +" cylinders.");
                brn.write("\nFor LOOK, the total head movement was " + eLOOK   +" cylinders.");
                brn.write("\nFor CLOOK, the total head movement was " + fC_LOOK +" cylinders.\n");
                brn.close();

            }//end try
            catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }//end catch
            //display purposes
            System.out.println("For FCFS, the total head movement was " + aFCFS   +" cylinders.");
            System.out.println("For SSTF, the total head movement was " + bSSTF   +" cylinders.");
            System.out.println("For SCAN, the total head movement was " + cSCAN   +" cylinders.");
            System.out.println("For CSCAN, the total head movement was " + dC_SCAN +" cylinders.");
            System.out.println("For LOOK, the total head movement was " + eLOOK   +" cylinders.");
            System.out.println("For CLOOK, the total head movement was " + fC_LOOK +" cylinders.\n\n");

        }
    }//end main
}//end assignment it took me 20 plus to develope the code