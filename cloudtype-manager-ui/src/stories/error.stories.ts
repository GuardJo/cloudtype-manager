import {Meta, StoryObj} from "@storybook/nextjs";
import ErrorPage from "@/app/error";
import {action} from "storybook/actions";

const meta = {
    title: 'page/Error',
    component: ErrorPage
} satisfies Meta<typeof ErrorPage>

export default meta

type Story = StoryObj<typeof meta>

export const Default: Story = {
    args: {
        error: new Error('Test Error'),
        reset: action("reset")
    },
}

export const HasDigest: Story = {
    args: {
        error: {
            ...new Error(),
            message: 'Test Error',
            digest: 'TEST_ERROR_001'
        },
        reset: action("reset")
    },
}
