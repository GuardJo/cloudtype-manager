import {Meta, StoryObj} from "@storybook/nextjs";
import ServiceInformation from "@/components/service-information";

const meta = {
    title: 'components/ServiceInformation',
    component: ServiceInformation,
} satisfies Meta<typeof ServiceInformation>

export default meta

type Story = StoryObj<typeof meta>

export const Default: Story = {
    args: {
        infoContent: 'Test Content',
        startUrl: '/test'
    }
}