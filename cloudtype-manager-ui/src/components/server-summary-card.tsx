'use client'

import {ServerSummary} from "@/lib/models";
import {Database} from "lucide-react";
import {useRouter} from "next/navigation";

/* 서버 요약 정보 카드 컴포넌트 */
export default function ServerSummaryCard({server}: ServerSummaryCardProps) {
    const router = useRouter()

    const handleOpenServerDetail = () => {
        router.push(`/servers/${server.serverId}`)
    }

    return (
        <button
            onClick={handleOpenServerDetail}
            className='w-full text-white bg-slate-700 hover:bg-slate-600 active:bg-slate-500 rounded-xl p-4 transition-all duration-200 transform hover:scale-[1.02] active:scale-[0.98] border border-slate-600 hover:border-slate-500 animate-fade-in-up'
            style={{
                animationDelay: '100ms'
            }}>
            <div className='flex items-center space-x-4'>
                {/* Server Icon */}
                <div className='bg-slate-600 p-3 rounded-lg transition-colors'>
                    <Database className='w-6 h-6'/>
                </div>

                {/* Server Info */}
                <div className='flex-1 text-left'>
                    <h3 className='font-semibold text-lg mb-1'>{server.serverName}</h3>
                    <div className='flex items-center space-x-2'>
                        <div className={`w-2 h-2 rounded-full ${server.activate ? 'bg-green-400' : 'bg-red-400'}`}/>
                        <span className={`text-sm ${server.activate ? 'text-green-400-400' : 'text-red-400'}`}>
                        {server.activate ? 'On' : 'Off'}
                    </span>
                    </div>
                </div>

                {/* Arrow indicator */}
                <div className='text-slate-400 transition-transform duration-200 group-hover:translate-x-1'>
                    <svg className='w-5 h-5' fill='none' viewBox='0 0 24 24'>
                        <path strokeLinecap='round' strokeLinejoin='round' strokeWidth={2} d='M9 5l7 7-7 7'/>
                    </svg>
                </div>
            </div>
        </button>
    )
}

interface ServerSummaryCardProps {
    server: ServerSummary
}