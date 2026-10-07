# Overall Architecture

# Overall Architecture

## Executive Summary

This document describes the overall architecture of the **corporateshop** project: a custom Intershop Commerce Management (ICM) 7.10.41.5-LTS multi-channel storefront implementation. The project extends the standard `commerce_management_b2c` and `commerce_management_b2x` assemblies with responsive storefront cartridges, content model cartridges, REST API customizations, OpenID Connect adapters, B2B/B2C/SMB variants, microservices, and demo content. Confidence: 95%.

Source: `build.gradle` declares the Intershop version and assembly inheritance. <ref_snippet file="D:\AIEnablement\intershop-cisetup-sources\projects\corporateshop\build.gradle" lines="79:95"/>

## Project Type

- **Type:** Intershop Commerce Management (ICM) multi-project Gradle build
- **Primary Domain:** E-commerce storefront (B2C, B2B, SMB, B2X)
- **Packaging:** Intershop cartridges and assemblies
- **Deployment:** Intershop application server with `build/server` output and microservice distribution
- Confidence: 95%

## Overall Architecture Overview

```
                              +-------------------+
                              |   Web Browser /   |
                              |   REST Client     |
                              +---------+---------+
                                        |
                                        | HTTP(S)
                                        v
+----------------------------------------------------------------------------+
|                     Intershop Web Server / Servlet Engine                  |
|   (app_sf_responsive, app_sf_responsive_b2c/b2b/smb,                       |
|    app_sf_responsive_cm, ac_oidc_sf_responsive*, as_responsive*)           |
+-----------------------------------+----------------------------------------+
                                    |
              +---------------------+---------------------+
              |                                           |
              v                                           v
+-----------------------------------+      +----------------------------------+
|     Server-Side Pipeline Engine   |      |       REST API Framework         |
|   (Pipeline XML -> Pipelets /     |      |  (ProductHandler, JSR-311,       |
|    Java classes -> ISML templates)|      |   app_sf_rest_custom)            |
+-----------------------------------+      +----------------------------------+
              |                                           |
              +---------------------+---------------------+
                                    |
                                    v
+----------------------------------------------------------------------------+
|                Business / Platform Cartridges (com.intershop)              |
|  bc_product, bc_basket, bc_customer, bc_order, bc_orderprocess,            |
|  bc_payment, bc_catalog, bc_search, bc_rma, sld_pmc, sld_mcm, ...          |
+-----------------------------------+----------------------------------------+
                                    |
                                    v
+----------------------------------------------------------------------------+
|          ORM / Persistence Layer -> Database (Derby / Postgres / Oracle)   |
+----------------------------------------------------------------------------+

+----------------------------------------------------------------------------+
|                              Assemblies                                    |
|  inspired-b2c  : packages storefront cartridges for B2C/SMB               |
|  inspired-b2x  : packages storefront cartridges for B2B/B2X               |
|  microservices : platform-scheduling, checkout-recurringorders            |
+----------------------------------------------------------------------------+
```

Confidence: 90%.

## Modules and Their Purpose

| Module | Type | Purpose | Confidence |
|--------|------|---------|------------|
| `corporateshop` (root) | Gradle root | Version, dependency recommendations, subproject configuration, SCM, publishing | 95% |
| `inspired-b2c` | Assembly | B2C/SMB assembly packaging cartridges, microservices, config, sites, tests | 95% |
| `inspired-b2x` | Assembly | B2B/B2X assembly packaging cartridges, microservices, config, sites, tests | 95% |
| `as_responsive` | Application suite | Declares B2C/SMB application types and bundles storefront cartridges | 95% |
| `as_responsive_b2b` | Application suite | Declares B2B application types | 90% |
| `app_sf_responsive` | Java + static cartridge | Core responsive storefront: pipelines, ISML, Java pipelets, components, CSS | 95% |
| `app_sf_responsive_b2c` | Static cartridge | B2C-specific ISML and templates | 95% |
| `app_sf_responsive_b2b` | Static + Java cartridge | B2B-specific pipelines, pipelets, templates, requisition/approval logic | 95% |
| `app_sf_responsive_smb` | Static cartridge | SMB-specific storefront artifacts | 95% |
| `app_sf_responsive_cm` | Static cartridge | Responsive content model (page variants, components) | 90% |
| `app_sf_responsive_costcenter` | Static cartridge | Cost center functionality | 85% |
| `app_sf_responsive_gdpr` | Static cartridge | GDPR/privacy features | 80% |
| `app_sf_responsive_rma` | Static cartridge | Returns (RMA) integration | 85% |
| `app_sf_base_cm` | Static cartridge | Base content model pagelets and components (Carousel, Container, Dialog, Image, Text, etc.) | 95% |
| `app_sf_pwa_cm` | Static cartridge | PWA content model | 90% |
| `app_sf_rest_custom` | Java + static cartridge | Custom REST handlers (ProductHandler) | 95% |
| `ac_oidc_sf_responsive*` | Static cartridges | OpenID Connect authentication for responsive storefront variants | 90% |
| `responsive_config` | Static config | Configuration cartridge for `a_responsive` | 85% |
| `responsive_sites` | Static config | Sites cartridge for `a_responsive` | 85% |
| `demo_responsive*` | Static/demo | Demo catalog, content, search, OCST, B2B data | 85% |
| `app_sf_responsive_test` | Test cartridge | Unit and embedded tests | 90% |
| `app_sf_responsive_b2b_test` | Test cartridge | B2B unit and embedded tests | 90% |
| `dev_storefront` | Dev cartridge | Developer-only storefront content | 80% |
| `microservices` | Application | Microservice distribution (scheduling, recurring orders) | 85% |

