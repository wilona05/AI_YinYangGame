package code;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Individual {
    private int[][] board; //board yinyang
    private int n; //ukuran board
    private double fitness; //nilai fitness board
    
    //constructor
    public Individual(int n, int seed){
        this.n = n;
        this.board = fillBoardRandom(seed);
    }

    //constructor
    public Individual(int n, int[][] board){
        this.n = n;
        this.board = board;
    }
	
    //method untuk mengisi board dgn random
    private int[][] fillBoardRandom(int seed) {
        Random r = new Random(seed);
        int[][] newBoard = new int[this.n][this.n];

        for(int i=0; i<this.n; i++){
            for(int j=0; j<this.n; j++){
                boolean cur = r.nextBoolean();
                if(cur == true){
                    newBoard[i][j] = 1;
                }else{
                    newBoard[i][j] = 2;
                }
            }
        }
        return newBoard;
    }

    //method untuk menghitung fitness board. Nilai fitness yang lebih rendah = lebih baik
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

    //getter
    public int[][] getBoard(){
        return this.board;
    }

    //method untuk print board
	public void printBoard(){
        String filename = "output.txt";
        String result = "";
	    for(int i=0; i<this.board.length; i++){
	        for(int j=0; j<this.board.length; j++){
	            System.out.print(this.board[i][j]+" ");
                result += board[i][j] + " ";
    	    }
    	    System.out.println();
            result += "\n";
	    }

        //output ke file txt
        try(FileWriter writer = new FileWriter(filename)){
            writer.write(result);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
	}

    //method untuk menghitung banyak area 2x2
    public int countTwoByTwo(){
        int count = 0;
        boolean[][] visited = new boolean[n][n];
        for(int i=0;i<n-1;i++){
            for (int j=0;j<n-1;j++){
                if (!visited[i][j] && board[i][j]==board[i+1][j] && board[i][j]==board[i][j+1] && board[i][j]==board[i+1][j+1]){
                    count++;
                }
            }
        }
        return count;
    }

    //method untuk menandai area 2x2 yang sudah divisit
    public void markVisited(boolean[][] visited, int i, int j){
        visited[i][j] = true;
        visited[i+1][j] = true;
        visited[i][j+1] = true;
        visited[i+1][j+1] = true;
    }

    //method untuk menghitung banyak connected graph
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
        if(count == 2) return 0; //solusi yang valid
        return count;
    }

    //method untuk menelusuri board secara dfs
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

    //getter
    public double getFitness() {
        return this.fitness;
    }
}
