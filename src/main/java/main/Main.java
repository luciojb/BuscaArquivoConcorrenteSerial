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
    Scanner s = new Scanner(System.in);
    System.out.print("Difite o termo da busca: ");
    String term = s.nextLine();
    System.out.println("\n Resultado para busca Serial: \n");
    long serial = ReadFiles.readNameFromFilesSerial(term);
    System.out.println("\n Resultado para busca Concorrente: ");
    long conc = ReadFiles.readNameFromFileConcurrent(term);

    System.out.printf("Tempo serial = %dms; tempo paralelo = %dms; Diferença: %dms", serial, conc, serial - conc);
  }

}
