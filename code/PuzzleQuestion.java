package code;

public class PuzzleQuestion {
	private int[][] puzzle;
	
    public PuzzleQuestion(int[][] puzzleInput) {
        this.puzzle = puzzleInput;
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

    
}
