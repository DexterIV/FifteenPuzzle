package projectFifteen;

import java.util.logging.Level;
import java.util.logging.Logger;

public class FifteenBoard implements Comparable<FifteenBoard>
{
    private int[][] board;
    private int X, Y;
    int N, M;
    int recursionLevel;
    String solutionString = "";
    private char lastMove;

    private int heuristicType;
    private int heuristicValue;

    FifteenBoard(int N, int M, int[][] tab, String type) throws Exception
    {
        if (N > 1 && M > 1 && tab.length == N && tab[0].length == M)
        {
            this.board = tab;
            int[] zero = this.find(0);
            assert zero != null;
            this.X = zero[1];
            this.Y = zero[0];
            this.N = N;
            this.M = M;
            this.recursionLevel = 0;
        }
        else
            throw new Exception("Wrong data");
        this.heuristicValue = 0;

        switch (type) {
            case "hamm":
                this.heuristicType = 1;
                break;
            case "manh":
                this.heuristicType = 2;
                break;
            case "own":
                this.heuristicType = 3;
                break;
        }
    }

    FifteenBoard(int N, int M, int[][] tab) throws Exception
    {
        if (N > 1 && M > 1 && tab.length == N && tab[0].length == M)
        {
            this.board = tab;
            int[] zero = this.find(0);
            assert zero != null;
            this.X = zero[1];
            this.Y = zero[0];
            this.N = N;
            this.M = M;
            this.recursionLevel = 0;
        }
        else
            throw new Exception("Wrong data");
    }

    FifteenBoard(int N, int M) throws Exception
    {
        if (N > 1 && M > 1)
        {
            this.board = new int[N][M];

            int number = 1;
            for (int[] table : this.board)
            {
                for (int i = 0; i < table.length; i++)
                {
                    table[i] = number;
                    number++;
                }
            }
            this.board[N - 1][M - 1] = 0;

            this.X = N-1;
            this.Y = M-1;
            this.N = N;
            this.M = M;

            this.recursionLevel = 0;
        }
        else
            throw new Exception("Wrong data");
    }

