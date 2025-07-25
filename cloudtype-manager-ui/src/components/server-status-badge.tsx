import {Check} from "lucide-react";

/* 서버 상태 뱃지 컴포넌트 구현 */
export default function ServerStatusBadge({activate}: ServerStatusBadgeProps) {
    return (
        <div className='flex items-center justify-between bg-slate-700 rounded-xl p-4 border border-slate-600'>
            <div className='flex items-center space-x-3'>
                <div className={`w-3 h-3 rounded-full ${activate ? 'bg-green-400 animate-pulse' : 'bg-red-400'}`}/>
                <span className={`text-lg font-medium ${activate ? 'text-green-400' : 'text-red-400'}`}>
                    {activate ? 'Online' : 'Offline'}
                </span>
            </div>
            {activate && <Check className='w-6 h-6 text-green-400'/>}
        </div>
    )
}

interface ServerStatusBadgeProps {
    activate: boolean,
}