import type {Preview} from '@storybook/nextjs'
import "../src/app/globals.css"
import {handlers} from '../src/mocks/handler'
import MockProvider from '../src/containers/mock-provider'
import QueryProvider from '../src/containers/query-provider'

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
        msw: {
            handlers: [...handlers]
        }
    },
    decorators: [
        (Story) => {
            return (
                <MockProvider>
                    <QueryProvider>
                        <Story/>
                    </QueryProvider>
                </MockProvider>
            )
        }
    ]
};

export default preview;