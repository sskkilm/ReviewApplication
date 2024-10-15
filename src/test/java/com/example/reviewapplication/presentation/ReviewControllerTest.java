package com.example.reviewapplication.presentation;

import com.example.reviewapplication.application.ReviewService;
import com.example.reviewapplication.dto.ReviewCreateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ReviewService reviewService;

    @Test
    void 리뷰_생성_시_요청부의_필드들은_Null_일_수_없다() throws Exception {
        //given
        String jsonData = objectMapper.writeValueAsString(
                ReviewCreateRequest.builder()
                        .userId(null)
                        .score(null)
                        .content(null)
                        .build()
        );
        MockMultipartFile file = new MockMultipartFile(
                "data", null, String.valueOf(MediaType.APPLICATION_JSON), jsonData.getBytes()
        );

        //when
        ResultActions perform = mockMvc.perform(multipart("/products/1/reviews")
                .file(file)
                .contentType(MediaType.MULTIPART_FORM_DATA)
        );

        //then
        perform
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.userId").value("must not be null"))
                .andExpect(jsonPath("$.score").value("must not be null"))
                .andExpect(jsonPath("$.content").value("must not be null"))
                .andDo(print());
    }

    @Test
    void 리뷰_생성_시_평점은_1이상_이어야_한다() throws Exception {
        //given
        String jsonData = objectMapper.writeValueAsString(
                ReviewCreateRequest.builder()
                        .userId(1L)
                        .score(0)
                        .content("content")
                        .build()
        );
        MockMultipartFile file = new MockMultipartFile(
                "data", null, String.valueOf(MediaType.APPLICATION_JSON), jsonData.getBytes()
        );

        //when
        ResultActions perform = mockMvc.perform(multipart("/products/1/reviews")
                .file(file)
                .contentType(MediaType.MULTIPART_FORM_DATA)
        );
    }

    @Test
    void 리뷰_생성_시_평점은_5이하_이어야_한다() throws Exception {
        //given
        String jsonData = objectMapper.writeValueAsString(
                ReviewCreateRequest.builder()
                        .userId(1L)
                        .score(6)
                        .content("content")
                        .build()
        );
        MockMultipartFile file = new MockMultipartFile(
                "data", null, String.valueOf(MediaType.APPLICATION_JSON), jsonData.getBytes()
        );

        //when
        ResultActions perform = mockMvc.perform(multipart("/products/1/reviews")
                .file(file)
                .contentType(MediaType.MULTIPART_FORM_DATA)
        );

        //then
        perform
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.score").value("must be less than or equal to 5"))
                .andDo(print());
    }

    @Test
    void 리뷰_조회_시_cursor와_size는_0보다_커야_한다() throws Exception {
        //given

        //when
        ResultActions perform = mockMvc.perform(
                get("/products/1/reviews")
                        .param("cursor", "0")
                        .param("size", "0")
        );

        //then
        perform
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("cursor and size must be positive"))
                .andDo(print());

    }

}