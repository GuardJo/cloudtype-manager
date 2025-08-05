import {Meta, StoryObj} from "@storybook/nextjs";
import ServerRackIllustration from "@/components/server-rack-illustration";

const meta = {
    title: 'components/ServerRackIllustration',
    component: ServerRackIllustration,
} satisfies Meta<typeof ServerRackIllustration>

export default meta

type Story = StoryObj<typeof meta>

export const Default: Story = {
    args: {},
}