import type {Metadata} from "next";
import "./globals.css";
import MobileLayout from "@/containers/mobile-layout";

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
        <MobileLayout>
            {children}
        </MobileLayout>
        </body>
        </html>
    );
}
