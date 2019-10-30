package com.company;
import java.util.LinkedList;
import java.util.Random;

public class LimitedQueueRandom<E> extends LinkedList<E> {
    private int limit;

    public LimitedQueueRandom(int limit) {
      this.limit = limit;
    }

    @Override
    public boolean add(E o) {
      super.add(o);
      while (size() > limit) {
        Random r = new Random();
        remove(r.nextInt(limit));
      }
      return true;
    }
}
