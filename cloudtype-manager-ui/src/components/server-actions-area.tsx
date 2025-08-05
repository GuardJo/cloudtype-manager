"use client"

import {Button} from "@/components/ui/button";

/* 관리 서버 요청 기능 영역 컴포넌트 */
export default function ServerActionsArea({dashboardUrl, viewEventUrl}: ServerActionsAreaProps) {
    const handleOpenExternalUrl = (externalUrl: string | undefined) => {
        if (externalUrl !== undefined) {
            window.open(new URL(externalUrl));
        }
    }

    return (
        <div className='space-y-4'>
            <Button
                disabled={dashboardUrl === undefined || dashboardUrl === null}
                className='w-full bg-slate-200 text-slate-800 hover:bg-slate-100 font-medium py-4 text-lg rounded-xl transition-all duration-200 hover:scale-105 active:scale-95'
                onClick={() => handleOpenExternalUrl(dashboardUrl)}
            >
                Go to Dashboard
            </Button>
            <Button
                disabled={viewEventUrl === undefined || viewEventUrl === null}
                className='w-full bg-slate-700 text-white border-slate-600 hover:bg-slate-600 font-medium py-4 text-lg rounded-xl transition-all duration-200 hover:scale-105 active:scale-95'
                variant='outline'
                onClick={() => handleOpenExternalUrl(viewEventUrl)}
            >
                View Events
            </Button>
        </div>
    )
}

interface ServerActionsAreaProps {
    dashboardUrl?: string,
    viewEventUrl?: string,
}