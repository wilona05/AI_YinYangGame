package code;

import java.util.Random;
import java.util.Scanner;

public class YinYangGA {


    public static void main(String[] args) {
        //input
		Scanner sc = new Scanner(System.in);
        System.out.print("n: ");
		int n = sc.nextInt();
        System.out.print("seed: ");
        int seed = sc.nextInt();

        //Proses
        //Membuat soal puzzle YinYang
        PuzzleQuestion puzzle = new PuzzleQuestion(n, seed);
        System.out.println("Puzzle Question (0=yang, 1=yin, 2=empty):");
		puzzle.printPuzzleQuestion();

        //GA
        //Inisialisasi
        Population population = new Population();
        Individual fittest;
        int generationCount = 0;
        
        final int POPSIZE = 10;
        final int LIMIT = 10;
        final double ALPHA = 0.7; //weight untuk jumlah area 2x2
        final double BETA = 0.3;  //weight untuk jumlah connected components

        population.initializePopulation(n, POPSIZE, seed);
        population.calculateFitness(ALPHA, BETA);

        //Proses GA
        while (population.bestFitness>0 && generationCount<=LIMIT){
            
            //output Individual terbaik di generasi ini
            System.out.println("Generation: " + generationCount);
            fittest = population.getFittest();
            fittest.printBoard();
            System.out.println("Fitness: " + fittest.getFitness());
            
            if(fittest.getFitness() == 0){ //cek apakah solusi sudah ditemukan
                System.out.println("Solution found in generation " + generationCount);
                break;
            }

            //selection
            Individual parent1 = selectParent(population);
            Individual parent2 = selectParent(population);
            
            //crossover
            Individual offspring = crossover(parent1, parent2);

            //mutasi
            mutate(offspring);
            offspring.countFitness(ALPHA, BETA);
            
            //elitism
            population.addIndividual(offspring);
        
            //hitung ulang fitness
            population.calculateFitness(ALPHA, BETA);
            generationCount++;   
        }
        
        //Output hasil GA
        System.out.println("Generation: " + generationCount);
        fittest = population.getFittest();
        fittest.printBoard();
        System.out.println("Fitness: " + fittest.getFitness());

	}

    //roulette wheel
    public static Individual selectParent(Population population){
        double totalFitness = 0;
        for(Individual individual : population.individuals){
            totalFitness += 1.0 / (individual.getFitness() + 1);
        }

        double random = Math.random() * totalFitness;
        double cumulativeFitness = 0;

        for(Individual individual : population.individuals){
            cumulativeFitness += 1.0 / (individual.getFitness() + 1);
            if(cumulativeFitness >= random){
                return individual;
            }
        }
        return population.individuals[population.popSize-1];
    }

    public static Individual crossover(Individual parent1, Individual parent2){
        int n = parent1.getBoard().length;
        Random random = new Random();
        int crossoverPoint = random.nextInt(n);

        int[][] offspringBoard = new int[n][n];
        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                if(j < crossoverPoint){
                    offspringBoard[i][j] = parent1.getBoard()[i][j];
                }else{
                    offspringBoard[i][j] = parent2.getBoard()[i][j];
                }
            }
        }
        return new Individual(n, offspringBoard);
    }

    public static void mutate(Individual individual){
        Random random = new Random();
        int i = random.nextInt(individual.getBoard().length);
        int j = random.nextInt(individual.getBoard().length);
       
        if(random.nextBoolean()){
            individual.getBoard()[i][j] = 0;
        }else{
            individual.getBoard()[i][j] = 1;
        }
    }
}
