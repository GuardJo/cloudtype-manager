import ServerCreateForm from "@/components/server-create-form";

/* 관리 서버 추가 페이지 */
export default function AddServerPage() {
    return (
        <div className='min-h-screen bg-slate-800 text-white animate-slide-in'>
            <ServerCreateForm/>
        </div>
    )
}