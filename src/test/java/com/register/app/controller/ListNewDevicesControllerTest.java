package com.register.app.controller;

import com.register.app.RegisterController;
import com.register.app.request.NewDeviceRequest;
import com.register.app.response.ListNewDevicesResponse;
import com.register.app.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class ListNewDevicesControllerTest {
    private final iListNewDevicesService listNewDevicesService = mock(iListNewDevicesService.class);
    private final iGetAllDevicesTreeService getAllDevicesTreeService = mock(iGetAllDevicesTreeService.class);
    private final iGetDeviceByMacAddressService getDeviceByMacAddressService = mock(iGetDeviceByMacAddressService.class);
    private final iGetDevicesSortedByTypeService getDevicesSortedByTypeService = mock(iGetDevicesSortedByTypeService.class);
    private final iGetDeviceTopologyService getDeviceTopologyService = mock(iGetDeviceTopologyService.class);

    private final ListNewDevicesResponse listNewDevicesResponse = new ListNewDevicesResponse();
    private final List<String> statusList = new ArrayList<>();
    private final RegisterController registerController =
            new RegisterController(
                    listNewDevicesService,
                    getAllDevicesTreeService,
                    getDeviceByMacAddressService,
                    getDevicesSortedByTypeService,
                    getDeviceTopologyService);
    private NewDeviceRequest newDeviceRequest = new NewDeviceRequest();
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new Exception(), registerController).build();

        // request
        newDeviceRequest =
                NewDeviceRequest.builder()
                        .deviceType("Switch")
                        .macAddress("A1:B2:C3:D4:E5")
                        .uplinkMacAddress("01:02:03:04:05")
                        .build();
        // response
        statusList.add("Device is saved");
        listNewDevicesResponse.setSaveStatus(statusList);
    }

    @Test
    public void whenSaveNewDevicesSuccess() throws Exception {
        List<NewDeviceRequest> newDeviceRequests = new ArrayList<>();
        newDeviceRequests.add(newDeviceRequest);
        when(listNewDevicesService.executeRequest(newDeviceRequests)).thenReturn(listNewDevicesResponse);
        ObjectMapper mapper = new ObjectMapper();

        mockMvc
                .perform(
                        post("/newDevices")
                                .characterEncoding("utf-8")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(newDeviceRequests)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.saveStatus").exists())
                .andDo(print());
    }

    @Test
    public void whenSaveDevicesBadRequest() throws Exception {
        mockMvc
                .perform(
                        post("/newDevices")
                                .characterEncoding("utf-8")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("null"))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }
}
