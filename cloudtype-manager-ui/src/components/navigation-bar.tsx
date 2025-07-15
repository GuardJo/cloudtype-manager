"use client"

import {Database, Home, Settings} from "lucide-react";
import {useRouter} from "next/navigation";

/* 네비게이션바 컴포넌트 */
export default function NavigationBar({activeTab}: NavigationBarProps) {
    const router = useRouter()

    return (
        <nav className="fixed bottom-0 left-0 right-0 z-50 bg-slate-700 border-t border-slate-600">
            <div className="flex items-center justify-around py-2">
                <button
                    className={`flex flex-col items-center py-2 px-4 min-w-0 flex-1 transition-colors ${
                        activeTab === 'home' ? 'text-white' : 'text-slate-400'
                    }`}
                    onClick={() => {
                        router.push("/")
                    }}
                >
                    <Home className="w-6 h-6 mb-1"/>
                    <span className="text-xs">Home</span>
                </button>
                <button className={`flex flex-col items-center py-2 px-4 min-w-0 flex-1 ${
                    activeTab === 'servers' ? 'text-white' : 'text-slate-400'
                }`}
                        onClick={() => {
                            router.push("/servers")
                        }}
                >
                    <Database className="w-6 h-6 mb-1"/>
                    <span className="text-xs">Servers</span>
                </button>
                <button className={`flex flex-col items-center py-2 px-4 min-w-0 flex-1 ${
                    activeTab === 'settings' ? 'text-white' : 'text-slate-400'
                }`}
                        onClick={() => {
                            router.push("/settings")
                        }}
                >
                    <Settings className="w-6 h-6 mb-1"/>
                    <span className="text-xs">Settings</span>
                </button>
            </div>
        </nav>
    )
}

interface NavigationBarProps {
    activeTab: ActiveTab
}