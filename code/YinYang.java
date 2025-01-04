package code;

import java.util.Collections;
import java.util.Scanner;

public class YinYang {
    static final int populationSize = 10; //ukuran populasi
    static final int maxGeneration = 1000; //maksimal generasi
    static final double alpha = 0.7; //weight untuk jumlah area 2x2
    static final double beta = 0.3;  //weight untuk jumlah connected components
    
    public static void main(String[] args) {
        //input
		Scanner sc = new Scanner(System.in);
        System.out.print("n: ");
		int n = sc.nextInt(); //ukuran papan nxn
        System.out.print("seed: ");
        int seed = sc.nextInt(); //untuk urutan angka acak

        //GA
        //Inisialisasi
        Population population = new Population(populationSize, n, seed);
        population.randomPopulation(); //generate random individual
        int generation = 0;
        boolean solutionFound = false;
        Individual bestIndividual;

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
            bestIndividual.printBoard();

            //cek apakah bestIndividual adalah solusi yang valid
            if(bestIndividual.getFitness() == 0){
                System.out.println("Solution found in generation " + generation);
                solutionFound = true;
                break;
            }

            //generasi lama (elitism 50%)
            Population newPopulation = new Population(populationSize, n, seed);
            for(int i=0; i<populationSize/2; i++){
                newPopulation.individuals.add(population.individuals.get(i));
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
                newPopulation.mutate(offspring);
                
                //masukkan individu baru
                newPopulation.individuals.add(offspring);
            }

            population = newPopulation;
            generation++;
        }

        if(!solutionFound) { //solusi valid tidak ditemukan
            System.out.println("No solution found within the maximum generations.");
        }
    }
}
