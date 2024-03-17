package com.vkig.pathdiscoverer.services;

import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Service
public class DiscovererService {
    public List<String> findUnique(String folder, String extension) throws Exception {
        if(!Files.isDirectory(Path.of(folder))){
            throw new Exception("The specified path is not a valid directory!");
        }
        return findUniqueRecursion(folder, extension).stream().sorted().toList();
    }

    private Set<String> findUniqueRecursion(String folder, String extension){
        File path = new File(folder);
        File[] filesInFolder = path.listFiles();
        Set<String> uniqueFiles = new HashSet<>();
        if(filesInFolder == null){
            return uniqueFiles;
        }
        for(File file : filesInFolder){
            if(file.isDirectory()){
                uniqueFiles.addAll(findUniqueRecursion(Paths.get(folder, file.getName()).toString(), extension));
            } else if(file.getName().toLowerCase().endsWith("." + extension)) {
                uniqueFiles.add(file.getName());
            }
        }
        return uniqueFiles;
    }
}
