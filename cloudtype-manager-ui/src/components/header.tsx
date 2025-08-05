'use client'

import {ArrowLeft, HelpCircle} from "lucide-react";
import {HeaderTitle} from "@/lib/models";
import {useRouter} from "next/navigation";

/* 헤더 컴포넌트 */
export default function Header({headerTitle, showBackButton = false}: HeaderProps) {
    const router = useRouter()

    const handleBackClick = () => {
        router.back()
    }

    return (
        <header
            className='fixed top-0 left-0 right-0 z-50 px-4 py-3 flex items-center transition-all duration-300 bg-slate-700'>
            {showBackButton && (
                <button className='p-1 mr-3 text-white hover:bg-slate-600 rounded-lg transition-colors'
                        onClick={handleBackClick}>
                    <ArrowLeft className='w-6 h-6'/>
                </button>
            )}
            <h1 className="text-lg font-medium text-white flex-1">{headerTitle}</h1>
            <button className="p-1 text-white">
                <HelpCircle className="w-6 h-6"/>
            </button>
        </header>
    )
}

interface HeaderProps {
    headerTitle: HeaderTitle,
    showBackButton?: boolean,
}