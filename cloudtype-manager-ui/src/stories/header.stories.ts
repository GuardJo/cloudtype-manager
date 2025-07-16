import {Meta, StoryObj} from "@storybook/nextjs";
import Header from "@/components/header";

const meta = {
    title: 'components/Header',
    component: Header
} satisfies Meta<typeof Header>

export default meta

type Story = StoryObj<typeof meta>

export const Home: Story = {
    args: {
        headerTitle: 'Cloudtype Manager'
    },
}
export const Servers: Story = {
    args: {
        headerTitle: 'Servers'
    },
}
export const Settings: Story = {
    args: {
        headerTitle: 'Settings'
    },
}