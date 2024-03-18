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

/**
 * Controller class to handle incoming request and accomplish the purpose of the application.
 */
@Controller
@AllArgsConstructor
public class MainController {
    private LoggerService loggerService;
    private GeneratorService generatorService;
    private DiscovererService discovererService;

    /**
     * Endpoint to discover directory tree recursively to find distinct file names within the specified root folder and
     * given extension.
     * @param folder The root of the recursive tree search.
     * @param extension The searched file extension.
     * @return List of distinct file names inside the <i>folder</i> or a JSON object with error message in case of error.
     */
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

    /**
     * Makes it possible to query a list of previous calls on <i>/unique</i> endpoint
     * @return The list of {@link LogItem}s with the logged information
     */
    @GetMapping("/history")
    @ResponseBody
    public List<LogItem> getHistory(){
        return loggerService.generateHistory();
    }

    /**
     * This endpoint will start the generatorService's generateDirectoryTree method in order to create random
     * directory tree with files to be able to test <i>/unique</i> endpoint with randomly generated data.
     * @param root Specifies where to start the random directory tree generation.
     * @return JSON object with success or error message.
     */
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

    /**
     * Redirecting to this documentation.
     * @return Redirection to '/'
     */
    @GetMapping("/docs")
    public String getJavaDocs(){
        return "redirect:/";
    }
}
