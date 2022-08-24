package utils;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author : Lúcio José Beirão
 * @since : 17/08/2022
 **/
public class ReadFiles {

  private static Hashtable<String, String> dnaMapping = new Hashtable<String, String>() {{
    put("A", "T");
    put("T", "A");
    put("C", "G");
    put("G", "C");
  }};

  public static long readNameFromFilesSerial(String name) throws IOException {
    Path path = Path.of("src/main/resources/arquivosNomes");
    if (!Files.isDirectory(path)) {
      throw new IllegalArgumentException("Caminho não é uma pasta!");
    }

    List<Path> result;

    try (Stream<Path> walk = Files.walk(path)) {
      result = walk
          .filter(p -> !Files.isDirectory(p.toAbsolutePath()))
          .filter(f -> f.getFileName().toString().endsWith("txt"))
          .collect(Collectors.toList());
    }
    long time = System.currentTimeMillis();
    for (Path file : result) {
      LineNumberReader reader;
      try {
        reader = new LineNumberReader(new FileReader(file.toAbsolutePath().toString()));
        String line = reader.readLine();
        int nLine = 1;
        while (line != null) {
          // read next line
          line = reader.readLine();
          if (line != null && line.toLowerCase().contains(name)) {
            System.out.println(String.format("Aqruivo: %s | linha: %d | nome: %s", file.getFileName().toString(), nLine, line));
          }
          nLine++;
        }
        reader.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return System.currentTimeMillis() - time;


  }

  public static long readNameFromFileConcurrent(String name) throws IOException, InterruptedException {
    Path path = Path.of("src/main/resources/arquivosNomes");
    if (!Files.isDirectory(path)) {
      throw new IllegalArgumentException("Caminho não é uma pasta!");
    }

    List<Path> result;

    try (Stream<Path> walk = Files.walk(path)) {
      result = walk
          .filter(p -> !Files.isDirectory(p.toAbsolutePath()))
          .filter(f -> f.getFileName().toString().endsWith("txt"))
          .collect(Collectors.toList());
    }
    long time = System.currentTimeMillis();
    List<Thread> threads = new ArrayList<>();
    for (Path file : result) {
      Thread t = new Thread(() -> {
        LineNumberReader reader;
        try {
          reader = new LineNumberReader(new FileReader(file.toAbsolutePath().toString()));
          String line = reader.readLine();
          int nLine = 0;
          while (line != null) {
            // read next line
            line = reader.readLine();
            if (line != null && line.contains(name)) {
              System.out.println(String.format("Aqruivo: %s | linha: %d | nome: %s", file.getFileName().toString(), reader.getLineNumber(), line));
            }
            nLine++;
          }
          reader.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      });
      threads.add(t);
      t.start();
    }
    for (Thread t : threads) {
      t.join();
    }
    return System.currentTimeMillis() - time;

  }


  public static long readDNASerial() throws IOException {
    Path path = Path.of("src/main/resources/arquivosDNA");
    if (!Files.isDirectory(path)) {
      throw new IllegalArgumentException("Caminho não é uma pasta!");
    }

    List<Path> result;

    try (Stream<Path> walk = Files.walk(path)) {
      result = walk
          .filter(p -> !Files.isDirectory(p.toAbsolutePath()))
          .filter(f -> f.getFileName().toString().endsWith("txt"))
          .collect(Collectors.toList());
    }
    long time = System.currentTimeMillis();
    for (Path file : result) {
      LineNumberReader reader;
      List<String> newLines = new ArrayList<>();

      try {
        if (!file.getFileName().toString().contains("complemento")) {
          System.out.printf("Lendo arquivo serial: %s \n", file.getFileName().toString());
          reader = new LineNumberReader(new FileReader(file.toAbsolutePath().toString()));
          String line = reader.readLine();
          int nLine = 1;
          while (line != null) {
            // read next line
            line = reader.readLine();

            if (line != null) {
              newLines.add(completeDNALineSerial(line));
            }
            nLine++;
          }
          reader.close();
          writeDNAFile(path, file.getFileName().toString().replace(".txt", "_complemento.txt"), newLines);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }

    }
    return System.currentTimeMillis() - time;


  }

  private static String completeDNALineSerial(String line) {
    StringBuilder newLine = new StringBuilder();
    for (String c : line.split("")) {
      newLine.append(dnaMapping.get(c));
    }
    return newLine.toString();
  }

  private static void writeDNAFile(Path path, String filename, List<String> lines) {
    try {
      FileWriter fw = new FileWriter(path.toAbsolutePath() + "/" + filename);

      for (String line : lines) {
        fw.write(line);
      }

      fw.close();
    } catch (IOException ie) {
      ie.printStackTrace();
    }
  }

  public static long readDNAFromFileConcurrent() throws IOException, InterruptedException {
    Path path = Path.of("src/main/resources/arquivosDNA");
    if (!Files.isDirectory(path)) {
      throw new IllegalArgumentException("Caminho não é uma pasta!");
    }

    List<Path> result;

    try (Stream<Path> walk = Files.walk(path)) {
      result = walk
          .filter(p -> !Files.isDirectory(p.toAbsolutePath()))
          .filter(f -> f.getFileName().toString().endsWith("txt"))
          .collect(Collectors.toList());
    }
    long time = System.currentTimeMillis();
    List<Thread> threads = new ArrayList<>();
    for (Path file : result) {
      Thread t = new Thread(() -> {
        LineNumberReader reader;
        List<String> newLines = new ArrayList<>();

        try {
          if (!file.getFileName().toString().contains("complemento")) {
            System.out.printf("Lendo arquivo concorrente: %s \n", file.getFileName().toString());
            reader = new LineNumberReader(new FileReader(file.toAbsolutePath().toString()));
            String line = reader.readLine();
            int nLine = 1;
            while (line != null) {
              // read next line
              line = reader.readLine();

              if (line != null) {
                newLines.add(completeDNALineSerial(line));
              }
              nLine++;
            }
            reader.close();
            writeDNAFile(path, file.getFileName().toString().replace(".txt", "_complemento_concorrente.txt"), newLines);
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      });
      threads.add(t);
      t.start();
    }
    for (Thread t : threads) {
      t.join();
    }
    return System.currentTimeMillis() - time;


  }

}
