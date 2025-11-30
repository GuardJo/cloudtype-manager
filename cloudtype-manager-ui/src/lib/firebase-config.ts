import {FirebaseApp, getApps, initializeApp} from "firebase/app";

class FirebaseService {
    private static instance: FirebaseService

    private app: FirebaseApp | null = null

    private constructor() {
    }

    public static getInstance(): FirebaseService {
        if (!FirebaseService.instance) {
            FirebaseService.instance = new FirebaseService()
        }

        return FirebaseService.instance
    }

    public getFirebaseApp(): FirebaseApp {
        if (!this.app && getApps().length === 0) {
            this.app = initializeApp({
                apiKey: process.env.NEXT_PUBLIC_FIREBASE_API_KEY,
                authDomain: process.env.NEXT_PUBLIC_FIREBASE_AUTH_DOMAIN,
                projectId: process.env.NEXT_PUBLIC_FIREBASE_PROJECT_ID,
                storageBucket: process.env.NEXT_PUBLIC_FIREBASE_STORAGE_BUCKET,
                messagingSenderId: process.env.NEXT_PUBLIC_FIREBASE_MESSAGING_SENDER_ID,
                appId: process.env.NEXT_PUBLIC_FIREBASE_APP_ID,
                measurementId: process.env.NEXT_PUBLIC_FIREBASE_MEASUREMENT_ID,
            })
        }

        return this.app || getApps()[0]
    }
}

export const firebaseService = FirebaseService.getInstance()