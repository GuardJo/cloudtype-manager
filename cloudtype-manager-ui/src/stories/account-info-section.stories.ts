import {Meta, StoryObj} from "@storybook/nextjs";
import AccountInfoSection from "@/components/account-info-section";

const meta = {
    title: 'components/AccountInfoSection',
    component: AccountInfoSection
} satisfies Meta<typeof AccountInfoSection>

export default meta

type Story = StoryObj<typeof meta>

export const Default: Story = {
    args: {
        userName: '테스터',
        userEmail: "tester@gmail.com"
    },
}
