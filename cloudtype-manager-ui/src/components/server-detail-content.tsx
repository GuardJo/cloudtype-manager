"use client"

import ServerRackIllustration from "@/components/server-rack-illustration";
import ServerStatusBadge from "@/components/server-status-badge";
import ServerActionsArea from "@/components/server-actions-area";
import {useQuery} from "@tanstack/react-query";
import {getServerDetail} from "@/lib/server-api-handler";
import {ServerDetail} from "@/lib/models";
import {notFound} from "next/navigation";
import {useEffect, useState} from "react";

/* 서버 상세 정보 컨텐츠 */
export default function ServerDetailContent({serverId}: ServerDetailContentProps) {
    const [serverDetail, setServerDetail] = useState<ServerDetail | undefined>()
    const {data, isLoading} = useQuery({
        queryKey: ['getServerDetail', serverId],
        queryFn: () => getServerDetail(serverId)
    })

    useEffect(() => {
        if (!isLoading) {
            if (data?.statusCode === 200) {
                setServerDetail(data?.data)
            } else if (data?.statusCode === 404) {
                notFound()
            } else {
                throw new Error('Failed get serverDetail')
            }
        }
    }, [isLoading, data]);

    return (
        <div className='pt-16 pb-6'>
            {serverDetail &&
                <>
                    <div className='px-6 py-8 animate-fade-in'>
                        <h2 className='text-3xl font-bold mb-4'>{serverDetail.serverName}</h2>
                        <p className='text-slate-400 text-base' onClick={() => window.open(serverDetail.hostingUrl)}>
                            Server URL: <span className='text-slate-300'>{serverDetail.hostingUrl}</span>
                        </p>
                    </div>
                    <ServerRackIllustration/>
                    <div className='px-6 py-6 bg-slate-800 animate-fade-in-up' style={{animationDelay: '400ms'}}>
                        <h3 className='text-xl font-bold mb-4 text-white-300'>Status</h3>
                        <ServerStatusBadge activate={serverDetail.activate}/>
                    </div>
                    <div className='px-6 py-6 animate-fade-in-up' style={{animationDelay: '600ms'}}>
                        <h3 className='text-xl font-bold mb-6'>Actions</h3>
                        <ServerActionsArea dashboardUrl={serverDetail.managementUrl}
                                           viewEventUrl={`${serverDetail.managementUrl}#events`}/>
                    </div>
                </>
            }
        </div>
    )
}

interface ServerDetailContentProps {
    serverId: number,
}