package com.vkig.pathdiscoverer.services;

import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service to handle recursive directory search for distinct filenames with given extension.
 */
@Service
public class DiscovererService {
    /**
     * This method finds all the distinct filenames with the given extension in the specified root folder recursively.
     * @param folder The root of the search.
     * @param extension The extension that the search takes into account.
     * @return Alphabetically ordered list of distinct filenames.
     * @throws Exception In case of the specified root folder is not a valid directory Exception will be thrown.
     */
    public List<String> findUnique(String folder, String extension) throws Exception {
        if(!Files.isDirectory(Path.of(folder))){
            throw new Exception("The specified path is not a valid directory!");
        }
        return findUniqueRecursion(folder, extension).stream().sorted().toList();
    }

    /**
     * Private recursive method to achieve the recursive logic of the method above.
     * @param folder The folder where the method is during the search.
     * @param extension The file extension that the method takes into account.
     * @return A set with the distinct filenames within the specified folder in any depth.
     */
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
