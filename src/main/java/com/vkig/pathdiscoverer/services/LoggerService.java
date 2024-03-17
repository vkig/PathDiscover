package com.vkig.pathdiscoverer.services;

import com.vkig.pathdiscoverer.dtos.LogItem;
import com.vkig.pathdiscoverer.dtos.UniqueRequestParams;
import com.vkig.pathdiscoverer.models.LogEntity;
import com.vkig.pathdiscoverer.repositories.LogRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class LoggerService {
    private LogRepository logRepository;

    public void log(UniqueRequestParams params, List<String> result){
        LogEntity logEntity = new LogEntity();
        String whoAmIResult = "";
        try {
            ProcessBuilder builder = new ProcessBuilder("whoami");
            Process process = builder.start();
            whoAmIResult = new BufferedReader(new InputStreamReader(process.getInputStream())).readLine();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        logEntity.setWho(whoAmIResult);
        logEntity.setWhen(LocalDateTime.now());
        logEntity.setWhat(params.getFolder() + ";" + params.getExtension());
        StringBuilder stringBuilder = new StringBuilder();
        for(String file : result){
            stringBuilder.append(file).append(";");
        }
        logEntity.setResult(stringBuilder.toString());
        logRepository.save(logEntity);
    }

    public List<LogItem> generateHistory(){
        List<LogEntity> logEntities = logRepository.findAll();
        return logEntities.stream().map(logEntity -> {
            LogItem logItem = new LogItem();
            logItem.setWhen(logEntity.getWhen());
            logItem.setWho(logEntity.getWho());
            logItem.setWhat(new UniqueRequestParams(logEntity.getWhat().split(";")[0], logEntity.getWhat().split(";")[1]));
            List<String> result = new ArrayList<>(Arrays.asList(logEntity.getResult().split(";")));
            logItem.setResult(result);
            return logItem;
        }).toList();
    }
}
