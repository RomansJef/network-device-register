package com.register.app.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Save Devices By Type Response.")
public class ListNewDevicesResponse {

    @ApiModelProperty(notes = "List of statuses for device save")
    private List<String> saveStatus;
}
