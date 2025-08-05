/* Server Rack 일러스트 컴포넌트 */
export default function ServerRackIllustration() {
    return (
        <div className='px-6 py-6 bg-gradient-to-b from-slate-800 to-slate-300 animate-fade-in-up'
             style={{animationDelay: '200ms'}}>
            <div className='flex justify-center'>
                <div className='relative'>
                    <div className='bg-slate-900 rounded-lg p-6 border-4 border-slate-700 shadow-2xl'>
                        <div className='bg-slate-800 rounded-t-lg p-2 mb-2'>
                            <div className='flex items-center justify-between text-xs text-slate-400'>
                                <span>SERVER RACK</span>
                                <div className='flex space-x-1'>
                                    <div className='w-2 h-2 bg-green-400 rounded-full animate-pulse'/>
                                    <div className='w-2 h-2 bg-yellow-400 rounded-full animate-pulse'
                                         style={{animationDelay: '500ms'}}/>
                                    <div className='w-2 h-2 bg-red-400 rounded-full animate-pulse'
                                         style={{animationDelay: '1000ms'}}/>
                                </div>
                            </div>
                        </div>

                        <div className='space-y-2'>
                            {[1, 2, 3, 4, 5, 6].map((unit) => (
                                <div key={unit}
                                     className='bg-slate-700 rounded p-3 border border-slate-600 animate-fade-in-up'
                                     style={{animationDelay: `${unit * 100}ms`}}>
                                    <div className='flex items-center justify-center'>
                                        <div className='flex items-center space-x-3'>
                                            <div className={`w-3 h-3 rounded-full ${
                                                unit <= 3 ? 'bg-green-400' : unit === 4 ? 'bg-yellow-400' : 'bg-red-400'} animate-pulse`}/>
                                            <span className='text-xs text-slate-300'>Unit {unit}</span>
                                        </div>
                                        <div className='flex space-x-2 text-xs'>
                                            <span className='text-green-400'>CPU</span>
                                            <span className='text-blue-400'>RAM</span>
                                            <span className='text-purple-400'>DISK</span>
                                            <span className='text-orange-400'>NET</span>
                                        </div>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}