/* 서버 콘솔 이미지 컴포넌트 */
export default function ServerConsole() {
    return (
        <div className='absolute inset-0 flex items-center justify-center'>
            <div className='w-80 h-48 bg-slate-600 rounded-lg border border-slate-500 p-4'>
                <div className='flex items-center justify-between mb-3'>
                    <div className='text-xs text-slate-300'>Console</div>
                    <div className='flex space-x-1'>
                        <div className='w-2 h-2 bg-slate-400 rounded-full'/>
                        <div className='w-2 h-2 bg-slate-400 rounded-full'/>
                        <div className='w-2 h-2 bg-slate-400 rounded-full'/>
                    </div>
                </div>
                <div className='space-y-2'>
                    {[1, 2, 3, 4].map((serverNumber) => (
                        <div key={serverNumber} className='flex items-center justify-between text-xs'>
                            <div className='flex items-center space-x-2'>
                                <div className='w-2 h-2 bg-green-400 rounded-full'/>
                                <span className='text-slate-300'>Server {serverNumber}</span>
                            </div>
                            <div className='flex space-x-4 text-slate-400'>
                                <span>CPU</span>
                                <span>RAM</span>
                                <span>Status</span>
                            </div>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    )
}