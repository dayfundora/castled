---
sidebar_position: 3
---

# Hubspot

Hubspot is a leading cloud-based CRM, which provides a set of marketing and sales tools, which helps organizations better engage with their customers.

## Creating an app connection

![Hubspot app config form](/docs/static/img/screens/destinations/app_salesforce_config.png)

Enter a unique name for your hubspot app. You will be directed towards the hubspot login screen for authentication. After authentication, you will be redirected back to the Castled console and a Hubspot app is created on Castled. You are now ready to create a pipeline to move the data to Hubspot.

## Creating a sync pipeline

### Supported Objects

Castled supports the objects **Contact**, **Company**, **Ticket** and **Deal**

### Sync modes

Castled supports all 3 modes **Insert**, **Update** and **Upsert** for all the supported objects.

### Primary Key Eligibility

| Object        | Eligible primary keys       
| ------------- | -------------
| Contact       | **email**
| Company       | **domain**
| Deal          | Combination of any fields, excluding timestamp fields
| Ticket        | Combination of any fields, excluding timestamp fields

**email/domain** needs to be mapped to select primary keys for **Contact** and **Company** respectively.