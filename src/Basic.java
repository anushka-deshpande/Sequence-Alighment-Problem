import java.io.*;
import java.util.*;

public class Basic
{

    String finalS1 = "";
    String finalS2 = "";
    int delta = 30;
    int[][] alpha = {{0, 110, 48, 94},
            {110, 0, 118, 48},
            {48, 118, 0, 110},
            {94, 48, 110, 0}};

    public static void main(String [] args) throws IOException
    {

        String filename = "/Users/anushkadeshpande/IdeaProjects/javaProject/input1.txt";
        Basic obj = new Basic();
        obj.getInput(filename);

        System.out.println(obj.finalS1);
        System.out.println(obj.finalS2);

        System.out.println(obj.generateOPT());

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

    public int generateOPT()
    {
        int m = finalS1.length();
        int n = finalS2.length();
        int opt[][] = new int[finalS1.length()][finalS2.length()];
        //System.out.println(m + " " + n);

        for(int i=0;i<m;i++)
        {
            opt[i][0] = delta * i;
        }

        for(int j=0;j<n;j++)
        {
            opt[0][j] = delta * j;
        }

        for(int i=1;i<m;i++)
        {
            for(int j = 1; j<n;j++)
            {
                opt[i][j] = Math.min(MismatchCost(i, j) + opt[i-1][j-1], Math.min(delta + opt[i-1][j], delta + opt[i][j-1]));
                //System.out.print(opt[i][j] + " ");
            }
            //System.out.println();
        }
        return opt[m-1][n-1];
    }
}