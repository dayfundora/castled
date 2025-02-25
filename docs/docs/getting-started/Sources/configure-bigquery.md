---
sidebar_position: 3
---

# Google BigQuery


Google Biquery is a public cloud data warehouse from Google.

Castled enables you to fetch data from one or more tables spanning across one or more schemas(dataset in BigQuery terminology) in your warehouse and sync it to the corresponding objects in the destination apps.

Castled uses a **diff** computation logic for the data sync happening between you warehouse and destination app. This is done to make sure the computation happens in ISOLATION in the bookkeeping dataset CASTLED created for this purpose in your warehouse and is not creating any kind of performance bottleneck at the source as well as destination.This process makes sure
- The database load at the source stays well within limits
- The payload used to invoke the destination APIs are kept as light as possible.

## Permission Details

For the castled connection to work, the account provided to Castled must have the below permissions
1. Permission to create a new dataset ‘castled’ and full admin access to all the tables/views with in that dataset. This includes permission to create/update/delete and write to these tables and create jobs within this dataset.
2. READ ONLY access for any dataset you want Castled to sync the data with the destination app. This will allow read only access on all the tables/views with in that dataset.

BigQuery manages permissions using Identity & Access Management (IAM) mechanism.When an identity calls a Google Cloud API, BigQuery requires that the identity has the appropriate permissions to use the resource. You can grant permissions by granting roles to a user, a group, or a service account.For the above mentioned permissions Castled needs the below roles
1. **bigquery.dataViewer** - This role gives read only access to all the datasets and the tables/views inside these datasets at the project or organisation level.
2. **bigquery.user** - This role allows Castled to create new datasets and here it will allow us to create the ‘castled’ dataset.This grants castled bigquery.dataowner role on this ‘castled’ dataset created.

We recommend giving the above mentioned roles when you are creating a BigQuery connection. If your policy don’t allow giving these roles, then we recommend giving the below fine grained permissions for the Castled service account.
1. **bigquery.dataViewer** - This role gives read only access to all the datasets and the tables/views inside these datasets at the project or organisation level.
2. Manually create the ‘castled’ dataset and give Castled service account **bigquery.dataOwner** role on this dataset.
3. Give Castled service account **bigquery.jobs.create** permission (via a custom role) at the project level. This will enable us to run jobs with in the project.

Refer this **[link](https://cloud.google.com/bigquery/docs/access-control )** for more details on IAM related roles and permissions.

## Connector Details

**For configuring a new connector for BigQuery the following fields needs to be captured**
- **Name**
    - A name for your connector configuration 
- **Project ID**
    - **[Identifier](https://cloud.google.com/resource-manager/docs/creating-managing-projects#identifying_projects)** for your project.
- **GCS Bucket Name**
  - **[Name](https://cloud.google.com/storage/docs/key-terms#buckets)** of the bucket to be used for the sync
- **Dataset Location**
  - Location of your bucket.When you create a bucket, you permanently define its geographic location, which is the physical place where object data in the bucket resides.

When using the Castled recommended permissions, once you enter the above mentioned configuration details, you will be prompted the three commands to be mandatorily run in your Google Cloud Shell in the Google Cloud Console before clicking the Submit button of the Configuration screen.

```
gcloud projects add-iam-policy-binding [project-name] \
  --member serviceAccount:[service-account-user] \
  --role roles/bigquery.dataViewer
  
gcloud projects add-iam-policy-binding [project-name] \ 
--member serviceAccount:[service-account-user] \ 
--role roles/bigquery.user
 
gsutil iam ch serviceAccount:[[service-account-user]]:roles/storage.admin gs://[gcs-bucket-name]
```
In the commands displayed above 
- **[project-name]** is your Project Name.
- **[service-account-user]** is your Castled Service Account.
- **[gcs-bucket-name]** is your the GCS Bucket Name.

![Docusaurus](/docs/static/img/screens/sources/bigquery/wh_bigquery_config.png)


