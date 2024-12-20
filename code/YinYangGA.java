package code;

import java.util.Scanner;

public class YinYangGA {


    public static void main(String[] args) {
        //input
		Scanner sc = new Scanner(System.in);
        System.out.print("n: ");
		int n = sc.nextInt();
        System.out.print("seed: ");
        int seed = sc.nextInt();
        System.out.print("population size: ");
        int popSize = sc.nextInt();

        //Proses
        //Membuat soal puzzle YinYang
        PuzzleQuestion puzzle = new PuzzleQuestion(n, seed);
        System.out.println("Puzzle Question (0=empty, 1=yin, 2=yang):");
		puzzle.printPuzzleQuestion();

        //GA
        //Inisialisasi
        Population population = new Population();
        Individual fittest;
        int generationCount = 0;
        final int LIMIT = 1000;
        population.initializePopulation(n, popSize, seed);

        final double ALPHA = 0.7; //weight untuk jumlah area 2x2
        final double BETA = 0.3;  //weight untuk jumlah connected components
        population.calculateFitness(ALPHA, BETA);

        //Proses GA
        while (population.bestFitness>0 && generationCount<=LIMIT){
            generationCount++;

            //selection

            //crossover

            //mutasi

            //elitism

            //calculate fitness generasi ini
            population.calculateFitness(ALPHA, BETA);

            //output Individual terbaik di generasi ini
            // System.out.println("Generation: " + generationCount + " Fittest:");
            // fittest = population.getFittest();
            // fittest.printBoard();
            // System.out.println("Fitness: " + fittest.getFitness());
        }

        //Output hasil GA
        System.out.println("Generation: " + generationCount + " Fittest Individual:");
        fittest = population.getFittest();
        fittest.printBoard();
        System.out.println("Fitness: " + fittest.getFitness());
	}
}
