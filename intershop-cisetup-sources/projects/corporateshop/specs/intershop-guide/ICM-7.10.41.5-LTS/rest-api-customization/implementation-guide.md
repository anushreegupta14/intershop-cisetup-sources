# Implementation Guide: New REST API Customization

## Purpose and Scope

### Purpose
This guide enables an Intershop Commerce Management (ICM) developer to add a new REST API customization by creating a dedicated cartridge. It supports three common scenarios:

1. **Override an existing REST handler contract** (e.g., `ProductHandler`, `BasketHandler`) to change the behavior of an existing endpoint.
2. **Add a new REST resource family** (list + item resources, handler contract, and resource wiring) to expose a new endpoint.
3. **Extend an existing BO-to-RO mapper** through a Guice `FunctionExtension` to add response fields where the endpoint uses an extensible mapper.

### In-Scope
- Creating a new Intershop cartridge (`app_sf_rest_<feature>`)
- Writing `build.gradle`, Java handler classes, component XML, and unit tests
- Writing mapper extensions, Guice modules, and `objectgraph.properties` registration
- Registering the cartridge with the target application type(s) and assembly
- Building, testing, and verifying the REST customization

### Out-of-Scope
- Front-end storefront changes
- Database schema or ORM entity changes
- Creating new business cartridges (`bc_*`)
- CI/CD deployment pipeline configuration

Confidence: 95%.

For overall architecture, see [Overall Architecture](../overall-architecture.md). For design rationale, patterns, and sequence diagrams, see [Technical Design Document](./tech-design.md).

## Universal Intershop Project Guidelines

The following rules are mandatory for any implementation in this project and must be applied before any code is written:

1. **Default Intershop cartridges are read-only** — Only the project-owned/custom cartridge (e.g., `app_sf_rest_custom` for this feature) may contain new or overridden artifacts. No source, component, pipeline, ISML, or build changes are permitted in any default Intershop cartridge (`app_sf_*` starter-store cartridges, `as_responsive*`, platform `bc_*`/`ac_*`, etc.).
2. **Additive-only changes in custom cartridges** — Custom cartridges may extend or override default behavior, but they must not remove or disable existing features unless the requirement explicitly and specifically calls for it.
3. **Database migrations via `dbmigrate`** — Any DDL or DML change must be implemented as a `dbmigrate` step, not as DB init or DB prepare logic.
4. **No dependency class collisions** — New dependencies added to a custom cartridge must not have packages/classes that overlap with Intershop platform or default cartridge dependencies. Always run `./gradlew :inspired-b2c:checkClassCollisions` after adding or changing dependencies.
5. **Access data through business objects** — Wherever data is accessible through a BO, its public extensions, repositories, or business services, use those APIs instead of persistent objects. Use persistence access only when the required data is not exposed by a supported business API; document the gap and isolate the fallback. Verify each BO identifier and attribute API for this platform version; do not assume every BO ID is a persistence UUID.

Confidence: 95%.

## Core Capabilities

| Capability | Use Case | Complexity | Example | Confidence |
|------------|----------|------------|---------|------------|
| **Handler override** | Modify product search results, basket calculations, order formatting, etc. | Medium | Override `ProductHandler` to inject custom attributes into product JSON | 95% |
| **Mapper extension** | Add fields where the existing BO-to-RO mapper is extensible | Low-Medium | Guice `FunctionExtension<UserBO, SMBCustomerUserRO>` | 95% |
| **New list resource** | Expose a new collection endpoint (e.g., `/customs`). | Medium-High | Add `CustomListResource` as a sub-resource of `RootResource` | 85% |
| **New item resource** | Expose a new single-item endpoint (e.g., `/customs/{id}`). | Medium-High | Add `CustomItemResource` under the list resource | 85% |
| **New handler contract** | Introduce a completely new domain API. | High | Add `CustomHandler` contract + implementation | 80% |
| **B2C/B2B/SMB variant** | Provide different behavior per application type. | Medium | Add handler only for B2B `intershop.SimpleSMBResponsive` | 85% |
| **Unit testing** | Verify handler logic without a running server. | Low | Mockito + `RequestRule` tests | 95% |

## Tech Stack

