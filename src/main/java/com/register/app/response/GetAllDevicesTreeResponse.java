package com.register.app.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Response to get all devices in tree model.")
public class GetAllDevicesTreeResponse {

    @ApiModelProperty(notes = "List of devices")
    private Map<String, Map<String, List<String>>> devicesList;
}
