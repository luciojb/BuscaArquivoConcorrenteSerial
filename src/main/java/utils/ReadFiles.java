package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author : Lúcio José Beirão
 * @since : 17/08/2022
 **/
public class ReadFiles {

  public static long readNameFromFilesSerial(String name) throws IOException {
    Path path = Path.of("src/main/resources");
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
      BufferedReader reader;
      try {
        reader = new BufferedReader(new FileReader(file.toAbsolutePath().toString()));
        String line = reader.readLine();
        int nLine = 1;
        while (line != null) {
          // read next line
          line = reader.readLine();
          if (line != null && line.contains(name)) {
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
    Path path = Path.of("src/main/resources");
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
        BufferedReader reader;
        try {
          reader = new BufferedReader(new FileReader(file.toAbsolutePath().toString()));
          String line = reader.readLine();
          int nLine = 0;
          while (line != null) {
            // read next line
            line = reader.readLine();
            if (line != null && line.contains(name)) {
              System.out.println(String.format("Aqruivo: %s | linha: %d | nome: %s", file.getFileName().toString(), nLine, line));
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

}
