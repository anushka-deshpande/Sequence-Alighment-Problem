import java.io.*;
import java.util.*;

public class Basic
{

    String finalS1 = "";
    String finalS2 = "";

    String alignment1 = "";
    String alignment2 = "";
    int delta = 30;
    int[][] alpha = {{0, 110, 48, 94},
            {110, 0, 118, 48},
            {48, 118, 0, 110},
            {94, 48, 110, 0}};

    int OPTval = 0;
    float timeTaken = 0;
    float memoryUsed = 0;

    public static void main(String [] args) throws IOException
    {

        String filename = "/Users/anushkadeshpande/IdeaProjects/javaProject/input1.txt";
        Basic obj = new Basic();
        obj.getInput(filename);

        System.out.println(obj.finalS1);
        System.out.println(obj.finalS2);

        double beforeUsedMem = getMemoryInKB();
        double startTime = getTimeInMilliseconds();

        int [][] OPT = obj.generateOPT();
        obj.generateAlignments(OPT);

        double afterUsedMem = getMemoryInKB();
        double endTime = getTimeInMilliseconds();

        double totalUsage = afterUsedMem-beforeUsedMem;
        double totalTime = endTime - startTime;

        obj.createOutput("/Users/anushkadeshpande/IdeaProjects/javaProject/output.txt", totalTime, totalUsage);

        System.out.println(obj.alignment1);
        System.out.println(obj.alignment2);

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
        List<Integer> a1 = new ArrayList<>();
        List<Integer> a2 = new ArrayList<>();

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

        int j = 0;
        String s1 = scanFile.nextLine();
        String originalS1 = s1;

        while(scanFile.hasNextInt())
        {
            int index = scanFile.nextInt();
            a1.add(index);
            j += 1;
            s1 = s1.substring(0, index+1) + s1 + s1.substring(index+1);
        }

        int k = 0;
        String s2 = scanFile.next();
        String originalS2 = s2;

        while(scanFile.hasNextInt())
        {
            int index = scanFile.nextInt();
            a2.add(index);
            k += 1;
            s2 = s2.substring(0, index + 1) + s2 + s2.substring(index + 1);
        }
        scanFile.close();

        assert (s1.length() == Math.pow(2, j) * originalS1.length() &&
                s1.length() == Math.pow(2, k) * originalS2.length());

        finalS1 = s1;
        finalS2 = s2;
    }
    public int MismatchCost(int i, int j)
    {
        char a = finalS1.charAt(i);
        char b = finalS2.charAt(j);

        int x = 0,y = 0;
        switch(a)
        {
            case 'A':
                x = 0;
                break;
            case 'C':
                x = 1;
                break;
            case 'G':
                x = 2;
                break;
            case 'T':
                x = 3;
                break;
        }

        switch(b)
        {
            case 'A':
                y = 0;
                break;
            case 'C':
                y = 1;
                break;
            case 'G':
                y = 2;
                break;
            case 'T':
                y = 3;
                break;
        }

        return alpha[x][y];
    }

    public int[][] generateOPT()
    {
        int m = finalS1.length();
        int n = finalS2.length();
        int opt[][] = new int[finalS1.length()+1][finalS2.length()+1];
        //System.out.println(m + " " + n);

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
                if(finalS1.charAt(i-1)==finalS2.charAt(j-1))
                {
                    opt[i][j] = opt[i-1][j-1];
                }
                else
                {
                    opt[i][j] = Math.min(MismatchCost(i - 1, j - 1) + opt[i - 1][j - 1], Math.min(delta + opt[i - 1][j], delta + opt[i][j - 1]));
                    //System.out.print(opt[i][j] + " ");
                }
            }
            //System.out.println();
        }
        OPTval = opt[m][n];
        return opt;
    }

    public void generateAlignments(int[][] OPT)
    {
        int i = finalS1.length();
        int j = finalS2.length();

        while(i > 0 && j > 0)
        {
            if (finalS1.charAt(i-1) == finalS2.charAt(j-1))
            {
                alignment1 = finalS1.charAt(i-1) + alignment1;
                alignment2 = finalS2.charAt(j-1) + alignment2;
                i--;
                j--;
            }
            else if(OPT[i][j-1] + delta == OPT[i][j])
            {
                alignment2 = finalS2.charAt(j-1) + alignment2;
                alignment1 = "_" + alignment1;
                j--;
            }
            else if(OPT[i-1][j] + delta == OPT[i][j]) {
                alignment1 = finalS1.charAt(i - 1) + alignment1;
                alignment2 = "_" + alignment2;
                i--;
            }

            else //if(OPT[i-1][j-1] + MismatchCost(i-1, j-1) == OPT[i][j])
            {
                alignment1 = finalS1.charAt(i-1) + alignment1;
                alignment2 = finalS2.charAt(j-1) + alignment2;
                i--;
                j--;
            }
        }

        if (j > 0)
        {
            while(j > 0)
            {
                alignment2 = finalS2.charAt(j-1) + alignment2;
                alignment1 = "_" + alignment1;
                j--;
            }
        }

        if(i > 0)
        {
            while( i > 0)
            {
                alignment1 = finalS1.charAt(i-1) + alignment1;
                alignment2 = "_" + alignment2;
                i--;
            }
        }
    }

    public void createOutput(String outputFile, double time, double memory)
    {
        try
        {
            File file = new File(outputFile);
            FileWriter fileWriter = new FileWriter(outputFile);
            fileWriter.write(OPTval + "\n");
            fileWriter.write(alignment1 + "\n");
            fileWriter.write(alignment2 + "\n");
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