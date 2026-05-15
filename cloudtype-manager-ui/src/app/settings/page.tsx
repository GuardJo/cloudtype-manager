'use client'

import AccountInfoSection from "@/components/account-info-section";
import SettingSupportButton from "@/components/setting-support-button";
import {useRouter} from "next/navigation";
import {useQuery} from "@tanstack/react-query";
import {getUserInfo} from "@/lib/user-api-handler";
import LogoutButton from "@/components/logout-button";

/* 설정 페이지 */
export default function SettingsPage() {
    const router = useRouter()
    const {data, isLoading} = useQuery({
        queryKey: ['getUserInfo'],
        queryFn: () => getUserInfo()
    })

    const handleGotoPushTokenSetting = () => {
        router.push('/settings/push-token')
    }

    const handleGotoContactUs = () => {
        router.push('/settings/contact-us')
    }

    const alertComingSoon = () => {
        alert('Coming Soon')
    }

    return (
        <div className='text-white pt-16 pb-24 animate-slide-in flex flex-col'>
            <div className='px-6 py-6 flex-1'>
                {isLoading || data?.statusCode !== 200 ?
                    <AccountInfoSection userName='loading...' userEmail='loading...'/> :
                    <AccountInfoSection userName={data.data.name} userEmail={data.data.email}/>}

                <h2 className='text-lg font-bold mb-4'>Notifications</h2>
                <SettingSupportButton settingName='Push Token'
                                      settingDescription='View and regenerate your notification push token'
                                      onclick={handleGotoPushTokenSetting}/>

                <h2 className='text-lg font-bold mt-8 mb-4'>Support</h2>
                <SettingSupportButton settingName='Contact Us' onclick={handleGotoContactUs}/>
                <SettingSupportButton settingName='Terms of Service' onclick={alertComingSoon}/>
                <SettingSupportButton settingName='Privacy Policy' onclick={alertComingSoon}/>
            </div>

            <div className='px-6 mt-auto pt-8'>
                <LogoutButton userId={data!.data.id}/>
            </div>
        </div>
    )
}