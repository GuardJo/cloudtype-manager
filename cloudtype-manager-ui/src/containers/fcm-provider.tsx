'use client'

import {ReactNode, useEffect} from "react";
import {initializeApp} from "firebase/app";
import {getMessaging, getToken, onMessage} from "firebase/messaging";

/* 푸시 알림 설정 */
export default function FcmProvider({children}: { children: ReactNode }) {
    const registerServiceWorker = async () => {
        if ('serviceWorker' in navigator) {
            try {
                await navigator.serviceWorker.register('/firebase-messaging-sw.js', {
                    scope: '/'
                })
                console.log('Service Worker registered successfully')
            } catch (error) {
                console.error('Error registering Service Worker:', error)
            }
        }
    }

    const onMessageFcm = async () => {
        const permission = await Notification.requestPermission()
        if (permission !== 'granted') {
            console.log('Permission granted')
            return
        }

        await registerServiceWorker()

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
                    // TODO FCM token 서버 전송 기능 구현
                } else {
                    console.log('No registration token available. Request permission to generate one.')
                }
            })
            .catch((err) => {
                console.log('An error occurred while retrieving token. ', err)
            })

        onMessage(messaging, (payload) => {
            console.log('Message : ', payload.notification?.title, payload.notification?.body)

            if (Notification.permission === 'granted') {
                const notification = new Notification(payload.notification?.title || '알림', {
                    body: payload.notification?.body,
                    icon: '/images/icons/icon-192.png',
                    tag: 'foreground-notification'
                })

                notification.onclick = () => {
                    window.focus()
                    notification.close()
                }
            }
        })
    }

    useEffect(() => {
        onMessageFcm()
    }, [])

    return <>{children}</>
}