'use client'

import {ArrowLeft, Home, Search} from "lucide-react";
import {Button} from "@/components/ui/button";
import {useRouter} from "next/navigation";
import Link from "next/link";

/* 전역 404 페이지 컴포넌트 */
export default function NotFound() {
    const router = useRouter()

    return (
        <div
            className='min-h-screen bg-slate-800 text-white flex flex-col items-center justify-center p-6 animate-fade-in'>
            {/* 404 Illustration */}
            <div className='mb-8 animate-fade-in-up'>
                <div className='relative'>
                    {/* large 404 Text */}
                    <div className='text-8xl font-bold text-slate-600 select-none'>404</div>

                    {/* Floating Server Icons */}
                    <div
                        className='absolute -top-4 -left-4 w-8 h-8 bg-slate-700 rounded-lg flex items-center justify-center animate-pulse'>
                        <div className='w-4 h-4 bg-red-400 rounded-full'/>
                    </div>
                    <div
                        className='absolute -top-2 -right-6 w-6 h-6 bg-slate-700 rounded-lg flex items-center justify-center animate-pulse'
                        style={{animationDelay: '500ms'}}>
                        <div className='w-3 h-3 bg-yellow-400 rounded-full'/>
                    </div>
                    <div
                        className='absolute -bottom-6 left-1/2 transform -translate-x-1/2 w-10 h-10 bg-slate-700 rounded-lg flex items-center justify-center animate-pulse'
                        style={{animationDelay: '1000ms'}}>
                        <div className='w-5 h-5 bg-green-400 rounded-full'/>
                    </div>
                </div>
            </div>

            {/* Error Message */}
            <div className='text-center mb-8 animate-fade-in-up' style={{animationDelay: '200ms'}}>
                <h1 className='text-3xl font-bold mb-4'>Page Not Found</h1>
                <p className='text-slate-400 text-lg mb-2'>찾고 계신 페이지가 존재하지 않습니다.</p>
                <p className='text-slate-500 text-base'>이동, 삭제되었거나 잘못된 URL을 입력했을 수 있습니다.</p>
            </div>

            {/* Search Suggestion */}
            <div className='w-full max-w-md mb-8 animate-fade-in-up' style={{animationDelay: '400ms'}}>
                <div className='relative'>
                    <Search className='absolute left-4 top-1/2 transform -translate-y-1/2 w-5 h-5 text-slate-400'/>
                    <input type='text' placeholder='Search for servers...'
                           onKeyDown={(e) => {
                               if (e.key === 'Enter') {
                                   router.push('/servers')
                               }
                           }}
                           className='w-full bg-slate-700 border border-slate-600 rounded-xl pl-12 pr-4 py-3 text-white placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-slate-500 focus:border-transparent transition-all duration-200'/>
                </div>
            </div>

            {/* Action Buttons */}
            <div className='flex flex-col sm:flex-row gap-4 w-full max-w-md animate-fade-in-up'
                 style={{animationDelay: '600ms'}}>
                <Button variant='outline'
                        onClick={() => router.back()}
                        className='flex-1 bg-slate-700 text-white border-slate-600 hover:bg-slate-600 font-medium py-3 rounded-xl transition-all duration-200 hover:scale-105 active:scale-95'>
                    <ArrowLeft className='w-4 h-4 mr-2'/>
                    Go Back
                </Button>
                <Button
                    onClick={() => router.push('/')}
                    className='flex-1 bg-slate-200 text-slate-800 hover:bg-slate-100 font-medium py-3 rounded-xl transition-all duration-200 hover:scale-105 active:scale-95'>
                    <Home className='w-4 h-4 mr-2'/>
                    Go Home
                </Button>
            </div>

            {/* Quick Link */}
            <div className='mt-8 text-center animate-fade-in-up' style={{animationDelay: '800ms'}}>
                <p className='text-slate-500 text-sm mb-4'>Quick Links:</p>
                <div className='flex flex-wrap justify-center gap-4'>
                    <Link href='/servers'
                          className='text-slate-400 hover:text-white text-sm underline transition-colors'>
                        View Servers
                    </Link>
                    <Link href='/servers/add'
                          className='text-slate-400 hover:text-white text-sm underline transition-colors'>
                        Add Servers
                    </Link>
                    <Link href='/settings'
                          className='text-slate-400 hover:text-white text-sm underline transition-colors'>
                        Settings
                    </Link>
                </div>
            </div>
        </div>
    )
}