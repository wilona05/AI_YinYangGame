package code;

import java.util.ArrayList;
import java.util.Random;

public class Population {
    public ArrayList<Individual> individuals;
    private int maxSize; //banyak individu maks dalam populasi
    private int curPopulationSize; //banyak individu dalam populasi saat ini
    private int boardSize; //ukuran papan yinyang
    private Random rand; //untuk randomization
    private PuzzleQuestion puzzleQuestion; //board yinyang awal (soal yang akan disolve)

    //constructor papan soal random
    public Population(int maxSize, int boardSize, Random rand, PuzzleQuestion puzzleQuestion){
        this.individuals = new ArrayList<>();
        this.maxSize = maxSize;
        this.curPopulationSize = 0;
        this.boardSize = boardSize;
        this.rand = rand;
        this.puzzleQuestion = puzzleQuestion;
    }

    //method untuk mengenerate populasi awal secara acak
    public void randomPopulation(){
        for(int i=0; i<this.maxSize; i++){
            if(this.curPopulationSize < this.maxSize){ //tambah individu jika belum melebihi ukuran maksimal populasi
                this.individuals.add(new Individual(this.boardSize, this.rand, puzzleQuestion));
                this.curPopulationSize++;
            }
        }
    }

    //method seleksi menggunakan roulette wheel
    public Individual[] selectParent(){
        Individual[] parents = new Individual[2]; //menyimpan hasil seleksi
        
        double totalFitness = 0;
        //hitung total fitness (meminimalkan: fitness makin kecil --> peluang makin besar)
        for(Individual individual : this.individuals){
            totalFitness += 1.0 / (individual.getFitness() + 1); 
        }

        //hitung probabilitas seleksi untuk setiap individu
        for(Individual individual : this.individuals){
            individual.parentProbability = (1.0 / (individual.getFitness() + 1)) / totalFitness;
        }

        //pilih 2 individu
        for(int i=0; i<2; i++){
            int j = 0;
            double prob = rand.nextDouble(); //angka acak 
            double cumulativeProb = 0.0;

           // Pilih individu yang cumulativeProb >= prob
            while (cumulativeProb < prob && j < this.individuals.size()) {
                cumulativeProb += this.individuals.get(j).parentProbability;
                j++;
            }
        
            parents[i] = this.individuals.get(j-1); //simpan individu yang terpilih
        }
        return parents;
    }

    //method untuk menggabungkan 2 individu, kemudian menghasilkan individu baru (single-point crossover)
    public Individual crossover(Individual parent1, Individual parent2){
        int n = parent1.getBoard().length; //ukuran papan
        int crossoverPoint = rand.nextInt(n); //titik crossover

        int[][] offspringBoard = new int[n][n]; //board hasil crossover
        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                if(j < crossoverPoint){ //jika kolom saat ini berada sebelum titik crossover, ambil elemen dari parent1
                    offspringBoard[i][j] = parent1.getBoard()[i][j];
                }else{ //jika kolom saat ini berada setelah/sama dengan titik crossover, ambil elemen dari parent2
                    offspringBoard[i][j] = parent2.getBoard()[i][j];
                }
            }
        }
        return new Individual(n, rand, puzzleQuestion, offspringBoard); //kembalikan individu baru dengan board hasil crossover
    }
}
