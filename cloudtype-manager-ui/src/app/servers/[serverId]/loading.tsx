/* 서버 상세 페이지 스켈레톤 컴포넌트 */
export default function Loading() {
    return <ServerDetailSkeleton/>;
}

function ServerDetailSkeleton() {
    return (
        <div className="min-h-screen bg-slate-800 text-white animate-fade-in">
            {/* Content */}
            <div className="pt-16 pb-6">
                {/* Server Info Section Skeleton */}
                <div className="px-6 py-8">
                    {/* Server Name Skeleton */}
                    <div className="animate-pulse">
                        <div className="h-9 bg-slate-700 rounded-lg w-3/4 mb-4"></div>
                        <div className="flex items-center space-x-2">
                            <div className="h-4 bg-slate-700 rounded w-20"></div>
                            <div className="h-4 bg-slate-600 rounded w-48"></div>
                        </div>
                    </div>
                </div>

                {/* Server Rack Illustration Skeleton */}
                <div className="px-6 py-8 bg-gradient-to-b from-slate-800 to-slate-300">
                    <div className="flex justify-center">
                        <div className="relative animate-pulse">
                            {/* Server Rack Skeleton */}
                            <div className="bg-slate-900 rounded-lg p-6 border-4 border-slate-700 shadow-2xl">
                                <div className="bg-slate-800 rounded-t-lg p-2 mb-2">
                                    <div className="flex items-center justify-between">
                                        <div className="h-3 bg-slate-700 rounded w-20"></div>
                                        <div className="flex space-x-1">
                                            <div className="w-2 h-2 bg-slate-700 rounded-full animate-pulse"></div>
                                            <div
                                                className="w-2 h-2 bg-slate-700 rounded-full animate-pulse"
                                                style={{animationDelay: "500ms"}}
                                            ></div>
                                            <div
                                                className="w-2 h-2 bg-slate-700 rounded-full animate-pulse"
                                                style={{animationDelay: "1000ms"}}
                                            ></div>
                                        </div>
                                    </div>
                                </div>

                                {/* Server Units Skeleton */}
                                <div className="space-y-2">
                                    {[1, 2, 3, 4, 5, 6].map((unit) => (
                                        <div
                                            key={unit}
                                            className="bg-slate-700 rounded p-3 border border-slate-600 animate-pulse"
                                            style={{animationDelay: `${unit * 100}ms`}}
                                        >
                                            <div className="flex items-center justify-between">
                                                <div className="flex items-center space-x-3">
                                                    <div
                                                        className="w-3 h-3 bg-slate-600 rounded-full animate-pulse"></div>
                                                    <div className="h-3 bg-slate-600 rounded w-12"></div>
                                                </div>
                                                <div className="flex space-x-2">
                                                    <div className="h-3 bg-slate-600 rounded w-6"></div>
                                                    <div className="h-3 bg-slate-600 rounded w-6"></div>
                                                    <div className="h-3 bg-slate-600 rounded w-8"></div>
                                                    <div className="h-3 bg-slate-600 rounded w-6"></div>
                                                </div>
                                            </div>
                                        </div>
                                    ))}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                {/* Status Section Skeleton */}
                <div className="px-6 py-6 bg-slate-800">
                    <div className="animate-pulse">
                        <div className="h-6 bg-slate-700 rounded w-16 mb-4"></div>
                        <div className="bg-slate-700 rounded-xl p-4 border border-slate-600">
                            <div className="flex items-center justify-between">
                                <div className="flex items-center space-x-3">
                                    <div className="w-3 h-3 bg-slate-600 rounded-full animate-pulse"></div>
                                    <div className="h-6 bg-slate-600 rounded w-20"></div>
                                </div>
                                <div className="w-6 h-6 bg-slate-600 rounded"></div>
                            </div>
                        </div>
                    </div>
                </div>

                {/* Actions Section Skeleton */}
                <div className="px-6 py-6">
                    <div className="animate-pulse">
                        <div className="h-6 bg-slate-700 rounded w-20 mb-6"></div>
                        <div className="space-y-4">
                            <div className="h-14 bg-slate-700 rounded-xl animate-pulse"></div>
                            <div className="h-14 bg-slate-700 rounded-xl animate-pulse"
                                 style={{animationDelay: "200ms"}}></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}
