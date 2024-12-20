package code;

public class Population {
    int popSize;
    Individual[] individuals = new Individual[popSize];
    Individual fittest;
    double bestFitness = Double.MAX_VALUE;
    int seed;

    public void initializePopulation(int n, int popSize, int seed){
        this.popSize = popSize;
        this.seed = seed;
        //membuat Individual (board yang diisi 1 & 2 secara random) sebanyak popSize
        for (int i=0; i<popSize; i++){
            this.individuals[i] = new Individual(n, seed);  
        }
    }

    public Individual getFittest() {
        double bestFitness = Double.MAX_VALUE;
        for (int i=0; i<popSize; i++){
            if(bestFitness >= individuals[i].getFitness()){
                this.bestFitness = individuals[i].getFitness();
                this.fittest = individuals[i];
            }
        }
        return this.fittest;
    }

    public void calculateFitness(double ALPHA, double BETA){
        for (int i=0;i<popSize;i++){
            individuals[i].countFitness(ALPHA, BETA);
        }
        getFittest();
    }

}
