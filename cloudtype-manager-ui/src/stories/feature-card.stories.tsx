import {Meta, StoryObj} from "@storybook/nextjs";
import FeatureCard from "@/components/feature-card";
import {Info, Link, TrendingUp} from "lucide-react";

const meta = {
    title: 'components/FeatureCard',
    component: FeatureCard
} satisfies Meta<typeof FeatureCard>

export default meta

type Story = StoryObj<typeof meta>

export const LinkFeature: Story = {
    args: {
        featureIcon: <Link className='text-white w-6 h-6'/>,
        cardTitle: 'Link',
        cardContent: 'Test Content'
    }
}

export const TrendingUpFeature: Story = {
    args: {
        featureIcon: <TrendingUp className='text-white w-6 h-6'/>,
        cardTitle: 'Trending Up',
        cardContent: 'Test Content'
    }
}

export const InfoFeature: Story = {
    args: {
        featureIcon: <Info className='text-white w-6 h-6'/>,
        cardTitle: 'Info',
        cardContent: 'Test Content'
    }
}