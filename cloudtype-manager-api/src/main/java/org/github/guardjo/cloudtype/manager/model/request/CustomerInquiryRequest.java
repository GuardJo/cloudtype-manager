package org.github.guardjo.cloudtype.manager.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/* 고객 문의 요청 */
public record CustomerInquiryRequest(
        @Schema(description = "문의 제목", example = "서비스 문의드립니다.")
        @NotBlank(message = "문의 제목을 입력해주세요.")
        @Size(max = 100, message = "문의 제목은 최대 100자까지 입력 가능합니다.")
        String title,

        @Schema(description = "문의 종류", example = "오류")
        @NotBlank
        String inquiryType,

        @Schema(description = "문의 내용", example = "문제가 발생했으니 도와주세요.")
        @Size(max = 1000, message = "문의 내용은 최대 1000자까지 입력 가능합니다.")
        String content
) {
}
