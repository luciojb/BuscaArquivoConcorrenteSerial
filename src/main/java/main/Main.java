package main;

import java.io.IOException;
import java.util.Scanner;
import utils.ReadFiles;

/**
 * @author : Lúcio José Beirão
 * @since : 17/08/2022
 **/
public class Main {

  public static void main(String[] args) throws IOException, InterruptedException {
    rodarBuscaNomes();
    rodarComplementoDNA();
  }

  private static void rodarBuscaNomes() throws IOException, InterruptedException {
    Scanner s = new Scanner(System.in);
    System.out.print("Digite o termo da busca: ");
    String term = s.nextLine();
    long[] seriais = new long[10];
    long[] concs = new long[10];

    for (int i = 0; i < 10; i++) {
      System.out.println("\n Resultado para busca Serial: \n");
      long serial = ReadFiles.readNameFromFilesSerial(term.toLowerCase());
      System.out.println("\n Resultado para busca Concorrente: ");
      long conc = ReadFiles.readNameFromFileConcurrent(term.toLowerCase());

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

  private static void rodarComplementoDNA() throws IOException, InterruptedException {
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
