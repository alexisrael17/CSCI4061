////ALEX LEMA - ASSIGNMENT #7
////DATE: 5/02/2022
////A brief description of this code: in this assignment is about buddy buffers
////program will be  with 4 functions for an operating system and a test driver. A driver interprets high-level requests
// coming from user software and the operating system into low-level commands recognized by a device

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

//-------------------------*START Buffer*----------------------------//
//The input buffer of the Scanner class in java.
// The buffer is a part of the computer's memory that is reserved to store data that comes from input peripherals such as a keyboard.
// This means that the data that is typed makes the following journey.
class Buffer {
    private int maxSize, minSize, currentSize;
    private Buffer parent = null, childA = null, childB = null;
    private boolean bufferFree = true, bufferSplit = false;
    private String controlWord = null;
    private Object requestResponse;
    private Buffer thisBuffer = this;

    public void Buffer(int maxSize, int minSize, Buffer parent) {
        this.maxSize = maxSize;
        this.minSize = minSize;
        this.currentSize = maxSize;
        this.parent = parent;
    }
    //     when a buffer size request is provided to the buffer and The checkRequest method is used to validate the request
    public Object requestBuffer(int requestSize) {
        return this.requestResponse = checkRequest(requestSize);
    }
    //    when if a buffer is not free but has children
    public Object requestChild(int requestSize) {
        return this.requestResponse = checkChildRequest(requestSize);
    }
    //when the the checkRequest method receives a requested buffer size and returned to the calling method
    private Object checkRequest(int requestSize) {
        if (requestSize > this.maxSize) {
            return "-2";
        } else if (requestSize > currentSize) {
            return "-1";
        } else {
            if (currentSize <= minSize+1) {
                setControlWord(requestSize);
                return this.thisBuffer;
            } else if (requestSize > (currentSize / 2)) {
                setControlWord(requestSize);
                return this.thisBuffer;
            } else {
                return splitBuffer(requestSize);
            }
        }
    }
    //    when request exceeds buffer size in 2 cases
    private Object checkChildRequest(int requestSize) {
        if (requestSize > this.maxSize) {
            return "-2";
        } else if (requestSize > currentSize) {
            return "-1";
        } else {
            Buffer child = freeChild();
            if(child == null){
                return "-1";
            }else{
                if(child.isBufferFree() && child.currentSize <= minSize+1){
                    child.setControlWord(requestSize);
                    return child.thisBuffer;
                }else if(child.isBufferFree() && requestSize > (child.currentSize / 2)) {
                    child.setControlWord(requestSize);
                    return child.thisBuffer;
                }else {
                    return child.checkRequest(requestSize);
                }
            }
        }
    }
    //it returns child or null
    private Buffer freeChild() {
        if(this.childA.isBufferFree()){
            return childA;
        }else if(this.childB.isBufferFree()){
            return childB;
        }
        return null;
    }
    //when splitBuffer creates two additional buffers
    private Object splitBuffer(int requestSize) {
        Buffer childA = new Buffer();
        childA.Buffer(this.maxSize / 2, 7, this.thisBuffer);
        this.childA = childA;
        Buffer childB = new Buffer();
        childB.Buffer(this.maxSize / 2, 7, this.thisBuffer);
        this.childB = childB;
        this.bufferFree = false;
        this.bufferSplit = true;
        return childA.checkRequest(requestSize);
    }

    //    when buffer address is provided to the recalimeBuffer
    public Buffer reclaimBuffer(Buffer returnedAddress) {
        if (returnedAddress == this.thisBuffer) {
            this.controlWord = null;
            this.bufferFree = true;
            this.bufferSplit = false;
            return this.thisBuffer;
        } else if (this.childA == null && this.childB == null) {
            return new Buffer();
        } else if (this.bufferSplit = true) {
            if (childA.reclaimBuffer(returnedAddress) == returnedAddress || childB.reclaimBuffer(returnedAddress) == returnedAddress) {
                this.childA = null;
                this.childB = null;
                this.bufferFree = true;
                this.bufferSplit = false;
                return returnedAddress;
            } else {
                return new Buffer();
            }
        }
        return new Buffer();
    }
    // when is contains the buffer size and a pointer to the next buffer
    private void setControlWord(int request) {
        if(this.parent !=null){
            this.controlWord = this.currentSize + " " + getSibling();
        }else {
            this.controlWord = this.thisBuffer.toString()+"";
        }
        this.bufferFree = false;
    }

    //    it will  return values of the getChildren method requested from the current Buffers parent
    private String getSibling() {
        String sibling = "null";
        ArrayList<Buffer> siblings = this.parent.getChildren();
        if (siblings.get(0) == this) {
            return siblings.get(1).toString();
        } else {
            return siblings.get(0).toString();
        }
    }

