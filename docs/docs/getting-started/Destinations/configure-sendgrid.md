---
sidebar_position: 7
---

# Sendgrid

SendGrid is a cloud-based customer communication platform that drives engagement and business growth. Sendgrid provides reliable email delivery
as a service for transactional and marketing use cases.

## Creating an app connection

Setting up sendgrid app connection is straightforward. All you need is a connection name and api key.
**_Name_** can be anything that you want to call this connection. **_API Key_** for your Sendgrid account can be obtained by following the instructions [here](https://docs.sendgrid.com/ui/account-and-settings/api-keys).
You need to give _Full Access_ permission for the **_API Key_** for the sync to be able to make changes to the records.

![sendgrid config form](/docs/static/img/screens/destinations/app_sendgrid_config.png)

## Creating a sync pipeline

### Sendgrid Lists

Sendgrid lists allows you to group the contact records being synced. You can specify the list a contact record should belong to during pipeline creation time.
If left empty all the contact will be added to a default global list.

### Sync modes

#### Upsert

Sendgrid only supports upsert sync mode i.e. if the record being synced is new it will be inserted and if it already exists it will be updated. Records
are matched between source and destination using the only primary key `email`.

![sendgrid sync config form](/docs/static/img/screens/destinations/app_sendgrid_sync_config.png)
