/**
 * 헤더명 종류
 */
export type HeaderTitle = 'Cloudtype Manager' | 'Servers' | 'Settings' | 'Server Detail'

/**
 * navbar 이동 탭 종류
 */
export type ActiveTab = 'home' | 'servers' | 'settings'

/**
 * 서버 정보
 */
export type ServerSummary = {
    serverId: number,
    serverName: string,
    activate: boolean
}

/**
 * 서버 상세 정보
 */
export type ServerDetail = ServerSummary & {
    hostingUrl: string,
    managementUrl?: string,
}

/**
 * 서버 추가 요청
 */
export type ServerAddParams = {
    serverName: string,
    serverUrl: string,
    managementUrl: string,
}

/**
 * access-token 재발급 요청
 */
export type AuthRefreshParams = {
    refreshToken: string,
}

/**
 * 인증 토큰 정보
 */
export type AuthTokenInfo = {
    accessToken: string,
    refreshToken: string,
}

/**
 * AppPush 토큰 저장 요청
 */
export type AppPushTokenAddParams = {
    device: string,
    token: string
}

/**
 * API 서버 기본 응답 구조
 */
export type BaseResponse<T> = {
    statusCode: number,
    status: string,
    data: T
}