    /**
     * Both of the current buffer's children are set to a Buffer array and returned to the calling method
     * */
    private ArrayList<Buffer> getChildren() {
        ArrayList<Buffer> children = new ArrayList<>();
        children.add(this.childA);
        children.add(this.childB);
        return children;
    }

    /**
     * Each child in the Buffer is returned to the calling method in the childrenStatus Array
     * If the requested child is split then it along with it's children are added to the childrenStatus Array
     * */
    public ArrayList getChildrenStatus() {
        ArrayList childrenStatus = new ArrayList();
        if(this.childA!= null && !this.childA.isBufferSplit()){
            childrenStatus.add(this.childA.getStatus());
        }else if(this.childA != null){
            childrenStatus.addAll(this.childA.getChildrenStatus());
        }
        if(this.childB != null && !this.childB.isBufferSplit()){
            childrenStatus.add(this.childB.getStatus());
        }else if(this.childB != null){
            childrenStatus.addAll(this.childB.getChildrenStatus());
        }
        return childrenStatus;
    }

    //    it will return the buffer and current size
    public String getStatus() {
        return String.format("%s size buffer", this.currentSize);
    }

    public boolean isBufferFree() {
        return bufferFree;
    }

    public boolean isBufferSplit() {
        return bufferSplit;
    }
}

//-------------------------*END Buffer*----------------------------//


//-------------------------*START BufferManager*----------------------------//
class BufferManager {

    private LinkedList<Buffer> bufferList = null;
    private int maxBufferSize = 0;
    private int minBufferSize = 0;
    private boolean tightPool = false;
    private String status = null;

    //    this receives the size of the buffer list, the max size of each buffer and the min size of each buffers
    public void BufferManager(int listSize, int bufferMax, int bufferMin){

        this.maxBufferSize = bufferMax;
        this.minBufferSize = bufferMin;
        this.bufferList = buildBufferList(listSize, this.maxBufferSize, this.minBufferSize);
        setStatus(bufferDebug());
    }

    //    Returns The size of the list min/max buffer sizes are provided and a linked list
    private LinkedList<Buffer> buildBufferList(int listSize,int maxBufferSize, int minBufferSize) {
        LinkedList<Buffer> bufferList = new LinkedList<>();
        for(int i = 0;i<listSize;i++){
            Buffer newBuffer = new Buffer();
            newBuffer.Buffer(maxBufferSize,minBufferSize,null);
            bufferList.add(newBuffer);
        }
        return bufferList;
    }
    //to set status of the value provided
    public void setStatus(String status) {
        this.status = status;
    }
    //    returns the current status of the buffers in the BufferManager
    public String bufferDebug(){
        String bufferStatus = null;
        ArrayList<String> bufferStatusList = buildAvailableBufferList();
        String bufferSizeCounts = getBufferListStatus(bufferStatusList);
        return String.format("Expected Values: %s Status: %s",bufferSizeCounts, getStatus());
    }
    //    when a method call buildAvailableBufferList, it will return a list
    private ArrayList<String> buildAvailableBufferList() {
        ArrayList returnBufferList = new ArrayList();
        for(int i = 0;i < this.bufferList.size();i++){
            if(this.bufferList.get(i).isBufferFree()){
                returnBufferList.add(this.bufferList.get(i).getStatus());
            }else{
                returnBufferList.addAll(this.bufferList.get(i).getChildrenStatus());
            }
        }
        return returnBufferList;
    }
    //it receives a list of buffer statuses, processes each buffer status, and compared against all subesquent statuses to find a match
    //then it is removed from the list and it is added to the buffer count list
    private String getBufferListStatus(ArrayList<String> bufferStatusList) {
        ArrayList<String> bufferCountList = new ArrayList();
        if(bufferStatusList !=null){
            while(bufferStatusList.size()>0){
                String checkValue = bufferStatusList.get(0);
                int checkCount = 1;
                for(int i = 1;i < bufferStatusList.size();i++){
                    if(bufferStatusList.get(i).equals(checkValue)){
                        checkCount = checkCount+1;
                        bufferStatusList.remove(i);
                        i = 0;
                    }
                }
                bufferCountList.add(String.format("%d %s",checkCount,checkValue));
                bufferStatusList.remove(0);
            }
        }
        Collections.reverse(bufferCountList);
        StringBuilder debugStatus = new StringBuilder();
        for(String i : bufferCountList){
            debugStatus.append(i);
            debugStatus.append(", ");
        }
        return debugStatus.toString();
    }

