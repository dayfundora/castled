---
sidebar_position: 6
---

# Salesforce

Salesforce is a popular cloud based CRM, which helps the sales/marketing teams of your organization to better engage customers and leads.

## Creating an app connection

![salesforce app config form](/docs/static/img/screens/destinations/app_salesforce_config.png)

Adding the app will take you to the saleforce login screen

![salesforce login screen](/docs/static/img/screens/destinations/app_salesforce_login.png)

Enter your salesforce credentials and you will be redirected back to the Castled console and a Salesforce connector will be created.

## Creating a sync pipeline

### Sync Modes

Salesforce supports all 3 sync modes, **UPSERT**, **INSERT** and **UPDATE**

### Primary Key Eligibility

Only fields which are marked **Unique** or marked as an **External ID** can be used as primary keys in salesforce connector

![salesforce field config](/docs/static/img/screens/destinations/app_salesforce_field.png)