See [Technical Design Document](./tech-design.md#high-level-technology-stack-overview) for the detailed stack. In summary:

- **Backend:** Java 8, Intershop Component Framework, Guice object graph, Intershop REST framework, JSR-311
- **Build:** Gradle, `java-cartridge`, `static-cartridge`
- **Business APIs:** `ProductBO`, `BasketBO`, `OrderBO`, `CustomerBO`, etc.
- **Testing:** JUnit 4/5, Mockito, Hamcrest, `RequestRule`

Confidence: 95%.

## Sequence Diagram of End-to-End Flow

See [Technical Design Document, Section "End-to-End Sequence Diagram"](./tech-design.md#end-to-end-sequence-diagram).

## Detailed Folder Structure

A new REST customization cartridge is structured as follows (placeholders are in angle brackets):

```
app_sf_rest_<feature>/
├── build.gradle
├── src/
│   ├── main/
│   │   ├── resources/resources/app_sf_rest_<feature>/objectgraph/
│   │   │   └── objectgraph.properties           (for Guice modules)
│   │   └── java/
│   │       └── com/intershop/application/rest/<domain>/
│   │           ├── capi/
│   │           │   └── resource/
│   │           │       └── <Feature>ListResource.java        (only for new resources)
│   │           │       └── <Feature>ItemResource.java         (only for new resources)
│   │           └── internal/
│   │               └── <domain>/
│   │                   └── <Feature>HandlerImpl.java
│   └── test/
│       └── java/tests/com/intershop/application/rest/<domain>/
│           └── internal/
│               └── <Feature>HandlerImplTest.java
└── staticfiles/
    └── cartridge/
        └── components/
            ├── implementations.component       # handler and resource implementations
            ├── contracts.component             # only if new contracts are introduced
            ├── instances.component             # only if new resource wiring is required
            └── apps-extension.component        # self-register this cartridge with the target application types
```

If you are only overriding an existing handler, you can omit `capi/resource`, `contracts.component`, and `instances.component`. Always include `apps-extension.component` if the new cartridge is not already selected by the target application type(s).

Confidence: 95%.

## Reusable Functionalities

### Framework Resources (do not re-implement)

| Class / Component | Purpose | Evidence |
|-------------------|---------|----------|
| `AbstractRestCollectionResource` | Base for list resources | `app_sf_rest` `implementations.component` <ref_snippet file="D:\AIEnablement\intershop-cisetup-sources\projects\corporateshop\build\server\share\system\cartridges\app_sf_rest\release\components\implementations.component" lines="55:62"/> |
| `AbstractRestResource` | Base for item resources | `app_sf_rest` `implementations.component` <ref_snippet file="D:\AIEnablement\intershop-cisetup-sources\projects\corporateshop\build\server\share\system\cartridges\app_sf_rest\release\components\implementations.component" lines="63:67"/> |
| `RootResource` | Top-level REST API root | `app_sf_rest_b2c` `instances.component` <ref_snippet file="D:\AIEnablement\intershop-cisetup-sources\projects\corporateshop\build\server\share\system\cartridges\app_sf_rest_b2c\release\components\instances.component" lines="9:14"/> |
| `AppRootResourceAssignment` | Binds root to application | `app_sf_rest_b2c` `instances.component` <ref_snippet file="D:\AIEnablement\intershop-cisetup-sources\projects\corporateshop\build\server\share\system\cartridges\app_sf_rest_b2c\release\components\instances.component" lines="5:7"/> |
| `TokenAuthenticationProvider` | REST authentication | `app_sf_rest_b2c` `instances.component` <ref_snippet file="D:\AIEnablement\intershop-cisetup-sources\projects\corporateshop\build\server\share\system\cartridges\app_sf_rest_b2c\release\components\instances.component" lines="17:23"/> |
| `RESTAuthorizationService` | REST authorization | `app_sf_rest_b2c` `instances.component` <ref_snippet file="D:\AIEnablement\intershop-cisetup-sources\projects\corporateshop\build\server\share\system\cartridges\app_sf_rest_b2c\release\components\instances.component" lines="24:52"/> |

### Existing Handler Contracts (reuse if applicable)

Defined in `app_sf_rest/release/components/contracts.component`:

- `ProductHandler`
- `BasketHandler`
- `BasketLineItemHandler`
- `OrderHandler`
- `CustomerAddressHandler`
- `CustomerPaymentHandler`
- `CategoryHandler`
- `SearchIndexHandler`
- `PromotionHandler`
- `RecommendationHandler`

<ref_snippet file="D:\AIEnablement\intershop-cisetup-sources\projects\corporateshop\build\server\share\system\cartridges\app_sf_rest\release\components\contracts.component" lines="1:80"/>

### Base Handler Implementations (extend, do not copy)

| Class | Contract | Evidence |
|-------|----------|----------|
| `ProductSearchHandlerImpl` | `ProductHandler` | `app_sf_rest` `implementations.component` line 120 |
| `BasketHandlerImpl` | `BasketHandler` | `app_sf_rest` `implementations.component` line 122 |
| `OrderHandlerImpl` | `OrderHandler` | `app_sf_rest` `implementations.component` line 136 |
| `CustomerAddressHandlerImpl` | `CustomerAddressHandler` | `app_sf_rest` `implementations.component` line 154 |
| `SearchIndexHandlerImpl` | `SearchIndexHandler` | `app_sf_rest` `implementations.component` line 210 |
| `RecommendationHandlerImpl` | `RecommendationHandler` | `app_sf_rest_recomm` `implementations.component` line 45 |

### Business Objects and Helpers

Read values through BO getters, public BO extensions, repositories, and business services before considering persistence access. For the installed platform, `BasicProfilePOToUserBOMapper` constructs `UserBO` with the profile UUID as its ID, so use `user.getID()` for that value. A missing `PersistentObjectBOExtension` must not suppress a value already available from the BO. This identifier relationship is specific to this implementation; verify other BOs separately.

| Class | Purpose |
|-------|---------|
| `ProductBO`, `BasketBO`, `OrderBO`, `CustomerBO` | Business objects for domain data |
| `ProductRO.ProductROBuilder` | Builder for product REST response objects |
| `Request` / `PipelineDictionary` | Access current request, channel, and pipeline data |
| `AttributeGroup`, `AttributeDescriptor` | Work with custom product attributes |

### Test Utilities

| Class | Purpose |
|-------|---------|
| `RequestRule` | JUnit rule providing a `Request` instance for unit tests |
| `NamingMgr` / `PagingMgr` | Framework managers; mock in unit tests if the handler uses them |
| `Mockito`, `Hamcrest` | Mocking and assertions |

Confidence: 90%.

## Patterns and Anti-Patterns

### Recommended Patterns

1. **Handler override by same component name** — Declare an `<implementation>` with the same `name` as the OOTB handler. Because the new cartridge is loaded after `app_sf_rest`, the custom implementation wins. <ref_snippet file="D:\AIEnablement\intershop-cisetup-sources\projects\corporateshop\build\server\share\system\cartridges\app_sf_rest\release\components\implementations.component" lines="120:121"/> Confidence: 95%.
2. **Resource and handler separation** — Resources handle HTTP routing; handlers contain business logic. <ref_snippet file="D:\AIEnablement\intershop-cisetup-sources\projects\corporateshop\build\server\share\system\cartridges\app_sf_rest\release\components\implementations.component" lines="55:67"/> Confidence: 95%.
3. **Use `scope="global"`** — REST components are global across sites. Confidence: 95%.
4. **Sub-resource composition** — List resources provide `itemResource`; item resources provide `subResource`. Confidence: 95%.
5. **Self-registration via `apps-extension.component`** — A custom cartridge should add itself to the target `CartridgeListProvider` from its own `apps-extension.component` and must be listed in the assembly `storefrontCartridges`. This avoids editing default application-suite cartridges. Evidence: `app_sf_responsive_gdpr` and `ac_oidc_sf_responsive`. Confidence: 95%.
6. **Tests with `RequestRule`** — REST handlers may rely on `Request.getCurrent()`; use `RequestRule` to set a request. Confidence: 90%.

### Anti-Patterns

1. **Renaming existing contracts** — Breaks all resources that depend on them.
2. **Putting business logic in resources** — Use the handler instead.
3. **Wrong cartridge order** — An override must be loaded **after** `app_sf_rest`; otherwise the default handler is used.
4. **Missing `app_sf_rest` dependency** — The custom cartridge must compile against `app_sf_rest` / `app_sf_rest_common` and `rest`.
5. **Forgetting to self-register the cartridge** — A cartridge is not effective unless it is in the assembly `storefrontCartridges` and registered with the target `CartridgeListProvider` via an `apps-extension.component`.
6. **No unit tests** — Handler logic should be unit-testable without a server.
7. **Adding colliding dependencies** — Do not add artifacts whose packages/classes overlap with Intershop platform dependencies (e.g., `javax.ws.rs:jsr311-api` while the platform uses `jakarta.ws.rs:jakarta.ws.rs-api:2.1.6`).

## Considerations

In addition to the table below, **dependency collisions** are a common cause of build failures. New dependencies must not overlap with Intershop platform artifacts. The `build/` and `target/` directories may be stale after a dependency change, so run `./gradlew clean` before verifying. Always run `./gradlew :inspired-b2c:checkClassCollisions` after adding a new dependency.

| Area | Guidance | Confidence |
|------|----------|------------|
| **Performance** | Avoid N+1 attribute/BO lookups; cache channel lookups where possible. | 85% |
| **Security** | The REST root resource wires `TokenAuthenticationProvider` and `RESTAuthorizationService`; do not bypass them. New endpoints should respect the existing `RESTAuthorizationService` rules. | 90% |
| **Data Consistency** | Handlers run inside the Intershop request/transaction context; use business objects rather than direct SQL. | 90% |
| **Backward Compatibility** | If you override an existing handler, keep the response contract stable unless the API version changes. | 85% |
| **Response field serialization** | When adding a property to a resource object (RO), verify the active serializer and inherited visibility/inclusion rules. Where explicit JSON exposure is required, annotate the public getter with `@JsonProperty` using the API field name. Add `@JsonInclude(JsonInclude.Include.ALWAYS)` to that getter only when the contract requires the property to be returned for all values, including `false`, zero, and `null`. Keep annotations on the getter rather than duplicating them on the backing field. Use `com.fasterxml.jackson.annotation` annotations and verify that `jackson-annotations` is available on the compile classpath, aligned with the platform dependencies. For XML responses, verify the JAXB mapping separately. | 95% |
| **Dependency collisions** | New dependencies must not overlap with Intershop platform artifacts. Run `./gradlew :inspired-b2c:checkClassCollisions` to verify. | 95% |
| **SEO / Browser / UX / Mobile** | Not applicable to a pure REST endpoint; if the endpoint is consumed by a front-end, ensure the consumer handles the new JSON shape. | 95% |
| **Accessibility** | Not applicable to REST. | 95% |

## Generic Code Snippets

Generic templates use placeholders in angle brackets. Replace them with the actual names for your feature. Section 10 provides a concrete user-mapper example.

### 1. Cartridge `build.gradle`

```gradle
apply plugin: 'java-cartridge'
apply plugin: 'static-cartridge'

intershop {
    displayName = 'Application - REST <Feature> Customization'
    description = 'Custom REST API for <domain>.'
}

dependencies {
    // Base REST framework
    compile group: 'com.intershop.business', name: 'app_sf_rest'
    compile group: 'com.intershop.business', name: 'app_sf_rest_common'

    // Domain business cartridge(s)
    compile group: 'com.intershop.business', name: 'bc_<domain>'

    // Platform foundations
    compile group: 'com.intershop.platform', name: 'bc_foundation'
    compile group: 'com.intershop.platform', name: 'businessobject'
    compile group: 'com.intershop.platform', name: 'core'
    compile group: 'com.intershop.platform', name: 'orm'
    compile group: 'com.intershop.platform', name: 'pipeline'
    compile group: 'com.intershop.platform', name: 'rest'
    // Use the same REST API artifact the Intershop platform cartridges use
    // (jakarta.ws.rs-api) to avoid checkClassCollisions failures.
    compile 'jakarta.ws.rs:jakarta.ws.rs-api:2.1.6'

    // Test dependencies
    testCompile 'org.mockito:mockito-core'
    testCompile 'org.hamcrest:hamcrest-core'
    testCompile 'org.hamcrest:hamcrest-library'
    testCompile 'junit:junit'
    testCompile 'org.junit.jupiter:junit-jupiter'
    testCompile 'org.junit.jupiter:junit-jupiter-api'
    testCompile 'org.junit.vintage:junit-vintage-engine'
    testCompile 'org.junit.platform:junit-platform-runner'
    testCompile group: 'com.intershop.platform', name: 'pf_core_test'
}
```

Confidence: 95%.

### 2. `staticfiles/cartridge/components/implementations.component` — Handler Override

```xml
<?xml version="1.0" encoding="UTF-8"?>
<components xmlns="http://www.intershop.de/component/2010" scope="global">

    <!-- Override the default ProductSearchHandlerImpl by using the same name.
         The component framework resolves the last implementation with this name. -->
    <implementation name="ProductSearchHandlerImpl"
        class="com.intershop.application.rest.<domain>.internal.<Feature>HandlerImpl"
        factory="JavaBeanFactory"
        implements="ProductHandler" />

</components>
```

Confidence: 95%.

### 3. `staticfiles/cartridge/components/implementations.component` — New Resource Family

```xml
<?xml version="1.0" encoding="UTF-8"?>
<components xmlns="http://www.intershop.de/component/2010" scope="global">

    <!-- Optional new contract -->
    <contract name="<Feature>Handler" class="com.intershop.application.rest.<domain>.capi.<Feature>Handler" />

    <!-- New handler -->
    <implementation name="<Feature>HandlerImpl"
        class="com.intershop.application.rest.<domain>.internal.<Feature>HandlerImpl"
        factory="JavaBeanFactory"
        implements="<Feature>Handler" />

    <!-- New list resource -->
    <implementation name="<Feature>ListResource"
        class="com.intershop.application.rest.<domain>.capi.resource.<Feature>ListResource"
        factory="JavaBeanFactory"
        implements="AbstractRestCollectionResource">
        <requires name="itemResource" contract="RestResource" cardinality="1..1" />
        <requires name="name" contract="String" cardinality="1..1" />
        <requires name="handler" contract="<Feature>Handler" cardinality="1..1" />
    </implementation>

    <!-- New item resource -->
    <implementation name="<Feature>ItemResource"
        class="com.intershop.application.rest.<domain>.capi.resource.<Feature>ItemResource"
        factory="JavaBeanFactory"
        implements="AbstractRestResource">
        <requires name="subResource" contract="RestResource" cardinality="0..n" />
        <requires name="handler" contract="<Feature>Handler" cardinality="1..1" />
    </implementation>

</components>
```

Confidence: 90%.

### 4. `staticfiles/cartridge/components/instances.component` — New Resource Wiring

```xml
<?xml version="1.0" encoding="UTF-8"?>
<components xmlns="http://www.intershop.de/component/2010" scope="global">

    <instance name="intershop.WebShop.RESTAPI.<Feature>ListResource" with="<Feature>ListResource">
        <fulfill requirement="name" value="<plural-resource-name>" />
        <fulfill requirement="itemResource" with="intershop.WebShop.RESTAPI.<Feature>ItemResource" />
        <fulfill requirement="handler" with="intershop.WebShop.RESTAPI.<Feature>Handler" />
    </instance>

    <instance name="intershop.WebShop.RESTAPI.<Feature>ItemResource" with="<Feature>ItemResource">
        <fulfill requirement="handler" with="intershop.WebShop.RESTAPI.<Feature>Handler" />
    </instance>

    <instance name="intershop.WebShop.RESTAPI.<Feature>Handler" with="<Feature>HandlerImpl" />

    <!-- Add as sub-resource of the appropriate root (B2C/B2B/SMB) -->
    <fulfill requirement="subResource" of="intershop.<ApplicationType>.RESTAPI.root"
        with="intershop.WebShop.RESTAPI.<Feature>ListResource" />

</components>
```

Confidence: 85%.

### 5. Java Handler Override Template

```java
package com.intershop.application.rest.<domain>.internal;

import com.intershop.<domain>.capi.<Domain>BO;
import com.intershop.sellside.rest.common.capi.resource.<Domain>Handler;
import com.intershop.sellside.rest.common.internal.<Domain>HandlerImpl; // or <Domain>SearchHandlerImpl
import com.intershop.sellside.rest.common.capi.resourceobject.<Domain>RO;

/**
 * Custom <domain> handler that overrides OOTB behavior.
 */
public class <Feature>HandlerImpl extends <Domain>HandlerImpl
{
    @Override
    public void setCustomAttributes(<Domain>RO.<Domain>ROBuilder builder, <Domain>BO <domain>)
    {
        super.setCustomAttributes(builder, <domain>);

        if (<domain> == null)
        {
            return;
        }

        // Add custom attributes / logic here
        // builder.getAttributes().put("customKey", <domain>.getAttribute("customKey"));
    }
}
```

Confidence: 85% (exact base class and hook method names must be verified).

### 6. Java Handler from Scratch Template

```java
package com.intershop.application.rest.<domain>.internal;

import com.intershop.application.rest.<domain>.capi.<Feature>Handler;
import com.intershop.<domain>.capi.<Domain>BO;
import com.intershop.sellside.rest.common.capi.resourceobject.<Domain>RO;

public class <Feature>HandlerImpl implements <Feature>Handler
{
    @Override
    public <Domain>RO get<Domain>(String id)
    {
        // Load <Domain>BO by id
        // Build and return <Domain>RO
        return null;
    }

    @Override
    public <Domain>RO create<Domain>(<Domain>RO ro)
    {
        // Create business object
        return null;
    }
}
```

Confidence: 80% (exact handler interface methods must be defined).

### 7. Unit Test Template

```java
package tests.com.intershop.application.rest.<domain>.internal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.intershop.application.rest.<domain>.internal.<Feature>HandlerImpl;
import com.intershop.beehive.core.capi.naming.NamingMgr;
import com.intershop.beehive.core.capi.paging.PagingMgr;
import com.intershop.beehive.core.capi.paging.PageableIterator;
import com.intershop.beehive.core.capi.pipeline.PipelineDictionary;
import com.intershop.beehive.core.capi.request.Request;
import com.intershop.beehive.core.request.test.RequestRule;
import com.intershop.<domain>.capi.<Domain>BO;
import com.intershop.sellside.rest.common.capi.resourceobject.<Domain>RO;

public class <Feature>HandlerImplTest
{
    @Rule
    public RequestRule requestRule = new RequestRule();

    private <Feature>HandlerImpl handler;
    private <Domain>BO <domain>;
    private <Domain>RO.<Domain>ROBuilder builder;

    @Before
    public void setUp()
    {
        // Mock framework managers if the handler calls NamingMgr / PagingMgr
        NamingMgr namingMgr = mock(NamingMgr.class);
        PagingMgr pagingMgr = mock(PagingMgr.class);
        PageableIterator pageableIterator = mock(PageableIterator.class);
        when(namingMgr.lookupManager("PagingMgr")).thenReturn(pagingMgr);
        NamingMgr.setInstance(namingMgr);

        <domain> = mock(<Domain>BO.class);
        builder = new <Domain>RO.<Domain>ROBuilder(Collections.emptyList());
        handler = new <Feature>HandlerImpl();
    }

    @Test
    public void testSetCustomAttributesAddsCustomValue()
    {
        when(<domain>.getAttribute("customAttribute")).thenReturn("customValue");

        handler.setCustomAttributes(builder, <domain>);

        assertThat(builder.getAttributes(), hasEntry("customAttribute", "customValue"));
    }
}
```

Confidence: 85% (exact builder API and manager names must be verified).

### 8. Cartridge Self-Registration (`apps-extension.component`)

Instead of editing the application-suite cartridge (`as_responsive`, `as_responsive_b2b`), the new cartridge should register itself with the target `CartridgeListProvider`(s). This is the same pattern used by `app_sf_responsive_gdpr` and `ac_oidc_sf_responsive`.

In `app_sf_rest_<feature>/staticfiles/cartridge/components/apps-extension.component`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<components xmlns="http://www.intershop.de/component/2010">
    <fulfill requirement="selectedCartridge" value="app_sf_rest_<feature>" of="intershop.B2CResponsive.Cartridges" />
    <fulfill requirement="selectedCartridge" value="app_sf_rest_<feature>" of="intershop.SMBResponsive.Cartridges" />
    <fulfill requirement="selectedCartridge" value="app_sf_rest_<feature>" of="intershop.SimpleSMBResponsive.Cartridges" />
</components>
```

Source: `app_sf_responsive_gdpr` self-registration. <ref_snippet file="D:\AIEnablement\intershop-cisetup-sources\projects\corporateshop\app_sf_responsive_gdpr\staticfiles\cartridge\components\apps-extension.component" lines="1:5"/>

Confidence: 95%.

### 9. Assembly Cartridge List Update

In `inspired-b2c` (or `inspired-b2x`) `build.gradle`:

```gradle
def storefrontCartridges = [
    ...
    'app_sf_rest',
    'app_sf_rest_<feature>',
    ...
]
include(*(storefrontCartridges.collect { project(":$it") }), in:[development, test, production])

order = listFromAssembly('com.intershop.assembly:commerce_management_b2c') + storefrontCartridges + ...
```

Source: `inspired-b2c` assembly. <ref_snippet file="D:\AIEnablement\intershop-cisetup-sources\projects\corporateshop\inspired-b2c\build.gradle" lines="54:90"/>

Confidence: 95%.

### 10. Guice Mapper Extension and Objectgraph Registration

The following concrete example uses the project's `UserBO` to `SMBCustomerUserRO` extension. Verify that the target endpoint uses this mapper; cartridge selection alone does not make the extension apply to other response types. Keep the platform mapper and contribute to its existing extension set.

**Module:** `src/main/java/com/intershop/application/rest/custom/user/internal/modules/CustomUserMapperModule.java`

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

Implement `FunctionExtension<UserBO, SMBCustomerUserRO>` with `isApplicable` and `apply`. Preserve the target RO and its existing fields; read the UUID through `source.getID()` and add the desired fields with `target.addCustomField(...)`. The project example uses `useruuid` and `formattedUUID`; field names are case-sensitive. Its formatted value removes non-alphanumeric characters and is not a standard UUID conversion.

**Registration file:** `src/main/resources/resources/app_sf_rest_custom/objectgraph/objectgraph.properties`

```properties
global.modules = com.intershop.application.rest.custom.user.internal.modules.CustomUserMapperModule
```

The first `resources` directory is the Java resource source root; the second is part of the classpath resource name expected by Intershop. The packaged JAR must contain `resources/app_sf_rest_custom/objectgraph/objectgraph.properties`. Placing the file at `src/main/resources/app_sf_rest_custom/objectgraph/objectgraph.properties` omits that required prefix. When other modules are already registered, retain them in the same `global.modules` value using a properties continuation; do not overwrite them or add a duplicate key.

Use the exact generic types and any binding qualifier required by the platform mapper. Include the cartridge supplying the mapper/RO (`app_sf_rest_smb` here), `app_sf_rest_common`, and the BO API dependencies, aligned with the platform. A new component implementation declaration is not required solely to register this extension; assembly inclusion and application self-registration remain required.

Multiple module class names are **whitespace-separated, not comma-separated**. A comma is treated as part of the class name and can cause `ObjectGraphException: Error loading module ...Module,` during startup. For example, when both modules exist in the cartridge:

```properties
global.modules = com.intershop.application.rest.custom.user.internal.modules.CustomUserMapperModule \
    com.intershop.application.rest.custom.customer.internal.modules.CustomCustomerMapperModule
```

The backslash continues the same property onto the next line; do not put a comma before it.

Evidence: the project's `CustomUserMapperModule`, `CustomSMBCustomerUserROFunctionExtension`, and registration file; the standard `app_sf_responsive/src/main/resources/resources/app_sf_responsive/objectgraph/objectgraph.properties` uses the same resource layout. The installed `app_sf_rest_smb` JAR supplies `AppSfRestSMBMapperModule` and `SMBCustomerUserROMapper`.

**Verification and troubleshooting:**

1. Test response enrichment with a mocked BO exposing the required getters and no persistence extension. Check original fields and exact custom-field names.
2. Build the cartridge and inspect its JAR for the registration entry and module/extension classes. A passing unit test does not prove runtime module discovery.
3. Confirm the actual running server receives the rebuilt cartridge, that the target application selects it, and that the server is restarted to reload the object graph.
4. Call the authenticated endpoint for the intended application variant. If fields are missing, trace module loading, exact binding types, mapper invocation, `isApplicable`, BO values, and serialization in that order. Check server logs for injection errors.

Confidence: 95% for the local registration and mapper pattern; the deployed endpoint must be verified separately.

## Detailed Implementation Plan & Checklist

### Choosing the Extension Mechanism

Trace the actual endpoint and application variant first. Use a mapper extension when its BO-to-RO conversion uses `ExtensibleFunction`; use the existing handler hook for endpoints that do not expose that extension point. Guice set contributions are additive and are not governed by the component framework's same-name override rule.

### Phase 1: Design and Decisions

| Step | Action | Owner | Verification |
|------|--------|-------|--------------|
| 1.1 | Choose the target handler/resource to customize | Developer | Document decision |
| 1.2 | Choose handler override, new resource family, or existing mapper extension | Developer | Actual endpoint and extension point verified |
| 1.3 | Identify application types (B2C, B2B, SMB, all) | Developer | `as_responsive*` selected |
| 1.4 | Name the new cartridge: `app_sf_rest_<feature>` | Developer | Name approved |

### Phase 2: Cartridge Creation

| Step | Action | Files / Commands | Verification |
|------|--------|------------------|--------------|
| 2.1 | Create cartridge directory and `build.gradle` | `app_sf_rest_<feature>/build.gradle` | `build.gradle` compiles against `app_sf_rest`, `rest`, `bc_<domain>` and passes `checkClassCollisions` |
| 2.2 | Create Java package structure | `src/main/java/com/intershop/application/rest/<domain>/...` | Package matches component declarations |
| 2.3 | Implement the handler or mapper extension and its Guice module | `src/main/java/.../internal/` | Class compiles; matches the selected extension point |
| 2.4 | (Optional) Implement resource classes | `.../capi/resource/<Feature>ListResource.java`, `<Feature>ItemResource.java` | Resources implement `AbstractRestCollectionResource` / `AbstractRestResource` |
| 2.5 | Create component declarations for handler/resource changes | `staticfiles/cartridge/components/implementations.component` | Correct `name`/`class`/`implements`; mapper-only contributions use Guice registration instead |
| 2.6 | (Optional) Create contracts and instances | `contracts.component`, `instances.component` | New resources are wired as sub-resources of the root |
| 2.7 | Create `apps-extension.component` for self-registration | `staticfiles/cartridge/components/apps-extension.component` | Cartridge is added to the `CartridgeListProvider` of all target application types |

### Phase 3: Wiring

| Step | Action | Files | Verification |
|------|--------|-------|--------------|
| 3.1 | Create `apps-extension.component` to self-register with the target application type(s) | `app_sf_rest_<feature>/staticfiles/cartridge/components/apps-extension.component` | Cartridge appears in `CartridgeListProvider` for B2C, SMB, and B2B |
| 3.2 | Add the new cartridge to the assembly | `inspired-b2c/build.gradle` and/or `inspired-b2x/build.gradle` | Cartridge is in `storefrontCartridges` and `order` |
| 3.3 | Ensure correct cartridge load order | Assembly `order` list | New cartridge is after `app_sf_rest` if it overrides |
| 3.4 | Run `./gradlew :inspired-b2c:checkClassCollisions` | Build output | No class-collision errors |
| 3.5 | For mapper extensions, register the Guice module | `src/main/resources/resources/<cartridge>/objectgraph/objectgraph.properties` | Exact extension binding matches the platform mapper; existing modules retained |

### Phase 4: Testing

| Step | Action | Files | Verification |
|------|--------|-------|--------------|
| 4.1 | Write unit tests for the handler | `src/test/java/tests/.../<Feature>HandlerImplTest.java` | Tests pass with `./gradlew :app_sf_rest_<feature>:test` |
| 4.2 | Write embedded / integration test if the handler interacts with DB | `app_sf_responsive_test` or new test cartridge | Test runs against local server |
| 4.3 | Run REST smoke test against a running server | `restTest` task in `inspired-b2c` | Endpoint returns expected JSON |
| 4.4 | Verify BO-based enrichment without persistence extensions | Mapper extension unit test | Fields are populated through supported BO APIs |
| 4.5 | Verify serialization of newly added response properties | Serialization test using the endpoint's serializer configuration and REST smoke test | Actual response contains the expected field names and values, including both boolean states and applicable default/null values according to the contract; verify each supported response format |

### Phase 5: Build and Deployment

| Step | Action | Command | Verification |
|------|--------|---------|--------------|
| 5.1 | Build the new cartridge | `./gradlew :app_sf_rest_<feature>:build` | `target/` contains `cartridge.zip` |
| 5.2 | Build the assembly | `./gradlew :inspired-b2c:build` | Assembly includes the cartridge and `checkClassCollisions` passes |
| 5.3 | Deploy / test in target environment | Deployment scripts | REST endpoint behaves as expected |
| 5.4 | Verify Guice resources in the deployed JAR and reload the object graph | JAR inspection and server restart | `resources/<cartridge>/objectgraph/objectgraph.properties` and module classes are present; authenticated endpoint returns the fields |

## Queries for User

The following information is needed to produce a concrete, predictable implementation plan:

1. **What is the target domain?** (product, customer, basket, order, category, search, recommendation, or other)
2. **Does the change need a handler override, a new REST resource family, or enrichment through an existing mapper extension?**
3. **What is the desired cartridge name?** (e.g., `app_sf_rest_product`, `app_sf_rest_order_custom`)
4. **Which application types must expose this REST API?** (B2C, B2B, SMB, all)
5. **Which existing REST contract should be reused or replaced?** (e.g., `ProductHandler`, `BasketHandler`)
6. **What business data or attributes must be added or modified in the REST response?**
7. **Are there authentication/authorization requirements beyond the default `RESTAuthorizationService`?**
8. **Do you need sub-resources under the new endpoint?** (e.g., `/products/{id}/customs`)
9. **Is the exact parent handler API (`setCustomAttributes` etc.) known, or do you need help locating the Intershop source/javadocs?**
10. **Which test coverage is required?** (unit tests only, embedded server tests, full REST smoke tests)

## Confidence Score Summary

| Section | Confidence |
|---------|------------|
| Purpose and Scope | 95% |
| Core Capabilities | 90% |
| Tech Stack | 95% |
| Folder Structure | 95% |
| Reusable Functionalities | 90% |
| Patterns and Anti-Patterns | 90% |
| Considerations | 85% |
| Generic Code Snippets | 85% (some parent class methods not in source) |
| Implementation Plan | 95% |
| Queries for User | 95% |
