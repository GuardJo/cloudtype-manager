package org.github.guardjo.cloudtype.manager.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/* 고객 문의 요청 */
public record CustomerInquiryRequest(
        @Schema(description = "문의 제목", example = "서비스 문의드립니다.")
        @NotBlank
        String title,

        @Schema(description = "문의 종류", example = "오류")
        @NotBlank
        String inquiryType,

        @Schema(description = "문의 내용", example = "문제가 발생했으니 도와주세요.")
        @NotBlank
        String content
) {
}
