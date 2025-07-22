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
    const pathname = usePathname()

    useEffect(() => {
        if (pathname === '/') {
            setActiveTabName('home')
            setHeaderTitle('Cloudtype Manager')
        } else if (pathname.startsWith('/servers')) {
            setActiveTabName('servers')
            setHeaderTitle('Servers')
        } else if (pathname.startsWith('/settings')) {
            setActiveTabName('settings')
            setHeaderTitle('Settings')
        } else {
            setActiveTabName('home')
            setHeaderTitle('Cloudtype Manager')
        }
    }, [pathname, activeTabName]);

    return (
        <div className='min-h-screen bg-slate-800'>
            <Header headerTitle={headerTitle}/>
            <main>{children}</main>
            <NavigationBar activeTab={activeTabName}/>
        </div>
    )
}