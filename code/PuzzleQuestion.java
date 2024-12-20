package code;

import java.util.*;

public class PuzzleQuestion {
    private int n;
	private int[][] puzzle;

    public PuzzleQuestion(int n, int seed){
        this.n = n;
        this.puzzle = randomPuzzle(n, seed);
    }
	
    public int[][] getPuzzleQuestion(){
        return this.puzzle;
    }

	public void printPuzzleQuestion(){
	    for(int i=0; i <puzzle.length; i++){
	        for(int j=0; j <puzzle.length; j++){
	            System.out.print(puzzle[i][j]+" ");
    	    }
    	    System.out.println();
	    }
	}

    private int[][] randomPuzzle(int n, int seed){
        int[][] puzzle = new int[n][n];
        Random r = new Random(seed);
        int[] num = new int[n*n]; //untuk menyimpan yin, yang, dan kosong
        
        //0=yang, 1=yin, 2=kosong
        //jumlah awal yin dan yang berdasarkan ukuran papan
        int countYin = n-1;
        int countYang = n-1;
        
        //isi yin
        for(int i=0; i<countYin; i++){
            num[i] = 1;
        }

        //isi yang
        for(int i=countYin; i<countYin+countYang; i++){
            num[i] = 0;
        }

        //kosong
        for(int i=countYin+countYang; i<n*n; i++){
            num[i] = 2;
        }
        
        //acak urutan angka
        for(int i=0; i<n; i++){
            int j = r.nextInt(num.length);
            int temp = num[i];
            num[i] = num[j];
            num[j] = temp;
        }
        
        //isi puzzle
        int idx = 0;
        for(int i=0; i <n; i++){
	        for(int j=0; j <n; j++){
	            puzzle[i][j] = num[idx];
	            idx++;
    	    }
	    }
	    
	    return puzzle;
    }

    
}
