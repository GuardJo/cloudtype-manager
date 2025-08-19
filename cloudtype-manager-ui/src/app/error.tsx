'use client'

import {useRouter} from "next/navigation";
import {useEffect} from "react";
import {AlertTriangle, Bug, Home, RefreshCw} from "lucide-react";
import {Button} from "@/components/ui/button";

/* 루트 경로 하위에서 발샌한 예외 처리 페이지 */
export default function Error({error, reset}: ErrorProps) {
    const router = useRouter()

    useEffect(() => {
        console.error('Application Error: ', error)
    }, [error]);

    return (
        <div
            className='min-h-screen bg-slate-800 text-white flex flex-col items-center justify-center p-6 animate-fade-in'>
            {/* Error Illustration */}
            <div className='mb-8 animate-fade-in-up'>
                <div className='relative'>
                    {/* Error Icon with Glitch Effect */}
                    <div
                        className='w-32 h-32 bg-slate-700 rounded-2xl flex items-center justify-center border-2 border-red-500/20'>
                        <AlertTriangle className='w-16 h-16 text-red-400 animate-pulse'/>
                    </div>

                    {/* Floating Error Indicators */}
                    <div
                        className='absolute -top-2 -right-2 w-6 h-6 bg-red-500 rounded-full flex items-center justify-center animate-bounce'>
                        <span className='text-white text-xs font-bold'>!</span>
                    </div>
                    <div
                        className='absolute -bottom-2 -left-2 w-8 h-8 bg-slate-600 rounded-lg flex items-center justify-center animate-pulse'
                        style={{animationDelay: '500ms'}}>
                        <Bug className='w-4 h-4 text-red-400'/>
                    </div>
                </div>
            </div>

            {/* Error Message */}
            <div className='text-center mb-8 animate-fade-in-up' style={{animationDelay: '200ms'}}>
                <h1 className='text-3xl font-bold mb-4'>Something went wrong!</h1>
                <p className='text-slate-400 text-lg mb-4'>요청을 처리하는 동안 예기치 않은 오류가 발생했습니다.</p>

                {/* Error Details (Develop Mode) */}
                {process.env.NODE_ENV === 'development' && (
                    <div className='bg-slate-900 border border-slate-600 rounded-lg p-4 mb-4 text-left max-w-md'>
                        <p className='text-red-400 text-sm font-mono break-all'>{error.message}</p>
                        {error.digest && <p className='text-slate-500 text-xs mt-2'>Error ID: {error.digest}</p>}
                    </div>
                )}

                <p className='text-slate-500 text-base'>서버는 여전히 원활하게 운영되고 있으니 걱정하지 마세요.</p>
            </div>

            {/* Server Status Indicators */}
            <div className='flex items-center gap-4 mb-8 animate-fade-in-up' style={{animationDelay: '400ms'}}>
                <div className='flex items-center gap-2'>
                    <div className='w-3 h-3 bg-green-400 rounded-full animate-pulse'/>
                    <span className='text-sm text-slate-400'>Servers Online</span>
                </div>
                <div className='flex items-center gap-2'>
                    <div className='w-3 h-3 bg-yellow-400 rounded-full animate-pulse'/>
                    <span className='text-sm text-slate-400'>Monitoring Active</span>
                </div>
                <div className='flex items-center gap-2'>
                    <div className='w-3 h-3 bg-blue-400 rounded-full animate-pulse'/>
                    <span className='text-sm text-slate-400'>System Stable</span>
                </div>
            </div>

            {/* Action Buttons */}
            <div className='flex flex-col sm:flex-row gap-4 w-full max-w-md animate-fade-in-up'
                 style={{animationDelay: '600ms'}}>
                <Button onClick={reset}
                        className='flex-1 bg-slate-200 text-slate-800 hover:bg-slate-100 font-medium py-3 rounded-xl transition-all duration-200 hover:scale-105 active:sacle-95'>
                    <RefreshCw className='w-4 h-4 mr-2'/>
                    Try Again
                </Button>
                <Button onClick={() => router.push('/')}
                        className='flex-1 bg-slate-700 text-white border-slate-600 hover:bg-slate-600 font-medium py-3 rounded-xl transition-all duration-200 hover:sacle-105 active:scale-95'>
                    <Home className='w-4 h-4 mr-2'/>
                    Go Home
                </Button>
            </div>

            {/* Help Text */}
            <div className='mt-8 text-center animate-fade-in-up' style={{animationDelay: '800ms'}}>
                <p className='text-slate-500 text-sm mb-2'>이 문제가 지속되면 지원팀에 문의하세요.</p>
                <button onClick={() => {
                    alert("Github : https://github.com/GuardJo")
                }}
                        className='text-slate-400 hover:text-white text-sm underline transition-colors'>
                    Report this issue
                </button>
            </div>
        </div>
    )
}

interface ErrorProps {
    error: Error & { digest?: string }
    reset: () => void
}