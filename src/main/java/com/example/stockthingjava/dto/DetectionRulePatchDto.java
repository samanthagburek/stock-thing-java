package com.example.stockthingjava.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class DetectionRulePatchDto {

    private String name;
    private String description;
    private Integer threshold;
    private Boolean enabled;

}

