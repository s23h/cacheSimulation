package com.company;

import java.util.Arrays;

public class Main {

  public static void getConfidenceInterval(int cacheSize, int population, String cacheType, int samplesize, int iterationsPerSample) {
    double[] hitRatioSamples = new double[samplesize];
    double[] missRateSamples = new double[samplesize];
    for (int i=0;i<samplesize;i++){
      Simulation s = new Simulation(cacheSize, population, cacheType);
      s.runSimulation(iterationsPerSample);
      hitRatioSamples[i] = s.getHitRatio();
      missRateSamples[i] = s.getMissRate();
    }

    double hitRatioMean = getMean(hitRatioSamples);
    double hitRatioHalfInterval =  1.96*getStdDev(hitRatioSamples)/Math.sqrt(samplesize);
    double missRateMean = getMean(missRateSamples);
    double missRateHalfInterval =  1.96*getStdDev(missRateSamples)/Math.sqrt(samplesize);

    System.out.println("Hit Ratio mean: " + hitRatioMean);
    System.out.println("Hit Ratio confidence interval: [" +
        (hitRatioMean - hitRatioHalfInterval) +
        ", " +
        (hitRatioMean + hitRatioHalfInterval) +
        "]")
        ;

    System.out.println("Miss Rate mean: " + missRateMean);
    System.out.println("Miss Rate confidence interval: [" +
        (missRateMean - missRateHalfInterval) +
        ", " +
        (missRateMean + missRateHalfInterval) +
        "]")
    ;


  }

  public static double getMean(double[] samples) {
    return Arrays.stream(samples).sum()/samples.length;
  }

  public static double getStdDev(double[] samples) {
    double mean = Arrays.stream(samples).sum()/samples.length;
    return Math.sqrt(Arrays.stream(samples).map(i -> Math.pow((i-mean), 2)).sum()/(samples.length - 1));
  }

  public static void main(String[] args) {
    getConfidenceInterval(2, 3, "fifo", 10000, 10000);
  }

}
