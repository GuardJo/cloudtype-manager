'use client';

import {useEffect} from 'react';
import {useRouter, useSearchParams} from 'next/navigation';

export default function OAuthCallbackPage() {
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
