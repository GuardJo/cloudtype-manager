import {getMessaging, getToken, Messaging, onMessage} from "firebase/messaging";
import {FirebaseApp} from "firebase/app";
import {firebaseService} from "@/lib/firebase-config";

class FirebaseMessagingService {
    private messaging: Messaging | null = null
    private static instance: FirebaseMessagingService

    private constructor() {
    }

    public static getInstance(): FirebaseMessagingService {
        if (!FirebaseMessagingService.instance) {
            FirebaseMessagingService.instance = new FirebaseMessagingService()
        }

        return FirebaseMessagingService.instance
    }

    /**
     * 알림 권한 요청
     */
    public async requestNotificationPermission(): Promise<boolean> {
        if (typeof window === 'undefined') {
            return false
        }

        const notification = await Notification.requestPermission()
        return notification === 'granted'
    }

    /**
     * firebase service worker 등록
     */
    public async registerFirebaseServiceWorker(): Promise<void> {
        if (typeof window === 'undefined') {
            return;
        }

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

    /**
     * FCM token 밝브
     */
    public async getFCMToken(): Promise<string> {
        if (typeof window === 'undefined') {
            return '';
        }

        const messaging = this.initMessaging()
        if (!messaging) {
            return '';
        }

        return getToken(messaging, {vapidKey: process.env.NEXT_PUBLIC_FIREBASE_VAPID_KEY})
    }

    /**
     * foreground 메시지 핸들링
     */
    public setupForegroundMessageHandler(): void {
        if (typeof window === 'undefined') {
            return
        }

        const messaging = this.initMessaging()
        if (!messaging) {
            return;
        }

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

    private initMessaging() {
        if (!this.messaging && typeof window !== 'undefined') {
            const firebaseApp: FirebaseApp = firebaseService.getFirebaseApp()
            this.messaging = getMessaging(firebaseApp)
        }

        return this.messaging
    }
}

export const firebaseMessagingService = FirebaseMessagingService.getInstance()