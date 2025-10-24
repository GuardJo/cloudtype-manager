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