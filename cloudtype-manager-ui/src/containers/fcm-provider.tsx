'use client'

import {ReactNode, useEffect} from "react";
import {initializeApp} from "firebase/app";
import {getMessaging, getToken, onMessage} from "firebase/messaging";

/* 푸시 알림 설정 */
export default function FcmProvider({children}: { children: ReactNode }) {
    const onMessageFcm = async () => {
        const permission = await Notification.requestPermission()
        if (permission !== 'granted') {
            console.log('Permission granted')
            return
        }

        const firebaseApp = initializeApp({
            apiKey: process.env.NEXT_PUBLIC_FIREBASE_API_KEY,
            authDomain: process.env.NEXT_PUBLIC_FIREBASE_AUTH_DOMAIN,
            projectId: process.env.NEXT_PUBLIC_FIREBASE_PROJECT_ID,
            storageBucket: process.env.NEXT_PUBLIC_FIREBASE_STORAGE_BUCKET,
            messagingSenderId: process.env.NEXT_PUBLIC_FIREBASE_MESSAGING_SENDER_ID,
            appId: process.env.NEXT_PUBLIC_FIREBASE_APP_ID,
            measurementId: process.env.NEXT_PUBLIC_FIREBASE_MEASUREMENT_ID,
        })

        const messaging = getMessaging(firebaseApp)

        getToken(messaging, {vapidKey: process.env.NEXT_PUBLIC_FIREBASE_VAPID_KEY})
            .then((currentToken) => {
                if (currentToken) {
                    console.log('FCM Token:', currentToken)
                } else {
                    console.log('No registration token available. Request permission to generate one.')
                }
            })
            .catch((err) => {
                console.log('An error occurred while retrieving token. ', err)
            })

        onMessage(messaging, (payload) => {
            console.log('Message : ', payload.notification?.title, payload.notification?.body)
        })
    }

    useEffect(() => {
        onMessageFcm()
    }, [])

    return <>{children}</>
}