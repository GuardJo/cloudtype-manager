import type {StorybookConfig} from '@storybook/nextjs';

const config: StorybookConfig = {
    "stories": [
        "../src/**/*.mdx",
        "../src/**/*.stories.@(js|jsx|mjs|ts|tsx)"
    ],
    "addons": [],
    "framework": {
        "name": "@storybook/nextjs",
        "options": {}
    },
    "staticDirs": [
        "..\\public"
    ],
    env: (envConfig) => (
        {
            ...envConfig,
            NEXT_PUBLIC_API_SERVER_URL: 'http://localhost:8080',
            NEXT_PUBLIC_MOCK_SERVER_ENABLE: 'true'
        }
    )
};
export default config;