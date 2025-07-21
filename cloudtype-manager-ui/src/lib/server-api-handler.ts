import {BaseResponse, ServerSummary} from "@/lib/models";

const apiUrl = process.env.NEXT_PUBLIC_API_SERVER_URL;

/**
 * 서버 목록 조회 API 요청
 */
export async function getServers(): Promise<BaseResponse<ServerSummary[]>> {
    const response = await fetch(`${apiUrl}/api/v1/servers`, {
        method: 'GET',
        headers: {
            "Content-Type": "application/json",
        }
    });

    return response.json();
}