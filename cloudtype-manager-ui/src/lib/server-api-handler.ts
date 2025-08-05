import {BaseResponse, ServerDetail, ServerSummary} from "@/lib/models";
import {getAuthHeaders, validateResponse} from "@/lib/utils";

const apiUrl = process.env.NEXT_PUBLIC_API_SERVER_URL;

/**
 * 서버 목록 조회 API 요청
 */
export async function getServers(): Promise<BaseResponse<ServerSummary[]>> {
    const response = await fetch(`${apiUrl}/api/v1/servers`, {
        method: 'GET',
        headers: getAuthHeaders(),
    });

    return validateResponse(response);
}

/**
 * 서버 상세 정보 조회 API 요청
 */
export async function getServerDetail(serverId: number): Promise<BaseResponse<ServerDetail>> {
    const response = await fetch(`${apiUrl}/api/v1/servers/${serverId}`, {
        method: 'GET',
        headers: getAuthHeaders(),
    })

    return validateResponse(response);
}