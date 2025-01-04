package code;

public class Population {
    int maxPopulationSize;
    Individual[] individuals = new Individual[maxPopulationSize];
    Individual fittest;
    double bestFitness = Double.MAX_VALUE;
    int seed;

    public void initializePopulation(int n, int popSize, int seed){
        this.maxPopulationSize = popSize;
        this.seed = seed;
        this.individuals = new Individual[popSize];
        //membuat Individual (board yang diisi 1 & 2 secara random) sebanyak popSize
        for (int i=0; i<popSize; i++){
            this.individuals[i] = new Individual(n, seed+i); //seed+i agar tiap individu memiliki board yang unik
        }
    }

    //method untuk mencari individual dengan fitness terbaik (terendah)
    public Individual getFittest() {
        double curBestFitness = Double.MAX_VALUE;
        for (int i=0; i<maxPopulationSize; i++){
            if(individuals[i].getFitness() < curBestFitness){
                curBestFitness = individuals[i].getFitness();
                this.bestFitness = individuals[i].getFitness();
                this.fittest = individuals[i];
            }
        }
        return this.fittest;
    }

    public void calculateFitness(double ALPHA, double BETA){
        for (int i=0;i<maxPopulationSize;i++){
            individuals[i].countFitness(ALPHA, BETA);
        }
        getFittest();
    }

    //method untuk memasukkan individu baru ke dalam populasi
    public void addIndividual(Individual individual){
        double worstFitness = Double.MIN_VALUE;
        int worstIdx = -1;

        //cari individu dengan fitness terburuk (tertinggi)
        for(int i=0; i<maxPopulationSize; i++){
            if(individuals[i].getFitness() > worstFitness){
                worstFitness = individuals[i].getFitness();
                worstIdx = i;
            }
        }

        if(worstIdx != -1){
            individuals[worstIdx] = individual;
        }
    }
}
