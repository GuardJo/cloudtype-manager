import {getAuthHeaders, validateResponse} from "@/lib/utils";
import {AppPushTokenAddParams, BaseResponse} from "@/lib/models";

const apiUrl = process.env.NEXT_PUBLIC_API_SERVER_URL;

/**
 * AppPush 토큰 추가 API 요청
 * @param token AppPush token
 */
export async function addAppPushToken(token: string): Promise<BaseResponse<string>> {
    const payload: AppPushTokenAddParams = {
        device: 'web',
        token: token,
    };

    const response = await fetch(`${apiUrl}/api/v1/notifications/push-token`, {
        method: 'POST',
        headers: getAuthHeaders(),
        body: JSON.stringify(payload)
    });

    return validateResponse(response, addAppPushToken, token)
}