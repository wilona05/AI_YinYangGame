package code;

import java.util.ArrayList;
import java.util.Random;

public class Population {
    public ArrayList<Individual> individuals;
    private int maxSize; //banyak individu maks dalam populasi
    private int curPopulationSize; //banyak individu dalam populasi saat ini
    private int boardSize; //ukuran papan yinyang
    private int seed; //untuk randomization

    //constructor
    public Population(int maxSize, int boardSize, int seed){
        this.individuals = new ArrayList<>();
        this.maxSize = maxSize;
        this.curPopulationSize = 0;
        this.boardSize = boardSize;
        this.seed = seed;
    }

    //method untuk mengenerate populasi awal secara acak
    public void randomPopulation(){
        for(int i=0; i<this.maxSize; i++){
            if(this.curPopulationSize < this.maxSize){ 
                this.individuals.add(new Individual(this.boardSize, this.seed+i));
                this.curPopulationSize++;
            }
        }
    }

    //method seleksi menggunakan roulette wheel
    public Individual selectParent(){
        double totalFitness = 0;

        //hitung total fitness (meminimalkan: fitness makin kecil, peluang makin besar)
        for(Individual individual : this.individuals){
            totalFitness += 1.0 / (individual.getFitness() + 1); 
        }

        double random = Math.random() * totalFitness;
        double cumulativeFitness = 0;

        //pilih individu
        for(Individual individual : this.individuals){
            cumulativeFitness += 1.0 / (individual.getFitness() + 1);
            if(cumulativeFitness >= random){
                return individual;
            }
        }
        return this.individuals.get(this.individuals.size() - 1);
    }

    //method untuk menggabungkan 2 individu, kemudian menghasilkan individu baru
    public Individual crossover(Individual parent1, Individual parent2){
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

    //method untuk perubahan acak gen individu (meningkatkan keberagaman individu)
    public void mutate(Individual individual){
        Random random = new Random();
        int i = random.nextInt(individual.getBoard().length);
        int j = random.nextInt(individual.getBoard().length);
       
        if(random.nextBoolean()){
            individual.getBoard()[i][j] = 1;
        }else{
            individual.getBoard()[i][j] = 2;
        }
    }
}
