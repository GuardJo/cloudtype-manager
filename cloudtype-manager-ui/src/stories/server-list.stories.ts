import {Meta, StoryObj} from "@storybook/nextjs";
import ServerList from "@/components/server-list";

const meta = {
    title: 'components/ServerList',
    component: ServerList
} satisfies Meta<typeof ServerList>

export default meta

type Story = StoryObj<typeof meta>

export const Default: Story = {
    args: {}
}
