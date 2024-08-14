//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.pettoyou.server.hospital.controller.HospitalControllerAdmin;
//import com.pettoyou.server.hospital.dto.request.HospitalDto;
//import com.pettoyou.server.hospital.service.HospitalService;
//import com.pettoyou.server.store.dto.BusinessHourDto;
//import com.pettoyou.server.store.dto.RegistrationInfoDto;
//import com.pettoyou.server.store.dto.request.AddressDto;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
//
//import java.net.URI;
//import java.sql.Time;
//import java.util.Collections;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest()
//@ContextConfiguration(classes = {HospitalControllerAdmin.class, HospitalService.class})
//public class HospitalControllerAdminTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private HospitalService hospitalService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private MockMultipartFile hospitalImg;
//    private MockMultipartFile storeInfoImg;
//    private MockMultipartFile thumbnailImg;
//
//    AddressDto addressDto = AddressDto.builder()
//            .zipCode("123-45")
//            .sido("Seoul")
//            .sigungu("Gangnam")
//            .doro("Teheran-ro")
//            .latitude(37.4979)
//            .longitude(127.0276)
//            .build();
//
//
//    BusinessHourDto.Request businessHour = BusinessHourDto.Request.builder()
//            .dayOfWeek(1)
//            .startTime(Time.valueOf("09:00:00"))
//            .endTime(Time.valueOf("18:00:00"))
//            .breakStartTime(Time.valueOf("12:00:00"))
//            .breakEndTime(Time.valueOf("13:00:00"))
//            .openSt(true)
//            .build();
//
//    RegistrationInfoDto.Request registrationInfo = RegistrationInfoDto.Request.builder()
//            .ceoName("John Doe")
//            .ceoPhone("010-1234-5678")
//            .ceoEmail("johndoe@example.com")
//            .businessNumber("123-45-67890")
//            .build();
//
//    HospitalDto hospitalDto = HospitalDto.builder()
//            .hospitalName("Good Hospital")
//            .hospitalPhone("010-1234-5678")
//            .address(addressDto)
//            .businessHours(List.of(businessHour))
//            .registrationInfo(registrationInfo)
//            .build();
//
//    @BeforeEach
//    public void setup() {
//        hospitalImg = new MockMultipartFile("hospitalImg", "hospitalImg.jpg", MediaType.IMAGE_JPEG_VALUE, "hospital image".getBytes());
//        storeInfoImg = new MockMultipartFile("storeInfoImg", "storeInfoImg.jpg", MediaType.IMAGE_JPEG_VALUE, "store info image".getBytes());
//        thumbnailImg = new MockMultipartFile("thumbnailImg", "thumbnailImg.jpg", MediaType.IMAGE_JPEG_VALUE, "thumbnail image".getBytes());
//
//    }
//    private String createExpectedLocation(String id) {
//        // 실제 예상되는 Location URI를 생성합니다.
//        String currentPath = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
//        String newPath = currentPath.replace("/admin", "");
//        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
//                .path(newPath + "/{id}")
//                .buildAndExpand(id)
//                .toUri();
//        return location.toString();
//    }
//
//    @Test
//    public void registerHospital_success() throws Exception {
//        String hospitalId = "1";
//        when(hospitalService.registerHospital(any(List.class), any(MultipartFile.class), any(MultipartFile.class), any(HospitalDto.class)))
//                .thenReturn(hospitalId);
//        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/hospital/admin")
//                        .file(hospitalImg)
//                        .file(storeInfoImg)
//                        .file(thumbnailImg)
//                        .with(user("admin").roles("ADMIN"))
//                        .contentType(MediaType.MULTIPART_FORM_DATA)
//                        .content(objectMapper.writeValueAsString(hospitalDto))
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.message").value("병원 등록 완료!"))
//                .andExpect(header().string("Location", createExpectedLocation(hospitalId)));
//    }
//
//    @Test
//    public void registerHospital_withoutOptionalFiles_success() throws Exception {
//        String hospitalId = "1";
//        when(hospitalService.registerHospital(Collections.emptyList(), null, null, hospitalDto))
//                .thenReturn(hospitalId);
//
//        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/hospital/admin")
//                        .param("hospitalDto", objectMapper.writeValueAsString(hospitalDto))
//                        .contentType(MediaType.MULTIPART_FORM_DATA)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.message").value("병원 등록 완료!"))
//                .andExpect(header().string("Location", URI.create("/hospitals/" + hospitalId).toString()));
//    }
//}
