import {Meta, StoryObj} from "@storybook/nextjs";
import LandingContent from "@/components/landing-content";

const meta = {
    title: 'components/LandingContent',
    component: LandingContent,
} satisfies Meta<typeof LandingContent>

export default meta

type Story = StoryObj<typeof meta>

export const Default: Story = {
    args: {},
}