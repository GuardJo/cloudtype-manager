'use client'

import AccountInfoSection from "@/components/account-info-section";
import SettingSupportButton from "@/components/setting-support-button";
import {useRouter} from "next/navigation";

/* 설정 페이지 */
export default function SettingsPage() {
    const router = useRouter()

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
        <div className='min-h-screen bg-slate-800 text-white pt-16 pb-6 animate-slide-in flex flex-col'>r2
            <div className='px-6 py-6 flex-1'>
                {/* TODO 사용자 정보 받아오기 */}
                <AccountInfoSection userName='tester' userEmail='test@gmail.com'/>

                <h2 className='text-lg font-bold mb-4'>Notifications</h2>
                <SettingSupportButton settingName='Push Token'
                                      settingDescription='View and regenerate your notification push token'
                                      onclick={handleGotoPushTokenSetting}/>

                <h2 className='text-lg font-bold mt-8 mb-4'>Support</h2>
                <SettingSupportButton settingName='Contact Us' onclick={handleGotoContactUs}/>
                <SettingSupportButton settingName='Terms of Service' onclick={alertComingSoon}/>
                <SettingSupportButton settingName='Privacy Policy' onclick={alertComingSoon}/>
            </div>
        </div>
    )
}