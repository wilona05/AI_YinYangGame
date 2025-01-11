package code;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Individual {
    private int[][] board; //board yinyang
    private int n; //ukuran board
    private double fitness; //nilai fitness board
    private PuzzleQuestion puzzleQuestion; //board yinyang awal (soal yang akan disolve)
    private Random rand;
    public double parentProbability; //probabilitas parent

    //constructor dengan board random(untuk inisialisasi populasi)
    public Individual(int n, Random rand, PuzzleQuestion puzzleQuestion){
        this.n = n;
        this.puzzleQuestion = puzzleQuestion;
        this.rand = rand;
        this.board = fillBoardRandom();
        this.parentProbability = 0;
    }

    //constructor dengan input board
    public Individual(int n, Random rand, PuzzleQuestion puzzleQuestion, int[][] board){
        this.n = n;
        this.board = board;
        this.puzzleQuestion = puzzleQuestion;
        this.rand = rand;
    }
	
    //method untuk mengisi board dgn random
    private int[][] fillBoardRandom() {
        int[][] newBoard = new int[this.n][this.n];
        int[][] puzzle = puzzleQuestion.getPuzzleQuestion();

        for(int i=0; i<this.n; i++){
            for(int j=0; j<this.n; j++){
                //jika posisi ini di soal adalah '-1' atau -2 (fixed yin/yang), copy dari soal
                if (puzzle[i][j] == -1 || puzzle[i][j] == -2) {
                    newBoard[i][j] = puzzle[i][j];
                } 
                //jika posisi ini di soal adalah '0' (kosong), maka randomize angka pada posisi ini menjadi 1 atau 2 (changeable yin/yang)
                else {
                    newBoard[i][j] = rand.nextBoolean() ? 1 : 2;
                }
            }
        }
        return newBoard;
    }

    //method untuk menghitung fitness board. Nilai fitness yang lebih rendah = lebih baik
    //alpha : weight untuk jumlah area 2x2
    //beta  : weight untuk jumlah connected components
    //(https://www.geeksforgeeks.org/number-of-connected-components-in-a-2-d-matrix-of-strings/)
    public double countFitness(double alpha, double beta) {
        int connectedComponents = countConnectedComponents(); //hitung connected components 
        if (connectedComponents==1){ //solusi terburuk
            return Double.MAX_VALUE;
        }else if(connectedComponents == 2){ //connectedComponent yang tepat
            connectedComponents = 0;
        } 
        int twoByTwo = countTwoByTwo(); //hitung banyak area 2x2
        double fitness = (alpha*twoByTwo) + (beta*connectedComponents); //hitung fitness
        this.fitness = fitness;
        return fitness;
    }

    //getter
    public int[][] getBoard(){
        return this.board;
    }


    //method untuk print board (https://www.geeksforgeeks.org/java-program-to-save-a-string-to-a-file/)
	public void printBoard(int generation, double fitness){
        String filename = "output.txt";
        String result = "";
        String format = "";
	    for(int i=0; i<this.board.length; i++){
	        for(int j=0; j<this.board.length; j++){
	            System.out.printf("%4d", this.board[i][j]);
                format = String.format("%4d", this.board[i][j]);
                result += format;
    	    }
    	    System.out.println();
            result += "\n";
	    }

        if (fitness > 0){ //solusi valid tidak ditemukan, output solusi terbaik
            result += "\nNo solution found within the maximum generations.\nGeneration: " + generation;
            result += "\nFitness: " + fitness;
        }
        else{ //solusi valid ditemukan
            result += "\nSolution found.\nGeneration: " + generation;
            result += "\nFitness: " + fitness;
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
        for(int i=0;i<n-1;i++){
            for (int j=0;j<n-1;j++){
                int topLeft = Math.abs(board[i][j]);
                int topRight = Math.abs(board[i][j+1]);
                int botLeft = Math.abs(board[i+1][j]);
                int botRight = Math.abs(board[i+1][j+1]);
                if (topLeft==topRight && topLeft==botLeft && topLeft==botRight){
                    count++;
                }
            }
        }
        return count;
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
            //jika posisi baru tidak di luar papan dan belum pernah dikunjungi, 
            //dan angka pada posisi baru sama dengan angka sekarang (fixed & changeable dianggap sama)
            if ((currRow+rowMoves[i]>=0) && (currRow+rowMoves[i]<n) && (currCol+colMoves[i]>=0) && (currCol+colMoves[i]<n) && (currNum == board[newRow][newCol] || currNum*-1 == board[newRow][newCol]) && (!visited[newRow][newCol])){
                dfs(visited, currNum, newRow, newCol);
            }
        }        
    }

    //getter
    public double getFitness() {
        return this.fitness;
    }

    //method untuk perubahan acak gen individu (meningkatkan keberagaman individu)
    public void mutate(){
        int i, j;
        //randomize posisi (row & column) yang akan dimutasi
        //jika pada posisi itu adalah -1 atau -2 (fixed yin/yang), maka randomize lagi 
        do{
            i = rand.nextInt(this.board.length);
            j = rand.nextInt(this.board[0].length);
        } while (this.board[i][j] == -1 || this.board[i][j] == -2);
       
        //mutasi (flip yin->yang, atau yang->yin)
        if(this.board[i][j] == 1){
            this.board[i][j] = 2;
        }else{
            this.board[i][j] = 1;
        }
    }
}