package com.vkig.pathdiscoverer.services;

import com.vkig.pathdiscoverer.dtos.LogItem;
import com.vkig.pathdiscoverer.dtos.UniqueRequestParams;
import com.vkig.pathdiscoverer.models.LogEntity;
import com.vkig.pathdiscoverer.repositories.LogRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Logger service to be able to track which WebApplication got the incoming request on <i>/unique</i> endpoint
 * Also tracking the time of the endpoint call, the request parameters and the result of the PathDiscovery logic.
 */
@Service
public class LoggerService {
    private final LogRepository logRepository;

    public LoggerService(LogRepository logRepository){
        this.logRepository = logRepository;
    }

    /**
     * This method is to track who, when, with what parameters called the <i>/unique</i> endpoint
     * and what was the result of the PathDiscovery logic.
     * @param params The request params in a wrapper object.
     * @param result The list of distinct filenames (result of the discoverer logic).
     */
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

    /**
     * This method maps the entity that is stored in the database ({@link LogEntity}) to 'user-friendly'
     * format ({{@link LogItem}})
     * @return The list of 'user-friendly' log items.
     */
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
