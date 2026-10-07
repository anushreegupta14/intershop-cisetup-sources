import { test, expect } from '@playwright/test';

const userUrl = process.env.USER_URL ||
  'INTERSHOP/rest/WFS/inSPIRED-inTRONICS_Business-Site/-/customers/AgroNet/users/abeat%40test.intershop.de';

test('user response contains userUUID and formattedUUID', async ({ request }) => {
  const response = await request.get(userUrl);
  expect(response.ok()).toBeTruthy();

  const body = await response.json();
  expect(body).toHaveProperty('userUUID');
  expect(body).toHaveProperty('formattedUUID');
  expect(body.formattedUUID).toMatch(/^[A-Za-z0-9_-]{1,64}$/);
  expect(body.formattedUUID.length).toBeLessThanOrEqual(64);
});
