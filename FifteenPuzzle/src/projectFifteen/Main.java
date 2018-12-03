package projectFifteen;

import java.io.*;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main
{
    private static String parameters;
    private static String inputFile;
    private static String solutionFile;
    private static String statisticFile;
    private static String strategy;

    public static void main(String[] args)
    {
        strategy = args[0];
        parameters = args[1];
        inputFile = args[2];
        solutionFile = args[3];
        statisticFile = args[4];

        int N = 0, M = 0;
        int[][] tab;

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile)))
        {
            String firstLine = br.readLine();
            N = Integer.parseInt(firstLine.split(" ")[0]);
            M = Integer.parseInt(firstLine.split(" ")[1]);
            tab = new int[N][M];

            for (int i = 0; i < N; i++)
            {
                String[] line = br.readLine().split(" ");
                for (int j = 0; j < M; j++)
                    tab[i][j] = Integer.parseInt(line[j]);
            }

            try
            {
                switch (strategy)
                {
                    case "bfs":
                    {
                        FifteenBoard fifteenBoard = new FifteenBoard(N, M, tab);
                        Solver.BFS(fifteenBoard, parameters);
                        break;
                    }
                    case "dfs":
                    {
                        FifteenBoard fifteenBoard = new FifteenBoard(N, M, tab);
                        Solver.DFS(fifteenBoard, parameters);
                        break;
                    }

                    case "astr":
                    {
                        FifteenBoard fifteenBoard = new FifteenBoard(N, M, tab, parameters);
                        Solver.aStar(fifteenBoard);
                        break;
                    }
                }
            }
            catch (Exception ex)
            {   Logger.getLogger(Solver.class.getName()).log(Level.SEVERE, null, ex); }
        } catch (IOException ex)
        {   Logger.getLogger(Solver.class.getName()).log(Level.SEVERE, null, ex); }
    }

    static void save(boolean solutionFound, String solution, int visited, int processed, int recursionLevel, double delta)
    {
        DecimalFormat df = new DecimalFormat("#.###");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(solutionFile)))
        {
            if (solutionFound)
            {
                bw.write(Integer.toString(solution.length()));
                bw.newLine();
                bw.write(solution);
            }
            else
                bw.write("-1");
        }
        catch (IOException ex)
        { Logger.getLogger(Solver.class.getName()).log(Level.SEVERE, null, ex); }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(statisticFile)))
        {
            if (solutionFound)
                bw.write(Integer.toString(solution.length()));
            else
                bw.write("-1");

            bw.newLine();
            bw.write(Integer.toString(visited));
            bw.newLine();
            bw.write(Integer.toString(processed));
            bw.newLine();
            bw.write(Integer.toString(recursionLevel));
            bw.newLine();
            bw.write(df.format(delta));
        }
        catch (IOException ex)
        { Logger.getLogger(Solver.class.getName()).log(Level.SEVERE, null, ex); }
    }
}
