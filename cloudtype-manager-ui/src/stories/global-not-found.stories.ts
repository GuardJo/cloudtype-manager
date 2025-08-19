import {Meta, StoryObj} from "@storybook/nextjs";
import NotFound from "@/app/not-found";

const meta = {
    title: 'page/NotFound',
    component: NotFound
} satisfies Meta<typeof NotFound>

export default meta

type Story = StoryObj<typeof meta>

export const Default: Story = {
    args: {},
}