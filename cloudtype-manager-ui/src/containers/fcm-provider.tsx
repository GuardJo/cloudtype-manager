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

                await firebaseMessagingService.getFCMToken()
                    .then((token) => {
                        const authToken = localStorage.getItem(AUTH_TOKEN_KEY)
                        if (token.trim().length > 0 && authToken !== null) {
                            pushTokenMutation.mutate(token)
                        } else {
                            console.error('Unable to retrieve FCM registration token')
                        }
                    })
                    .catch((error) => console.error('Unable to retrieve FCM registration token', error))

                firebaseMessagingService.setupForegroundMessageHandler()
            } catch (error) {
                console.error('FCM initialization failed', error)
            }
        };

        initFcm()
    }, [])

    return <>{children}</>
}