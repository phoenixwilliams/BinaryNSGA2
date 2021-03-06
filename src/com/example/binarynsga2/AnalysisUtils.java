package com.example.binarynsga2;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public final class AnalysisUtils {

    public static void generateDatFile(ArrayList<Solution> population, String filename, int numObjectives)
    {
        try{
            FileWriter myWriter = new FileWriter(filename+".dat");

            for (Solution sol:population)
            {
                for (int i=0; i<numObjectives;i++) {
                    myWriter.write(Double.toString(sol.getFitness().get(i)) + " ");
                }
                myWriter.write("\n");
            }
            myWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static ArrayList<ArrayList<Double>> getFitnessPoints(ArrayList<Solution> population)
    {
        ArrayList<ArrayList<Double>> points = new ArrayList<>();

        for (Solution sol:population)
        {
            points.add(sol.getFitness());
        }
        return  points;
    }

}