Source: `inspired-b2c` / `inspired-b2x` cartridge lists. <ref_snippet file="D:\AIEnablement\intershop-cisetup-sources\projects\corporateshop\inspired-b2c\build.gradle" lines="44:90"/>

## Project and Folder Structure for Each Module

### Java/Static Cartridge (e.g., `app_sf_responsive`, `app_sf_rest_custom`)

```
<cartridge>/
├── build.gradle
├── src/main/java/...           # Java pipelets, handlers, services, Guice modules
├── src/main/resources/resources/<cartridge>/objectgraph/objectgraph.properties
│                               # Guice module registration; keep both resources segments
├── src/test/java/...           # JUnit / Mockito / Hamcrest tests
├── staticfiles/cartridge/
│   ├── components/             # implementations.component, instances.component, contracts.component
│   ├── pipelines/              # *.pipeline XML definitions
│   ├── templates/default/      # *.isml templates
│   ├── static/default/less/    # LESS stylesheets
│   └── static/default/css/     # Compiled CSS
└── target/                     # Build output
```

Confidence: 95%.

### Static-Only Cartridge (e.g., `app_sf_base_cm`, `app_sf_responsive_b2c`)

```
<cartridge>/
├── build.gradle
└── staticfiles/cartridge/
    ├── components/
    ├── pagelets/components/    # *.pagelet2, .properties, .svg definitions
    ├── templates/default/...   # ISML components / page variants
    └── config/                 # pageletACL.properties, etc.
```

Confidence: 95%.

### Application Suite (e.g., `as_responsive`)

```
as_responsive/
├── build.gradle                # Bundles app_sf_responsive, app_sf_responsive_b2c, etc.
└── staticfiles/cartridge/components/apps.component
```

Confidence: 95%.

### Assembly (e.g., `inspired-b2c`)

```
inspired-b2c/
├── build.gradle                # assembly { ... } block, cartridge inclusions, host types
└── src/test/...                # Geb/Selenium/REST integration tests
```

Confidence: 95%.

### Microservices

```
microservices/
├── build.gradle                # application plugin, distribution, drivers
├── src/initscript/             # Linux/Windows service wrappers
└── deployment/deploy.gradle    # Deployment descriptor
```

Confidence: 90%.

## Integration Points (End-to-End Request-Response Flow)

### Storefront Page Request Flow

1. Browser sends HTTP request to Intershop Web Server/Servlet Engine.
2. URL is parsed by `StorefrontLinkParser` and routed to the appropriate pipeline (e.g., `ViewProduct-Start`, `ViewStandardCatalog-Browse`).
3. Pipeline executes XML-defined pipelet chain; custom Java pipelets process request parameters and call business APIs.
4. Business/Platform cartridges (`bc_product`, `bc_catalog`, etc.) load data via ORM from database.
5. Pipeline sets dictionaries and calls ISML templates (`*.isml`) in `staticfiles/cartridge/templates/default/`.
6. ISML is compiled to JSP and rendered to HTML; LESS-compiled CSS and static assets are served.
7. HTML is returned to the browser.

Source: `app_sf_responsive` `instances.component` for link parser and `app_sf_responsive` `build.gradle` for dependencies. <ref_snippet file="D:\AIEnablement\intershop-cisetup-sources\projects\corporateshop\app_sf_responsive\staticfiles\cartridge\components\instances.component" lines="46:69"/>

