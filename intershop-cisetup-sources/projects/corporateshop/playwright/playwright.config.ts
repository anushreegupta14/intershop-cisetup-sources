import { defineConfig } from '@playwright/test';

const apiUser = process.env.API_USER || '';
const apiPassword = process.env.API_PASSWORD || '';
const basicAuth = apiUser && apiPassword
  ? Buffer.from(`${apiUser}:${apiPassword}`).toString('base64')
  : undefined;

export default defineConfig({
  testDir: './tests',
  fullyParallel: false,
  workers: 1,
  reporter: [
    ['html', { outputFolder: 'playwright-report' }],
    ['junit', { outputFile: 'test-results/junit.xml' }],
    ['list']
  ],
  use: {
    baseURL: process.env.BASE_URL || 'https://localhost',
    ignoreHTTPSErrors: true,
    extraHTTPHeaders: basicAuth
      ? { Authorization: `Basic ${basicAuth}` }
      : {},
    trace: 'on-first-retry',
  },
});
