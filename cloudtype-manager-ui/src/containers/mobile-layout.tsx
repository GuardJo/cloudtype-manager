'use client'

import React, {ReactNode, useEffect, useState} from "react";
import Header from "@/components/header";
import NavigationBar from "@/components/navigation-bar";
import {usePathname} from "next/navigation";
import {ActiveTab, HeaderTitle} from "@/lib/models";

/* 모바일 레이아웃 컴포넌트 */
export default function MobileLayout({children}: { children: ReactNode }) {
    const [activeTabName, setActiveTabName] = useState<ActiveTab>('home')
    const [headerTitle, setHeaderTitle] = useState<HeaderTitle>('Cloudtype Manager')
    const [showNavigationBar, setShowNavigationBar] = useState(true)
    const [showHeaderBackButton, setShowHeaderBackButton] = useState(false)

    const pathname = usePathname()

    useEffect(() => {
        if (pathname === '/') {
            setActiveTabName('home')
            setHeaderTitle('Cloudtype Manager')
        } else if (pathname.startsWith('/servers')) {
            setActiveTabName('servers')
            setHeaderTitle(pathname.startsWith('/servers/') ? 'Server Detail' : 'Servers')
        } else if (pathname.startsWith('/settings')) {
            setActiveTabName('settings')
            setHeaderTitle('Settings')
        } else {
            setActiveTabName('home')
            setHeaderTitle('Cloudtype Manager')
        }

        setShowNavigationBar(!pathname.startsWith('/servers/'))
        setShowHeaderBackButton(pathname.startsWith('/servers/'))
    }, [pathname, activeTabName]);

    return (
        <div className='min-h-screen bg-slate-800'>
            <Header headerTitle={headerTitle} showBackButton={showHeaderBackButton}/>
            <main>{children}</main>
            {showNavigationBar && (
                <NavigationBar activeTab={activeTabName}/>
            )}
        </div>
    )
}