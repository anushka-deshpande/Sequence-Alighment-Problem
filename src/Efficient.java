import java.io.*;
import java.util.*;

public class Efficient
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

    public int MismatchCost(int i, int j) {
        char a = finalS1.charAt(i);
        char b = finalS2.charAt(j);

        int x = 0, y = 0;
        switch (a) {
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
        switch (b) {
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

    public static void main(String args[]) throws IOException
    {
        String filename = "/Users/anushkadeshpande/IdeaProjects/javaProject/input1.txt";
        Efficient obj = new Efficient();
        obj.getInput(filename);

        System.out.println(obj.finalS1);
        System.out.println(obj.finalS2);

        double beforeUsedMem = getMemoryInKB();
        double startTime = getTimeInMilliseconds();

        String[] alignments = obj.divide(obj.finalS1, obj.finalS2);

        double afterUsedMem = getMemoryInKB();
        double endTime = getTimeInMilliseconds();

        double totalUsage = afterUsedMem-beforeUsedMem;
        double totalTime = endTime - startTime;

        obj.createOutput("/Users/anushkadeshpande/IdeaProjects/javaProject/output.txt", totalTime, totalUsage);

        System.out.println(obj.alignment1);
        System.out.println(obj.alignment2);
    }

    public String[] divide(String x, String y)
    {
        String al1 = "";
        String al2 = "";

        if( x.length() <= 2 || y.length() <= 2)
        {
            return new String[]{getAlignment(x, y)};
        }
        else
        {
            int[] yLeftAlign = setCost(x.substring(0, x.length()/2), y);
            int[] yRightAlign = setCost(reverseString(x.substring(x.length()/2)), reverseString(y));

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

            if( k >=1)
            {
                yL = y.substring(0, k);
                yR = y.substring(k);
            }

            String[] lower = divide(x.substring(0, x.length()/2), yL);
            String[] upper = divide(x.substring(x.length()/2), yR);

            al1 = lower[0] + upper[0];
            al2 = lower[1] + upper[1];

            return new String[] {al1, al2};

        }
    }

    public int[] setCost(String x, String y)
    {
        return new int[]{1};
    }

    public String getAlignment(String x, String y)
    {
        return "";
    }

    public String reverseString(String s)
    {
        StringBuffer sb = new StringBuffer(s);
        sb.reverse();
        return sb.toString();
    }

    public void createOutput(String s, double t, double d)
    {

    }
}
