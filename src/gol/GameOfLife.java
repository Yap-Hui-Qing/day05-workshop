package gol;

import java.io.*;

public class GameOfLife {
    private final String golFile;

    private char[][] board = null;
    private int width = 0;
    private int height = 0;
    private int offsetX = 0;
    private int offsetY = 0;

    private int generation = 0;

    public GameOfLife (String gol){
        golFile = gol;
    }

    public GameOfLife (char[][] b){
        this.golFile = "";
        this.board = b;
        this.height = b.length;
        this.width = b[0].length;
    }

    public int getGeneration() {return generation;}
    public void setGeneration(int generation) {this.generation = generation;}

    public int getWidth() {return width;}
    public int getHeight() {return height;}

    // read a text file
    public void readFile() throws Exception{
        Reader fis = new FileReader(golFile);
        BufferedReader br = new BufferedReader(fis);
        String line;

        while ((line = br.readLine()) != null){
            line = line.trim();
            // System.out.println(line);

            if (line.startsWith(Constants.COMMENTS))
                continue;
            
            String[] terms = line.split(" ");
            if (terms[0].equals(Constants.GRID)){
                width = Integer.parseInt(terms[1]);
                height = Integer.parseInt(terms[2]);
                board = new char[height][width];
            } else if (terms[0].equals(Constants.START)){
                offsetX = Integer.parseInt(terms[1]);
                offsetY = Integer.parseInt(terms[2]);

            } else if (terms[0].equals(Constants.DATA)){
                // set all locations in the board to blank
                board = initializeBoard(width, height);
                populateBoard(br);
                return;
            }
        }
    }

    private char[][] initializeBoard(int width, int height){
        char[][] board = new char[height][width];
        for (int y = 0; y<height; y++){
            board[y] = Constants.BLANK.substring(0,width).toCharArray();
        }
        return board;
    }

    private void populateBoard(BufferedReader br) throws Exception{
        String line;
        int y = offsetY;
        while ((line = br.readLine()) != null){
            char[] data = line.toCharArray();
            for (int x = 0; x < data.length; x++){
                board[y][x + offsetX] = data[x];
            }
            y ++;
        }
    }

    public GameOfLife nextGeneration(){
        char[][] nextGboard = initializeBoard(width, height);

        // cycle through every location on the board
        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                int neighbours = Evaluator.countNeighbours(x, y, board);
                if (board[y][x] == Constants.STAR){
                    // die of starvation
                    if (neighbours <= 1){
                        nextGboard[y][x] = Constants.DIE;
                    } else if (neighbours >= 2 && neighbours <= 3){
                        nextGboard[y][x] = board[y][x];
                    } else if (neighbours >= 4) {
                        // die of overpopulation
                        nextGboard[y][x] = Constants.DIE;
                    }
                } else {
                    if (neighbours == 3){
                        nextGboard[y][x] = Constants.STAR;
                    }
                }
            }
        }

        GameOfLife nextGol = new GameOfLife(nextGboard);
        nextGol.setGeneration(generation + 1);

        return nextGol;

    }

    public void printBoard(){
        System.out.printf("Generation %d\n", generation);
        for (int i = 0; i < height; i++){
            System.out.printf("|%s|\n", new String(board[i]));
        }
    }
}
