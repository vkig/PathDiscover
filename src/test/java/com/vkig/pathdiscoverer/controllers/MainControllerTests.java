package com.vkig.pathdiscoverer.controllers;

import com.vkig.pathdiscoverer.dtos.LogItem;
import com.vkig.pathdiscoverer.dtos.UniqueRequestParams;
import com.vkig.pathdiscoverer.services.DiscovererService;
import com.vkig.pathdiscoverer.services.GeneratorService;
import com.vkig.pathdiscoverer.services.LoggerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class MainControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private GeneratorService generatorService;
    @MockBean
    private DiscovererService discovererService;
    @MockBean
    private LoggerService loggerService;

    @Test
    void getUniqueShouldReturnOkWithListOfUniqueFileNames() throws Exception {
        when(discovererService.findUnique(".", "txt")).thenReturn(List.of("1.txt", "2.txt"));
        this.mockMvc.perform(get("/unique?folder=.&extension=txt")).andExpect(status().isOk())
                .andExpect(content().json("['1.txt', '2.txt']"));
    }

    @Test
    void getUniqueShouldReturnBadRequestWithErrorMessageWithNulls() throws Exception {
        this.mockMvc.perform(get("/unique")).andExpect(status().isBadRequest())
                .andExpect(content()
                        .json("{'error': 'The sent parameters: \\'null\\' and \\'null\\' are not correct!'}"))
                .andReturn();
    }

    @Test
    void getUniqueShouldReturnBadRequestWithErrorMessageWithEmptyStrings() throws Exception {
        this.mockMvc.perform(get("/unique?folder=&extension=")).andExpect(status().isBadRequest())
                .andExpect(content()
                        .json("{'error': 'The sent parameters: \\'\\' and \\'\\' are not correct!'}"))
                .andReturn();
    }

    @Test
    void getUniqueShouldWorkWithAndWithoutDotResultingOk() throws Exception {
        when(discovererService.findUnique(".", "txt")).thenReturn(List.of("1.txt", "2.txt"));
        MvcResult mvcResult1 = this.mockMvc.perform(get("/unique?folder=.&extension=txt")).andExpect(status().isOk())
                .andExpect(content().json("['1.txt', '2.txt']")).andReturn();
        MvcResult mvcResult2 = this.mockMvc.perform(get("/unique?folder=.&extension=.txt")).andExpect(status().isOk())
                .andExpect(content().json("['1.txt', '2.txt']")).andReturn();
        assertEquals(mvcResult1.getResponse().getContentAsString(), mvcResult2.getResponse().getContentAsString());
    }

    @Test
    void getHistoryShouldReturnWithEmptyList() throws Exception {
        when(loggerService.generateHistory()).thenReturn(List.of());
        this.mockMvc.perform(get("/history")).andExpect(content().json("[]"));
    }

    @Test
    void getHistoryShouldReturnWithGivenList() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        when(loggerService.generateHistory()).thenReturn(List.of(new LogItem("user", now,
                new UniqueRequestParams(".", "txt"), List.of("1.txt", "2.txt"))));
        this.mockMvc.perform(get("/history")).andExpect(content()
                .json("[{'who': 'user', 'when': '"+ now.toString()
                        +"', 'what': {'folder': '.', 'extension': 'txt'}, 'result': ['1.txt','2.txt']}]"));
    }
}
