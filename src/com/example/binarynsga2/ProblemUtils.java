package com.example.binarynsga2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class ProblemUtils {

    public static int ZDTObjectives(){return 2;}


    public static ArrayList<Double> ZDT1(ArrayList<Double> decisionVariables)
    {
        double f1 = decisionVariables.get(0);
        double sum =0;
        for (int i=1; i<decisionVariables.size(); i++)
        {
            sum += decisionVariables.get(i);
        }
        double g = 1.0 + (9.0/(decisionVariables.size()-1))*sum;
        double h = 1.0 - Math.sqrt(f1/g);
        double f2 = g*h;

        return new ArrayList<>(Arrays.asList(f1,f2));
    }

    public static ArrayList<Double> paretoZDT1(ArrayList<Double> decisionVariables)
    {
        double f1 = decisionVariables.get(0);
        double sum =0;
        for (int i=1; i<decisionVariables.size(); i++)
        {
            sum += decisionVariables.get(i);
        }
        double g = 1.0;
        double h = 1.0 - Math.sqrt(f1/g);
        double f2 = g*h;

        return new ArrayList<>(Arrays.asList(f1,f2));
    }

    public static ArrayList<Double> ZDT2(ArrayList<Double> decisionVariables)
    {
        double f1 = decisionVariables.get(0);
        double sum =0;
        for (int i=1; i<decisionVariables.size(); i++)
        {
            sum += decisionVariables.get(i);
        }
        double g = 1.0 + (9.0/(decisionVariables.size()-1))*sum;
        double h = 1.0 - Math.pow(f1/g,2);
        double f2 = g*h;

        return new ArrayList<>(Arrays.asList(f1,f2));
    }

    public static ArrayList<Double> paretoZDT2(ArrayList<Double> decisionVariables)
    {
        double f1 = decisionVariables.get(0);
        double sum =0;
        for (int i=1; i<decisionVariables.size(); i++)
        {
            sum += decisionVariables.get(i);
        }
        double g = 1.0;
        double h = 1.0 - Math.pow(f1/g,2);
        double f2 = g*h;

        return new ArrayList<>(Arrays.asList(f1,f2));
    }

    public static ArrayList<Double> ZDT3(ArrayList<Double> decisionVariables)
    {
        double f1 = decisionVariables.get(0);
        double sum =0;
        for (int i=1; i<decisionVariables.size(); i++)
        {
            sum += decisionVariables.get(i);
        }
        double g = 1.0 + (9.0/(decisionVariables.size()-1.0))*sum;
        double h = 1.0 - Math.sqrt(f1/g) - (f1/g)*Math.sin(10.0*Math.PI*f1);
        double f2 = g*h;

        return new ArrayList<>(Arrays.asList(f1,f2));
    }

    public static ArrayList<Double> paretoZDT3(ArrayList<Double> decisionVariables)
    {
        double f1 = decisionVariables.get(0);
        double sum =0;
        for (int i=1; i<decisionVariables.size(); i++)
        {
            sum += decisionVariables.get(i);
        }
        double g = 1.0;
        double h = 1.0 - Math.sqrt(f1/g) - (f1/g)*Math.sin(10.0*Math.PI*f1);
        double f2 = g*h;

        return new ArrayList<>(Arrays.asList(f1,f2));
    }

    public static ArrayList<Double> ZDT4(ArrayList<Double> decisionVariables)
    {
        double f1 = decisionVariables.get(0);
        double sum =0;
        for (int i=1; i<decisionVariables.size(); i++)
        {
            sum += (Math.pow(decisionVariables.get(i),2.0) - 10.0*Math.cos(4.0*Math.PI*decisionVariables.get(i)));
        }
        double g = 1.0 + 10.0*(decisionVariables.size()-1)+sum;
        double h = 1.0 - Math.sqrt(f1/g);
        double f2 = g*h;

        return new ArrayList<>(Arrays.asList(f1,f2));
    }

    public static ArrayList<Double> paretoZDT4(ArrayList<Double> decisionVariables)
    {
        double f1 = decisionVariables.get(0);
        double sum =0;
        for (int i=1; i<decisionVariables.size(); i++)
        {
            sum += (Math.pow(decisionVariables.get(i),2.0) - 10.0*Math.cos(4.0*Math.PI*decisionVariables.get(i)));
        }
        double g = 1.0;
        double h = 1.0 - Math.sqrt(f1/g);
        double f2 = g*h;

        return new ArrayList<>(Arrays.asList(f1,f2));
    }

    public static ArrayList<Double> ZDT6(ArrayList<Double> decisionVariables)
    {
        double f1 = 1.0 - Math.exp(-4.0*decisionVariables.get(0))*Math.pow(Math.sin(6.0*Math.PI*decisionVariables.get(0)),6.0);
        double f2,g,h;

        g=0.0;
        for (int i=1; i<decisionVariables.size(); i++)
        {
            g += decisionVariables.get(i);
        }
        g = 9.0*Math.pow(g/(decisionVariables.size()-1), 0.25) + 1.0;
        h = 1.0 - Math.pow(f1/g,2.0);
        f2 = g*h;


        return new ArrayList<>(Arrays.asList(f1,f2));
    }

    public static ArrayList<Double> paretoZDT6(ArrayList<Double> decisionVariables)
    {
        double f1 = 1.0 - Math.exp(-4.0*decisionVariables.get(0))*Math.pow(Math.sin(6.0*Math.PI*decisionVariables.get(0)),6.0);
        double f2,g,h;

        g=0.0;
        for (int i=1; i<decisionVariables.size(); i++)
        {
            g += decisionVariables.get(i);
        }
        g = 1.0;
        h = 1.0 - Math.pow(f1/g,2.0);
        f2 = g*h;


        return new ArrayList<>(Arrays.asList(f1,f2));
    }

    public static ArrayList<Double> DTLZ1(ArrayList<Double> decisionVariables, int numObjectives)
    {
        int k=0, temp=0;
        double g= 0.0;
        ArrayList<Double> fitness = new ArrayList<>();

        k = decisionVariables.size() - numObjectives + 1;
        for (int i=0; i<decisionVariables.size();i++)
        {
            g += Math.pow(decisionVariables.get(i)-0.5, 2)-Math.cos(20.0*(decisionVariables.get(i)-0.5));
        }
        g = 100 * (k+g);

        for (int i=0;i<numObjectives;i++)
        {
            fitness.add((1.0+g)*0.5);
        }

        for (int i=0; i<numObjectives; i++)
        {
            for (int j=0;j<numObjectives-(i+1);j++)
            {
                fitness.set(i, fitness.get(i)*decisionVariables.get(j));
            }
            if (i!=0)
            {
                temp = numObjectives - (i+1);
                fitness.set(i, fitness.get(i)*(1-decisionVariables.get(temp)));
            }
        }
        return fitness;
    }


}
