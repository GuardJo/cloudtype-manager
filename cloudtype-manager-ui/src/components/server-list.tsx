'use client'

import {ServerSummary} from "@/lib/models";
import ServerSummaryCard from "@/components/server-summary-card";
import {useEffect, useState} from "react";
import {useQuery} from "@tanstack/react-query";
import {getServers} from "@/lib/server-api-handler";

/* 관리 서버 목록 컴포넌트 */
export default function ServerList() {
    const [servers, setServers] = useState<ServerSummary[]>([]);

    const {data, isLoading} = useQuery({
        queryKey: ['getServers'],
        queryFn: getServers
    })

    useEffect(() => {
        if (isLoading) {
            return
        } else {
            setServers(data!.data);
        }
    }, [data, isLoading]);

    return (
        <div className='px-6 space-y-4'>
            {servers.map((server: ServerSummary, index: number) => (
                <ServerSummaryCard server={server} key={index}/>
            ))}
        </div>
    )
}