    private int[] find(int number)
    {
        for (int i = 0; i < this.board.length; ++i)
        {
            for (int j = 0; j < this.board[i].length; ++j)
            {
                if (this.board[i][j] == number)
                {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    boolean move(char direction)
    {
        if (direction != 'L' && direction != 'U' && direction != 'D' && direction != 'R')
        {
            System.out.println("Illegal move!");
            return false;
        }

        if (direction == 'L' && this.X > 0 && lastMove != 'R')
        {
            int a = this.board[this.Y][this.X];
            this.board[this.Y][this.X] = this.board[this.Y][this.X - 1];
            this.board[this.Y][this.X - 1] = a;
            this.X--;
            this.calculateHeuristic();
            return checkMove(direction);
        }

        if (direction == 'R' && this.X < this.M - 1 && lastMove != 'L')
        {
            int a = this.board[this.Y][this.X];
            this.board[this.Y][this.X] = this.board[this.Y][this.X + 1];
            this.board[this.Y][this.X + 1] = a;
            this.X++;
            this.calculateHeuristic();
            return checkMove(direction);
        }

        if (direction == 'U' && this.Y > 0 && lastMove != 'D')
        {
            int a = this.board[this.Y][this.X];
            this.board[this.Y][this.X] = this.board[this.Y - 1][this.X];
            this.board[this.Y - 1][this.X] = a;
            this.Y--;
            this.calculateHeuristic();
            return checkMove(direction);
        }

        if (direction == 'D' && this.Y < this.N - 1 && lastMove != 'U')
        {
            int a = this.board[this.Y][this.X];
            this.board[this.Y][this.X] = this.board[this.Y + 1][this.X];
            this.board[this.Y + 1][this.X] = a;
            this.Y++;
            this.calculateHeuristic();
            return checkMove(direction);
        }

        return false;
    }

    private void calculateHeuristic()
    {
        this.heuristicValue = 0;

        switch (heuristicType)
        {
            case 1: //hamming
            {
                for (int i = 0; i < this.board.length; i++)
                {
                    for (int j = 0; j < this.board[i].length; j++)
                    {
                        if (this.board[i][j] != ((j + 1) + (i * this.N)) % (this.N * this.M) && this.board[i][j] != 0)
                            this.heuristicValue++;
                    }
                }
                break;
            }

            case 2: //manhattan
            {
                int count = 1;

                for (int i = 0; i < this.board.length; i++)
                {
                    for (int j = 0; j < this.board[i].length; j++)
                    {
                        if (count == N * M)
                            continue;

                        int[] at = find(count);
                        assert at != null;
                        this.heuristicValue += Math.abs(i - at[0]) + Math.abs(j - at[1]);
                        count++;
                    }
                }
                break;
            }
            case 3: //own
            {
                int count = 1;

                for (int i = 0; i < this.board.length; i++)
                {
                    for (int j = 0; j < this.board[i].length; j++)
                    {
                        if (count == N * M)
                            continue;

                        int[] at = find(count);
                        assert at != null;
                        this.heuristicValue += Math.abs(i - at[0]) + Math.abs(j - at[1])
                                + ((this.board[i][j] != ((j + 1) + (i * this.N)) % (this.N * this.M) && this.board[i][j] != 0) ? 1 : 0);
                        count++;
                    }
                }
                break;
            }
            default:
                break;
        }
    }

    @Override
    public int compareTo(FifteenBoard other)
    {
        int thisFifteen = this.recursionLevel + this.heuristicValue;
        int otherFifteen = other.recursionLevel + other.heuristicValue;

        return Integer.compare(thisFifteen, otherFifteen);
    }


    private boolean checkMove(char direction)
    {
        lastMove = direction;
        this.recursionLevel++;
        this.solutionString += direction;

        if(solutionString.length() > 12)
        {
            String one = solutionString.substring(solutionString.length() - 12, solutionString.length() - 8);
            String two = solutionString.substring(solutionString.length() - 8, solutionString.length() - 4);
            String three = solutionString.substring(solutionString.length() - 4);

            return !one.equals(two) || !one.equals(three);
        }

        return true;
    }

    boolean isCorrect(FifteenBoard correctFifteenBoard)
    {

        for (int i = 0; i < this.board.length; i++)
        {
            for (int j = 0; j < this.board[i].length; j++)
            {
                if (this.board[i][j] != correctFifteenBoard.board[i][j])
                {
                    return false;
                }

            }
        }
        return true;
    }

    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        for (int[] aBoard : this.board) {
            for (int anABoard : aBoard) {
                if (anABoard == 0)
                    stringBuilder.append(" X");

                else if (anABoard < 10) {
                    stringBuilder.append(" ").append(anABoard);
                } else
                    stringBuilder.append(anABoard).append(" ");
            }
            stringBuilder.append("\n");
        }
        stringBuilder.append("Cost: ").append(this.recursionLevel).append(" HVal: ").append(this.heuristicValue);
        return stringBuilder.toString();
    }

    @Override
    public FifteenBoard clone()
    {
        FifteenBoard newFifteenBoard = null;

        try
        {
            newFifteenBoard = new FifteenBoard(this.N, this.M);

            for (int i = 0; i < this.board.length; i++)
            {
                System.arraycopy(this.board[i], 0, newFifteenBoard.board[i], 0, this.board[i].length);
            }

            newFifteenBoard.X = this.X;
            newFifteenBoard.Y = this.Y;
            newFifteenBoard.recursionLevel = this.recursionLevel;
            newFifteenBoard.solutionString = this.solutionString;
            newFifteenBoard.lastMove = this.lastMove;

            newFifteenBoard.heuristicType = this.heuristicType;
            newFifteenBoard.heuristicValue = this.heuristicValue;
        }
        catch (Exception ex)
        { Logger.getLogger(FifteenBoard.class.getName()).log(Level.SEVERE, null, ex); }

        return newFifteenBoard;
    }

}