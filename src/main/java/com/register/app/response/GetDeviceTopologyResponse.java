package com.register.app.response;

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
@ApiModel(description = "Response to get device topology.")
public class GetDeviceTopologyResponse {

    @ApiModelProperty(notes = "List of devices")
    private String devices;
}
