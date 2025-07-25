'use client';

import {Suspense, useEffect} from 'react';
import {useRouter, useSearchParams} from 'next/navigation';

function OAuthCallbackProgress() {
    const searchParams = useSearchParams();
    const router = useRouter();

    useEffect(() => {
        const token = searchParams.get('token');
        if (token) {
            localStorage.setItem('authToken', token);
            router.replace('/servers');
        } else {
            router.replace('/login');
        }
    }, [searchParams, router]);

    return (
        <div>
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
