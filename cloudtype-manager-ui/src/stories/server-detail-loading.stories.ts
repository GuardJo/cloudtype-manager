import {Meta, StoryObj} from "@storybook/nextjs";
import Loading from "@/app/servers/[serverId]/loading";

const meta = {
    title: 'components/ServerDetailLoading',
    component: Loading
} satisfies Meta<typeof Loading>

export default meta

type Story = StoryObj<typeof meta>

export const Default: Story = {
    args: {},
}

