package com.vkig.pathdiscoverer.services;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class GeneratorServiceTest {
    @InjectMocks
    GeneratorService generatorService;
    @Test
    void generateDirectoryTreeShouldThrowExceptionIfRootIsNotValid() throws Exception {
        assertThrows(Exception.class, ()->generatorService.generateDirectoryTree("randomnotexistingdirectory"));
    }

    @Test
    void generateDirectoryTreeShouldPutAtLeastSomethingIntoSpecifiedValidDirectory() throws Exception {
        String stringPath = "myDirectory";
        Path path = Path.of(stringPath);
        Files.createDirectory(path);
        generatorService.generateDirectoryTree(stringPath);
        File directory = new File(stringPath);
        File[] filesInDirectory = directory.listFiles();
        assertNotNull(filesInDirectory);
        assertTrue(filesInDirectory.length > 0);
        FileUtils.deleteDirectory(new File(stringPath));
    }
}
