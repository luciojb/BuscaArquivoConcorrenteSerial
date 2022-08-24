package main;

import java.io.IOException;
import utils.ReadFiles;

/**
 * @author : Lúcio José Beirão
 * @since : 17/08/2022
 **/
public class MainDNA {

  public static void main(String[] args) throws IOException, InterruptedException {

    long[] seriais = new long[10];
    long[] concs = new long[10];

    for (int i = 0; i < 10; i++) {
      long serial = ReadFiles.readDNASerial();
      long conc = ReadFiles.readDNAFromFileConcurrent();

      System.out.printf("Tempo serial = %dms; tempo concorrente = %dms; Diferença: %dms", serial, conc, serial - conc);
      seriais[i] = serial;
      concs[i] = conc;
    }

    System.out.println("Tempos seriais");
    for (long i : seriais) {
      System.out.println(i);
    }
    System.out.println("Tempos concorrentes");
    for (long i : concs) {
      System.out.println(i);
    }
  }

}
