import {Meta, StoryObj} from "@storybook/nextjs";
import SettingSupportButton from "@/components/setting-support-button";
import {action} from "storybook/actions";

const meta = {
    title: 'components/SettingSupportButton',
    component: SettingSupportButton
} satisfies Meta<typeof SettingSupportButton>

export default meta

type Story = StoryObj<typeof meta>

export const Default: Story = {
    args: {
        settingName: 'Test Button',
        onclick: action('move test')
    },
}

export const HasDescription: Story = {
    args: {
        settingName: 'Test with description',
        settingDescription: 'This is Test Button',
        onclick: action('Move Test')
    }
}