package com.company;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class Simulation{

  private int cacheSize;
  private int population;
  private LinkedList<Integer> cache;
  private int requestCount;
  private int cacheHits;
  private List<Double> probabilities;
  private double totalTime = 0;
  private double lambdaSum;
  private AliasMethod eventGenerator;

  Simulation(int cacheSize, int population, String cacheType) {
    assert cacheSize < population;
      this.cacheSize = cacheSize;
      this.population = population;
      if (cacheType.toLowerCase().equals("random")) {
        cache = new LimitedQueueRandom<>(cacheSize);
      } else if (cacheType.toLowerCase().equals("fifo")) {
        cache = new LimitedQueue<>(cacheSize);
      }
      populateCache();
      getSumLambda();
      this.probabilities = new ArrayList<>();
      getProbabilities();
      this.eventGenerator = new AliasMethod(probabilities);

  }

  private void populateCache() {
    for (int i = 1; i <= cacheSize; i++) {
      cache.add(i);
    }
  }

  private void getSumLambda() {
    for (int i=1; i <= population; i++) {
      lambdaSum += 1d/i;
    }
  }

  private void getProbabilities() {
    for (int i=0; i < population; i++) {
      probabilities.add(i, ((1d / (i+1)) / lambdaSum));
    }
  }

  private void request(int k) {
    requestCount++;
    if (cache.contains(k)) {
      cacheHits++;
    } else {
      cache.add(k);
    }
  }

  double getHitRatio() {
    return (double)cacheHits/requestCount;
  }


  private int getEvent() {
    return eventGenerator.next()+1;
  }


  private double poissonRandomInterArrivalDelay(double L) {
    return (Math.log(1.0-Math.random()))/-L;
  }

  void runSimulation(int iterations) {
    for (int i = 0; i<iterations; i++) {
      double lambda = 1/lambdaSum;
      double delay = poissonRandomInterArrivalDelay(lambda);
      totalTime += delay;
      request(getEvent());
    }
  }

  double getMissRate(){
    return (requestCount - cacheHits) / totalTime;
  }
}
