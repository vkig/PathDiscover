package com.vkig.pathdiscoverer.services;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class DiscovererServiceTests {
    DiscovererService discovererService = new DiscovererService();
    @Test
    void findUniqueShouldThrowExceptionIfRootIsNotValid() throws Exception {
        assertThrows(Exception.class, ()->discovererService.findUnique("randomnotexistingdirectory", "txt"));
    }

    @Test
    void findUniqueShouldFindThePreCreatedFiles() throws Exception {
        String stringPath = "myDirectory";
        Files.createDirectory(Path.of(stringPath));
        Files.createDirectory(Path.of(stringPath,"A"));
        Files.createDirectory(Path.of(stringPath,"B"));
        Files.createDirectory(Path.of(stringPath,"C"));
        Files.createFile(Path.of(stringPath, "1.txt"));
        Files.createFile(Path.of(stringPath, "2.txt"));
        Files.createFile(Path.of(stringPath, "A", "2.txt"));
        Files.createFile(Path.of(stringPath, "A", "3.txt"));
        Files.createFile(Path.of(stringPath, "B", "3.txt"));
        Files.createFile(Path.of(stringPath, "B", "4.txt"));
        Files.createFile(Path.of(stringPath, "C", "4.txt"));
        Files.createFile(Path.of(stringPath, "C", "5.txt"));
        List<String> result = discovererService.findUnique(stringPath, "txt");
        assertEquals(result, List.of("1.txt", "2.txt", "3.txt", "4.txt", "5.txt"));
        FileUtils.deleteDirectory(new File(stringPath));
    }
}
