package code;

import java.io.FileReader;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class YinYang {
    static final int populationSize = 10; //ukuran populasi
    static final int maxGeneration = 1000; //maksimal generasi
    static final double alpha = 0.7; //weight untuk jumlah area 2x2
    static final double beta = 0.3;  //weight untuk jumlah connected components
    static final double mutationRate = 0.9;    //probabilitas untuk mutasi
    static final double coolingRate = 0.99;

    public static void main(String[] args) {
        //Input
        int n = 1, seed = 1;
        PuzzleQuestion puzzleQuestion = null;
        try (Scanner sc = new Scanner(new FileReader("input.txt"))) {
            n = sc.nextInt(); //ukuran papan nxn            
            seed = sc.nextInt(); //untuk urutan angka acak
        
            // input papan puzzle
            int[][] puzzleInput = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    puzzleInput[i][j] = sc.nextInt();
                }
            }
            puzzleQuestion = new PuzzleQuestion(puzzleInput);        
        } catch (Exception e) {
            System.err.println("Error membaca input file: " + e.getMessage());
        }

        //GA
        //Inisialisasi Populasi
        Random rand = new Random(seed);
        Population population = new Population(populationSize, n, rand, puzzleQuestion.getPuzzleQuestion());
        population.randomPopulation(); //generate populasi awal yang berisi individual random
        int generation = 0;
        boolean solutionFound = false;
        Individual bestIndividual;
        double initialTemp = 100.0; //suhu awal
        double temperature = initialTemp;

        while(generation<maxGeneration && !solutionFound){
            //hitung ulang fitness setiap individual
            for(Individual individual : population.individuals){
                individual.countFitness(alpha, beta);
                System.out.print(individual.getFitness()+" ");
            }
            System.out.println();

            //mengurutkan individu dalam populasi berdasarkan fitness (menaik)
            Collections.sort(population.individuals, (i1, i2) -> Double.compare(i1.getFitness(), i2.getFitness()));

            bestIndividual = population.individuals.get(0);
            System.out.println("Generation: " + generation);
            System.out.println("Fitness: " + bestIndividual.getFitness());
            bestIndividual.printBoard(generation, bestIndividual.getFitness());

            //cek apakah bestIndividual adalah solusi yang valid
            if(bestIndividual.getFitness() == 0){
                System.out.println("Solution found in generation " + generation);
                solutionFound = true;
                break;
            }

            //generasi lama (elitism 50%)
            Population newPopulation = new Population(populationSize, n, rand, puzzleQuestion.getPuzzleQuestion());
            // for (int i = 0; i < populationSize / 2; i++) {
            //     newPopulation.individuals.add(population.individuals.get(i));
            // }
            for(int i=0; i<populationSize; i++){
                Individual individual = population.individuals.get(i);
                double probability = Math.exp(-individual.getFitness()/temperature);

                //pilih individu terbaik
                if(rand.nextDouble() < probability || i==0){
                    newPopulation.individuals.add(individual);
                    if(newPopulation.individuals.size() >= populationSize/2){
                        break;
                    }
                }
            }

            //generasi baru
            while(newPopulation.individuals.size() < populationSize){
                //selection
                Individual parent1 = newPopulation.selectParent();
                Individual parent2 = newPopulation.selectParent();
                
                //crossover
                Individual offspring = newPopulation.crossover(parent1, parent2);
                offspring.countFitness(alpha, beta); //hitung fitness
    
                //mutasi
                if (rand.nextDouble() < mutationRate) { //Math.random() mengembalikan angka di antara 0 sampai 1
                    newPopulation.mutate(offspring);
                }
                //masukkan individu baru
                newPopulation.individuals.add(offspring);
            }
            temperature *= coolingRate;
            if (temperature <0.1){
                temperature = 0.1;
            }
            
            population = newPopulation;
            generation++;
        }

        if(!solutionFound) { //solusi valid tidak ditemukan
            System.out.println("No solution found within the maximum generations.");
        }
    }
}