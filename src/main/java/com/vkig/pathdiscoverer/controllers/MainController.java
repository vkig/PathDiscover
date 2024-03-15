package com.vkig.pathdiscoverer.controllers;

import com.vkig.pathdiscoverer.dtos.LogItem;
import com.vkig.pathdiscoverer.dtos.UniqueRequestParams;
import com.vkig.pathdiscoverer.services.DiscovererService;
import com.vkig.pathdiscoverer.services.LoggerService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class MainController {
    private LoggerService loggerService;

    @GetMapping("/unique")
    @ResponseBody
    public ResponseEntity<?> getUnique(@RequestParam(required = false) String folder, @RequestParam(required = false) String extension){
        if(extension == null || extension.isEmpty() || folder == null || folder.isEmpty()){
            return ResponseEntity.badRequest().body(Map.of("error:", "The sent parameters: '" + folder + "' and '" + extension + "' are not correct!"));
        }
        if(extension.startsWith(".")){
            extension = extension.substring(1);
        }
        List<String> uniqueFiles = DiscovererService.findUnique(folder, extension);
        loggerService.log(new UniqueRequestParams(folder, extension), uniqueFiles);
        return ResponseEntity.ok(uniqueFiles);
    }

    @GetMapping("/history")
    @ResponseBody
    public List<LogItem> getHistory(){
        return loggerService.generateHistory();
    }
}
