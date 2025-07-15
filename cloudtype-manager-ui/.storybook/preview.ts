import type {Preview} from '@storybook/nextjs'
import "../src/app/globals.css"

const preview: Preview = {
    parameters: {
        controls: {
            matchers: {
                color: /(background|color)$/i,
                date: /Date$/i,
            },
        },
        nextjs: {
            appDirectory: true,
        },
        docs: {
            story: {
                inline: false,
                iframeHeight: 400
            }
        },
    },
};

export default preview;