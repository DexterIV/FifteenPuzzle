package projectFifteen;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

class Solver
{
    private static long startTime, endTime;
    private static int visited = 0, processed = 0;
    private static int maxObtainedRecursion = 0;
    private static char[] movementsOrder;

    static boolean BFS(FifteenBoard startingFifteenBoard, String parameters)
    {
        movementsOrder = parameters.toCharArray();
        LinkedList<FifteenBoard> bfsList = new LinkedList<>();

        try
        {
            FifteenBoard solution = new FifteenBoard(startingFifteenBoard.N, startingFifteenBoard.M);

            startTime = System.nanoTime();
            bfsList.add(startingFifteenBoard);

            while (bfsList.size() > 0)
            {
                FifteenBoard currentFifteenBoard = bfsList.poll();
                processed++;

                assert currentFifteenBoard != null;
                if(currentFifteenBoard.isCorrect(solution)){
                    maxObtainedRecursion = currentFifteenBoard.recursionLevel;
                    return FoundSolution(currentFifteenBoard);}
                for (int i = 0; i < 4; i++)
                {
                    FifteenBoard movedFifteenBoard = currentFifteenBoard.clone();

                    if(movedFifteenBoard.move(movementsOrder[i]))
                    {
//                        if(maxObtainedRecursion < movedFifteenBoard.recursionLevel)
//                            maxObtainedRecursion = movedFifteenBoard.recursionLevel;

                        visited++;
                        bfsList.add(movedFifteenBoard);
                    }
                }
            }
        }
        catch (Exception ex)
        { Logger.getLogger(Solver.class.getName()).log(Level.SEVERE, null, ex); }

        return NoSolution();
    }

    static boolean DFS(FifteenBoard startingFifteenBoard, String parameters)
    {
        int maxRecursionLevel = 20; // used only in DFS
        movementsOrder = parameters.toCharArray();
        Stack<FifteenBoard> dfsList = new Stack<>();

        try
        {
            FifteenBoard solution = new FifteenBoard(startingFifteenBoard.N, startingFifteenBoard.M);

            startTime = System.nanoTime();
            dfsList.push(startingFifteenBoard);

            while (dfsList.size() > 0)
            {
                FifteenBoard currentFifteenBoard = dfsList.pop();
                processed++;

                if(currentFifteenBoard.isCorrect(solution))
                    return FoundSolution(currentFifteenBoard);

                if (currentFifteenBoard.recursionLevel >= maxRecursionLevel)
                    continue;

                for (int i = 3; i >= 0; i--)
                {
                    FifteenBoard movedFifteenBoard = currentFifteenBoard.clone();

                    if(movedFifteenBoard.move(movementsOrder[i]))
                    {
                        if(maxObtainedRecursion < movedFifteenBoard.recursionLevel)
                            maxObtainedRecursion = movedFifteenBoard.recursionLevel;

                        visited++;
                        dfsList.push(movedFifteenBoard);
                    }
                }
            }
        }
        catch (Exception ex)
        { Logger.getLogger(Solver.class.getName()).log(Level.SEVERE, null, ex); }

        return NoSolution();
    }


    static boolean aStar(FifteenBoard startingFifteenBoard)
    {
        movementsOrder = new char[] {'U', 'R', 'D', 'L'};
        shuffleArray(movementsOrder);
        PriorityQueue<FifteenBoard> astList = new PriorityQueue<>();

        try
        {
            FifteenBoard solution = new FifteenBoard(startingFifteenBoard.N, startingFifteenBoard.M);

            startTime = System.nanoTime();
            astList.add(startingFifteenBoard);

            while (astList.size() > 0)
            {
                FifteenBoard currentFifteenBoard = astList.poll();
                processed++;

                assert currentFifteenBoard != null;
                if(currentFifteenBoard.isCorrect(solution))
                    return FoundSolution(currentFifteenBoard);

                for (int i = 0; i < 4; i++)
                {
                    FifteenBoard movedFifteenBoard = currentFifteenBoard.clone();

                    if(movedFifteenBoard.move(movementsOrder[i]))
                    {
                        if(maxObtainedRecursion < movedFifteenBoard.recursionLevel)
                            maxObtainedRecursion = movedFifteenBoard.recursionLevel;

                        visited++;
                        astList.add(movedFifteenBoard);
                    }
                }
            }
        }
        catch (Exception ex)
        { Logger.getLogger(Solver.class.getName()).log(Level.SEVERE, null, ex); }

        return NoSolution();
    }

    private static boolean FoundSolution(FifteenBoard fifteenBoard)
    {
        endTime = System.nanoTime();
        double delta = (endTime - startTime) / 1000000.0;
        Main.save(true, fifteenBoard.solutionString, visited, processed, maxObtainedRecursion, delta);

        return true;
    }

    private static boolean NoSolution()
    {
        endTime = System.nanoTime();
        double delta = (endTime - startTime) / 1000000.0;
        Main.save(false, "", visited, processed, maxObtainedRecursion, delta);

        System.out.println("Solution wasn't found");
        return false;
    }

    private static void shuffleArray(char[] array)
    {
        int index;
        char temp;
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--)
        {
            index = random.nextInt(i + 1);
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }
}
