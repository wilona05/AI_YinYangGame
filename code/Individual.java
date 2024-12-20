package code;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public class Individual {
    private int[][] board;
    private PuzzleQuestion puzzle;
    private int n;
    private double fitness;
    

    public Individual(int n, int seed){
        this.n = n;
        this.board = fillBoardRandom(seed);
    }

    public Individual(int n, int[][] board){
        this.n = n;
        this.board = board;
    }
	
    //mengisi board dgn random
    private int[][] fillBoardRandom(int seed) {
        Random r = new Random(seed);
        int[][] newBoard = new int[this.n][this.n];

        for(int i=0; i<this.n; i++){
            for(int j=0; j<this.n; j++){
                boolean cur = r.nextBoolean();
                if(cur == true){
                    newBoard[i][j] = 0;
                }else{
                    newBoard[i][j] = 1;
                }
            }
        }
        return newBoard;
    }

    //Hitung fitness board ini. Nilai fitness yang lebih rendah yang lebih baik
    //alpha : weight untuk jumlah area 2x2
    //beta  : weight untuk jumlah connected components
    public double countFitness(double ALPHA, double BETA) {
        int connectedComponents = countConnectedComponents();
        if (connectedComponents==1){
            return Double.MAX_VALUE;
        }
        int twoByTwo = countTwoByTwo();
        double fitness = (ALPHA*twoByTwo) + (BETA*connectedComponents);
        this.fitness = fitness;
        return fitness;
    }

    public int[][] getBoard(){
        return this.board;
    }


    //https://www.geeksforgeeks.org/java-program-to-save-a-string-to-a-file/
	public void printBoard(){
        String filename = "output.txt";

        String results = "";
	    for(int i=0; i<this.board.length; i++){
	        for(int j=0; j<this.board.length; j++){
	            results += board[i][j]+" ";
    	    }
    	    results += "\n";
	    }


        try(FileWriter writer = new FileWriter(filename)){
            writer.write(results);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

	}

    public int countTwoByTwo(){
        int count = 0;
        for(int i=0;i<n-1;i++){
            for (int j=0;j<n-1;j++){
                if (board[i][j]==board[i+1][j] && board[i][j]==board[i][j+1] && board[i][j]==board[i+1][j+1]){
                    count++;
                }
            }
        }
        return count;
    }

    public int countConnectedComponents(){
        int count = 0;
        boolean[][] visited = new boolean[n][n];

        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                if(!visited[i][j]){
                    dfs(visited, board[i][j], i, j);
                    count++;
                }
            }
        }
        return count;
    }

    private void dfs(boolean[][] visited, int currNum, int currRow, int currCol) {
        visited[currRow][currCol] = true;

        //bawah, atas, kanan, kiri
        int[] rowMoves = {1, -1, 0, 0};
        int[] colMoves = {0, 0, 1, -1};

        int newRow, newCol;
        for (int i=0;i<4;i++){
            newRow = currRow+rowMoves[i];
            newCol = currCol+colMoves[i];
            //jika posisi baru tidak di luar papan dan belum pernah dikunjungi, dan angka pada posisi baru sama dengan angka sekarang 
            if ((currRow+rowMoves[i]>=0) && (currRow+rowMoves[i]<n) && (currCol+colMoves[i]>=0) && (currCol+colMoves[i]<n) && (currNum == board[newRow][newCol]) && (!visited[newRow][newCol])){
                dfs(visited, currNum, newRow, newCol);
            }
        }        
    }

    public double getFitness() {
        return this.fitness;
    }

    
    
}
