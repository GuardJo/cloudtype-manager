'use client'

import ServerNotFoundIllustration from "@/components/server-not-found-illustration";
import {Button} from "@/components/ui/button";
import {ArrowLeft, Plus} from "lucide-react";
import {useRouter} from "next/navigation";

/* 서버 정보 조회 실패 페이지 */
export default function ServerNotFound() {
    const router = useRouter()

    return (
        <div className='pt-24 pb-20 flex flex-col items-center justify-center px-6 text-white'>
            {/* Server Not Found Illustration */}
            <ServerNotFoundIllustration/>

            {/* Error Message */}
            <div className='text-center mb-8 animate-fade-in-up' style={{animationDelay: '200ms'}}>
                <h1 className='text-2xl font-bold mb-4'>Server Not Found</h1>
                <p className='text-slate-400 text-base mb-2'>
                    찾고 있는 서버가 존재하지 않거나 제거되었습니다.
                </p>
                <p className='text-slate-500 text-sm'>삭제되었거나 서버 ID가 올바르지 않을 수 있습니다.</p>
            </div>

            {/* Action Buttons */}
            <div className='flex flex-col gap-4 w-full max-w-sm animate-fade-in-up' style={{animationDelay: '400ms'}}>
                <Button
                    onClick={() => router.replace('/servers')}
                    className='bg-slate-200 text-slate-800 hover:bg-slate-100 font-medium py-3 rounded-xl transition-all duration-200 hover:scale-105 active:scale-95'>
                    <ArrowLeft className='w-4 h-4 mr-2'/>
                    Back to Servers
                </Button>
                <Button
                    onClick={() => router.push('/servers/add')}
                    className='bg-slate-700 text-white border-slate-600 hover:bg-slate-600 font-medium py-3 rounded-xl transition-all duration-200 hover:scale-105 active:scale-95'>
                    <Plus className='w-4 h-4 mr-2'/>
                    Add New Server
                </Button>
            </div>
        </div>
    )
}