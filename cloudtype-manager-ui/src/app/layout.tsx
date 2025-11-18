import type {Metadata} from "next";
import "./globals.css";
import MobileLayout from "@/containers/mobile-layout";
import MockProvider from "@/containers/mock-provider";
import QueryProvider from "@/containers/query-provider";
import FcmProvider from "@/containers/fcm-provider";

export const metadata: Metadata = {
    title: "Cloudtype Manager",
    description: "Cloudtype Management System",
};

export default function RootLayout({
                                       children,
                                   }: Readonly<{
    children: React.ReactNode;
}>) {
    return (
        <html lang="ko">
        <body
            className={`antialiased`}
        >
        <MockProvider>
            <QueryProvider>
                <FcmProvider>
                    <MobileLayout>
                        {children}
                    </MobileLayout>
                </FcmProvider>
            </QueryProvider>
        </MockProvider>
        </body>
        </html>
    );
}
