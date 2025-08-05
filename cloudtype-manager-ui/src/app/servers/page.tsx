'use client'
import {Plus} from "lucide-react";
import ServerList from "@/components/server-list";
import {useRouter} from "next/navigation";

/* 서버 목록 페이지 */
export default function ServerListPage() {
    const router = useRouter()

    const handleGotoAddServer = () => {
        router.push('/servers/add')
    }

    return (
        <div className='bg-slate-800 text-white animate-slide-in'>
            <div className='pt-16 pb-20'>
                {/* 상단 부분 */}
                <div className='flex items-center justify-between px-6 py-4'>
                    <h1 className='text-2xl font-bold'>Servers</h1>
                    <button
                        className='p-2 hover:bg-slate-700 rounded-lg transition-all duration-200 hover:scale-105 active:scale-95'
                        onClick={handleGotoAddServer}
                    >
                        <Plus className='w-6 h-6'/>
                    </button>
                </div>

                {/* 서버 목록 */}
                <ServerList/>
            </div>
        </div>
    )
}