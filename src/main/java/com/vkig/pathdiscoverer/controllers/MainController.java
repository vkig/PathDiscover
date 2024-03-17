package com.vkig.pathdiscoverer.controllers;

import com.vkig.pathdiscoverer.dtos.LogItem;
import com.vkig.pathdiscoverer.dtos.UniqueRequestParams;
import com.vkig.pathdiscoverer.services.DiscovererService;
import com.vkig.pathdiscoverer.services.GeneratorService;
import com.vkig.pathdiscoverer.services.LoggerService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class MainController {
    private LoggerService loggerService;
    private GeneratorService generatorService;
    private DiscovererService discovererService;

    @GetMapping("/unique")
    @ResponseBody
    public ResponseEntity<?> getUnique(@RequestParam(required = false) String folder, @RequestParam(required = false) String extension){
        if(extension == null || extension.isEmpty() || folder == null || folder.isEmpty()){
            return ResponseEntity.badRequest().body(Map.of("error", "The sent parameters: '" + folder + "' and '" + extension + "' are not correct!"));
        }
        if(extension.startsWith(".")){
            extension = extension.substring(1);
        }
        List<String> uniqueFiles = null;
        try {
            uniqueFiles = discovererService.findUnique(folder, extension);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
        loggerService.log(new UniqueRequestParams(folder, extension), uniqueFiles);
        return ResponseEntity.ok(uniqueFiles);
    }

    @GetMapping("/history")
    @ResponseBody
    public List<LogItem> getHistory(){
        return loggerService.generateHistory();
    }

    @PostMapping("/gen")
    @ResponseBody
    public ResponseEntity<?> createRandomDirectoryTree(@RequestParam(required = false) String root){
        if(root == null || root.isEmpty()){
            return ResponseEntity.badRequest().body(Map.of("error", "The root request parameter can't be null or empty!"));
        }
        try {
            generatorService.generateDirectoryTree(root);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
        return ResponseEntity.ok(Map.of("message", "Random directory tree was successfully created."));
    }
}
