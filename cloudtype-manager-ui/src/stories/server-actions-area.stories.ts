import {Meta, StoryObj} from "@storybook/nextjs";
import ServerActionsArea from "@/components/server-actions-area";

const meta = {
    title: 'components/ServerActionsArea',
    component: ServerActionsArea,
} satisfies Meta<typeof ServerActionsArea>

export default meta;

type Story = StoryObj<typeof meta>

export const DisableActions: Story = {
    args: {},
}

export const enableDashboardAction: Story = {
    args: {
        dashboardUrl: new URL('https://google.com')
    }
}

export const enableViewEventAction: Story = {
    args: {
        viewEventUrl: new URL('https://google.com')
    }
}

export const enableAllActions: Story = {
    args: {
        dashboardUrl: new URL('https://naver.com'),
        viewEventUrl: new URL('https://google.com'),
    }
}