import {Meta, StoryObj} from "@storybook/nextjs";
import ServerDetailContent from "@/components/server-detail-content";

const meta = {
    title: 'components/ServerDetailContent',
    component: ServerDetailContent
} satisfies Meta<typeof ServerDetailContent>

export default meta

type Story = StoryObj<typeof meta>

export const Default: Story = {
    args: {
        serverId: 1,
    },
}
