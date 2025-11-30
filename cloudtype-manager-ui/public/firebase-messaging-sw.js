importScripts(
    'https://www.gstatic.com/firebasejs/12.1.0/firebase-app-compat.js',
)
importScripts(
    'https://www.gstatic.com/firebasejs/12.1.0/firebase-messaging-compat.js',
)

const firebaseConfig = {
    apiKey: 'AIzaSyBFiXCz2Ykt70NTLbrKiqdIa6qZ861_L1U',
    authDomain: 'cloudtype-manager-notification.firebaseapp.com',
    projectId: 'cloudtype-manager-notification',
    storageBucket: 'cloudtype-manager-notification.firebasestorage.app',
    messagingSenderId: '1058863090766',
    appId: '1:1058863090766:web:de18ad70198770c479071f',
    measurementId: 'G-0VDYQDF03Y',
};

// Initialize Firebase
const app = firebase.initializeApp(firebaseConfig);
const messaging = firebase.messaging()

messaging.onBackgroundMessage(function (payload) {
    const notificationTitle = payload.notification.title;
    const notificationOptions = {
        body: payload.notification.body,
        icon: '/images/icons/icon-192.png',
        tag: 'background-notification',
        requireInteraction: true,
        actions: [
            {
                action: 'open',
                title: '열기',
            },
            {
                action: 'close',
                title: '닫기',
            }
        ]
    }

    self.registration.showNotification(notificationTitle, notificationOptions)
})

self.addEventListener('notificationclick', function (event) {
    event.notification.close();

    if (event.action === 'close') {
        return
    } else {
        event.waitUntil(
            clients.openWindow('/')
        )
    }
})