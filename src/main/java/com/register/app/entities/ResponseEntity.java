package com.register.app.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Response entity")
public class ResponseEntity {
    @ApiModelProperty(notes = "Type of device")
    private String deviceType;

    @ApiModelProperty(notes = "MacAddress of device")
    private String macAddress;
}
