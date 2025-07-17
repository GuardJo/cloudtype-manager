'use client'

import {Button} from "@/components/ui/button";
import {useRouter} from "next/navigation";

/* 서비스 정보 제공 컴포넌트 */
export default function ServiceInformation({infoContent, startUrl = '/'}: ServiceInformationProps) {
    const router = useRouter()

    return (
        <div className='relative z-10 text-center animate-fade-in'>
            <h1 className='text-3xl font-bold leading-tight'>
                Manager Your Service
                <br/>
                with Ease
            </h1>
            <p className='text-slate-300 text-base leading-relaxed mb-8 max-w-sm mx-auto'>
                {infoContent}
            </p>
            <Button
                onClick={() => router.push(startUrl)}
                className='w-full max-w-sm bg-slate-200 text-slate-800 hover:bg-slate-100 font-medium py-3 rounded-full transition-all duration-200 hover:scale-105 active:scale-95'>
                Get Started
            </Button>
        </div>
    )
}

interface ServiceInformationProps {
    infoContent: string,
    startUrl?: string
}