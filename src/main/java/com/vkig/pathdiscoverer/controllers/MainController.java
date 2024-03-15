package com.vkig.pathdiscoverer.controllers;

import com.vkig.pathdiscoverer.dtos.LogItem;
import com.vkig.pathdiscoverer.services.DiscovererService;
import com.vkig.pathdiscoverer.services.LoggerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class MainController {
    private LoggerService loggerService;

    @GetMapping("/unique")
    @ResponseBody
    public List<String> getUnique(String folder, String extension){
        return DiscovererService.findUnique(folder, extension);
    }

    @GetMapping("/history")
    @ResponseBody
    public List<LogItem> getHistory(){
        List<LogItem> history = new ArrayList<LogItem>();

        return history;
    }
}
