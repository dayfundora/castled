---
sidebar_position: 9
---

# Customer.io

Customer.io is a messaging platform that allows marketers to take an idea and turn it into a powerful automated message campaign.It enables you to send targeted emails campaigns, SMS and other push notifications.

## Creating an app connection

For configuring a new app connector for customer.io, the following fields needs to be captured
- **Name**
    - A name you want to call this connector
- **Site ID**
    - Site ID generated for the workspace you are creating in customer.io 
- **API Key**
    - Tracking API Key generated for the workspace you are creating in customer.io

![Docusaurus](/docs/static/img/screens/destinations/customerio/app_cio_app_config.png)

**Login** to [customer.io](https://fly.customer.io/login) and navigate to **Settings > Account Settings > API Credentials > Tracking API Keys ** to get **Site ID** and **API Key** required for configuring the app connector

![Docusaurus](/docs/static/img/screens/destinations/customerio/app_cio_account_settings.png)

![Docusaurus](/docs/static/img/screens/destinations/customerio/app_cio_manage_credentials.png)

## Creating a sync pipeline

Castled supports syncing of 2 objects
- Person
- Event

![Docusaurus](/docs/static/img/screens/destinations/customerio/app_cio_sync_objects.png)

### Syncing Person

#### Configuration for Customer
For syncing Person object, you need to configure the following in App Sync Settings
- **Column uniquely identifying the Person Record**
    - Select the Warehouse table column which will uniquely identify the Person Record
- **Matching Primary Key For Destination App Record**
    - Select either email or ID as the primary key depending on the unique identifier you selected above.

![Docusaurus](/docs/static/img/screens/destinations/customerio/app_cio_person_sync_objects.png)

#### Sync Modes for Person
Castled supports only UPSERT mode for Person Object
![Docusaurus](/docs/static/img/screens/destinations/customerio/app_cio_person_sync_modes.png)

### Syncing Event

#### Event Types
Caslted supports sync of 2 type of Events
- Track Event
- Track Page View
![Docusaurus](/docs/static/img/screens/destinations/customerio/app_cio_event_types.png)


#### Common Configuration for all Event Types
For syncing Event object you need to configure the following in App Sync Settings
- **Select Event Type for tracking**
    - Event or Page View to be mandatorily selected
- **Warehouse Column uniquely identifying the Event Record**
    - Warehouse column which uniqely identifies an event record
- **Warehouse Column identifying Customer.io id (customer_id) of the person**
    - Warehouse column which uniquely identifies the Person associated with the event. If not mapped it will be treated as an anonymous event
- **Warehouse Column uniquely identifying the Event Timestamp**
    - Warehouse column which maps to the event timestamp.If not mapped customer.io will set default to the time the sync happened

#### Specific Configuration for Track Event
For syncing Track Event you need to configure the following in App Sync Settings
- **Warehouse Column identifying the Event Name**
    - Select the warehouse column containing the Event Name

![Docusaurus](/docs/static/img/screens/destinations/customerio/app_cio_event_config.png)

#### Specific Configuration for Track Page View
For syncing Track Page View you need to configure the following in App Sync Settings
- **Warehouse Column identifying the URL of the page viewed**
    - Select the warehouse column containing the Page URL

![Docusaurus](/docs/static/img/screens/destinations/customerio/app_cio_page_view_config.png)

#### Sync Modes for Events
Castled supports only INSERT mode for Event Object
![Docusaurus](/docs/static/img/screens/destinations/customerio/app_cio_event_sync_modes.png)

### Field mapping
Castled will auto map the warehouse fields to the app fields with the same name. In most of the cases that would suffice. However Castled provides the flexibility to modify the destination columns if required.