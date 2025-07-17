/* Key Feature Card 컴포넌트 */
import React from "react";

export default function FeatureCard({featureIcon, cardTitle, cardContent}: FeatureCardProps) {
    return (
        <div className='bg-slate-700 rounded-xl p-6 border border-slate-600 animate-fade-in-up'
             style={{animationDelay: '200ms'}}>
            <div className='flex items-start space-x-4'>
                <div className='bg-slate-600 p-3 rounded-lg'>
                    {featureIcon}
                </div>
                <div className='flex-1'>
                    <h3 className='text-white font-semibold text-lg mb-2'>
                        {cardTitle}
                    </h3>
                    <p className='text-slate-300 text-sm leading-relaxed'>
                        {cardContent}
                    </p>
                </div>
            </div>
        </div>
    )
}

interface FeatureCardProps {
    featureIcon: React.ReactNode;
    cardTitle: string,
    cardContent: string
}