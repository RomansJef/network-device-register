package com.register.app.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Get Device By macAddress")
public class GetDeviceByMacAddressRequest {

    @ApiModelProperty(notes = "macAddress")
    @NotBlank
    private String macAddress;
}
