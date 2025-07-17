import {Meta, StoryObj} from "@storybook/nextjs";
import ServerConsole from "@/components/server-console";

const meta = {
    title: 'components/ServerConsole',
    component: ServerConsole
} satisfies Meta<typeof ServerConsole>

export default meta

type Story = StoryObj<typeof meta>

export const Default: Story = {
    args: {},
}