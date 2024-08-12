package com.pettoyou.server.hospital.dto.request;

import com.pettoyou.server.domains.hospital.dto.request.HospitalQueryCond;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HospitalQueryCondTest {

    @Test
    public void getTagIdList() {
        HospitalQueryCond cond = new HospitalQueryCond(
                List.of(1L, 2L), // businesstHourTagIdList
                List.of(3L, 4L), // serviceTagIdList
                List.of(5L, 6L), // specialtiesTagIdList
                List.of(7L, 8L), // emergencyTagIdList
                null,            // radius (default to 5000)
                "OPEN"           // openCond
        );

        List<Long> expectedTagIdList = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
        assertEquals(expectedTagIdList, cond.getTagIdList());
    }

    @Test
    public void getTagIdList_NULL_Filter_테스트() {
        HospitalQueryCond cond = new HospitalQueryCond(
                List.of(1L, 2L), // businesstHourTagIdList
                null, // serviceTagIdList
                List.of(5L, 6L), // specialtiesTagIdList
                List.of(7L, 8L), // emergencyTagIdList
                null,            // radius (default to 5000)
                "OPEN"           // openCond
        );

        List<Long> expectedTagIdList = List.of(1L, 2L, 5L, 6L, 7L, 8L);
        assertEquals(expectedTagIdList, cond.getTagIdList());
    }

    @Test
    @DisplayName("Labels null일때 null 리턴 테스트")
    public void getTagIdList_NULL_테스트() {
        HospitalQueryCond cond = new HospitalQueryCond(
                null, // businesstHourTagIdList
                null, // serviceTagIdList
                null, // specialtiesTagIdList
                null, // emergencyTagIdList
                null,            // radius (default to 5000)
                "OPEN"           // openCond
        );

        List<Long> expectedTagIdList = null;
        assertEquals(expectedTagIdList, cond.getTagIdList());
    }



}