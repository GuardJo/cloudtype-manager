/**
 * 헤더명 종류
 */
export type HeaderTitle = 'Cloudtype Manager' | 'Servers' | 'Settings'

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