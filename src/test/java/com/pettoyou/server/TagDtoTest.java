//package com.pettoyou.server;
//
//import com.pettoyou.server.domains.hospital.dto.HospitalTagDto;
//import com.pettoyou.server.domains.hospital.entity.Hospital;
//import com.pettoyou.server.domains.hospital.entity.HospitalTag;
//import com.pettoyou.server.domains.hospital.entity.TagMapper;
//import com.pettoyou.server.domains.hospital.entity.enums.HospitalTagType;
//import org.junit.jupiter.api.Test;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//public class TagDtoTest {
//
//
//    @Test
//    void testGroupTagContentsByType() {
//
//        Hospital hospital = new Hospital("Test Hospital");
//
//        List<TagMapper> tagMappers = Arrays.asList(
//                new TagMapper(1l, new HospitalTag(1l, HospitalTagType.SERVICE, "Service Content 1"), hospital),
//                new TagMapper(new HospitalTag(HospitalTagType.BUSINESSHOUR, "Business Hour Content 1"), hospital),
//                new TagMapper(new HospitalTag(HospitalTagType.SPECIALITIES, "Speciality Content 1"), hospital),
//                new TagMapper(new HospitalTag(HospitalTagType.EMERGENCY, "Emergency Content 1"), hospital),
//                new TagMapper(new HospitalTag(HospitalTagType.SERVICE, "Service Content 2"), hospital)
//        );
//
//        HospitalTagDto tagDto = new HospitalTagDto();
//
//        // Act
//        Map<HospitalTagType, List<String>> result = tagDto.groupTagContentsByType(hospitalTagMappers);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(4, result.size());
//        assertEquals(Arrays.asList("Service Content 1", "Service Content 2"), result.get(HospitalTagType.SERVICE));
//        assertEquals(Arrays.asList("Business Hour Content 1"), result.get(HospitalTagType.BUSINESSHOUR));
//        assertEquals(Arrays.asList("Speciality Content 1"), result.get(HospitalTagType.SPECIALITIES));
//        assertEquals(Arrays.asList("Emergency Content 1"), result.get(HospitalTagType.EMERGENCY));
//    }
//}
