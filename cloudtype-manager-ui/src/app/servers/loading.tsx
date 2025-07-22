import {Skeleton} from "@/components/ui/skeleton";

/* 서버 목록 조회 로딩 스켈레톤 컴포넌트 */
export default function Loading() {
    return <ServerListSkeleton/>;
}

function ServerListSkeleton() {
    return (
        <div className="space-y-4">
            {[...Array(5)].map((_, i) => (
                <ServerCardSkeleton key={i}/>
            ))}
        </div>
    );
}

function ServerCardSkeleton() {
    return (
        <div className="w-full bg-slate-700 rounded-xl p-4">
            <div className="flex items-center space-x-4">
                <Skeleton className="w-12 h-12 rounded-lg bg-slate-600"/>
                <div className="flex-1 text-left space-y-2">
                    <Skeleton className="h-6 w-3/4 bg-slate-600"/>
                    <div className="flex items-center space-x-2">
                        <Skeleton className="w-2 h-2 rounded-full bg-slate-600"/>
                        <Skeleton className="h-4 w-8 bg-slate-600"/>
                    </div>
                </div>
                <Skeleton className="w-5 h-5 bg-slate-600"/>
            </div>
        </div>
    );
}