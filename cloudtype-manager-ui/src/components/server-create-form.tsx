import {Button} from "@/components/ui/button";

/* 관리 서버 등록 폼 */
export default function ServerCreateForm() {
    return (
        <>
            <div className='pt-16 pb-24 px-6'>
                <div className='space-y-6 py-6'>
                    {/* Server Name Field */}
                    <div className='space-y-3 animate-fade-in-up'>
                        <label htmlFor='serverName' className='block text-lg font-medium'>
                            Server Name
                        </label>
                        <input
                            className='w-full bg-slate-700 border border-slate-600 rounded-xl px-4 py-4 text-white placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-slate-500 focus:border-transparent transition-all duration-200 focus:scale-[1.02]'
                            id='serverName' type='text' placeholder='Enter server name'/>

                    </div>

                    {/* Server URL Field */}
                    <div className='space-y-3 animate-fade-in-up' style={{animationDelay: '200ms'}}>
                        <label htmlFor='serverUrl' className='block text-lg font-medium'>
                            Server URL
                        </label>
                        <input
                            className='w-full bg-slate-700 border border-slate-600 rounded-xl px-4 py-4 text-white placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-slate-500 focus:border-transparent transition-all duration-200 focus:scale-[1.02]'
                            id='serverUrl' type='text' placeholder='Enter server URL'/>
                    </div>

                    {/* PaaS Console Dashboard URL Field */}
                    <div className='space-y-3 animate-fade-in-up' style={{animationDelay: '400ms'}}>
                        <label htmlFor='dashboardUrl' className='block text-lg font-medium'>
                            PaaS Console Dashboard URL
                        </label>
                        <input
                            className='w-full bg-slate-700 border border-slate-600 rounded-xl px-4 py-4 text-white placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-slate-500 focus:border-transparent transition-all duration-200 focus:scale-[1.02]'
                            id='dashboardUrl' type='text' placeholder='Enter PaaS console dashboard URL'/>
                    </div>
                </div>
            </div>

            {/* Submit Button */}
            <div className='fixed bottom-6 left-6 right-6 animate-fade-in-up' style={{animationDelay: '600ms'}}>
                <Button
                    className='w-full font-medium py-4 text-lg rounded-xl transition-all duration-200 bg-slate-200 text-slate-800 hover:bg-slate-100 hover:scale-105 active:scale-95'>
                    Save
                </Button>
            </div>
        </>
    )
}