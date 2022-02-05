---
sidebar_position: 1
---

# Create Pipeline

Once the source **warehouse** and the destination **app** is configured and available, the last step in the sequence is the creation of a pipeline.

## Quick Demo

[![](https://cdn.loom.com/sessions/thumbnails/17bd25ed06cd4ca7a7215440606e2041-with-play.gif)](https://www.loom.com/embed/17bd25ed06cd4ca7a7215440606e2041)

## Steps

- **Step 1 : Click on Create Pipeline button**
  - You can create a new pipeline by clicking on the **CREATE** button in the **Pipeline list screen**
    ![Docusaurus](/docs/static/img/screens/pipelines/pipeline_menu.png)
- **Step 2 : Configure Source Connector**
  - On clicking **CREATE** button,you can see the complete list of warehouses supported by Castled and you can select the desired **Warehouse Type** as source
    ![Docusaurus](/docs/static/img/screens/pipelines/select_src_type.png)
  - Select a connector of your choice from the already configured warehouse connectors
    ![Docusaurus](/docs/static/img/screens/pipelines/select_src_config.png)
  - If there are no existing warehouse connectors or you want to create a new connector for the selected warehouse type , click on the **CREATE NEW WAREHOUSE** to configure a new warehouse and select it as the source.
  - Once the source warehouse is selected, write the required **Query** which will act as the data model.You will be shown the query result set to make sure the result set is as per the expectation. Proceed to the next step of configuring the destination app connector.
    ![Docusaurus](/docs/static/img/screens/pipelines/create_model.png)
- **Step 3 : Configure Destination App Connector**
  - Select the app connector type
    ![Docusaurus](/docs/static/img/screens/pipelines/select_dest_type.png)
  - Select one of the already configured destination app connectors
    ![Docusaurus](/docs/static/img/screens/pipelines/select_dest_config.png)
  - If there are no existing app connectors or you want to create a new connector for the app click on the **CREATE NEW APP** to configure a new app and select it as the destination.
- **Step 4 : Select the Destination Object to be sycned**
  - This step involves the selection of the Object at the destination which needs to be synced with the latest result set of the query ran at the source.
    ![Docusaurus](/docs/static/img/screens/pipelines/select_dest_object.png)
- **Step 5 : Select Sync mode**
  - Sync modes decides how the data will be synced between the source and the destination.Available options include
    - **INSERT** mode is to be used when you want to add new records to the destination
    - **UPSERT** mode is to be used when you want to add new records as well update the existing records.
    - **UPDATE** mode is to be used when you want to only update the existing records.
      ![Docusaurus](/docs/static/img/screens/pipelines/select_insert_type.png)
- **Step 6 : Map Fields of Source and Destination Object**
  - Map fields screen lists the **Warehouse (Source)** columns and the **App (Destination)** columns. By default all the columns in the selected warehouse will be listed.
    ![Docusaurus](/docs/static/img/screens/pipelines/mapping_screen.png)
  - For each column in the Warehouse , you can map an appropriate column in the Destination App object
    ![Docusaurus](/docs/static/img/screens/pipelines/complete_mapping.png)
  - It is mandatory to select atleast one column of the destination object as a **primary key**. Based on the criteria for uniqueness, more than one column can be selected as primary key.
    ![Docusaurus](/docs/static/img/screens/pipelines/primary_key_selection.png)
  - If mapping is not required for any of the columns of the destination object, it can be left blank.
- **Step 7 : Populate the Final Settings Pipeline name**
  - You can enter an unique name of the pipeline created.This name will be used for identify the pipeline in the system.
    ![Docusaurus](/docs/static/img/screens/pipelines/pipeline_name.png)
  - You can select the **run frequency** at which the pipeline will sync the data from the source to the destination object.You should be able to enter the time and mention if its **Seconds/Minutes/Hours**
    ![Docusaurus](/docs/static/img/screens/pipelines/pipeline_settings.png)
  - Submit to create the pipeline
    ![Docusaurus](/docs/static/img/screens/pipelines/submit_pipeline.png)

# View Pipeline Details

- Pipelines created are listed in the Pipeline list screen
  ![Docusaurus](/docs/static/img/screens/pipelines/pipeline_created.png)
- On clicking on the pipeline, you will be taken to the pipeline details page
  - Tab 1 : Run Details
    ![Docusaurus](/docs/static/img/screens/pipelines/pipeline_details_1.png)
  - Tab 2 : Query & Mapping Details
    ![Docusaurus](/docs/static/img/screens/pipelines/pipeline_details_2.png)
  - Tab 3 : Pipeline Schedule Details
    ![Docusaurus](/docs/static/img/screens/pipelines/pipeline_details_3.png)
