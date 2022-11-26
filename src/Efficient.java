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

    public int MismatchCost(char a, char b) {
        //char a = finalS1.charAt(i);
        //char b = finalS2.charAt(j);

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
        obj.alignment1 = alignments[0];
        obj.alignment2 = alignments[1];
        int cost = obj.getCost();

        double afterUsedMem = getMemoryInKB();
        double endTime = getTimeInMilliseconds();

        double totalUsage = afterUsedMem-beforeUsedMem;
        double totalTime = endTime - startTime;

        obj.createOutput("/Users/anushkadeshpande/IdeaProjects/javaProject/output2.txt",cost, totalTime, totalUsage);

        System.out.println(obj.alignment1);
        System.out.println(obj.alignment2);
    }

    public String[] divide(String x, String y)
    {
        String al1 = "";
        String al2 = "";

        if( x.length() <= 2 || y.length() <= 2)
        {
            return getAlignment(x, y);
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

        int m = x.length();
        int n = y.length();
        int opt[][] = new int[x.length()+1][y.length()+1];
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
                if(x.charAt(i-1)==y.charAt(j-1))
                {
                    opt[i][j] = opt[i-1][j-1];
                }
                else
                {
                    opt[i][j] = Math.min(MismatchCost(x.charAt(i - 1), y.charAt(j - 1)) + opt[i - 1][j - 1],
                            Math.min(delta + opt[i - 1][j], delta + opt[i][j - 1]));
                    //System.out.print(opt[i][j] + " ");
                }
            }
            //System.out.println();
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

            else //if(OPT[i-1][j-1] + MismatchCost(i-1, j-1) == OPT[i][j])
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
        StringBuffer sb = new StringBuffer(s);
        sb.reverse();
        return sb.toString();
    }

    public int getCost()
    {
        int cost = 0;

        for (int i = 0; i < alignment1.length(); i++) {
            if (i < alignment2.length()) {
                // alignment
                if (alignment1.charAt(i) == alignment2.charAt(i)) {
                    cost = cost + alpha[0][0];
                }
                // gap
                else if (alignment1.charAt(i) == '_' || alignment2.charAt(i) == '_') {
                    cost = cost + delta;
                }
                // mismatch
                else {
                    cost = cost + MismatchCost(alignment1.charAt(i), alignment2.charAt(i));
                }
            }
        }
        return cost;
    }

    public void createOutput(String outputFile,int cost, double time, double memory)
    {
        try
        {
            File file = new File(outputFile);
            FileWriter fileWriter = new FileWriter(outputFile);
            fileWriter.write(cost + "\n");
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
