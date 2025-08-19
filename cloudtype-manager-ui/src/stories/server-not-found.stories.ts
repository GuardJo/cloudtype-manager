import {Meta, StoryObj} from "@storybook/nextjs";
import ServerNotFound from "@/app/servers/[serverId]/not-found";

const meta = {
    title: 'page/ServerNotFound',
    component: ServerNotFound
} satisfies Meta<typeof ServerNotFound>

export default meta

type Story = StoryObj<typeof meta>

export const Default: Story = {
    args: {},
}