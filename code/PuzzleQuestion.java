package code;

public class PuzzleQuestion {
	private int[][] puzzle; //board YinYang
	
	//constructor
    public PuzzleQuestion(int[][] puzzleInput) {
        this.puzzle = puzzleInput;
    }

	//getter
    public int[][] getPuzzleQuestion(){
        return this.puzzle;
    }

	//method untuk menampilkan board YinYang
	public void printPuzzleQuestion(){
	    for(int i=0; i <puzzle.length; i++){
	        for(int j=0; j <puzzle.length; j++){
	            System.out.printf("%4d", puzzle[i][j]);
    	    }
    	    System.out.println();
	    }
	}
}