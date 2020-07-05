package com.example.binarynsga2;

import java.util.Comparator;

public class HypervolumeFitnessComparator implements Comparator<Solution> {

    Integer objective;
    public HypervolumeFitnessComparator(int objective)
    {
        this.objective = objective;
    }

    @Override
    public int compare(Solution o1, Solution o2) {
        return o2.getFitness(this.objective).compareTo(o1.getFitness(this.objective));
    }
}
