package com.register.app.entities;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * DTO and DB fields for device table.
 */
@Data
@Entity
@Table(name = "DEVICE_TABLE")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceEntity {

    /**
     * ID.
     */
    @Id
    @Column(name = "ID")
    @NotNull(message = "ID cannot be null")
    private String id;

    /**
     * Device Type.
     */
    @Column(name = "DEVICE_TYPE")
    @NotNull(message = "Device Type cannot be null")
    private String deviceType;

    /**
     * Mac Address.
     */
    @Column(name = "MAC_ADDRESS")
    @NotNull(message = "MacAddress cannot be null")
    private String macAddress;

    /**
     * MacAddress of uplink device.
     */
    @Column(name = "UPLINK_MAC_ADDRESS")
    @NotNull(message = "uplinkMacAddress cannot be null")
    private String uplinkMacAddress;

    /**
     * Date and time when device is registered.
     */
    @Column(name = "PUBLISHED_DATE", nullable = false)
    @NotNull(message = "Date cannot be null")
    private LocalDateTime publishedDate;

    @Override
    public String toString() {
        return "Device{" + "ID: " + id +
                ", DeviceType: '" + deviceType + '\'' +
                ", MacAddress: '" + macAddress + '\'' +
                ", UplinkMacAddress: '" + uplinkMacAddress + '\'' +
                ", PublishedDate: ' " + publishedDate + '\'' +
                '}';
    }
}
