import {Meta, StoryObj} from "@storybook/nextjs";
import ServerStatusBadge from "@/components/server-status-badge";

const meta = {
    title: 'components/ServerStatusBadge',
    component: ServerStatusBadge,
} satisfies Meta<typeof ServerStatusBadge>

export default meta;

type Story = StoryObj<typeof meta>

export const Offline: Story = {
    args: {
        activate: false,
    }
}

export const Online: Story = {
    args: {
        activate: true,
    }
}