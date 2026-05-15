import {getAuthHeaders} from "@/lib/utils";
import {AuthRefreshParams, AuthTokenInfo, BaseResponse, LogoutParams} from "@/lib/models";
import {AUTH_REFRESH_TOKEN_KEY, AUTH_TOKEN_KEY} from "@/lib/constants";

const apiUrl = process.env.NEXT_PUBLIC_API_SERVER_URL;

/**
 * refresh-token을 기반으로 인증 토큰 정보 재발급
 */
export async function refreshAuthToken() {
    const token = localStorage.getItem(AUTH_REFRESH_TOKEN_KEY);

    if (token === null) {
        window.location.href = '/login'
        return
    }

    const refreshRequest: AuthRefreshParams = {
        refreshToken: token,
    }
    const response = await fetch(`${apiUrl}/api/v1/auth/refresh`, {
        method: 'POST',
        headers: getAuthHeaders(),
        body: JSON.stringify(refreshRequest),
    })

    if (response.status === 200) {
        const data: BaseResponse<AuthTokenInfo> = await response.json();
        localStorage.setItem(AUTH_TOKEN_KEY, data.data.accessToken);
        localStorage.setItem(AUTH_REFRESH_TOKEN_KEY, data.data.refreshToken);
    } else {
        localStorage.removeItem(AUTH_TOKEN_KEY);
        localStorage.removeItem(AUTH_REFRESH_TOKEN_KEY);
        window.location.href = '/login'
    }
}

/**
 * 현재 지닌 access-token 기반 로그아웃 처리
 */
export async function logout(userId: string) {
    const accessToken = localStorage.getItem(AUTH_TOKEN_KEY);
    const refreshToken = localStorage.getItem(AUTH_REFRESH_TOKEN_KEY);

    const logoutRequest: LogoutParams = {
        username: userId,
        accessToken: accessToken!,
        refreshToken: refreshToken!,
    }
}