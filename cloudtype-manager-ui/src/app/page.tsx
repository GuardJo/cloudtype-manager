import LandingContent from "@/components/landing-content";

/* 메인 랜딩 페이지 */
export default function Home() {
    return (
        <div className="transition-transform duration-300 ease-out translate-x-0" style={{
            transform: 'translateX(0)',
        }}>
            <LandingContent/>
        </div>
    );
}
