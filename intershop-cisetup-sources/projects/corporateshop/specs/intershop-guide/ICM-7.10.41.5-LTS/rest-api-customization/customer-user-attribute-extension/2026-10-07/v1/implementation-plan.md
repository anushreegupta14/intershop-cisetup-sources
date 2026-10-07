# Implementation Plan: customer-user-attribute-extension

**Jira:** [SCRUM-6](https://theanushreegupta.atlassian.net/browse/SCRUM-6)  
**Guide:** `intershop-guide / ICM-7.10.41.5-LTS / rest-api-customization`  
**Specific functionality:** `customer-user-attribute-extension`  
**Date:** 2026-10-07  
**Plan version:** v1

## Goal

Add two attributes to the existing GET user REST API response for all storefront application types (B2C, SMB/B2B, SimpleSMB):

1. `userUUID` - the `UserBO` identifier (BasicProfile UUID)
2. `formattedUUID` - `userUUID` cleaned to contain only `A-Z a-z 0-9 - _` and truncated to a maximum of 64 characters

The implementation is delivered inside the existing `app_sf_rest_custom` cartridge using a Guice `FunctionExtension<UserBO, SMBCustomerUserRO>`.

## Traceability

| Source | What it provides | Confidence |
|---|---|---|
| `specs/intershop-guide/ICM-7.10.41.5-LTS/rest-api-customization/implementation-guide.md` | mapper extension pattern, `objectgraph.properties` registration, component self-registration | 95% |
| `specs/intershop-guide/ICM-7.10.41.5-LTS/rest-api-customization/tech-design.md` | Guice `FunctionExtension` pattern, cartridge activation, anti-patterns | 95% |
| Jira SCRUM-6 (retrieved via Atlassian MCP) | two field names, formatting rules, endpoint hint, cartridge hint | 100% |
| `app_sf_rest_custom/build.gradle` | existing dependencies include `app_sf_rest_smb`, `app_sf_rest_b2c`, `bc_user` | 100% |
| `app_sf_rest_custom/staticfiles/cartridge/components/apps-extension.component` | already self-registers with `B2CResponsive`, `SMBResponsive`, `SimpleSMBResponsive` | 100% |
| `inspired-b2c/build.gradle` | `app_sf_rest_custom` is already in `storefrontCartridges` and `order` | 100% |

## Open Point / Assumption

- The SMB/B2B user GET endpoint uses `SMBCustomerUserRO`.
- The B2C endpoint `customers/{customerNo}/users/{CustomerItemUserKey}` may use the same `SMBCustomerUserRO` mapper or a different B2C-specific user RO. The plan includes a discovery step; if the RO differs, add an equivalent `FunctionExtension` for that type.
- The exact method on `SMBCustomerUserRO` for adding a custom field is assumed to be `addCustomField(String, Object)` as described in the guide. Verify before coding and adjust if the platform API uses a different name.

## Test-First Approach

1. Add the failing JUnit test for `CustomSMBCustomerUserROFunctionExtension` first.
2. Implement the `FunctionExtension` and Guice module so the unit test passes.
3. Register the module and build the cartridge.
4. Run a human/integration smoke test against a running server.
5. Add an automated Playwright API test (or use `restTest` if Playwright is not configured) for end-to-end coverage.

## Phase 1: Discovery & Design

| Step | Action | Owner | Evidence / Verification |
|---|---|---|---|
| 1.1 | Confirm the user GET endpoint in SMB/B2B and B2C application contexts. Locate the resource and response object type for B2C (`app_sf_rest_b2c`) in addition to `SMBCustomerUserRO` (`app_sf_rest_smb`). | Developer | Class names and component files recorded. If B2C uses a different RO, open a follow-up step. |
| 1.2 | Verify `SMBCustomerUserRO` exposes a `addCustomField(String, Object)` or equivalent custom-field setter. | Developer | Exact method signature recorded in this plan before implementation. |
| 1.3 | Confirm `UserBO.getID()` returns the BasicProfile UUID in this ICM version. | Developer | Unit test can assert `source.getID()` is the value used. |

## Phase 2: Cartridge Setup

The `app_sf_rest_custom` cartridge already exists. No new `build.gradle` or assembly changes are needed for the SMB case.

### 2.1 Create package and Guice objectgraph registration

Create the following file:

`app_sf_rest_custom/src/main/resources/resources/app_sf_rest_custom/objectgraph/objectgraph.properties`

```properties
global.modules = com.intershop.application.rest.custom.user.internal.modules.CustomUserMapperModule
```

> Important: the runtime path inside the JAR must be `resources/app_sf_rest_custom/objectgraph/objectgraph.properties`. The first `resources` is the Gradle source root; the second is the classpath path expected by Intershop.

### 2.2 Ensure build dependencies are in place

`app_sf_rest_custom/build.gradle` already contains:

```gradle
compile group: 'com.intershop.business', name: 'app_sf_rest_common'
compile group: 'com.intershop.business', name: 'app_sf_rest_smb'
compile group: 'com.intershop.business', name: 'app_sf_rest_b2c'
compile group: 'com.intershop.platform', name: 'bc_user'
```

No change is required. If a B2C-specific user RO is needed in Phase 7, the matching API package is already on the compile classpath.

## Phase 3: Implementation

### 3.1 FunctionExtension implementation

File: `app_sf_rest_custom/src/main/java/com/intershop/application/rest/custom/user/internal/CustomSMBCustomerUserROFunctionExtension.java`

```java
package com.intershop.application.rest.custom.user.internal;

import java.util.regex.Pattern;

import com.intershop.component.user.capi.UserBO;
import com.intershop.sellside.rest.common.v1.capi.mapper.FunctionExtension;
import com.intershop.sellside.rest.smb.capi.resourceobject.SMBCustomerUserRO;

public class CustomSMBCustomerUserROFunctionExtension implements FunctionExtension<UserBO, SMBCustomerUserRO>
{
    private static final Pattern DISALLOWED = Pattern.compile("[^A-Za-z0-9_-]");
    private static final int MAX_LENGTH = 64;

    @Override
    public boolean isApplicable(UserBO source, SMBCustomerUserRO target)
    {
        return source != null && target != null;
    }

    @Override
    public SMBCustomerUserRO apply(UserBO source, SMBCustomerUserRO target)
    {
        if (source == null || target == null)
        {
            return target;
        }

        String userUUID = source.getID();
        if (userUUID != null && !userUUID.isEmpty())
        {
            target.addCustomField("userUUID", userUUID);

            String formatted = DISALLOWED.matcher(userUUID).replaceAll("");
            if (formatted.length() > MAX_LENGTH)
            {
                formatted = formatted.substring(0, MAX_LENGTH);
            }
            target.addCustomField("formattedUUID", formatted);
        }

        return target;
    }
}
```

> If `SMBCustomerUserRO` uses a different method than `addCustomField`, replace it with the platform method discovered in Phase 1.2.

### 3.2 Guice module

File: `app_sf_rest_custom/src/main/java/com/intershop/application/rest/custom/user/internal/modules/CustomUserMapperModule.java`

```java
package com.intershop.application.rest.custom.user.internal.modules;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import com.intershop.application.rest.custom.user.internal.CustomSMBCustomerUserROFunctionExtension;
import com.intershop.component.user.capi.UserBO;
import com.intershop.sellside.rest.common.v1.capi.mapper.FunctionExtension;
import com.intershop.sellside.rest.smb.capi.resourceobject.SMBCustomerUserRO;

public class CustomUserMapperModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        Multibinder<FunctionExtension<UserBO, SMBCustomerUserRO>> extensions =
            Multibinder.newSetBinder(binder(),
                new TypeLiteral<FunctionExtension<UserBO, SMBCustomerUserRO>>() { });
        extensions.addBinding().to(CustomSMBCustomerUserROFunctionExtension.class);
    }
}
```

### 3.3 Conditional: B2C user RO extension

If Phase 1.1 discovers that the B2C user endpoint uses a different RO (e.g., `CustomerUserRO`):

1. Add a second `FunctionExtension<UserBO, B2CUserRO>` with the same `userUUID`/`formattedUUID` logic.
2. Add a second `Multibinder` in `CustomUserMapperModule` or create a `B2CUserMapperModule` and add both class names to `global.modules` separated by whitespace. Do not use commas between class names.

## Phase 4: Testing (TDD)

### 4.1 Unit test for the FunctionExtension

File: `app_sf_rest_custom/src/test/java/tests/com/intershop/application/rest/custom/user/internal/CustomSMBCustomerUserROFunctionExtensionTest.java`

```java
package tests.com.intershop.application.rest.custom.user.internal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.intershop.application.rest.custom.user.internal.CustomSMBCustomerUserROFunctionExtension;
import com.intershop.component.user.capi.UserBO;
import com.intershop.sellside.rest.smb.capi.resourceobject.SMBCustomerUserRO;

public class CustomSMBCustomerUserROFunctionExtensionTest
{
    @Test
    public void addsUserUUIDAndFormattedUUID()
    {
        UserBO user = mock(UserBO.class);
        SMBCustomerUserRO target = mock(SMBCustomerUserRO.class);

        when(user.getID()).thenReturn("abc-123_def@456");

        new CustomSMBCustomerUserROFunctionExtension().apply(user, target);

        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Object> valueCaptor = ArgumentCaptor.forClass(Object.class);
        verify(target, times(2)).addCustomField(keyCaptor.capture(), valueCaptor.capture());

        Map<String, Object> captured = new HashMap<>();
        for (int i = 0; i < keyCaptor.getAllValues().size(); i++)
        {
            captured.put(keyCaptor.getAllValues().get(i), valueCaptor.getAllValues().get(i));
        }

        assertThat(captured, hasEntry("userUUID", "abc-123_def@456"));
        assertThat(captured, hasEntry("formattedUUID", "abc-123_def456"));
    }
}
```

If `addCustomField` is not void or is not mockable, adapt the test to assert on the populated target object through its getters.

### 4.2 Unit test coverage matrix

| Case | Expected |
|---|---|
| `userUUID` with special chars | `formattedUUID` strips all but `A-Z a-z 0-9 - _` |
| `formattedUUID` longer than 64 chars | truncated to 64 characters from the start |
| `userUUID` empty or null | neither attribute added, no exception |
| `source` or `target` is null | no exception, original target returned |
| `userUUID` already clean and <= 64 | `formattedUUID` equals `userUUID` |

### 4.3 Run JUnit tests with JaCoCo coverage

Command:

```sh
./gradlew :app_sf_rest_custom:test :app_sf_rest_custom:jacocoTestReport
```

Verification:
- Unit tests pass.
- JaCoCo HTML report is produced at `app_sf_rest_custom/target/reports/jacoco/test/html/index.html`.
- Save the coverage summary for the Pull Request.

**Human checkpoint:** If tests fail, fix the extension before proceeding. Do not continue to build until unit tests pass.

## Phase 5: Build, Wiring and Integration

### 5.1 Build the cartridge

Command:

```sh
./gradlew :app_sf_rest_custom:build
```

Verification: `app_sf_rest_custom/target/cartridge.zip` is produced and contains `resources/app_sf_rest_custom/objectgraph/objectgraph.properties` plus the compiled extension and module classes.

### 5.2 Check for class collisions

Command:

```sh
./gradlew :inspired-b2c:checkClassCollisions
```

### 5.3 Build the assembly

Command:

```sh
./gradlew :inspired-b2c:build
```

## Phase 6: Server and End-to-End Verification

### 6.1 Start a local server and deploy

Start the local ICM server for the `inspired-b2c` assembly so the Playwright and REST smoke tests can run against a live instance.



Verification:
- Server is reachable on the configured `webserverPort`.
- `app_sf_rest_custom` is listed in `cartridgelist.properties` and loaded after `app_sf_rest` and `app_sf_rest_smb`.

### 6.2 Manual REST smoke test

Call the user endpoint for the relevant application type. For SMB/B2B:

```sh
curl -u <user>:<pass> \
  https://<host>/INTERSHOP/rest/B2B/-/customers/{customerNo}/users/{CustomerItemUserKey}
```

For B2C:

```sh
curl -u <user>:<pass> \
  https://<host>/INTERSHOP/rest/B2C/-/customers/{customerNo}/users/{CustomerItemUserKey}
```

**Human checkpoint:** Confirm the response contains `userUUID` and `formattedUUID` with the expected values. If not, check:

- the rebuilt `app_sf_rest_custom` JAR contains `resources/app_sf_rest_custom/objectgraph/objectgraph.properties`
- the module and extension classes are in the JAR
- the application selected `app_sf_rest_custom`
- the server was restarted or the object graph was reloaded

### 6.3 Playwright / REST end-to-end test

If the project has a Playwright suite, add an API test such as:

```ts
import { test, expect } from '@playwright/test';

test('user response contains userUUID and formattedUUID', async ({ request }) => {
  const response = await request.get(
    'https://<host>/INTERSHOP/rest/B2C/-/customers/{customerNo}/users/{CustomerItemUserKey}',
    { headers: { Authorization: 'Basic <token>' } }
  );
  expect(response.ok()).toBeTruthy();

  const body = await response.json();
  expect(body).toHaveProperty('userUUID');
  expect(body).toHaveProperty('formattedUUID');
  expect(body.formattedUUID).toMatch(/^[A-Za-z0-9_-]{1,64}$/);
  expect(body.formattedUUID.length).toBeLessThanOrEqual(64);
});
```

Run Playwright against the started server:

```sh
npx playwright test
```

If Playwright is not configured in this repository, use the Gradle `restTest` task in `inspired-b2c` and add a Groovy Spock/REST test under `inspired-b2c/src/remoteTest/groovy/.../rest/`.

```sh
./gradlew :inspired-b2c:restTest
```

Verification:
- Playwright (or `restTest`) passes.
- Collect the Playwright `test-results` and `playwright-report` directories, or the `restTest` HTML/XML reports under `inspired-b2c/build/reports/remote/rest` and `inspired-b2c/build/test-results/rest`.
- These artifacts will be attached to the Pull Request.

**Human checkpoint:** Run the end-to-end verification against the started server and confirm the new fields appear. Do not consider the feature complete until this passes.

## Phase 7: Conditional B2C Follow-up

If Phase 1.1 finds that the B2C user response uses a different RO:

1. Repeat Phase 3 and 4 for the B2C user RO.
2. Register the second module in `objectgraph.properties` with whitespace-separated class names. Example:

```properties
global.modules = com.intershop.application.rest.custom.user.internal.modules.CustomUserMapperModule \
    com.intershop.application.rest.custom.user.internal.modules.B2CUserMapperModule
```

3. Run `./gradlew clean :app_sf_rest_custom:build :inspired-b2c:checkClassCollisions`.
4. Re-run the server smoke test and Playwright test for both B2C and SMB.

## Phase 8: Pull Request Preparation and Submission

### 8.1 Collect verification artifacts

Before raising the PR, gather the following evidence:

| Artifact | Source path / command |
|---|---|
| JaCoCo unit test coverage | `app_sf_rest_custom/target/reports/jacoco/test/html/index.html` |
| SonarQube report | Latest SonarQube scan for the feature branch (use the SonarQube MCP or `./gradlew :app_sf_rest_custom:sonarqube` if configured) |
| Playwright / REST results | `playwright-report/` and `test-results/` or `inspired-b2c/build/reports/remote/rest` and `inspired-b2c/build/test-results/rest` |

### 8.2 Create the feature branch and push

```sh
git checkout -b feature/customer-user-attribute-extension developbranch
git add app_sf_rest_custom/
git commit -m "Add userUUID and formattedUUID to user GET response"
git push -u origin feature/customer-user-attribute-extension
```

### 8.3 Raise the Pull Request to `developbranch`

Use the GitHub MCP or `gh` CLI to create a PR targeting `developbranch`.

PR description must contain:

- Link to Jira: [SCRUM-6](https://theanushreegupta.atlassian.net/browse/SCRUM-6)
- Summary of changes
- SonarQube report
- JaCoCo coverage report
- Playwright / `restTest` results

```sh
gh pr create --base developbranch --title "Add userUUID and formattedUUID to user GET response" --body-file pr-description.md
```

**Human checkpoint:** Verify the PR is created, CI checks are triggered, and the Sonar/coverage/Playwright reports are visible in the PR before merging.

## Rollback

1. Remove the `CustomSMBCustomerUserROFunctionExtension` class and `CustomUserMapperModule` (or unregister the module from `objectgraph.properties`).
2. Rebuild and redeploy `app_sf_rest_custom`.
3. The original `SMBCustomerUserRO` fields remain unchanged.

## Why This Plan Meets the Goal

- It uses the exact mechanism described in the Intershop implementation guide for enriching an existing BO-to-RO mapper: a Guice `FunctionExtension` and module registration.
- It preserves all existing `SMBCustomerUserRO` fields because the extension only adds fields.
- It implements the Jira acceptance criteria exactly: `userUUID` from `UserBO.getID()` and `formattedUUID` with allowed characters only and a 64-character cap.
- It keeps changes inside the project-owned `app_sf_rest_custom` cartridge and does not modify default Intershop cartridges.
- It includes tests at every logical layer: unit, build collision check, assembly build, and server/Playwright end-to-end, so issues are caught early.
- It calls out and plans for the only open item: a possibly different B2C user RO, with a conditional follow-up.
