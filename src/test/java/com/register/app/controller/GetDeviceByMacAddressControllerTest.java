package com.register.app.controller;

import com.register.app.request.GetDeviceByMacAddressRequest;
import com.register.app.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.register.app.entities.DeviceEntity;
import com.register.app.RegisterController;
import com.register.app.response.GetDeviceByMacAddressResponse;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class GetDeviceByMacAddressControllerTest {
    private final ListNewDevicesService listNewDevicesService = mock(ListNewDevicesService.class);
    private final GetAllDevicesTreeService getAllDevicesTreeService = mock(GetAllDevicesTreeService.class);
    private final GetDeviceByMacAddressService getDeviceByMacAddressService = mock(GetDeviceByMacAddressService.class);
    private final GetDevicesSortedByTypeService getDevicesSortedByTypeService = mock(GetDevicesSortedByTypeService.class);
    private final GetDeviceTopologyService getDeviceTopologyService = mock(GetDeviceTopologyService.class);

    private GetDeviceByMacAddressResponse getDeviceByMacAddressResponse;
    private final RegisterController registerController =
            new RegisterController(
                    listNewDevicesService,
                    getAllDevicesTreeService,
                    getDeviceByMacAddressService,
                    getDevicesSortedByTypeService,
                    getDeviceTopologyService);
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new Exception(), registerController).build();
        // entity
        DeviceEntity deviceEntity = DeviceEntity.builder()
                .id(UUID.randomUUID().toString())
                .deviceType("Switch")
                .macAddress("A1:B2:C3:D4:E5:E6")
                .uplinkMacAddress("01:02:03:04:05:06")
                .publishedDate(LocalDateTime.now())
                .build();
        // response
        getDeviceByMacAddressResponse = GetDeviceByMacAddressResponse.builder()
                .deviceType(deviceEntity.getDeviceType())
                .macAddress(deviceEntity.getMacAddress())
                .build();
    }

    @Test
    public void whenGetDevicesByTypeSuccess() throws Exception {
        GetDeviceByMacAddressRequest getDeviceByMacAddressRequest =
                GetDeviceByMacAddressRequest.builder().macAddress("A1:B2:C3:D4:E5:E6").build();
        // when
        when(getDeviceByMacAddressService.executeRequest(getDeviceByMacAddressRequest))
                .thenReturn(getDeviceByMacAddressResponse);
        ObjectMapper mapper = new ObjectMapper();

        // then
        mockMvc
                .perform(
                        get("/deviceByMacAddress")
                                .characterEncoding("utf-8")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(getDeviceByMacAddressRequest)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.deviceType").exists())
                .andExpect(jsonPath("$.macAddress").exists())
                .andDo(print());
    }

    @Test
    public void whenGetDevicesByTypeEmptyRequest() throws Exception {
        GetDeviceByMacAddressRequest getDeviceByMacAddressRequest =
                GetDeviceByMacAddressRequest.builder().macAddress("").build();
        ObjectMapper mapper = new ObjectMapper();

        mockMvc
                .perform(
                        get("/deviceByMacAddress")
                                .characterEncoding("utf-8")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(getDeviceByMacAddressRequest)))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }
}
