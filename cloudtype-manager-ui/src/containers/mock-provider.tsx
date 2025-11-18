'use client'

import {initMocks} from "@/mocks";
import {ReactNode, useEffect, useState} from "react";

export default function MockProvider({children}: { children: ReactNode }) {
    const [isReady, setIsReady] = useState(false)

    useEffect(() => {
        const init = async () => {
            if (process.env.NEXT_PUBLIC_MOCK_SERVER_ENABLE === 'true') {
                try {
                    await initMocks()
                    console.log('mock server is enabled')
                } catch (error) {
                    console.log('Failed initialize mock server', error);
                }
            }
            setIsReady(true)
        };

        init();
    }, []);

    if (!isReady) {
        return (
            <div className="flex items-center justify-center min-h-screen">
                <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"/>
            </div>
        );
    }

    return <>{children}</>
}