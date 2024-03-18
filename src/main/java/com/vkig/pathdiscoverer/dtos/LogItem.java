package com.vkig.pathdiscoverer.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * A DTO to return log information in 'user-friendly' format.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogItem {
    private String who;
    private LocalDateTime when;
    private UniqueRequestParams what;
    private List<String> result;
}
