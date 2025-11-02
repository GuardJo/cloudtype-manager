'use client'

import {ReactNode, useEffect} from "react";
import {firebaseMessagingService} from "@/lib/firebase-messaging-handler";

/* 푸시 알림 설정 */
export default function FcmProvider({children}: { children: ReactNode }) {
    useEffect(() => {
        const initFcm = async () => {
            try {
                const isPermissionGranted = await firebaseMessagingService.requestNotificationPermission()

                if (!isPermissionGranted) {
                    return;
                }

                await firebaseMessagingService.registerFirebaseServiceWorker()

                await firebaseMessagingService.getFCMToken()

                firebaseMessagingService.setupForegroundMessageHandler()
            } catch (error) {
                console.error('FCM initialization failed', error)
            }
        };

        initFcm()
    }, [])

    return <>{children}</>
}