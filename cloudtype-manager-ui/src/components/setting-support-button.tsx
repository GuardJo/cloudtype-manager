import {ChevronRight} from "lucide-react";

/* 설정 페이지 내 하위 설정 및 진입 버튼 컴포넌트 */
export default function SettingSupportButton({
                                                 settingName,
                                                 settingDescription = null,
                                                 onclick
                                             }: SettingSupportButtonProps) {
    const moveSubSettingPage = () => {
        onclick()
    }

    return (
        <button
            className='w-full flex items-center justify-between py-3 group transition-colors hover:bg-slate-700/50 rouded-lg px-2 -mx-2'
            onClick={moveSubSettingPage}>
            <div className='text-left'>
                <p className='font-medium'>{settingName}</p>
                {(settingDescription !== null) &&
                    <p className='text-slate-400 text-sm'>View and regenerate your notification push token.</p>}
            </div>
            <ChevronRight className='w-5 h-5 text-slate-400 group-hover:text-white transition-colors'/>
        </button>
    )
}

interface SettingSupportButtonProps {
    settingName: string,
    settingDescription?: string | null,
    onclick: () => void,
}
