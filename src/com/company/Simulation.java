package com.company;
import java.util.LinkedList;

class Simulation{

  private int cacheSize;
  private int population;
  private LinkedList<Integer> cache;
  private int requestCount;
  private int cacheHits;
  private double[] cumSum;
  private double totalTime = 0;
  private double lambdaSum;

  Simulation(int cacheSize, int population, String cacheType) {
    assert cacheSize < population;
      this.cacheSize = cacheSize;
      this.population = population;
      if (cacheType.toLowerCase().equals("random")) {
        cache = new LimitedQueueRandom(cacheSize);
      } else if (cacheType.toLowerCase().equals("fifo")) {
        cache = new LimitedQueue<>(cacheSize);
      }
      populateCache();
      cumSum = new double[population];
      getSumLambda();
  }

  private void populateCache() {
    for (int i = 0; i < cacheSize; i++) {
      cache.add(i+1);
    }
  }

  private void request(int k) {
    requestCount++;
    if (cache.contains(k)) {
      //cache hit
      cacheHits++;
    } else {
      //cache miss
      cache.add(k);
    }
  }

  double getHitRatio() {
    return (double)cacheHits/requestCount;
  }


  private void getSumLambda() {
    for (int i=1; i <= population; i++) {
      lambdaSum += 1d/i;
    }

    cumSum[0] = 1d / lambdaSum;
    for (int i=1; i < population; i++) {
      cumSum[i] = cumSum[i - 1] + ((1d / (i+1)) / lambdaSum);
    }

  }

  private int getEvent(double v){
    for (int i=0; i < population; i++) {
      if (v <= cumSum[i]) {
        return i+1;
      }
    }
    return 0;
  }


  private double poissonRandomInterArrivalDelay(double L) {
    return (Math.log(1.0-Math.random()))/-L;
  }

  void runSimulation(int iterations) {
    for (int i = 0; i<iterations; i++) {
      double event = poissonRandomInterArrivalDelay(lambdaSum);
      totalTime += event;
      request(getEvent(event));
    }
  }

  double getMissRate(){
    return (requestCount - cacheHits) / totalTime;
  }
}
