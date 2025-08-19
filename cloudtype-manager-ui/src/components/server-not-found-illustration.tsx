import {Database} from "lucide-react";

/* 서버 조회 실패 관련 일러스트 컴포넌트 */
export default function ServerNotFoundIllustration() {
    return (
        <div className='mb-8 animate-fade-in-up'>
            <div className='relative'>
                <div
                    className='w-24 h-24 bg-slate-700 rounded-2xl flex items-center justify-center border-2 border-slate-600'>
                    <Database className='w-12 h-12 text-slate-500'/>
                </div>

                <div
                    className='absolute -top-2 -right-2 w-6 h-6 bg-red-500 rounded-full flex items-center justify-center'>
                    <span className='text-white text-xs'>x</span>
                </div>

                <div className='absolute bottom-2 left-1/2 transform -translate-x-1/2 flex space-x-1'>
                    <div className='w-2 h-2 bg-slate-600 rounded-full animate-pulse'/>
                    <div className='w-2 h-2 bg-slate-600 rounded-full animate-pulse' style={{animationDelay: '200ms'}}/>
                    <div className='w-2 h-2 bg-slate-600 rounded-full animate-pulse' style={{animationDelay: '400ms'}}/>
                </div>
            </div>
        </div>
    )
}