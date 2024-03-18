package com.vkig.pathdiscoverer.services;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Service to be able to generate random test data for the path discoverer logic.
 */
@Service
public class GeneratorService {
    private static final String[] EXTENSIONS = new String[]{".txt", ".jpg", ".pdf", ".py", ".java", ".png", ".pdf", ".doc"};
    private static final int MIN_DEPTH = 3;
    private static final int MAX_DEPTH = 10;
    private static final int MAX_NUMBER_OF_FILES_IN_FOLDER = 10;
    private static final int MAX_NUMBER_OF_SUBFOLDERS = 5;
    private static final int MAX_FILE_NAME = 100;
    private static final int CHARS_IN_ABC = 26;

    /**
     * Starts the random directory tree generation with a random depth parameter (which is between MIN_DEPTH
     * and MAX_DEPTH constants).
     * @param root The folder where we start the directory and file generation.
     * @throws Exception In case of the root folder is not a valid directory an Exception is thrown.
     */
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

    /**
     * Help the method above to achieve the recursive step of the file and directory generation
     * @param folder The folder where we generate files and directories.
     * @param depth The actual depth level, decreasing during recursive calls.
     * @throws Exception In case of any IOException a simple Exception with some additional message will be thrown.
     */
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

    /**
     * Random file generation logic inside a given directory.
     * @param path The directory where the files are going to be created.
     * @throws Exception In case of the specified path is not referring to a valid directory an Exception will be thrown.
     */
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