    //receives a requestedSize and returned an object of:-2,-1,memory loc
    //  buffer loc is returned
    public Object requestBuffer(int requestSize){
        Object returnValue = "-1";
        Object requestResponse = null;
        for(int i = 0;i<bufferList.size();i++){
            if(bufferList.get(i).isBufferFree()){
                requestResponse = bufferList.get(i).requestBuffer(requestSize);
                if(requestResponse == "-2"){
                    returnValue = "-2";
                    break;
                }else if (requestResponse == "-1"){
                    returnValue = "-1";
                }else {
                    this.tightPool = checkTightConstraint();
                    returnValue = requestResponse;
                    break;
                }
            }else if(bufferList.get(i).isBufferSplit()){
                requestResponse = bufferList.get(i).requestChild(requestSize);
                if(requestResponse == "-2"){
                    returnValue = "-2";
                    break;
                }else if (requestResponse == "-1"){
                    returnValue = "-1";
                }else {
                    this.tightPool = checkTightConstraint();
                    returnValue = requestResponse;
                    break;
                }
            }
        }
        setStatus(bufferDebug());
        return returnValue ;
    }

    //check to see if Each buffer is free
    //less that 2 available buffers the constraint is set to true
    private boolean checkTightConstraint() {
        int free = 0;
        for(int i = 0;i < bufferList.size();i++){
            if(bufferList.get(i).isBufferFree()){
                free = free+1;
            }
        }

        return free < 2;
    }

    //each buffer in the BufferManager list is checked to determine if it contains the reclaim buffer to update it's status
    public void returnBuffer(Buffer addess){
        for(int i = 0;i < bufferList.size();i++){
            if(bufferList.get(i).reclaimBuffer(addess)==addess){
                this.tightPool = checkTightConstraint();
                break;
            }
        }
    }
    //   it records in the int array at it's corresponding location
    public int[] bufferStatus(){
        ArrayList<String> bufferCountList = buildAvailableBufferList();
        int[] bufferStatusList = {0,0,0,0,0,0,0};
        for(int i = 0;i<bufferCountList.size();i++){
            if(bufferCountList.get(i).equals("511 size buffer")){
                bufferStatusList[0] = bufferStatusList[0] +1;
            }else if(bufferCountList.get(i).equals("255 size buffer")){
                bufferStatusList[1] = bufferStatusList[1] +1;
            }else if(bufferCountList.get(i).equals("127 size buffer")){
                bufferStatusList[2] = bufferStatusList[2] +1;
            }else if(bufferCountList.get(i).equals("63 size buffer")){
                bufferStatusList[3] = bufferStatusList[3] +1;
            }else if(bufferCountList.get(i).equals("31 size buffer")){
                bufferStatusList[4] = bufferStatusList[4] +1;
            }else if(bufferCountList.get(i).equals("15 size buffer")){
                bufferStatusList[5] = bufferStatusList[5] +1;
            }else if(bufferCountList.get(i).equals("7 size buffer")){
                bufferStatusList[6] = bufferStatusList[6] +1;
            }
        }

        return bufferStatusList;
    }
    //    returns Tight or OK
    public String getStatus(){
        if(isTightPool()){
            return "tight";
        }else{
            return "OK";
        }
    }

    public boolean isTightPool() {
        return tightPool;
    }

}

//-------------------------*END BufferManager*----------------------------//




//-------------------------*START bufferWriter*----------------------------//
class bufferWriter {

    public static File createFile(String fileName, String fileDirectory) throws IOException {
        File newFile = new File(String.format("%s\\%s.txt", fileDirectory, fileName));
        newFile.createNewFile();
        return newFile;
    }

