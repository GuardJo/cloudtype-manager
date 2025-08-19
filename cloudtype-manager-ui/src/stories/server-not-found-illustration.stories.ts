import {Meta, StoryObj} from "@storybook/nextjs";
import ServerNotFoundIllustration from "@/components/server-not-found-illustration";

const meta = {
    title: 'components/ServerNotFoundIllustration',
    component: ServerNotFoundIllustration
} satisfies Meta<typeof ServerNotFoundIllustration>

export default meta

type Story = StoryObj<typeof meta>

export const Default: Story = {
    args: {},
}