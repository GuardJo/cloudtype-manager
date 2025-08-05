import {Meta, StoryObj} from "@storybook/nextjs";
import ServerCreateForm from "@/components/server-create-form";

const meta = {
    title: 'components/ServerCreateForm',
    component: ServerCreateForm,
} satisfies Meta<typeof ServerCreateForm>

export default meta

type Story = StoryObj<typeof meta>

export const Default: Story = {
    args: {}
}