package code;

import java.util.Collections;
import java.util.Random;

public class YinYang {
    static final double ALPHA = 0.7; //weight untuk jumlah area 2x2
    static final double BETA = 0.3;  //weight untuk jumlah connected components
    public int popSize = 10; //ukuran populasi
    public int maxGen = 1000; //maksimal generasi
    public double mutationRate = 0.9; //probabilitas untuk mutasi
    public double coolingRate = 0.99; //kontrol penurunan suhu
    Random rand;
    public int n;
    public PuzzleQuestion puzzleQuestion;

    public YinYang(int popsize, int maxGen, double mutationRate, double coolingRate, Random rand, int n, PuzzleQuestion puzzleQuestion){
        this.popSize = popsize;
        this.maxGen = maxGen;
        this.mutationRate = mutationRate;
        this.coolingRate = coolingRate;
        this.rand = rand;
        this.n = n;
        this.puzzleQuestion = puzzleQuestion;
    }

    public Individual runGA(){
        //GA
        //Inisialisasi Populasi
        Population population = new Population(popSize, n, rand, puzzleQuestion);
        population.randomPopulation(); //generate populasi awal yang berisi individual random

        int generation = 0; //banyak generasi saat ini
        boolean solutionFound = false; //penanda apakah solusi valid sudah ditemukan
        Individual bestIndividual = null; //individual dengan fitness terbaik (terendah)
        double initialTemp = 100.0; //suhu awal
        double temperature = initialTemp; //nilai suhu yang akan menurun tiap iterasi 

        while(generation<maxGen && !solutionFound){
            //hitung ulang fitness setiap individual
            for(Individual individual : population.individuals){
                individual.countFitness(ALPHA, BETA);
                // System.out.print(individual.getFitness()+" ");
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
                return bestIndividual;
            }

            //generasi lama
            Population newPopulation = new Population(popSize, n, rand, puzzleQuestion);
            // for (int i = 0; i < POP_SIZE / 2; i++) { //elitism 50%
            //     newPopulation.individuals.add(population.individuals.get(i));
            // }
            for(int i=0; i<popSize; i++){
                Individual individual = population.individuals.get(i);
                double probability = Math.exp(-individual.getFitness()/temperature);

                //pilih individu terbaik
                if(rand.nextDouble() < probability || i==0){
                    newPopulation.individuals.add(individual);
                    if(newPopulation.individuals.size() >= popSize/2){
                        break;
                    }
                }
            }

            //generasi baru
            while(newPopulation.individuals.size() < popSize){
                //selection
                Individual[] parents = newPopulation.selectParent();
                
                //crossover
                Individual offspring = newPopulation.crossover(parents[0], parents[1]);
                offspring.countFitness(ALPHA, BETA); //hitung fitness
    
                //mutasi
                if (rand.nextDouble() < mutationRate) { //Math.random() mengembalikan angka di antara 0 sampai 1
                    offspring.mutate();
                }
                //masukkan individu baru
                newPopulation.individuals.add(offspring);
            }

            //turunkan suhu
            temperature *= coolingRate;
            if (temperature < 0.1){
                temperature = 0.1;
            }
            
            population = newPopulation; //update populasi
            generation++; //tambah generasi
        }

        if(!solutionFound) { //solusi valid tidak ditemukan
            System.out.println("No solution found within the maximum generations.");
        }   
        return bestIndividual;
    }
}