package com.example.binarynsga2;

import java.lang.reflect.AnnotatedArrayType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class BinaryNSGA2 {

    public static ArrayList<Solution> generateChildPopulation(ArrayList<Solution> population, int matingPoolSize, int binaryEncodingLength, double mutationProb,
                                                              double repProb)
    //Method in Main Class as easy change to selection process rather than changing it in utils class
    {
        Random random = new Random();
        ArrayList<Solution> matingPool = new ArrayList<>(matingPoolSize);
        ArrayList<Solution> childPopulation = new ArrayList<>(population.size());
        ArrayList<ArrayList<Integer>> currOffsprings;
        Solution offspring1,offspring2;
        ArrayList<Integer> offspring1Genotype, offspring2Genotype;
        Solution parent1;
        Solution parent2;
        int p1,p2;

        for (int i=0; i<population.size(); i++)
        {
            //Generate mating pool via tournament selection

            for (int j=0; j<matingPoolSize; j++)
            {
                p1 = random.nextInt(population.size());
                p2 = random.nextInt(population.size());
                matingPool.add(NSGA2Utils.TournamentSelection(population.get(p1), population.get(p2)));
            }

            while (childPopulation.size() < population.size())
            {
                parent1 = matingPool.get(random.nextInt(matingPoolSize));
                parent2 = matingPool.get(random.nextInt(matingPoolSize));
                while(parent2.getCombinedGenotype().equals(parent1.getCombinedGenotype()))
                    {
                        parent2 = matingPool.get(random.nextInt(matingPoolSize));
                    }

                currOffsprings = NSGA2Utils.solutionUniformCrossover(matingPool.get(random.nextInt(matingPoolSize)),matingPool.get(random.nextInt(matingPoolSize)),
                        binaryEncodingLength,repProb);

                offspring1Genotype = NSGA2Utils.bitwiseMutation(currOffsprings.get(0),mutationProb);
                offspring2Genotype = NSGA2Utils.bitwiseMutation(currOffsprings.get(1),mutationProb);

                offspring1 = NSGA2Utils.genotypeToSolution(offspring1Genotype, binaryEncodingLength);
                offspring2 = NSGA2Utils.genotypeToSolution(offspring2Genotype, binaryEncodingLength);

                childPopulation.add(offspring1);
                childPopulation.add(offspring2);
            }
        }

        return childPopulation;

    }


    public static void nsga2()
    {
        //Initial Population
        int populationSize = 100;
        int iterations = 100;
        int binaryEncodingLength = 30;
        int variableNumber = 30;
        int matingPoolSize = 500;
        double mutationProbability = 1/variableNumber;
        double reproductionProbability = 0.1;

        long startTime = System.currentTimeMillis();
        ArrayList<Solution> population = NSGA2Utils.initialSolutionPopulation(populationSize, binaryEncodingLength, variableNumber);
        ArrayList<Solution> childPopulation;


        for (int i=0;i<iterations;i++)
        {
            //System.out.println(Integer.toString(i));
            NSGA2Utils.evaluatePopulationZDT2(population);
            NSGA2Utils.SetPopulationCrowdingDistance(population, ProblemUtils.ZDTObjectives());
            childPopulation = generateChildPopulation(population, matingPoolSize,
                    binaryEncodingLength,mutationProbability, reproductionProbability);

            NSGA2Utils.evaluatePopulationZDT2(childPopulation);
            NSGA2Utils.SetPopulationCrowdingDistance(childPopulation, ProblemUtils.ZDTObjectives());
            population.addAll(childPopulation);

            //Elitist Selection
            population = NSGA2Utils.ElitistSelection(population);
            NSGA2Utils.ResetPopulationAttributes(population);
        }

        long endTime = System.currentTimeMillis();
        float duration = (endTime - startTime)/1000F;

        NSGA2Utils.evaluatePopulationZDT2(population);
        ArrayList<Solution> non_dominated_front = new ArrayList<>();
        ArrayList<ArrayList<Double>> non_dominated_points = new ArrayList<>();
        int domCount;
        Collections.sort(population, new SolutionFitnessComparator(1));


        for (int i=population.size()-1; i>0; i--) {
            domCount = NSGA2Utils.GetDominatedCount(i, population);
            if (domCount==0)
            {
                non_dominated_front.add(population.get(i));
                non_dominated_points.add(population.get(i).getFitness());
            }
            System.out.println(Arrays.toString(population.get(i).getFitness().toArray()));
            //System.out.println(Arrays.toString(population.get(i).getGenotype().toArray()));
        }

        System.out.println("non-dominated set size:" + Integer.toString(non_dominated_front.size())+" process took:"+Float.toString(duration)+"seconds");
        AnalysisUtils.generateDatFile(non_dominated_front, "non-dominated-pop");

        ArrayList<ArrayList<Double>> no_duplicates_non_dominated_points = new ArrayList<>();

        for (ArrayList<Double> point: non_dominated_points)
        {
            if (!no_duplicates_non_dominated_points.contains(point));
            {
                no_duplicates_non_dominated_points.add(point);
            }
        }

        ArrayList<Double> worstPoint = new ArrayList<>();

        for (int i=0;i<variableNumber;i++)
        {
            worstPoint.add(1.0);
        }
        worstPoint = ProblemUtils.ZDT2(worstPoint);

        Double hypervolume = AttainmentUtils.computeHypervolume(no_duplicates_non_dominated_points,worstPoint);

        System.out.println("Hypervolume: "+Double.toString(hypervolume)+" using worst point:"+Arrays.toString(worstPoint.toArray()));

    }

    public static void main(String[] args)
    {
        nsga2();
       //ArrayList<Solution> pop = NSGA2Utils.initialSolutionPopulation(100000, 30,2);
       //NSGA2Utils.evaluatePopulationZDT1(pop);
       //AnalysisUtils.generateDatFile(pop, "objective-space");
    }


}
