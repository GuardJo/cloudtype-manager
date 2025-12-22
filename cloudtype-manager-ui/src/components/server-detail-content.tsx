"use client"

import ServerRackIllustration from "@/components/server-rack-illustration";
import ServerStatusBadge from "@/components/server-status-badge";
import ServerActionsArea from "@/components/server-actions-area";
import {useMutation, useQuery} from "@tanstack/react-query";
import {deleteServer, getServerDetail} from "@/lib/server-api-handler";
import {notFound, useRouter} from "next/navigation";
import Loading from "@/app/servers/[serverId]/loading";
import {Button} from "@/components/ui/button";
import {Trash2} from "lucide-react";
import {useState} from "react";

/* 서버 상세 정보 컨텐츠 */
export default function ServerDetailContent({serverId}: ServerDetailContentProps) {
    const router = useRouter()
    const [showDeleteConfirmBtn, isShowDeleteConfirmBtn] = useState<boolean>(false)

    const {data, isLoading} = useQuery({
        queryKey: ['getServerDetail', serverId],
        queryFn: () => getServerDetail(serverId)
    })

    const deleteMutation = useMutation({
        mutationKey: ['deleteServer', serverId],
        mutationFn: () => deleteServer(serverId),
        onSuccess: () => {
            isShowDeleteConfirmBtn(false)
            alert('Server Deleted Successfully!')
            router.replace('/servers')
        },
        onError: (e) => {
            console.error(e)
            alert(`삭제에 실패하였습니다.\nCause: ${e.message}`)
        }
    })

    if (isLoading) {
        return <Loading/>
    }

    if (data?.statusCode === 404) {
        notFound()
    }

    if (!data || data?.statusCode !== 200) {
        throw new Error('Failed to get serverDetail')
    }

    const serverDetail = data.data

    const confirmDeleteServer = () => {
        deleteMutation.mutate()
    }

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
                    <div className='px-6 py-6 animate-fade-in-up' style={{animationDelay: '800ms'}}>
                        <div className='border-t border-slate-700 pt-6'>
                            <h3 className='text-xl font-bold mb-4 text-red-400'>Danger Zone</h3>
                            <p className='text-slate-400 text-sm mb-4'>
                                Once you delete this server, there is no going back. Please be certain.
                            </p>
                            {!showDeleteConfirmBtn ? (
                                <Button
                                    onClick={() => isShowDeleteConfirmBtn(true)}
                                    className='w-full bg-red-600 hover:bg-red-700 text-white font-medium py-4 text-lg rounded-xl transition-all duration-200 hover:scale-105 active:scale-95 flex items-center justify-center space-x-2'>
                                    <Trash2 className='w-5 h-5'/>
                                    <span>Delete Server</span>
                                </Button>
                            ) : (
                                <div className='bg-slate-700 rounded-xl p-4 border-2 border-red-500 animate-fade-in'>
                                    <p className='text-white fonr-medium mb-4'>Are you sure you want to delete this
                                        server?</p>
                                    <div className='flex space-x-3'>
                                        <Button onClick={confirmDeleteServer}
                                                className='flex-1 bg-red-600 hover:bg-red-700 text-white font-medium py-3 rounded-lg transition-all duration-200'>
                                            Yes, Delete
                                        </Button>
                                        <Button onClick={() => isShowDeleteConfirmBtn(false)}
                                                className='flex-1 bg-slate-600 hover:bg-slate-500 text-white font-medium py-3 rounded-lg transition-all duration-200'>
                                            Cancel
                                        </Button>
                                    </div>
                                </div>
                            )}
                        </div>
                    </div>
                </>
            }
        </div>
    )
}

interface ServerDetailContentProps {
    serverId: number,
}