### REST API Request Flow

1. REST client sends request to `/INTERSHOP/rest/...`.
2. Intershop REST framework dispatches to the registered handler contract.
3. `app_sf_rest_custom` `ProductSearchHandlerImpl` implements `ProductHandler` and overrides product search behavior.
4. Handler accesses data through business objects and business services; those APIs manage persistence.
5. JSON/XML response is serialized and returned.

For endpoints that use an extensible BO-to-RO mapper, a Guice `FunctionExtension` can enrich the existing response without replacing the handler. Register the module through `src/main/resources/resources/<cartridge>/objectgraph/objectgraph.properties` using `global.modules`; its JAR entry must be `resources/<cartridge>/objectgraph/objectgraph.properties`. Assembly packaging and application cartridge selection are still required. See [Guice Mapper Extension Pattern](./rest-api-customization/tech-design.md#guice-mapper-extension-pattern) and the [implementation example](./rest-api-customization/implementation-guide.md#10-guice-mapper-extension-and-objectgraph-registration).

Source: `app_sf_rest_custom` `implementations.component`. <ref_snippet file="D:\AIEnablement\intershop-cisetup-sources\projects\corporateshop\app_sf_rest_custom\staticfiles\cartridge\components\implementations.component" lines="1:11"/>

In `objectgraph.properties`, separate module class names in `global.modules` with whitespace. For multiple lines, use a backslash continuation with whitespace between names. Commas are treated as part of the class name and cause module-loading errors.

### Content (CMS) Flow

1. Pagelets defined in `app_sf_base_cm/staticfiles/cartridge/pagelets/components/` describe configurable content components.
2. Page variants in `app_sf_responsive_cm` and templates define page layout.
3. Pipeline `ProcessViewContext...` loads pagelet assignments and view context.
4. ISML templates render pagelets with product/category data.

Source: `app_sf_base_cm` pagelets. <ref_snippet file="D:\AIEnablement\intershop-cisetup-sources\projects\corporateshop\app_sf_base_cm\staticfiles\cartridge\pagelets\components\component.common.carousel.pagelet2" lines="1:50"/>

## Technology Stack for Each Module

| Concern | Technology | Modules | Confidence |
|---------|------------|---------|------------|
| Build / Dependency Mgmt | Gradle + Intershop plugins (`ish-component-plugin`, `java-cartridge`, `static-cartridge`, `isml`, `cartridge-resourcelist`) | All | 95% |
| Platform | Intershop Commerce Management 7.10.41.5-LTS | All | 95% |
| Java Version | Java 8 (source/target 1.8) | All (except microservices runtime) | 95% |
| Backend Language | Java (pipelets, handlers, services) | `app_sf_responsive`, `app_sf_responsive_b2b`, `app_sf_rest_custom` | 95% |
| Templating | ISML (compiles to JSP) | `app_sf_responsive*`, `app_sf_base_cm`, `app_sf_pwa_cm` | 95% |
| Pipeline Orchestration | Intershop Pipeline XML | `app_sf_responsive*`, `ac_oidc_sf_responsive*` | 95% |
| Dependency Injection | Intershop Component Framework (`components/*.component`) and Guice modules (`objectgraph/objectgraph.properties`) | Java / REST cartridges | 95% |
| Styling | LESS -> CSS | `app_sf_responsive`, `app_sf_responsive_smb` | 95% |
| REST | JSR-311 (`javax.ws.rs`) | `app_sf_rest_custom` | 90% |
| Search | Apache Solr (cartridges `ac_solr_cloud*`) | Assemblies | 85% |
| Databases | Derby / PostgreSQL / Oracle | Runtime (configured in `microservices` build) | 85% |
| Testing | JUnit 4/5, Mockito, Hamcrest, Geb/Selenium, RESTClient | Test cartridges, `inspired-b2c`/`inspired-b2x` | 90% |
| Microservices | Intershop microservice application server | `microservices` | 85% |

Source: `build.gradle` plugins and `gradle.properties`. <ref_snippet file="D:\AIEnablement\intershop-cisetup-sources\projects\corporateshop\build.gradle" lines="189:221"/>

## Dependency Mapping Across Modules

- `app_sf_responsive` depends on many `bc_*` / `sld_*` business and platform cartridges. <ref_snippet file="D:\AIEnablement\intershop-cisetup-sources\projects\corporateshop\app_sf_responsive\build.gradle" lines="35:133"/>
- `app_sf_responsive_b2b` depends on `app_sf_responsive` and adds B2B pipelets. <ref_snippet file="D:\AIEnablement\intershop-cisetup-sources\projects\corporateshop\inspired-b2x\build.gradle" lines="53:70"/>
- `app_sf_responsive_cm` depends on `app_sf_responsive` and content cartridges. <ref_snippet file="D:\AIEnablement\intershop-cisetup-sources\projects\corporateshop\app_sf_responsive_cm\build.gradle" lines="15:35"/>
- `app_sf_pwa_cm` depends on `app_sf_base_cm`. <ref_snippet file="D:\AIEnablement\intershop-cisetup-sources\projects\corporateshop\app_sf_pwa_cm\build.gradle" lines="15:26"/>
- `app_sf_rest_custom` depends on `app_sf_rest`, `app_sf_rest_common`, `bc_product`, `rest`, etc. <ref_snippet file="D:\AIEnablement\intershop-cisetup-sources\projects\corporateshop\app_sf_rest_custom\build.gradle" lines="9:30"/>
- `as_responsive` depends on `app_sf_responsive`, `app_sf_responsive_b2c`, `app_sf_responsive_smb`, and `app_sf_rest`. Custom REST cartridges such as `app_sf_rest_custom` are included at the assembly level and self-register with the target application types via `apps-extension.component`.
- `app_sf_rest_custom` is packaged in `inspired-b2c` and `inspired-b2x` and self-registers with `intershop.B2CResponsive.Cartridges`, `intershop.SMBResponsive.Cartridges`, and `intershop.SimpleSMBResponsive.Cartridges`. <ref_snippet file="D:\AIEnablement\intershop-cisetup-sources\projects\corporateshop\inspired-b2c\build.gradle" lines="54:70"/> <ref_snippet file="D:\AIEnablement\intershop-cisetup-sources\projects\corporateshop\app_sf_rest_custom\staticfiles\cartridge\components\apps-extension.component" lines="1:12"/>
- `inspired-b2c` / `inspired-b2x` package all of the above plus `microservices`, `responsive_config`, `responsive_sites`, and Solr cartridges. <ref_snippet file="D:\AIEnablement\intershop-cisetup-sources\projects\corporateshop\inspired-b2c\build.gradle" lines="44:90"/>

## Universal Intershop Project Guidelines

The following rules apply project-wide, across all versions and client-specific Intershop projects, and must be observed by any implementation built on this architecture:

1. **No modifications to default Intershop cartridges** — Only project-owned/custom cartridges (e.g., `app_sf_rest_custom`) may contain custom code, component overrides, or new artifacts. Intershop starter-store cartridges, platform cartridges, and any other default Intershop cartridge must remain unmodified.
2. **Preserve existing features in new cartridges** — Any modification made in a new/custom cartridge must not remove or disable previous features unless explicitly requested.
3. **Database changes use `dbmigrate`** — Any DDL or DML change must be delivered as a `dbmigrate` artifact, not as part of DB init / DB prepare logic.
4. **No dependency class collisions** — New dependencies added to a custom cartridge must not have packages/classes that overlap with Intershop platform or default cartridge dependencies. If a collision appears after a dependency change, the `build/` and `target/` outputs may be stale; run `./gradlew clean` and then `./gradlew :inspired-b2c:checkClassCollisions` after adding or changing dependencies.
5. **Custom cartridges must self-register** — A new cartridge must be added to the assembly's `storefrontCartridges` list (so it is packaged and ordered) and must register itself with the target application type(s) via an `apps-extension.component` or `app-extension.component` inside the custom cartridge. Do not edit the default `as_responsive` or `as_responsive_b2b` application-suite cartridges to add a `selectedCartridge` entry.
6. **Access data through business objects** — Wherever data is accessible through a BO, its public extensions, repositories, or business services, use those APIs instead of persistent objects. Use persistence access only when the required data is not exposed by a supported business API; document the gap and isolate the fallback. Verify each BO identifier and attribute API for this platform version; do not assume every BO ID is a persistence UUID.

Confidence: 95%.

## Confidence Score Summary

| Section | Confidence |
|---------|------------|
| Executive Summary | 95% |
| Project Type | 95% |
| Overall Architecture Overview | 90% |
| Modules and Purpose | 90% |
| Project and Folder Structure | 95% |
| Integration Points / Flows | 90% |
| Technology Stack | 95% |
| Dependency Mapping | 95% |


---