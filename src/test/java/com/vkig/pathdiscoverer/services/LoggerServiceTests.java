package com.vkig.pathdiscoverer.services;

import com.vkig.pathdiscoverer.dtos.LogItem;
import com.vkig.pathdiscoverer.dtos.UniqueRequestParams;
import com.vkig.pathdiscoverer.models.LogEntity;
import com.vkig.pathdiscoverer.repositories.LogRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class LoggerServiceTests {
    private final LogRepository logRepository = Mockito.mock(LogRepository.class);
    private final LoggerService loggerService = new LoggerService(logRepository);

    @Test
    void logShouldSaveTheParamsTheResultWithTheCurrentTime(){
        LocalDateTime actual = LocalDateTime.now();
        MockedStatic<LocalDateTime> dateTime = Mockito.mockStatic(LocalDateTime.class);
        dateTime.when(LocalDateTime::now).thenReturn(actual);
        ArgumentCaptor<LogEntity> argumentCaptor = ArgumentCaptor.forClass(LogEntity.class);
        when(logRepository.save(any(LogEntity.class))).thenAnswer(i -> i.getArguments()[0]);
        String folder = ".";
        String extension = "txt";
        List<String> result = List.of("1.txt", "2.txt");
        loggerService.log(new UniqueRequestParams(folder, extension), result);
        verify(logRepository, Mockito.times(1)).save(any(LogEntity.class));
        verify(logRepository).save(argumentCaptor.capture());
        assertEquals(folder + ";" + extension, argumentCaptor.getValue().getWhat());
        assertEquals(String.join(";", result)+";", argumentCaptor.getValue().getResult());
        assertEquals(actual, argumentCaptor.getValue().getWhen());
    }

    @Test
    void generateHistoryShouldReturnWithConvertedRepositoryResult(){
        List<LogEntity> logEntities = new ArrayList<>();
        LogEntity logEntity1 = new LogEntity();
        LogEntity logEntity2 = new LogEntity();
        LocalDateTime time1 = LocalDateTime.now();
        LocalDateTime time2 = LocalDateTime.now();
        logEntity1.setWho("user1");
        logEntity1.setWhat("folder;txt");
        logEntity1.setResult("1.txt;2.txt;");
        logEntity1.setWhen(time1);
        logEntities.add(logEntity1);
        logEntity2.setWho("user2");
        logEntity2.setWhat("folder;png");
        logEntity2.setResult("1.png;2.png;");
        logEntity2.setWhen(time2);
        logEntities.add(logEntity2);
        when(logRepository.findAll()).thenReturn(logEntities);
        List<LogItem> logItems = loggerService.generateHistory();
        assertEquals(logItems.get(0).getWho(), "user1");
        assertEquals(logItems.get(1).getWho(), "user2");
        assertEquals(logItems.get(0).getWhat().getFolder(), "folder");
        assertEquals(logItems.get(0).getWhat().getExtension(), "txt");
        assertEquals(logItems.get(1).getWhat().getFolder(), "folder");
        assertEquals(logItems.get(1).getWhat().getExtension(), "png");
        assertEquals(logItems.get(0).getWhen(), time1);
        assertEquals(logItems.get(1).getWhen(), time2);
        assertEquals(logItems.get(0).getResult(), List.of("1.txt", "2.txt"));
        assertEquals(logItems.get(1).getResult(), List.of("1.png", "2.png"));
    }
}
