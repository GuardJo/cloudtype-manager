import {Meta, StoryObj} from "@storybook/nextjs";
import GoogleLoginForm from "@/components/google-login-form";

const meta = {
    title: 'components/GoogleLoginForm',
    component: GoogleLoginForm
} satisfies Meta<typeof GoogleLoginForm>

export default meta

type Story = StoryObj<typeof meta>

export const Default: Story = {
    args: {},
}