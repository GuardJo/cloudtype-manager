import ServerDetailContent from "@/components/server-detail-content";

/* 서버 상세 정보 페이지 */
export default async function ServerDetailPage({params}: { params: Promise<{ serverId: number }> }) {
    const {serverId} = await params

    return (
        <div className='min-h-screen bg-slate-800 text-white animate-slide-in'>
            <ServerDetailContent serverId={serverId}/>
        </div>
    )
}