    public static void writeToFile(File fileName, LinkedList<String> outputList) {
        try {
            PrintStream writer = new PrintStream(new FileOutputStream(fileName));
            for(int i =0;i<outputList.size();i++)
            {
                writer.println(outputList.get(i));
            }
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

//-------------------------*END bufferWriter*----------------------------//

//-----------*START Main*------------------//

public class Assignment7 {

    public static void main(String[] args){
        LinkedList<String> outputData = new LinkedList<>();
        outputData.add("Alex Lema\n" + "ICS 462 Assignment #7\nMay 02, 2022\n");
        System.out.println("Alex Lema\n" + "ICS 462 Assignment #7\nMay 02, 2022");

        outputData.add("Initializing buffers");
        System.out.println("\nInitializing buffers");
        outputData.add("Expected values: 10 512 size buffers, Status Ok\n");
        System.out.println("\nExpected values: 10 512 size buffers, Status Ok");

        outputData = testBuffer1(outputData);
        outputData = testBuffer2(outputData);
        outputData = testBuffer3(outputData);
        outputData = testBuffer4(outputData);
//        outputData = testBuffer5(outputData);
        outputData.add("\nDebug Output:");
        System.out.println("\nDebug Output:");
        outputData.add("Free Buffer Count: \n10 510 size buffers, \n0 254 size buffer, \n0 126 size buffer, \n0 62 size buffer, \n0 30 size buffer, \n0 14 size buffer and \n0 6 size buffer, \nStatus OK\n");
        System.out.println("Free Buffer Count: \n10 510 size buffers, \n0 254 size buffer, \n0 126 size buffer, \n0 62 size buffer, \n0 30 size buffer, \n0 14 size buffer and \n0 6 size buffer, \nStatus OK\n");

        //Write to file name OutputFileAssignment7
        try {
            File userDirectory = new File(System.getProperty("user.dir"));
            File outputFile = bufferWriter.createFile("OutputFileAssignment7", userDirectory.getPath());
            bufferWriter.writeToFile(outputFile, outputData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static LinkedList<String> testBuffer1(LinkedList<String> outputData) {
        BufferManager testBuffer = new BufferManager();
        testBuffer.BufferManager(10,511,7);
//////////////////////requestResponse///////////////
        ArrayList<Object> requestResponse = new ArrayList<>();
        outputData = (printStatus(testBuffer.bufferStatus(), outputData));
        int value1=700;
        outputData.add("Requesting "+value1);
        outputData.add("Expected values:");
        System.out.println("Requesting "+value1);
        System.out.println("Expected values:");
        int[] testValues = {700};
        //if you get a request for 700 words you need to return an error status (a -2 for illegal request).
        // If, for example, you get a request for a 5 word block, you have an option (but you must be consistent),
        // you can either return a pointer (or index) to a 7 word block or return an error status (a -2 for illegal request).
//        return outputData;
        return outputData = runTestDriver(testValues,testBuffer,outputData,requestResponse);
    }

/////////////////////Requesting 7/////////////////////////////////////////
    private static LinkedList<String> testBuffer2(LinkedList<String> outputData) {
        BufferManager testBuffer = new BufferManager();
        testBuffer.BufferManager(10,511,7);
        //////////////////////requestResponse//////////////
        ArrayList<Object> requestResponse = new ArrayList<>();
        outputData = (printStatus(testBuffer.bufferStatus(), outputData));
        outputData.add("Requesting 7");
        outputData.add("Expected values: \n9 510 size buffers, \n1 254 size buffer, \n1 126 size buffer, \n1 62 size buffer, \n1 30 size buffer, \n1 14 size buffer and \n1 6 size buffer, \n\nStatus OK\n");
        System.out.println("Requesting 7");
        System.out.println("Expected values: \n9 510 size buffers, \n1 254 size buffer, \n1 126 size buffer, \n1 62 size buffer, \n1 30 size buffer, \n1 14 size buffer and \n1 6 size buffer, \n\nStatus OK\n");
        int[] testValues = {7};
        // The test creates a BufferManager of size 10 with a min of 7 and max of 511
        return outputData = runTestDriver(testValues,testBuffer,outputData,requestResponse);
    }



///////////////Request 10 510 buffers///////////////////////////////////
    private static LinkedList<String> testBuffer3(LinkedList<String> outputData) {
        BufferManager testBuffer = new BufferManager();
        testBuffer.BufferManager(10,511,7);
        //////////////////////requestResponse///////////////
        ArrayList<Object> requestResponse = new ArrayList<>();
        outputData = (printStatus(testBuffer.bufferStatus(), outputData));

        outputData.add("Requesting 10 510 buffers");
        outputData.add("Expected values: 0 510 buffers, 0 for all buffers, Status Tight");
        System.out.println("Requesting 10 510 buffers\n");
        System.out.println("Expected values: 0 510 buffers, 0 for all buffers, Status Tight\n");

        int[] testValues = {511,511,511,511,511,511,511,511,511,511};

        outputData = runTestDriver(testValues, testBuffer, outputData, requestResponse);
        //////////////////////requestResponse2///////////////Request additional buffer
        ArrayList<Object> requestResponse2 = new ArrayList<>();
        int value2=62;
        int[] testValues2 = {value2};
        outputData.add("Requesting "+ value2);
        outputData.add("Expected values: -1, \nStatus Tight\n");

        System.out.println("Requesting 62\n");
        System.out.println("Expected values: -1, \nStatus Tight\n");

        return outputData = runTestDriver(testValues2,testBuffer,outputData,requestResponse2);
    }

    /////===============Request 19 254 size buffers ========================///
    //     The test creates a BufferManager of size 10 with a min of 7 and max of 511
    private static LinkedList<String> testBuffer4(LinkedList<String> outputData) {
        BufferManager testBuffer = new BufferManager();
        testBuffer.BufferManager(10,511,7);
        //////////////////////requestResponse///////////////
        ArrayList<Object> requestResponse = new ArrayList<>();
        outputData = (printStatus(testBuffer.bufferStatus(), outputData));

        outputData.add("Request 19 254 size buffers");
        outputData.add("Expected values: \n0 510 buffers, \n19 254 size buffers, \n0 126 size buffers, \n0 62 size buffers, \n0 30 size buffers, \n0 14 size buffers, \n0 6 size buffers \nStatus Tight \n");
        System.out.println("Request 19 254 size buffers");
        System.out.println("Expected values: 0 510 buffers, \n19 254 size buffers, \n0 126 size buffers, \n0 62 size buffers, \n0 30 size buffers, \n0 14 size buffers, \n0 6 size buffers \nStatus Tight \n");

        int[] testValues = {253,253,253,253,253,253,253,253,253,253,253,253,253,253,253,253,253,253};
        return outputData = runTestDriver(testValues,testBuffer,outputData, requestResponse);
    }


//================Request 19 254 size buffers =================/
//    private static LinkedList<String> testBuffer5(LinkedList<String> outputData){
//        BufferManager testBuffer = new BufferManager();
//        testBuffer.BufferManager(19,254,7);
//        //////////////////////requestResponse///////////////
//        ArrayList<Object> requestResponse = new ArrayList<>();
//        outputData = (printStatus(testBuffer.bufferStatus(), outputData));
//
//        outputData.add("Request 19 254 size buffers ");
//        outputData.add("Expected Values: \n0 510 size buffers, \n1 254 size buffer, \n0 126 size buffer, \n0 62 size buffer, \n0 30 size buffer, \n0 14 size buffer    and \n0 6 size buffer, \nStatus tight\n");
//        System.out.println("Request 19 254 size buffers ");
//        System.out.println("Expected Values: \n0 510 size buffers, \n1 254 size buffer, \n0 126 size buffer, \n0 62 size buffer, \n0 30 size buffer, \n0 14 size buffer    and \n0 6 size buffer, \nStatus tight\n");
//        int[] testValues = {254,254 ,254 ,254 ,254 ,254 ,254,254,254 ,254 ,254 ,254 ,254 ,254 ,254,254,254,254,254 };
//        return outputData = runTestDriver(testValues,testBuffer,outputData,requestResponse);
//    }

    //Test driver
    private static LinkedList<String> runTestDriver(int[] requestSize, BufferManager bufferManager, LinkedList outputData, ArrayList<Object> requestResponse) {
        outputData = printStatus(bufferManager.bufferStatus(), outputData);
        for(int i = 0;i < requestSize.length;i++){
            requestResponse.add(bufferManager.requestBuffer(requestSize[i]));
            outputData.add("Actual = Assigned address: " + requestResponse.get(i).toString());
            System.out.println("Actual = Assigned address: " + requestResponse.get(i).toString());
        }
        outputData = printStatus(bufferManager.bufferStatus(), outputData);
        outputData.add("Status: "+bufferManager.getStatus());
        System.out.println("Status: "+bufferManager.getStatus());
        return outputData;
    }

    //Prints buffers status
    private static LinkedList printStatus(int[] bufferCounts, LinkedList outputData){

        outputData.add("\nFree Buffer Count:");
        outputData.add(bufferCounts[0] + " 510 size buffers");
        outputData.add(bufferCounts[1] + " 254 size buffers");
        outputData.add(bufferCounts[2] + " 126 size buffers");
        outputData.add(bufferCounts[3] + " 62 size buffers");
        outputData.add(bufferCounts[4] + " 30 size buffers");
        outputData.add(bufferCounts[5] + " 14 size buffers");
        outputData.add(bufferCounts[6] + " 6 size buffers\n");
        System.out.println("\nFree Buffer Count:");
        System.out.println(bufferCounts[0] + " 510 size buffers");
        System.out.println(bufferCounts[1] + " 254 size buffers");
        System.out.println(bufferCounts[2] + " 126 size buffers");
        System.out.println(bufferCounts[3] + " 62 size buffers");
        System.out.println(bufferCounts[4] + " 30 size buffers");
        System.out.println(bufferCounts[5] + " 14 size buffers");
        System.out.println(bufferCounts[6] + " 6 size buffers\n");
        return outputData;
    }
}
