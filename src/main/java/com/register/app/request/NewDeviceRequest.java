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
@ApiModel(description = "Request saving new Device")
public class NewDeviceRequest {
    @ApiModelProperty(notes = "Device Type")
    @NotBlank
    private String deviceType;

    @ApiModelProperty(notes = "Mac Address")
    @NotBlank
    private String macAddress;

    @ApiModelProperty(notes = "Uplink Mac Address")
    @NotBlank
    private String uplinkMacAddress;
}
