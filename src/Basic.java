/* CSCI570 Fall 22' Project
   Sequence Alignment Problem
   Basic.java

   Team Members :-
   Anushka Deshpande 5914802345
   Omkar Nikhal 5144173884
 */


import java.io.*;
import java.util.*;

public class Basic
{
    String finalString1 = "";
    String finalString2 = "";
    String finalAlignment1 = "";
    String finalAlignment2 = "";
    int delta = 30;
    int[][] alpha = {
            {0, 110, 48, 94},
            {110, 0, 118, 48},
            {48, 118, 0, 110},
            {94, 48, 110, 0}
    };

    int OPTval = 0;

    public static void main(String [] args) throws IOException
    {
        String filename = args[0];
        Basic obj = new Basic();
        obj.getInput(filename);

        double beforeUsedMem = getMemoryInKB();
        double startTime = getTimeInMilliseconds();

        obj.generateAlignments();

        double afterUsedMem = getMemoryInKB();
        double endTime = getTimeInMilliseconds();

        double totalUsage = afterUsedMem-beforeUsedMem;
        double totalTime = endTime - startTime;

        obj.createOutput(args[1], totalTime, totalUsage);
    }

    private static double getMemoryInKB()
    {
        double total = Runtime.getRuntime().totalMemory();
        return (total-Runtime.getRuntime().freeMemory())/10e3;
    }

    private static double getTimeInMilliseconds()
    {
        return System.nanoTime()/10e6;
    }

    public void getInput(String filename)throws IOException
    {
        File file = new File(filename);
        Scanner scanFile;

        try {
            scanFile = new Scanner(file);
        }
        catch (Exception e)
        {
            System.out.println("File Not found");
            return;
        }

        int i = 0;
        String s1 = scanFile.nextLine();
        String originalString1 = s1;

        while(scanFile.hasNextInt())
        {
            int index = scanFile.nextInt();
            i += 1;
            s1 = s1.substring(0, index+1) + s1 + s1.substring(index+1);
        }

        int j = 0;
        String s2 = scanFile.next();
        String originalString2 = s2;

        while(scanFile.hasNextInt())
        {
            int index = scanFile.nextInt();
            j += 1;
            s2 = s2.substring(0, index + 1) + s2 + s2.substring(index + 1);
        }

        scanFile.close();

        assert (s1.length() == Math.pow(2, i) * originalString1.length() &&
                s1.length() == Math.pow(2, j) * originalString2.length());

        finalString1 = s1;
        finalString2 = s2;
    }
    public int MismatchCost(char a, char b)
    {
        int x = 0,y = 0;

        switch (a) {
            case 'A' -> x = 0;
            case 'C' -> x = 1;
            case 'G' -> x = 2;
            case 'T' -> x = 3;
        }

        switch (b) {
            case 'A' -> y = 0;
            case 'C' -> y = 1;
            case 'G' -> y = 2;
            case 'T' -> y = 3;
        }

        return alpha[x][y];
    }

    public int[][] generateOPT()
    {
        int m = finalString1.length();
        int n = finalString2.length();
        int[][] opt = new int[finalString1.length()+1][finalString2.length()+1];

        for(int i=0;i<=m;i++)
        {
            opt[i][0] = delta * i;
        }

        for(int j=0;j<=n;j++)
        {
            opt[0][j] = delta * j;
        }

        for(int i=1;i<=m;i++)
        {
            for(int j = 1; j<=n;j++)
            {
                if(finalString1.charAt(i-1)==finalString2.charAt(j-1))
                {
                    opt[i][j] = opt[i-1][j-1];
                }
                else
                {
                    opt[i][j] = Math.min(MismatchCost(finalString1.charAt(i - 1), finalString2.charAt(j - 1)) + opt[i - 1][j - 1],
                            Math.min(delta + opt[i - 1][j], delta + opt[i][j - 1]));
                }
            }
        }
        OPTval = opt[m][n];
        return opt;
    }

    public void generateAlignments()
    {
        int [][] OPT = generateOPT();
        int i = finalString1.length();
        int j = finalString2.length();

        while(i > 0 && j > 0)
        {
            if (finalString1.charAt(i-1) == finalString2.charAt(j-1))
            {
                finalAlignment1 = finalString1.charAt(i-1) + finalAlignment1;
                finalAlignment2 = finalString2.charAt(j-1) + finalAlignment2;
                i--;
                j--;
            }
            else if(OPT[i][j-1] + delta == OPT[i][j])
            {
                finalAlignment2 = finalString2.charAt(j-1) + finalAlignment2;
                finalAlignment1 = "_" + finalAlignment1;
                j--;
            }
            else if(OPT[i-1][j] + delta == OPT[i][j]) {
                finalAlignment1 = finalString1.charAt(i - 1) + finalAlignment1;
                finalAlignment2 = "_" + finalAlignment2;
                i--;
            }
            else
            {
                finalAlignment1 = finalString1.charAt(i-1) + finalAlignment1;
                finalAlignment2 = finalString2.charAt(j-1) + finalAlignment2;
                i--;
                j--;
            }
        }

        if (j > 0)
        {
            while(j > 0)
            {
                finalAlignment2 = finalString2.charAt(j-1) + finalAlignment2;
                finalAlignment1 = "_" + finalAlignment1;
                j--;
            }
        }

        if(i > 0)
        {
            while( i > 0)
            {
                finalAlignment1 = finalString1.charAt(i-1) + finalAlignment1;
                finalAlignment2 = "_" + finalAlignment2;
                i--;
            }
        }
    }

    public void createOutput(String outputFile, double time, double memory)
    {
        try
        {
            FileWriter fileWriter = new FileWriter(outputFile);
            fileWriter.write(OPTval + "\n");
            fileWriter.write(finalAlignment1 + "\n");
            fileWriter.write(finalAlignment2 + "\n");
            fileWriter.write(time + "\n");
            fileWriter.write(memory + "\n");
            fileWriter.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}