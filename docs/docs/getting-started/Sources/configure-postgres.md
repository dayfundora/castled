---
sidebar_position: 4
---

# Postgres

Castled enables you to fetch data from one or more tables spanning across one or more schemas in your Postgres and sync it to the corresponding objects in the destination app.

Castled uses a **diff** computation logic for the data sync happening between you warehouse and destination app. This is done to make sure the computation happens in ISOLATION in the bookkeeping schema created for this within your database and is not creating any kind of performance bottleneck at the source as well as the destination.This process makes sure
- The database load at the source stays well within limits
- The payload used to invoke the destination APIs are kept as light as possible.

## Permission Details

**Create a dedicated user CASTLED for connecting to your Postgres warehouse.For making sure the connection is established successfully the CASTLED user created should have the below permissions**
1. Required grants to create a new schema **CASTLED** with all required privileges to create/update/delete tables with in that schema.
2. **READ ONLY** access to the existing as well as future tables and views in the schemas from which you want Castled to sync the data to your destination app. 
3. Permission to execute the existing as well as future functions in the schema from which you want Castled to sync the data to your destination app.

You can use the below script to configure the required access required by Castled.

```
-----------------------------------USER CREATON STARTS-------------------------------------------------------
— Create a new user CASTLED for connecting to postgres DB
CREATE USER CASTLED WITH PASSWORD '<strong, unique password>';
-----------------------------------USER CREATON ENDS----------------------------------------------------------


-----------------------------------BOOK KEEPING SCHEMA ACCESS STARTS------------------------------------------
-- Create a private bookkeeping schema for storing sync data
CREATE SCHEMA CASTLED;

-- Give the CASTLED user full access to the bookkeeping schema
GRANT ALL ON SCHEMA CASTLED TO CASTLED;

-- Give CASTLED user access to all objects existing n the bookkeeping schema
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA CASTLED TO CASTLED;
-----------------------------------BOOK KEEPING SCHEMA ACCESS ENDS---------------------------------------------


-----------------------------------GRANT TABLE READ ONLY ACCESS STARTS------------------------------------------
-- Give access to CASTLED user to SEE your schema
GRANT USAGE ON SCHEMA "<your schema>" TO CASTLED;

-- Give READ ONLY access to  CASTLED user to read from all existing tables in your schema
GRANT SELECT ON ALL TABLES IN SCHEMA "<your schema>" TO CASTLED;

-- Give READ ONLY access to CASTLED user to read from all the future tables being created in your schema
ALTER DEFAULT PRIVILEGES IN SCHEMA "<your schema>" GRANT SELECT ON TABLES TO CASTLED;

-- Give access to  the CASTLED user to execute any existing functions in you schema
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA "<your schema>" TO CASTLED;

-- Give access to  the CASTLED user to execute any new functions added to this schema
ALTER DEFAULT PRIVILEGES IN SCHEMA "<your schema>" GRANT EXECUTE ON FUNCTIONS TO CASTLED;
-----------------------------------GRANT TABLE READ ONLY ACCESS ENDS---------------------------------------------
```


## Connector Details

**For configuring a new connector for Postgres the following fields needs to be captured**
- **Name**
    - A name to uniquely qualify the warehouse source created 
- **Database Server Host**
    - Host's IP address or **[DNS Name](https://docs.aws.amazon.com/redshift/latest/mgmt/jdbc20-obtain-url.html)**. 
- **Database Server Port**
    - The port on which your Amazon Postgres server is listening for connections. Default value: 5432.
- **Database name**
    - Name of database you created for your cluster
- **Schema Name**
    - Schema in your database to be used for the sync.
- **Database User**
    - Database username
- **Database Password**
    - Database password
- **Enable SSH Tunnel**
    - Enable this option to connect Postgres database host to Castled Data using a SSH Tunnel.This is done to provide an additional level of security and avoid exposing the Postgres setup to the public.
    - On enabling this option, you will have to enter the below details.Please refer [Connecting through SSH Tunnel](../Appendix/ssh-tunnel.md) for more details 
        -   **SSH Host**
        -   **SSH Port**
        -   **SSH User**

![Docusaurus](/docs/static/img/screens/sources/postgres/wh_postgres_config.png)

:::note
1. If the sync involves more than one schema in your database GRANT mentioned above needs to be given for each of those schemas.
2. If the views in your schema references tables of another schema in your database, GRANT needs to be given for those schemas as well.
:::

## Connecting to SSH Tunnel
Castled uses SSH Tunelling to connect in cases where Postgres is accessible only on private or internal networks. In order to make sure the connection is established successfully with SSH Tunneling, you need to provide an SSH Host visible on the public internet and at the same time connect to your private Postgres warehouse. The basic admin actions mentioned below are required for the same
1. Create a new user for Castled on the SSH Host (This user account is not related to the user account created for database related operations. Recommend using the same account name , but can be kept different if you want)
2. While configuring a new warehouse connector for Postgres on the ‘Create Connector’, you need to enter the following details 
    1. SSH Host IP/DNS Name
    2. SSH Port : Default is 22 unless changed
    3. SSH Username : User created for Castled on the SSH Host
    ![Docusaurus](/docs/static/img/screens/sources/redshift/wh_redshift_config_2.png)
3. On successful creation of the new Connector , Castled will generate a key pair fo the SSH authentication . Refer [Connecting through SSH Tunnel](../Appendix/ssh-tunnel.md)  for details on how to add the castled public key to authorized_keys.

