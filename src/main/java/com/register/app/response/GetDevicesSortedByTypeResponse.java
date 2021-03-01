package com.register.app.response;

import com.register.app.entities.ResponseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Response getting all devices sorted by type")
public class GetDevicesSortedByTypeResponse {

    @ApiModelProperty(notes = "List of devices sorted by type")
    private List<ResponseEntity> devicesList;
}
