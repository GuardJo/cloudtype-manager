'use client'

import {initMocks} from "@/mocks";
import {ReactNode, useEffect} from "react";

export default function MockProvider({children}: { children: ReactNode }) {
    useEffect(() => {
        if (process.env.NEXT_PUBLIC_MOCK_SERVER_ENABLE === 'true') {
            initMocks()
                .then(() => console.log('mock server is enabled'))
        }
    }, []);
    return <>{children}</>
}