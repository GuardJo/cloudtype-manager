'use client'

import {useMutation} from "@tanstack/react-query"
import {logout} from "@/lib/auth-api-handler";
import {useRouter} from "next/navigation";

/* 로그아웃 버튼 컴포넌트 */
export default function LogoutButton({userId}: LogoutButtonProps) {
    const router = useRouter()
    const logoutMutation = useMutation({
        mutationKey: ['logout', userId],
        mutationFn: (username: string) => logout(username),
        onSuccess: () => {
            window.alert('로그아웃 되었습니다.')
            router.replace('/')
        },
        onError: (error) => {
            console.error(error)
            window.alert(error.message)
        }
    })

    const handleLogout = () => {
        if (window.confirm('Would you like to log out?')) {
            logoutMutation.mutate(userId)
        }
    }

    return (
        <button
            className='w-full py-4 bg-slate-200 text-slate-800 font-semibold rounded-xl hover:bg-slate-300 active:scale-[0.98] transition-all'
            onClick={handleLogout}>
            Logout
        </button>
    )
}

interface LogoutButtonProps {
    userId: string
}