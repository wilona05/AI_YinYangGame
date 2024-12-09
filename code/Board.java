package code;

import java.util.*;

public class Board {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int [][] board = randomBoard(new int[n][n], n);
		printBoard(board);
	}
	
	public static void printBoard(int[][] board){
	    for(int i=0; i <board.length; i++){
	        for(int j=0; j <board.length; j++){
	            System.out.print(board[i][j]+" ");
    	    }
    	    System.out.println();
	    }
	}

    public static int[][] randomBoard(int[][] board, int n){
        Random r = new Random();
        int[] num = new int[n*n]; //untuk menyimpan yin, yang, dan kosong
        
        //0=kosong, 1=yin, 2=yang
        //jumlah awal yin dan yang berdasarkan ukuran papan
        int countYin = n-1;
        int countYang = n-1;
        
        //isi yin
        for(int i=0; i<countYin; i++){
            num[i] = 1;
        }

        //isi yang
        for(int i=countYin; i<countYin+countYang; i++){
            num[i] = 2;
        }
        
        //acak urutan angka
        for(int i=0; i<n; i++){
            int j = r.nextInt(num.length);
            int temp = num[i];
            num[i] = num[j];
            num[j] = temp;
        }
        
        //isi board
        int idx = 0;
        for(int i=0; i <n; i++){
	        for(int j=0; j <n; j++){
	            board[i][j] = num[idx];
	            idx++;
    	    }
	    }
	    
	    return board;
    }
}
