package com.example.binarynsga2;

import java.util.ArrayList;

public class Solution {
    private ArrayList<ArrayList<Integer>> genotype;
    private double crowdingDistance;
    private ArrayList<Double> fitness;


    public Solution(ArrayList<ArrayList<Integer>> genotype_){
        this.genotype = genotype_;
        this.crowdingDistance = 0.0;
    }

    public void setFitness(ArrayList<Double> fitness)
    {
        this.fitness = fitness;
    }
    public void setCrowdingDistance(Double crowdingDistance)
    {
        this.crowdingDistance = crowdingDistance;
    }
    public void increaseCrowdingDistance(Double crowdingDistance){this.crowdingDistance+=crowdingDistance;}
    public Double getFitness(int i){return this.fitness.get(i);}
    public ArrayList<Double> getFitness(){return this.fitness;}
    public Double getCrowdingDistance(){return this.crowdingDistance;}
    public void resetCrowdingDistance(){this.crowdingDistance=0.0;}
    public void resetFitness(){this.fitness=new ArrayList<Double>();}
    public ArrayList<ArrayList<Integer>> getGenotype(){return this.genotype;}


    public ArrayList<Integer> getCombinedGenotype()
    {
        ArrayList<Integer> combinedGenotype = new ArrayList<>();
        ArrayList<Integer> currVariable;

        for (int i=0;i<this.genotype.size();i++)        {
            currVariable = this.genotype.get(i);
            //System.out.println("getCombinedGenotype:"+Integer.toString(currVariable.size()));
            for (int j=0; j<currVariable.size();j++)
            {
                //System.out.println("getCombinedGenotype:"+Integer.toString(i)+Integer.toString(j));
                combinedGenotype.add(currVariable.get(j));
            }
        }


        return combinedGenotype;
    }

    public Double binary2Decimal(ArrayList<Integer> binaryNum)
    {
        Double decimal = 0.0;

        for (int i=1; i<binaryNum.size()+1; i++)
        {
            decimal += binaryNum.get(i-1) * 1/Math.pow(2,i);
        }
        return decimal;
    }

    public ArrayList<Double> getDecimalVariables()
    {
        ArrayList<Double> decimalVariables = new ArrayList<>();
        for (ArrayList<Integer> binary:this.genotype)
        {
            decimalVariables.add(binary2Decimal(binary));
        }

        return decimalVariables;
    }

}

