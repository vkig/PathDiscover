package com.vkig.pathdiscoverer.services;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Service
public class GeneratorService {
    private static final String[] EXTENSIONS = new String[]{".txt", ".jpg", ".pdf", ".py", ".java", ".png", ".pdf", ".doc"};
    private static final int MIN_DEPTH = 3;
    private static final int MAX_DEPTH = 10;
    private static final int MAX_NUMBER_OF_FILES_IN_FOLDER = 10;
    private static final int MAX_NUMBER_OF_SUBFOLDERS = 5;
    private static final int MAX_FILE_NAME = 100;
    private static final int CHARS_IN_ABC = 26;

    public void generateDirectoryTree(String root) throws Exception {
        if(!Files.isDirectory(Path.of(root))){
            throw new Exception("The specified path is not a valid directory!");
        }
        try {
            generateDirectoryTreeRecursion(root, new Random().nextInt(MIN_DEPTH, MAX_DEPTH));
        } catch (Exception e){
            throw new Exception("Something went wrong during random directory tree generation!");
        }
    }

    private void generateDirectoryTreeRecursion(String folder, int depth) throws Exception {
        if(depth == 0){
            return;
        }
        generateRandomFiles(folder);
        Set<Character> folderNames = new HashSet<>();
        for(int i = 0; i < new Random().nextInt(0, MAX_NUMBER_OF_SUBFOLDERS); i++){
            int tmpSize = folderNames.size();
            Character nextFolderName = (char) ('a' + new Random().nextInt(0, CHARS_IN_ABC));
            folderNames.add(nextFolderName);
            if(tmpSize < folderNames.size()){
                Path path = Path.of(folder, nextFolderName.toString());
                try {
                    Files.createDirectory(path);
                    generateDirectoryTreeRecursion(path.toString(), depth-1);
                } catch (IOException e){
                    throw new Exception("The subfolder creation at " + path.toString() + " was not successful.");
                }
            }
        }
    }

    private void generateRandomFiles(String path) throws Exception {
        if(!Files.isDirectory(Path.of(path))){
            System.err.println("The specified path is not a valid directory!");
            return;
        }
        Set<Integer> fileNames = new HashSet<>();
        for(int i = 0; i < new Random().nextInt(1, MAX_NUMBER_OF_FILES_IN_FOLDER); i++){
            int tmpSize = fileNames.size();
            Integer nextFileName = new Random().nextInt(1, MAX_FILE_NAME);
            fileNames.add(nextFileName);
            if(tmpSize < fileNames.size()){
                Path filePath = Path.of(path, nextFileName.toString() + EXTENSIONS[new Random().nextInt(0, EXTENSIONS.length)]);
                try {
                    Files.createFile(filePath);
                } catch (IOException e) {
                    throw new Exception("The file creation at " + filePath + " was not successful.");
                }
            }
        }
    }
}
