import {Meta, StoryObj} from "@storybook/nextjs";
import NavigationBar from "@/components/navigation-bar";

const meta = {
    title: 'components/NavigationBar',
    component: NavigationBar,
} satisfies Meta<typeof NavigationBar>

export default meta

type Story = StoryObj<typeof meta>

export const ActiveHome: Story = {
    args: {
        activeTab: 'home'
    }
}
export const ActiveServers: Story = {
    args: {
        activeTab: 'servers'
    }
}
export const ActiveSettings: Story = {
    args: {
        activeTab: 'settings'
    }
}