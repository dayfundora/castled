---
sidebar_position: 5
---

# Marketo

Marketo develops marketing automation software that provides inbound marketing, social marketing, CRM, and other related services.

## Creating an app connection

![marketo app config form](/docs/static/img/screens/destinations/app_marketo_config.png)

To get **Client ID** and **Client Secret** login to Marketo and navigate to _Admin > Integrations > LaunchPoint_ menu,
select a service and click View Details.

![marketo client info](/docs/static/img/screens/destinations/app_marketo_secrets.png)

**Base Url** can be found in _Admin > Integrations > Webservices_

![marketo base url info](/docs/static/img/screens/destinations/app_marketo_base_url.png)

In this case the **Base Url** is `https://064-CCJ-768.mkforest.com`

## Creating a sync pipeline

### Sync Modes

Marketo supports **Insert** and **Upsert** sync modes.

Primary keys eligibility of a field depends on the sync mode and the type of object being synced. Following table summarizes what fields are eligible to be a primary key based on the sync mode and object.

| Object\Mode   | Upsert        | Update                   |
| ------------- | ------------- | ------------------------ |
| Leads         | Any field     | Any field + ID field     |
| Companies     | Dedupe fields | Dedupe fields + ID field |
| Opportunities | Dedupe fields | Dedupe fields + ID field |

Primary key selection happens in the schema mapping page.
