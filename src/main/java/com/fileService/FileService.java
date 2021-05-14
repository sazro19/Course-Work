package com.fileService;

import com.model.Thing;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FileService {
    public static List<Thing> read(String inputDir) throws IOException {
        return Files.walk(Paths.get(inputDir))
                .filter(Files::isRegularFile)
                .map(path -> {
                    StringBuilder builder = new StringBuilder();
                    try {
                        Files.lines(path).forEachOrdered(e -> builder.append(" ").append(e));
                    } catch (IOException e) {
                        System.err.println("Unable to read file " + path);
                    }
                    return builder.toString();
                })
                .map(Thing::fromString)
                .collect(Collectors.toList());
    }

    public static void write(List<Thing> things, String outputDir) throws IOException {
        Files.createDirectories(Path.of(outputDir));
        things.forEach(thing -> {
            String path = outputDir + "\\" + thing.getName() + ".scs";
            try {
                File file = new File(path);
//                file.getParentFile().mkdirs();
                file.createNewFile();
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(thing.toScs());
                fileWriter.flush();
            } catch (IOException e) {
                System.err.println("Unable to write file " + path);
            }
        });
    }
}
