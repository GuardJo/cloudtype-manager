'use client'

import {ServerSummary} from "@/lib/models";
import ServerSummaryCard from "@/components/server-summary-card";
import {useEffect, useState} from "react";
import {useQuery} from "@tanstack/react-query";
import {getServers} from "@/lib/server-api-handler";
import Loading from "@/app/servers/loading";

/* 관리 서버 목록 컴포넌트 */
export default function ServerList() {
    const [servers, setServers] = useState<ServerSummary[]>([]);

    const {data, isLoading, isError, error} = useQuery({
        queryKey: ['getServers'],
        queryFn: getServers
    })

    useEffect(() => {
        if (isLoading) {
            return
        } else if (isError) {
            console.log(`Error: ${error.message}`);
        } else {
            setServers(data!.data)
        }
    }, [data, isLoading, isError, error]);

    return (
        <div className='px-6 space-y-4'>
            {isLoading ?
                <Loading/> :
                isError ? <div className="text-center">Error</div> :
                    servers.map((server: ServerSummary, index: number) => (
                        <ServerSummaryCard server={server} key={index}/>
                    ))
            }
        </div>
    )
}