import {type ClassValue, clsx} from "clsx"
import {twMerge} from "tailwind-merge"
import {BaseResponse} from "@/lib/models";
import {refreshAuthToken} from "@/lib/auth-api-handler";
import {AUTH_TOKEN_KEY} from "@/lib/constants";

export function cn(...inputs: ClassValue[]) {
    return twMerge(clsx(inputs))
}

/**
 * 인증 토큰 Header 반환
 */
export const getAuthHeaders = () => {
    const token = localStorage.getItem(AUTH_TOKEN_KEY);
    return {
        "Content-Type": "application/json",
        ...(token && {"Authorization": `Bearer ${token}`}),
    };
};

/**
 * API 응답 데이터 검증
 * @param response API 응답 데이터
 * @param apiCaller API 요청 메소드
 */
export const validateResponse = async <T, P extends unknown[]>(response: Response, apiCaller: (...params: P) => Promise<BaseResponse<T>>, ...params: P): Promise<BaseResponse<T>> => {
    const httpStatus = response.status;

    if (httpStatus === 401) {
        await refreshAuthToken()
        return apiCaller(...params)
    }

    return response.json()
}