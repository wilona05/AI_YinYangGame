package code;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        clearOutput();
        // Input
        int n = 1, seed = 1;
        int popSize = 1;
        int maxGen = 1;
        double mutationRate = 1;
        double coolingRate = 1;
        PuzzleQuestion puzzleQuestion = null;

        // baca param
        try (Scanner paramSc = new Scanner(new FileReader("param.txt"))) {
            while (paramSc.hasNext()) {
                popSize = paramSc.nextInt();
                maxGen = paramSc.nextInt();
                mutationRate = paramSc.nextDouble();
                coolingRate = paramSc.nextDouble();
            }
        } catch (Exception e) {
            System.err.println("Error membaca param file: " + e.getMessage());
        }

        // baca input
        try (Scanner inputSc = new Scanner(new FileReader("input.txt"))) {
            while (inputSc.hasNextInt()) {
                n = inputSc.nextInt(); // ukuran papan nxn
                seed = inputSc.nextInt(); // untuk urutan angka acak

                // input papan puzzle
                int[][] puzzleInput = new int[n][n];
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        if (inputSc.hasNextInt()) {
                            puzzleInput[i][j] = inputSc.nextInt();
                        }
                    }
                }
                puzzleQuestion = new PuzzleQuestion(puzzleInput);
                Random rand = new Random(seed);
                // ga
                YinYang ga = new YinYang(popSize, maxGen, mutationRate, coolingRate, rand, n, puzzleQuestion);
                Individual res = ga.runGA();
            }
        } catch (Exception e) {
            System.err.println("Error membaca input file: " + e.getMessage());
        }
    }

    public static void clearOutput() {
        String filename = "output.txt";
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}