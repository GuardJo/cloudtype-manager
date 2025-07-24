'use client'

import Image from "next/image";
import {useRouter} from "next/navigation";

/* 구글 로그인 폼 */
export default function GoogleLoginForm() {
    const googleLoginUrl = `${process.env.NEXT_PUBLIC_API_SERVER_URL}/oauth2/authorization/google`;
    const router = useRouter()

    const handleGoogleLogin = () => {
        router.push(googleLoginUrl)
    }


    return (
        <div
            className="min-h-screen bg-slate-800 text-white flex flex-col items-center justify-center p-6 animate-fade-in">
            <h1 className="text-3xl font-bold mb-10 text-center">CloudType Manager</h1>

            <div className="w-full max-w-md space-y-6">
                {/* Login Button */}
                <div className="animate-fade-in-up" style={{animationDelay: "300ms"}}>
                    <div onClick={handleGoogleLogin}
                         className='w-full font-medium text-lg flex items-center justify-center  hover:scale-[1.02] active:scale-[0.98]'>
                        <Image src='/web_neutral_rd_ctn.svg' alt='google-login-button' height={40} width={200}/>
                    </div>
                </div>
            </div>
        </div>
    );
}