import {Meta, StoryObj} from "@storybook/nextjs";
import LogoutButton from "@/components/logout-button";

const meta = {
    title: 'components/LogoutButton',
    component: LogoutButton
} satisfies Meta<typeof LogoutButton>

export default meta

type Story = StoryObj<typeof meta>

export const Default: Story = {
    args: {
        userId: 'tester'
    },
}