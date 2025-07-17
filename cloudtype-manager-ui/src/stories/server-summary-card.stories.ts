import {Meta, StoryObj} from "@storybook/nextjs";
import ServerSummaryCard from "@/components/server-summary-card";

const meta = {
    title: "components/ServerSummaryCard",
    component: ServerSummaryCard,
} satisfies Meta<typeof ServerSummaryCard>

export default meta

type Story = StoryObj<typeof meta>

export const ActiveServer: Story = {
    args: {
        server: {
            serverId: 1,
            serverName: "Server 1",
            activate: true
        }
    }
}

export const InactiveServer: Story = {
    args: {
        server: {
            serverId: 2,
            serverName: "Server 2",
            activate: false
        }
    }
}