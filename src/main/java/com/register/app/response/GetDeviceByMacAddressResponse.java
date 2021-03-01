package com.register.app.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Get Devices By Type Response.")
public class GetDeviceByMacAddressResponse {

    @ApiModelProperty(notes = "Type of device")
    private String deviceType;

    @ApiModelProperty(notes = "MacAddress of device")
    private String macAddress;
}
