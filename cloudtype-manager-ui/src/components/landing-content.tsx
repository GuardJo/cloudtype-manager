import ServerConsole from "@/components/server-console";
import ServiceInformation from "@/components/service-information";
import {ReactNode} from "react";
import {Info, Link, TrendingUp} from "lucide-react";
import FeatureCard from "@/components/feature-card";

/* 랜딩 페이지 컴포넌트 */
export default function LandingContent() {
    return (
        <div className='min-h-screen bg-slate-800 text-white animate-slide-in'>
            <div className='pt-16 pb-20'>
                {/* Hero Section */}
                <div className='relative px-6 py-12'>
                    {/* Background mockup image */}
                    <div className='opacity-20'>
                        <ServerConsole/>
                    </div>

                    {/* Hero Section */}
                    <ServiceInformation
                        infoContent={'CloudType Manager provides a comprehensive platform for managing your server URLs, monitoring their status, and accessing detailed server information. Our intuitive interface ensures a seamless experience for both beginners and experienced users.'}
                        startUrl={'/servers'}/>
                </div>

                {/* Key Features Section */}
                <div className='px-6 py-8'>
                    <h2 className='text-xl font-bold mb-6 animate-fade-in-up'>Key Features</h2>
                    <div className='space-y-4'>
                        {keyFeatures.map((feature, index) => (
                            <FeatureCard key={index} featureIcon={feature.icon} cardTitle={feature.title}
                                         cardContent={feature.content}/>
                        ))}
                    </div>
                </div>

                {/* Additional content to demonstrate scrolling */}
                <div className='px-6 py-8 animate-fade-in-up' style={{animationDelay: '400ms'}}>
                    <h2 className='text-xl font-bold mb-6'>Why Choose Cloudtype Manager?</h2>
                    <div className='space-y-4 text-slate-300'>
                        <p>Our platform is designed with simplicity and efficiency in mind, making server management
                            accessible to everyone.
                        </p>
                        <p>
                            With real-time monitoring and comprehensive analytics, you will always stay informed about
                            your server performance.
                        </p>
                        <p>
                            Join thousands of users who trust CloudType Manager for their server management needs.
                        </p>
                    </div>
                </div>
            </div>
        </div>
    )
}

type KeyFeature = {
    icon: ReactNode,
    title: string,
    content: string
}

const keyFeatures: KeyFeature[] = [
    {
        icon: <Link className='text-white w-6 h-6'/>,
        title: 'Server Management',
        content: 'Easily manage and organize your server URLs in one centralized location.'
    },
    {
        icon: <TrendingUp className='text-white w-6 h-6'/>,
        title: 'Real-time Status Monitoring',
        content: 'Monitor the real-time status of your servers, including uptime, response times, and resource usage.'
    },
    {
        icon: <Info className='text-white w-6 h-6'/>,
        title: 'Detailed Server Information',
        content: 'Access detailed information about each server, including configuration, logs, and performance metrics.'
    }
]