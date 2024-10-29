package gol;

// count the number of neighbours
public class Evaluator {
    
    public static int countNeighbours(int x, int y, char[][] board){
        int star = 0;
        int height = board.length;
        int width = board[0].length;
        
        for (int i = 0; i < Constants.X_DELTA.length; i ++){
            // get the position to examine
            int lx = x + Constants.X_DELTA[i];
            int ly = y + Constants.Y_DELTA[i];
            
            // check if position is within board
            if ((lx < 0) || (lx >= width))
                continue;
            if ((ly < 0) || (ly >= width))
                continue;
            if (board[ly][lx] == Constants.STAR)
                star ++;
        }

        return star;
    }
}
