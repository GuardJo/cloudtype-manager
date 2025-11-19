import {http, HttpResponse} from "msw";
import {AuthTokenInfo, BaseResponse, ServerDetail, ServerSummary} from "@/lib/models";
import {AUTH_TOKEN_KEY} from "@/lib/constants";

const mockApiServerUrl = process.env.NEXT_PUBLIC_API_SERVER_URL;

export const handlers = [
    http.get(`${mockApiServerUrl}/api/v1/servers`, () => {
        const serverList: BaseResponse<ServerSummary[]> = {
            statusCode: 200,
            status: 'OK',
            data: [
                {
                    serverId: 1,
                    serverName: '서버 1',
                    activate: true,
                },
                {
                    serverId: 2,
                    serverName: '서버 2',
                    activate: false,
                },
            ]
        }
        return HttpResponse.json(serverList);
    }),
    http.get(`${mockApiServerUrl}/api/v1/servers/:id`, ({params}) => {
        const serverId = params.id;

        if (serverId === '999') {
            const notFoundData: BaseResponse<string> = {
                statusCode: 404,
                status: 'NotFound',
                data: 'NotFound'
            }

            return new HttpResponse(JSON.stringify(notFoundData), {
                status: 404,
                statusText: 'NotFound'
            })
        } else if (serverId === 'error') {
            throw new Error('Test Error')
        } else if (serverId === 'token') {
            const accessToken = localStorage.getItem(AUTH_TOKEN_KEY);

            if (accessToken === null || accessToken !== 'accessToken22') {
                const unauthorizedData: BaseResponse<string> = {
                    statusCode: 401,
                    status: 'Unauthorized',
                    data: 'Unauthorized'
                }

                return new HttpResponse(JSON.stringify(unauthorizedData), {
                    status: 401,
                    statusText: 'Unauthorized'
                })
            }
        }

        const serverDetail: BaseResponse<ServerDetail> = {
            statusCode: 200,
            status: 'OK',
            data: {
                serverId: Number(serverId),
                serverName: 'Server A',
                activate: true,
                hostingUrl: 'https://naver.com',
                managementUrl: 'https://google.com'
            }
        }

        return HttpResponse.json(serverDetail);
    }),
    http.post(`${mockApiServerUrl}/api/v1/servers`, () => {
        const successes: BaseResponse<string> = {
            statusCode: 200,
            status: 'OK',
            data: 'Successes'
        }

        return HttpResponse.json(successes);
    }),
    http.post(`${mockApiServerUrl}/api/v1/auth/refresh`, () => {
        const authTokenInfo: BaseResponse<AuthTokenInfo> = {
            statusCode: 200,
            status: 'OK',
            data: {
                accessToken: 'accessToken22',
                refreshToken: 'refreshToken22'
            }
        }

        return HttpResponse.json(authTokenInfo);
    }),
    http.post(`${mockApiServerUrl}/api/v1/notifications/push-token`, () => {
        const successes: BaseResponse<string> = {
            statusCode: 200,
            status: 'OK',
            data: 'Successes'
        }

        return HttpResponse.json(successes);
    })
]