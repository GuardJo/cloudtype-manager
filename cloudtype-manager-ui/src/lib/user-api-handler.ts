import {BaseResponse, UserInfo} from "@/lib/models";
import {getAuthHeaders, validateResponse} from "@/lib/utils";

const apiUrl = process.env.NEXT_PUBLIC_API_SERVER_URL;

/**
 * 현재 접속 회원 정보 조회 API 요청
 */
export async function getUserInfo(): Promise<BaseResponse<UserInfo>> {
    const response = await fetch(`${apiUrl}/api/v1/users/me`, {
        method: 'GET',
        headers: getAuthHeaders()
    })

    return validateResponse(response, getUserInfo)
}