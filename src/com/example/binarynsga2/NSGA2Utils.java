package com.example.binarynsga2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public final class NSGA2Utils {

    public static ArrayList<Integer> initialSolution(int encodingLen)
    {
        Random rand = new Random();
        int variable;
        ArrayList<Integer> solution = new ArrayList<>();

        for (int i=0;i<encodingLen;i++)
        {
            variable = rand.nextInt(2);
            solution.add(variable);

        }
        return solution;
    }


    public static ArrayList<Solution> initialSolutionPopulation(int popSize, int binaryEncodingLength, int variableNum)
    {
        ArrayList<Solution> solutionPopulation = new ArrayList<>(popSize);
        ArrayList<ArrayList<Integer>> currentGenotype;
        Solution currentSolution;

        for (int i=0;i<popSize;i++)
        {
            currentGenotype = new ArrayList<>(variableNum);
            for (int j=0; j<variableNum; j++)
            {
                currentGenotype.add(initialSolution(binaryEncodingLength));
            }
            currentSolution = new Solution(currentGenotype);
            solutionPopulation.add(currentSolution);
        }
        return solutionPopulation;
    }
    public static void evaluatePopulationZDT1(ArrayList<Solution> population)
    {
        for (Solution sol:population)
        {
            sol.setFitness(ProblemUtils.ZDT1(sol.getDecimalVariables()));
        }
    }
    public static void evaluatePopulationZDT2(ArrayList<Solution> population)
    {
        for (Solution sol:population)
        {
            sol.setFitness(ProblemUtils.ZDT2(sol.getDecimalVariables()));
        }
    }
    public static void evaluatePopulationZDT3(ArrayList<Solution> population)
    {
        for (Solution sol:population)
        {
            sol.setFitness(ProblemUtils.ZDT3(sol.getDecimalVariables()));
        }
    }
    public static void evaluatePopulationZDT4(ArrayList<Solution> population)
    {
        for (Solution sol:population)
        {
            sol.setFitness(ProblemUtils.ZDT4(sol.getDecimalVariables()));
        }
    }
    public static void evaluatePopulationZDT6(ArrayList<Solution> population)
    {
        for (Solution sol:population)
        {
            sol.setFitness(ProblemUtils.ZDT6(sol.getDecimalVariables()));
        }
    }
    public static void SetPopulationCrowdingDistance(ArrayList<Solution> population, int objectives)
    {
        double fmin,fmax,distanceDenom,distanceNum;

        for (int i=0;i<objectives;i++)
        {
            int finalI = i;

            //Orders Population in ascending order of the corresponding fitness

            Collections.sort(population, new SolutionFitnessComparator(i));


            population.get(0).setCrowdingDistance(Double.POSITIVE_INFINITY);
            population.get(population.size()-1).setCrowdingDistance(Double.POSITIVE_INFINITY);
            fmin = population.get(0).getFitness(i);
            fmax = population.get(population.size()-1).getFitness(i);
            distanceDenom = fmax-fmin;

            for (int j=1;j<population.size()-1;j++)
            {
                distanceNum = population.get(j+1).getFitness(i) - population.get(j-1).getFitness(i);
                population.get(j).increaseCrowdingDistance(distanceNum/distanceDenom);
            }

        }
    }
    public static boolean dominatesMinOpt(ArrayList<Double> fitnessA, ArrayList<Double> fitnessB)
    {
        int equalLess = 0;
        int explicitLess = 0;


        for (int i=0; i<fitnessA.size(); i++)
        {
            if (fitnessA.get(i) <= fitnessB.get(i)) equalLess +=1;
            if (fitnessA.get(i) < fitnessB.get(i)) explicitLess +=1;
        }

        if (equalLess==fitnessA.size() && explicitLess>0 ) {return true;}
        else return false;
    }

    public static Solution TournamentSelection(Solution a, Solution b)
    {
        if (dominatesMinOpt(a.getFitness(),b.getFitness()))
        {
            return a;
        }
        else if(dominatesMinOpt(a.getFitness(),b.getFitness()))
        {
            return b;
        }
        else if (a.getCrowdingDistance()>b.getCrowdingDistance())
        {
            return a;
        }
        else{

            if (new Random().nextDouble() < 0.5){
                return a;
            }else{
                return b;
            }
        }
    }
    static Solution genotypeToSolution(ArrayList<Integer> solution, int variableEncodingLength) {

        ArrayList<ArrayList<Integer>> offspring1Genotype = new ArrayList<>();
        ArrayList<ArrayList<Integer>> offspring2Genotype = new ArrayList<>();

        ArrayList<ArrayList<Integer>> solutionGenotype = new ArrayList<>();
        Solution solutionObject;


        for (int i=0; i<solution.size(); i+=variableEncodingLength)
        {
            solutionGenotype.add(new ArrayList<Integer>(solution.subList(i,i+variableEncodingLength)));

        }

        solutionObject = new Solution(solutionGenotype);
        return solutionObject;
    }

    public static ArrayList<ArrayList<Integer>> solutionUniformCrossover(Solution parent1, Solution parent2, int variableEncodingLength,double repProb)
    {
        Random rand = new Random();

        double choice = rand.nextDouble();
        ArrayList<Integer> offspring1;
        ArrayList<Integer> offspring2;

        if (choice < repProb){
            ArrayList<ArrayList<Integer>> offsprings = uniformCrossover(parent1.getCombinedGenotype(), parent2.getCombinedGenotype());
            return offsprings;

        } else{
            offspring1 = parent1.getCombinedGenotype();
            offspring2 = parent2.getCombinedGenotype();

            return new ArrayList<>(Arrays.asList(offspring1,offspring2));

        }

    }
    public static ArrayList<ArrayList<Integer>> uniformCrossover(ArrayList<Integer> parent1, ArrayList<Integer> parent2)
    {
        Random rand = new Random();
        double choice;
        ArrayList<Integer> offspring1 = new ArrayList<>();
        ArrayList<Integer> offspring2 = new ArrayList<>();
        ArrayList<ArrayList<Integer>> offsprings = new ArrayList<>();

        for (int i=0;i<parent1.size();i++){
            choice = rand.nextDouble();
            if (choice>0.5)
            {
                offspring1.add(parent1.get(i));
                offspring2.add(parent2.get(i));
            } else
            {
                offspring1.add(parent2.get(i));
                offspring2.add(parent1.get(i));
            }
        }

        offsprings.add(offspring1);
        offsprings.add(offspring2);

        return offsprings;
    }

    public static ArrayList<Integer> bitwiseMutation(ArrayList<Integer> parent, double mutProbability)
    {
        ArrayList<Integer> offspring = new ArrayList<>();
        Random rand = new Random();
        double mut;

        for (Integer integer : parent) {
            mut = rand.nextDouble();
            if (mut < mutProbability) {
                if (integer == 1) offspring.add(0);
                else offspring.add(1);
            } else offspring.add(integer);
        }

        return offspring;
    }

    public static Integer GetDominatedCount(int solution, ArrayList<Solution> population)
    {
        int dominatedCount = 0;
        Solution checkedSol = population.get(solution);
        for (int i=0; i<population.size();i++)
        {
            if (i!=solution && dominatesMinOpt(population.get(i).getFitness(), checkedSol.getFitness()))
            {
                dominatedCount+=1;
            }
        }
        return dominatedCount;

    }

    public static ArrayList<Solution> ElitistSelection(ArrayList<Solution> combinedPopulation)
    {
        //Find dominated count
        double solutionsRemaining;
        ArrayList<Solution> nextGeneration = new ArrayList<>();
        ArrayList<Integer> dominatedCounts = new ArrayList<>();
        ArrayList<Solution> currentFront;

        for (int i=0; i<combinedPopulation.size(); i++) {
            dominatedCounts.add(GetDominatedCount(i, combinedPopulation));
        }

        while(nextGeneration.size()<(combinedPopulation.size()/2))
        {

            //get current front
            currentFront = new ArrayList<>();

            for (int i=0; i<combinedPopulation.size(); i++)
            {
                if (dominatedCounts.get(i)==0)
                {
                    currentFront.add(combinedPopulation.get(i));
                }
                dominatedCounts.set(i,dominatedCounts.get(i)-1);
            }

            if (currentFront.size()+nextGeneration.size()<(combinedPopulation.size()/2))
            {
                nextGeneration.addAll(currentFront);
            }else{
                solutionsRemaining = (combinedPopulation.size()/2) - nextGeneration.size();
                Collections.sort(currentFront,  new SolutionCrowdingComparator());
                //System.out.println(Integer.toString(combinedPopulation.size()/2));

                //System.out.println(Double.toString(solutionsRemaining)+"solutions left"+
                //       Integer.toString(nextGeneration.size()));

                for (int j=0;j<solutionsRemaining;j++)
                {
                    nextGeneration.add(currentFront.get(currentFront.size()-1-j));
                }
            }
        }
        return nextGeneration;
    }

    public static void ResetPopulationAttributes(ArrayList<Solution> population)
    {
        for (Solution sol: population)
        {
            sol.resetCrowdingDistance();
            sol.resetFitness();
        }
    }



}
