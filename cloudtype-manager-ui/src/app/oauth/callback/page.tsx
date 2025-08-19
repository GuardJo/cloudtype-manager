'use client';

import {Suspense, useEffect} from 'react';
import {useRouter, useSearchParams} from 'next/navigation';

function OAuthCallbackProgress() {
    const searchParams = useSearchParams();
    const router = useRouter();

    useEffect(() => {
        const accessToken = searchParams.get('accessToken')
        const refreshToken = searchParams.get('refreshToken')
        if (accessToken) {
            localStorage.setItem('authToken', accessToken);

            if (refreshToken) {
                localStorage.setItem('authRefToken', refreshToken)
            }

            router.replace('/servers');
        } else {
            router.replace('/login');
        }

    }, [searchParams, router]);

    return (
        <div className='min-h-screen bg-slate-800 text-white flex flex-col items-center justify-center p-6'>
            <p>Processing authentication...</p>
        </div>
    );
}

export default function OAuthCallbackPage() {
    return (
        <Suspense>
            <OAuthCallbackProgress/>
        </Suspense>
    )
}
