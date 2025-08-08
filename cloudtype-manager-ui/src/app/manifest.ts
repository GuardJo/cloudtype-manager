import {MetadataRoute} from "next";

export default function menifest(): MetadataRoute.Manifest {
    return {
        name: 'Cloudtype Manager',
        short_name: 'Cloudtype Manager',
        description: 'Cloudtype Management System',
        theme_color: '#000000',
        background_color: '#000000',
        start_url: '/',
        display: 'fullscreen',
        icons: [
            {
                src: '/images/icons/icon-192.png',
                sizes: '192x192',
                type: 'image/png',
            },
            {
                src: '/images/icons/icon-512.png',
                sizes: '512x512',
                type: 'image/png',
            }
        ]
    }
}