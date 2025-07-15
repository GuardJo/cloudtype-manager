/* 서버 상세 정보 페이지 */
export default async function ServerDetailPage({params}: { params: Promise<{ serverId: number }> }) {
    const {serverId} = await params

    return (
        /* TODO 컴포넌트 구현 예정 */
        <div className="flex flex-col items-center justify-center w-full">
            <h1>Server(id = {serverId}) Detail Page</h1>
        </div>
    )
}