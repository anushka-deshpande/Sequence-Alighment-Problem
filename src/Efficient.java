/* CSCI570 Fall 22' Project
   Sequence Alignment Problem
   Efficient.java

   Team Members :-
   Anushka Deshpande 5914802345
   Omkar Nikhal 5144173884
 */

import java.io.*;
import java.util.*;

public class Efficient
{
    String finalString1 = "";
    String finalString2 = "";
    String finalAlignment1 = "";
    String finalAlignment2 = "";
    int delta = 30;
    int cost = 0;
    int[][] alpha = {
            {0, 110, 48, 94},
            {110, 0, 118, 48},
            {48, 118, 0, 110},
            {94, 48, 110, 0}
    };

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

    public static void main(String[] args) throws IOException
    {
        String filename = args[0];
        Efficient obj = new Efficient();
        obj.getInput(filename);

        double beforeUsedMem = getMemoryInKB();
        double startTime = getTimeInMilliseconds();

        String[] alignments = obj.divide(obj.finalString1, obj.finalString2);
        obj.finalAlignment1 = alignments[0];
        obj.finalAlignment2 = alignments[1];
        obj.cost = obj.getCost();

        double afterUsedMem = getMemoryInKB();
        double endTime = getTimeInMilliseconds();

        double totalUsage = afterUsedMem-beforeUsedMem;
        double totalTime = endTime - startTime;

        obj.createOutput(args[1], totalTime, totalUsage);
    }

    public String[] divide(String x, String y)
    {
        String alignment1;
        String alignment2;

        if( x.length() <= 2 || y.length() <= 2)
        {
            return getAlignment(x, y);
        }
        else
        {
            int[] yLeftAlign = assignCost(x.substring(0, x.length()/2), y);
            int[] yRightAlign = assignCost(reverseString(x.substring(x.length()/2)), reverseString(y));

            int min = yLeftAlign[0] + yRightAlign[yRightAlign.length - 1];

            int k = 0;

            for(int i=1; i<yLeftAlign.length;i++)
            {
                if (yLeftAlign[i] + yRightAlign[yRightAlign.length-1-i] < min)
                {
                    min = yLeftAlign[i] + yRightAlign[yRightAlign.length-1-i];
                    k = i;
                }
            }

            String yL = "";
            String yR = "";

            if(k>=1)
            {
                yL = y.substring(0, k);
                yR = y.substring(k);
            }

            String[] lower = divide(x.substring(0, x.length()/2), yL);
            String[] upper = divide(x.substring(x.length()/2), yR);

            alignment1 = lower[0] + upper[0];
            alignment2 = lower[1] + upper[1];

            return new String[] {alignment1, alignment2};
        }
    }

    public int[] assignCost(String x, String y)
    {
        int [][] opt = new int[2][y.length()+1];
        int index = 1;

        while(index <= x.length())
        {
            if(index == 1)
            {
                for (int i = 0; i < y.length() + 1; i++)
                {
                    opt[0][i] = i * delta;
                }
            }

            opt[1][0] = index * delta;

            for(int j = 1; j < y.length() + 1; j++)
            {
                if(x.charAt(index-1) == y.charAt(j-1))
                {
                    opt[1][j] = opt[0][j-1];
                }
                opt[1][j] = Math.min(Math.min(MismatchCost(x.charAt(index-1), y.charAt(j-1)) + opt[0][j-1], delta + opt[0][j]), delta + opt[1][j-1]);
            }

            index++;

            for(int k=0;k<y.length()+1;k++)
            {
                opt[0][k] = opt[1][k];
            }
        }

        return opt[1];
    }

    public String[] getAlignment(String x, String y)
    {
        String align1 = "";
        String align2 = "";

        int[][] opt = new int[x.length()+1][y.length()+1];
        //System.out.println(m + " " + n);

        for(int i=0;i<=x.length();i++)
        {
            opt[i][0] = delta * i;
        }

        for(int j=0;j<=y.length();j++)
        {
            opt[0][j] = delta * j;
        }

        for(int i=1;i<=x.length();i++)
        {
            for(int j = 1; j<=y.length();j++)
            {
                if(x.charAt(i-1)==y.charAt(j-1))
                {
                    opt[i][j] = opt[i-1][j-1];
                }
                else
                {
                    opt[i][j] = Math.min(MismatchCost(x.charAt(i - 1), y.charAt(j - 1)) + opt[i - 1][j - 1],
                            Math.min(delta + opt[i - 1][j], delta + opt[i][j - 1]));
                }
            }
        }

        int i = x.length();
        int j = y.length();

        while(i > 0 && j > 0)
        {
            if (x.charAt(i-1) == y.charAt(j-1))
            {
                align1 = x.charAt(i-1) + align1;
                align2 = y.charAt(j-1) + align2;
                i--;
                j--;
            }
            else if(opt[i][j-1] + delta == opt[i][j])
            {
                align2 = y.charAt(j-1) + align2;
                align1 = "_" + align1;
                j--;
            }
            else if(opt[i-1][j] + delta == opt[i][j]) {
                align1 = x.charAt(i - 1) + align1;
                align2 = "_" + align2;
                i--;
            }
            else
            {
                align1 = x.charAt(i-1) + align1;
                align2 = y.charAt(j-1) + align2;
                i--;
                j--;
            }
        }

        if (j > 0)
        {
            while(j > 0)
            {
                align2 = y.charAt(j-1) + align2;
                align1 = "_" + align1;
                j--;
            }
        }

        if(i > 0)
        {
            while( i > 0)
            {
                align1 = x.charAt(i-1) + align1;
                align2 = "_" + align2;
                i--;
            }
        }

        return new String[] {align1, align2};
    }

    public String reverseString(String s)
    {
        StringBuilder sb = new StringBuilder(s);
        sb.reverse();
        return sb.toString();
    }

    public int getCost()
    {
        int cost = 0;
        for (int i = 0; i < finalAlignment1.length(); i++)
        {
            if (i < finalAlignment2.length())
            {
                if (finalAlignment1.charAt(i) == finalAlignment2.charAt(i))
                {
                    cost = cost + alpha[0][0];
                }
                else if (finalAlignment1.charAt(i) == '_' || finalAlignment2.charAt(i) == '_')
                {
                    cost = cost + delta;
                }
                else
                {
                    cost = cost + MismatchCost(finalAlignment1.charAt(i), finalAlignment2.charAt(i));
                }
            }
        }
        return cost;
    }

    public void createOutput(String outputFile, double time, double memory)
    {
        try
        {
            FileWriter fileWriter = new FileWriter(outputFile);
            fileWriter.write(cost + "\n");
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
