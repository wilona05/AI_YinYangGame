package code;

import java.io.FileReader;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class YinYang {
    static final int POP_SIZE = 10; //ukuran populasi
    static final int MAX_GEN = 1000; //maksimal generasi
    static final double ALPHA = 0.7; //weight untuk jumlah area 2x2
    static final double BETA = 0.3;  //weight untuk jumlah connected components
    static final double MUTATION_RATE = 0.9; //probabilitas untuk mutasi
    static final double COOLING_RATE = 0.99; //kontrol penurunan suhu

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
        Population population = new Population(POP_SIZE, n, rand, puzzleQuestion);
        population.randomPopulation(); //generate populasi awal yang berisi individual random

        int generation = 0; //banyak generasi saat ini
        boolean solutionFound = false; //penanda apakah solusi valid sudah ditemukan
        Individual bestIndividual; //individual dengan fitness terbaik (terendah)
        double initialTemp = 100.0; //suhu awal
        double temperature = initialTemp; //nilai suhu yang akan menurun tiap iterasi 

        while(generation<MAX_GEN && !solutionFound){
            //hitung ulang fitness setiap individual
            for(Individual individual : population.individuals){
                individual.countFitness(ALPHA, BETA);
                System.out.print(individual.getFitness()+" ");
            }
            System.out.println();

            //mengurutkan individu dalam populasi berdasarkan fitness (menaik)
            Collections.sort(population.individuals, (i1, i2) -> Double.compare(i1.getFitness(), i2.getFitness()));

            //tampilkan individu terbaik dalam populasi
            bestIndividual = population.individuals.get(0);
            System.out.println("Generation: " + generation);
            System.out.println("Fitness: " + bestIndividual.getFitness());
            bestIndividual.printBoard(generation, bestIndividual.getFitness());
            System.out.println();

            //cek apakah bestIndividual adalah solusi yang valid
            if(bestIndividual.getFitness() == 0){
                System.out.println("Solution found in generation " + generation);
                solutionFound = true;
                break;
            }

            //generasi lama
            Population newPopulation = new Population(POP_SIZE, n, rand, puzzleQuestion);
            // for (int i = 0; i < POP_SIZE / 2; i++) { //elitism 50%
            //     newPopulation.individuals.add(population.individuals.get(i));
            // }
            for(int i=0; i<POP_SIZE; i++){
                Individual individual = population.individuals.get(i);
                double probability = Math.exp(-individual.getFitness()/temperature);

                //pilih individu terbaik
                if(rand.nextDouble() < probability || i==0){
                    newPopulation.individuals.add(individual);
                    if(newPopulation.individuals.size() >= POP_SIZE/2){
                        break;
                    }
                }
            }

            //generasi baru
            while(newPopulation.individuals.size() < POP_SIZE){
                //selection
                Individual parent1 = newPopulation.selectParent();
                Individual parent2 = newPopulation.selectParent();
                
                //crossover
                Individual offspring = newPopulation.crossover(parent1, parent2);
                offspring.countFitness(ALPHA, BETA); //hitung fitness
    
                //mutasi
                if (rand.nextDouble() < MUTATION_RATE) { //Math.random() mengembalikan angka di antara 0 sampai 1
                    newPopulation.mutate(offspring);
                }
                //masukkan individu baru
                newPopulation.individuals.add(offspring);
            }

            //turunkan suhu
            temperature *= COOLING_RATE;
            if (temperature < 0.1){
                temperature = 0.1;
            }
            
            population = newPopulation; //update populasi
            generation++; //tambah generasi
        }

        if(!solutionFound) { //solusi valid tidak ditemukan
            System.out.println("No solution found within the maximum generations.");
        }
    }
}