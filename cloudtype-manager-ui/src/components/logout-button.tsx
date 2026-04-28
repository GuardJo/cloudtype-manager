/* 로그아웃 버튼 컴포넌트 */
export default function LogoutButton() {
    const handleLogout = () => {
        // TODO 기능 연동
    }

    return (
        <button
            className='w-full py-4 bg-slate-200 text-slate-800 font-semibold rounded-xl hover:bg-slate-300 active:scale-[0.98] transition-all'
            onClick={handleLogout}>
            Logout
        </button>
    )
}