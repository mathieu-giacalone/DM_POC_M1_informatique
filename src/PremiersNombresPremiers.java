// -*- coding: utf-8 -*-

import java.util.ArrayList;
import java.math.BigInteger;

public class PremiersNombresPremiers implements Runnable {
  static final long borne = 13_500_000 ;
  private long min;
  private long max;
  ArrayList<Long> premiersNombresPremiers = new ArrayList<Long>() ;

  public PremiersNombresPremiers (long min, long max) {
    this.min = min;
    this.max = max;
  }

  public static void main(String[] args) throws InterruptedException {
    final long début = System.nanoTime() ;

    int countOfThreads = 4;
    ArrayList<PremiersNombresPremiers> premierNombresPremiersCalcsRun = new ArrayList<>();
    ArrayList<Thread> premierNombresPremiersCalcs = new ArrayList<>();
    long min = 0;
    long max = borne/countOfThreads;
    for (int i = 0; i < countOfThreads; ++i) {
      PremiersNombresPremiers premiersNombresPremiers = new PremiersNombresPremiers(min, max);
      premierNombresPremiersCalcsRun.add(premiersNombresPremiers);
      Thread thread = new Thread(premiersNombresPremiers);
      premierNombresPremiersCalcs.add(thread);
      thread.start();
      min = max + 1;
      max = max + (borne/countOfThreads);
    }


    for (int i = 0; i < premierNombresPremiersCalcs.size(); ++i) {
      premierNombresPremiersCalcs.get(i).join();
    }

    final long fin = System.nanoTime() ;
    final long durée = (fin - début) / 1_000_000 ;
    ArrayList<Long> allPrimeNumber = new ArrayList<>();
    for (int i = 0; i < premierNombresPremiersCalcs.size(); ++i) {
      premierNombresPremiersCalcs.get(i).join();
      allPrimeNumber.addAll(premierNombresPremiersCalcsRun.get(i).premiersNombresPremiers);
    }
    System.out.print("Nombre de nombres premiers inférieurs à " + borne + " : ") ;
    System.out.println(allPrimeNumber.size()) ;
    System.out.format("Durée du calcul: %.3f s.%n", (double) durée/1000) ;
    System.out.println("Processeur disponible: " + Runtime.getRuntime().availableProcessors());
  }

  @Override
  public void run() {
    for (long i = min; i <= max; i++) {
      if (BigInteger.valueOf(i).isProbablePrime(50)) premiersNombresPremiers.add(i) ;
    }
  }
}



/*
  $ java PremiersNombresPremiers
  Nombre de nombres premiers inférieurs à 10000000 : 664579
  Durée du calcul: 23,336 s.
  $
*/
