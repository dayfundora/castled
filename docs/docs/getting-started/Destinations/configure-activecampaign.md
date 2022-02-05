---
sidebar_position: 8
---

# ActiveCampaign

ActiveCampaign gives you the email marketing, marketing automation, and CRM tools you need to create incredible customer experiences.

## Creating an app connection

For configuring a new app connector for customer.io, the following fields needs to be captured
- **Name**
    - A name you want to call this connector
- **API KEY**
- **API URL**

![Docusaurus](/docs/static/img/screens/destinations/activecampaign/app_actcamp_app_config.png)

**Login** to ActiveCampaign and navigate to **Settings > Developer**  to get **API KEY ** and **API URL** required for configuring the app connector

![Docusaurus](/docs/static/img/screens/destinations/activecampaign/app_actcamp_account_settings.png)


## Creating a sync pipeline

### Objects to sync
Castled supports syncing of **Contact** Object
![Docusaurus](/docs/static/img/screens/destinations/activecampaign/app_actcamp_sync_objects.png)

### Sync Modes 
Castled supports only **UPSERT** mode for Contact Object
![Docusaurus](/docs/static/img/screens/destinations/activecampaign/app_actcamp_sync_modes.png)

### Field mapping
- Castled populates all the warehouse columns by default.You shall map the WAREHOUSE COLUMN to the appropriate APP COLUMN in the destination app.
- Email is to be mandatorily mapped in the APP COLUMN as it is mandatory field for ActiveCampaign
- Default columns along with the Custom columns are populated in the drop down for the destination app column.