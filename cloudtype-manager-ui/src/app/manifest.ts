import {MetadataRoute} from "next";

export default function manifest(): MetadataRoute.Manifest {
    return {
        name: 'Cloudtype Manager',
        short_name: 'Cloudtype',
        description: 'Cloudtype Management System',
        theme_color: '#314158',
        background_color: '#314158',
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