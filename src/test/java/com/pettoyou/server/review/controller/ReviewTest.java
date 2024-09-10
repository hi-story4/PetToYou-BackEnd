package com.pettoyou.server.review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pettoyou.server.config.security.service.member.PrincipalDetails;
import com.pettoyou.server.constant.dto.ApiResponse;
import com.pettoyou.server.domains.hospital.entity.hospital.Hospital;
import com.pettoyou.server.domains.pet.entity.enums.Species;
import com.pettoyou.server.domains.photo.entity.PhotoData;
import com.pettoyou.server.domains.review.service.ReviewService;
import com.pettoyou.server.domains.member.entity.Member;
import com.pettoyou.server.domains.pet.entity.Pet;
import com.pettoyou.server.domains.review.dto.ReviewRespDto;
import com.pettoyou.server.domains.review.entity.Review;
import com.pettoyou.server.domains.store.entity.Address;
import com.pettoyou.server.domains.store.entity.BusinessHour;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class ReviewTest {

    MockMvc mockMvc;
    @Mock
    private PrincipalDetails principalDetails;

    @Autowired
    EntityManager em;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WebApplicationContext wac;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        // Mock 데이터 설정
        LocalDateTime createdAt = LocalDateTime.of(2023, 1, 1, 10, 0); // 예시 날짜와 시간
        LocalDateTime modifiedAt = LocalDateTime.of(2023, 1, 2, 11, 0); //
        LocalDate birth = LocalDate.of(2023, 1,10);
        ReviewRespDto reviewDto = ReviewRespDto.builder()
                .reviewId(1L)
                .createdAt(createdAt)
                .modifiedAt(modifiedAt)
                .rating(5)
                .treatment("Excellent")
                .price(10000)
                .content("Great experience!")
                .memberId(1L)
                .petName("Bobby")
                .species(Species.ABYSSINIAN)
                .birth(birth)
                .build();

        Page<ReviewRespDto> page = new PageImpl<>(Collections.singletonList(reviewDto), PageRequest.of(0, 10), 1);

        // Mock Service 메서드 호출 설정
        when(reviewService.getReview(anyLong(), any())).thenReturn(page);



        Hospital hospital1 = Hospital.builder()
                .storeId(3L)
                .storeName("hospital2")
                .thumbnail(new PhotoData("bucket", "object", "photoUrl"))
                .storePhone("010-1234-1234")
                .notice("notice")
                .websiteLink("website")
                .additionalServiceTag("additionalServicetags")
                .storeInfo("storeInfo")
                .storeInfoPhoto(new PhotoData("bucket", "object", "photoUrl"))
                .address(new Address("zipCode", "addressDetail", "sido", "sigungu", "eupmyun", "doro", null))
                .businessHours(Arrays.asList(new BusinessHour(1L, 1, Time.valueOf("09:00:00"), Time.valueOf("18:00:00"), null, null, true, null)))
                .build();

        Member member = Member.builder()
                .memberId(1L)
                .build();

        Pet pet = Pet.builder()
                .petId(1L)
                .petName("petName")
                .species(Species.ABYSSINIAN)
                .birth(birth)
                .build();

        Review review = Review.builder()
                .reviewId(1L)
                .rating(5)
                .treatment("treatment")
                .price(10000)
                .memberId(1L)
                .store(hospital1)
                .pet(pet)
                .build();
        principalDetails = mock(PrincipalDetails.class);
        when(principalDetails.getUserId()).thenReturn(1L);
    }
//mockMVC와 데이터

    @MockBean
    ReviewService reviewService;

    @Test
    @Transactional
    public void Get_컨트롤러_테스트() throws Exception {

        PageRequest pageable = PageRequest.of(0, 10);
        // When
        MvcResult result = mockMvc.perform(
                        get("/api/v1/store/{storeId}/review", 1)
                                .param("page", String.valueOf(pageable.getPageNumber()))
                                .param("size", String.valueOf(pageable.getPageSize()))
                                .param("sort", "created_at,desc")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                // Then
                .andExpect(status().isOk())
                .andReturn();
        // 응답 본문 확인
        String responseBody = result.getResponse().getContentAsString();
        ApiResponse<Page<ReviewRespDto>> response = objectMapper.readValue(responseBody,
                objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, objectMapper.getTypeFactory().constructParametricType(Page.class, ReviewRespDto.class)));

        // 응답 상태 및 데이터 검증
        assertThat(response.getHttpStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(response.getData().getContent()).hasSize(1); // 페이지에 포함된 리뷰 개수 확인
        ReviewRespDto returnedReview = response.getData().getContent().get(0);
        System.out.println(returnedReview);
    }


    @Test
    void Get_service_테스트 () {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("created_at"), Sort.Order.desc("rating")));
        Class<?> entity = Review.class;
        // Extract Sort from Pageable
        Sort sort = pageable.getSort();

        Page<ReviewRespDto> resp = reviewService.getReview(1L, pageable);

        System.out.println("페이징 정보");
        System.out.println(resp.getPageable());
        System.out.println(resp.getTotalElements());

        resp.stream().forEach(r -> System.out.println("서비스 테스트" + r));


    }



}
