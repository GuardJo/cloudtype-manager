import {User} from "lucide-react";

/* 계정 정보 섹션 컴포넌트 */
export default function AccountInfoSection({userName, userEmail}: AccountInfoSectionProps) {
    return (
        <div className='flex items-center gap-4 mb-8'>
            <div
                className='w-16 h-16 rounded-full bg-gradient-to-br from-slate-300 to-slate-400 flex items-center justify-center overflow-hidden'>
                <User className='w-10 h-10 text-slate-600'/>
            </div>
            <div>
                <p className='font-semibold text-lg'>{userName}</p>
                <p className='text-slate-400 text-sm'>{userEmail}</p>
            </div>
        </div>
    )
}

interface AccountInfoSectionProps {
    userName: string,
    userEmail: string,
}