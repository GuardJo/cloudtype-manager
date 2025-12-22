'use client'

import {ReactNode, useEffect} from "react";
import {firebaseMessagingService} from "@/lib/firebase-messaging-handler";
import {useMutation} from "@tanstack/react-query";
import {addAppPushToken} from "@/lib/notification-api-handler";
import {AUTH_TOKEN_KEY} from "@/lib/constants";

/* 푸시 알림 설정 */
export default function FcmProvider({children}: { children: ReactNode }) {
    const pushTokenMutation = useMutation({
        mutationKey: ['addAppPushToken'],
        mutationFn: (token: string) => addAppPushToken(token),
        onSuccess: () => console.log('FCM token added successfully'),
        onError: (e) => console.error(e)
    });

    useEffect(() => {
        const initFcm = async () => {
            try {
                const isPermissionGranted = await firebaseMessagingService.requestNotificationPermission()

                if (!isPermissionGranted) {
                    return;
                }

                await firebaseMessagingService.registerFirebaseServiceWorker()

                try {
                    const fcmToken = await firebaseMessagingService.getFCMToken()
                    if (fcmToken && fcmToken.trim().length > 0) {
                        const authToken = localStorage.getItem(AUTH_TOKEN_KEY)
                        if (authToken) {
                            pushTokenMutation.mutate(fcmToken)
                        }
                    } else {
                        console.error('Unable to retrieve FCM registration token')
                    }
                } catch (e) {
                    console.error('Unable to retrieve FCM registration token', e)
                }

                firebaseMessagingService.setupForegroundMessageHandler()
            } catch (error) {
                console.error('FCM initialization failed', error)
            }
        };

        initFcm()
    }, [])

    return <>{children}</>
}