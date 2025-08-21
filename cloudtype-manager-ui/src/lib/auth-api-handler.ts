import {getAuthHeaders} from "@/lib/utils";
import {AuthTokenInfo, BaseResponse} from "@/lib/models";

const apiUrl = process.env.NEXT_PUBLIC_API_SERVER_URL;

/**
 * refresh-token을 기반으로 인증 토큰 정보 재발급
 */
export async function refreshAuthToken() {
    const token = localStorage.getItem('authRefToken');

    if (token === null) {
        window.location.href = '/login'
        return
    }

    const response = await fetch(`${apiUrl}/api/v1/auth/refresh`, {
        method: 'POST',
        headers: getAuthHeaders()
    })

    if (response.status === 200) {
        const data: BaseResponse<AuthTokenInfo> = await response.json();
        localStorage.setItem('authToken', data.data.accessToken);
        localStorage.setItem('authRefToken', data.data.refreshToken);
    } else {
        window.location.href = '/login'
    }
}