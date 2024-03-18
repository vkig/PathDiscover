package com.vkig.pathdiscoverer.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO to support {@link LogItem} DTO's 'user-friendly' format.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UniqueRequestParams {
    private String folder;
    private String extension;
}
