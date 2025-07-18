import {ServerSummary} from "@/lib/models";
import ServerSummaryCard from "@/components/server-summary-card";

/* 관리 서버 목록 컴포넌트 */
export default function ServerList() {
    // TODO API 응답 데이터 기반으로 변경하기
    const servers: ServerSummary[] = [
        {
            serverId: 1,
            serverName: '서버 1',
            activate: true
        },
        {
            serverId: 2,
            serverName: '서버 2',
            activate: false
        },
        {
            serverId: 3,
            serverName: '서버 3',
            activate: true
        }
    ]

    return (
        <div className='px-6 space-y-4'>
            {servers.map((server: ServerSummary, index: number) => (
                <ServerSummaryCard server={server} key={index}/>
            ))}
        </div>
    )
}