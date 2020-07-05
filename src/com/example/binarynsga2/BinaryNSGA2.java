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
        int tryCount;

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
                p1 = random.nextInt(matingPool.size());
                p2 = random.nextInt(matingPool.size());
                parent1 = matingPool.get(p1);
                parent2 = matingPool.get(p2);
                tryCount = 0;
                while(parent2.getCombinedGenotype().equals(parent1.getCombinedGenotype()))
                    {
                        parent2 = matingPool.get(random.nextInt(matingPoolSize));
                        tryCount+=1;

                        if (tryCount == population.size())
                        {
                            break;
                        }

                    }

                //System.out.println("Parent1:"+Arrays.toString(parent1.getCombinedGenotype().toArray()));
                //System.out.println("Parent2:"+Arrays.toString(parent2.getCombinedGenotype().toArray()));
                currOffsprings = NSGA2Utils.solutionUniformCrossover(parent1, parent2, repProb);
                //System.out.println("offspring1:"+Arrays.toString(currOffsprings.get(0).toArray()));
                //System.out.println("offspring2:"+Arrays.toString(currOffsprings.get(1).toArray()));


                offspring1Genotype = NSGA2Utils.bitwiseMutation(currOffsprings.get(0), mutationProb);
                offspring2Genotype = NSGA2Utils.bitwiseMutation(currOffsprings.get(1), mutationProb);

                offspring1 = NSGA2Utils.genotypeToSolution(offspring1Genotype, binaryEncodingLength);
                offspring2 = NSGA2Utils.genotypeToSolution(offspring2Genotype, binaryEncodingLength);
                childPopulation.add(offspring1);
                childPopulation.add(offspring2);

            }
        }

        return childPopulation;

    }


    public static ArrayList<Solution> nsga2()
    {
        //Initial Population
        int populationSize = 100;
        int iterations = 100;
        int binaryEncodingLength = 30;
        int variableNumber = 10;
        int matingPoolSize = 50;
        double mutationProbability = 1/variableNumber;
        double reproductionProbability = 0.1;

        long startTime = System.currentTimeMillis();
        ArrayList<Solution> population = NSGA2Utils.initialSolutionPopulation(populationSize, binaryEncodingLength, variableNumber);
        ArrayList<Solution> childPopulation;


        for (int i=0;i<iterations;i++)
        {
            //System.out.println(Integer.toString(i));
            NSGA2Utils.evaluatePopulationZDT6(population);
            NSGA2Utils.SetPopulationCrowdingDistance(population, ProblemUtils.ZDTObjectives());
            childPopulation = generateChildPopulation(population, matingPoolSize,
                    binaryEncodingLength,mutationProbability, reproductionProbability);

            NSGA2Utils.evaluatePopulationZDT6(childPopulation);
            NSGA2Utils.SetPopulationCrowdingDistance(childPopulation, ProblemUtils.ZDTObjectives());
            population.addAll(childPopulation);

            //Elitist Selection
            population = NSGA2Utils.ElitistSelection(population);
            NSGA2Utils.ResetPopulationAttributes(population);
        }
        return population;
        /**
        long endTime = System.currentTimeMillis();
        float duration = (endTime - startTime)/1000F;

        NSGA2Utils.evaluatePopulationZDT1(population);
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
            //System.out.println(Integer.toString(domCount));
            //System.out.println(Arrays.toString(population.get(i).getFitness().toArray()));
            //System.out.println(Arrays.toString(population.get(i).getDecimalVariables().toArray()));
        }

        //ystem.out.println("non-dominated set size:" + Integer.toString(non_dominated_front.size())+
        //       " process took:"+Float.toString(duration)+"seconds");
        //nalysisUtils.generateDatFile(population, "non-dominated-pop");


        ///Calculate IGD
        //rrayList<ArrayList<Double>> populationPoints = AnalysisUtils.getFitnessPoints(population);
        //rrayList<Solution> paretoFrontSolutions = NSGA2Utils.initialSolutionPopulation(100000,
        //       binaryEncodingLength, 2);
        //SGA2Utils.evaluatePopulationParetoZDT1(paretoFrontSolutions);
        //nalysisUtils.generateDatFile(paretoFrontSolutions, "pareto-front");
        //rrayList<ArrayList<Double>> paretoFrontPoints = AnalysisUtils.getFitnessPoints(paretoFrontSolutions);
        //ouble IDG = AttainmentUtils.IGD(populationPoints, non_dominated_points);
        //ystem.out.println("IGD: "+Double.toString(IDG));
         */
    }

    public static void main(String[] args) {

        /**
        int runs = 50;
        int domCount;
        ArrayList<Solution> allSolutions = new ArrayList<>();
        ArrayList<Solution> nonDominatedSolutions = new ArrayList<>();

        for (int i=0; i<runs;i++)
        {

            System.out.println(Integer.toString(i));
            allSolutions.addAll(nsga2());
        }

        NSGA2Utils.evaluatePopulationZDT6(allSolutions);
        for (int i=0; i<allSolutions.size(); i++)
        {
            domCount = NSGA2Utils.GetDominatedCount(i, allSolutions);
            if (domCount == 0) {
                nonDominatedSolutions.add(allSolutions.get(i));
            }
        }

        Collections.sort(nonDominatedSolutions, new HypervolumeFitnessComparator(1));
        ArrayList<ArrayList<Double>> non_dominated_points = AnalysisUtils.getFitnessPoints(nonDominatedSolutions);
        NSGA2Utils.evaluatePopulationZDT6(nonDominatedSolutions);
        AnalysisUtils.generateDatFile(nonDominatedSolutions, "non-dominated-pop");
        //Calculate IGD
        ArrayList<Solution> paretoFrontSolutions = NSGA2Utils.initialSolutionPopulation(10000, 30, 10);
        NSGA2Utils.evaluatePopulationParetoZDT6(paretoFrontSolutions);
        ArrayList<Solution> truePF = new ArrayList<>();

        for (int i=0;i<paretoFrontSolutions.size();i++)
        {
            domCount = NSGA2Utils.GetDominatedCount(i, paretoFrontSolutions);
            if (domCount == 0) {
                truePF.add(paretoFrontSolutions.get(i));
            }
        }

        Collections.sort(truePF, new HypervolumeFitnessComparator(1));
        AnalysisUtils.generateDatFile(truePF, "pareto-front");
        ArrayList<ArrayList<Double>> paretoFrontPoints = AnalysisUtils.getFitnessPoints(truePF);
        Double IDG = AttainmentUtils.IGD(paretoFrontPoints, non_dominated_points);
        System.out.println("IGD: "+Double.toString(IDG));

        ArrayList<Double> worstPoint = new ArrayList<>(Arrays.asList(30.0,30.0));
        Double hypervolume = AttainmentUtils.computeHypervolume(non_dominated_points, worstPoint);
        System.out.println("Hypervolume: "+Double.toString(hypervolume));

        Double paretoHypervolume = AttainmentUtils.computeHypervolume(paretoFrontPoints, worstPoint);
        System.out.println("Hypervolume: "+Double.toString(paretoHypervolume));

        System.out.println("Normalised Hypervolume:"+Double.toString(hypervolume/paretoHypervolume));
         */

        ArrayList<Solution> pop = NSGA2Utils.initialSolutionPopulation(100000, 30,2);
        NSGA2Utils.evaluatePopulationDTLZ1(pop, 3);
        AnalysisUtils.generateDatFile(pop, "objective-space", 3);




    }
}
