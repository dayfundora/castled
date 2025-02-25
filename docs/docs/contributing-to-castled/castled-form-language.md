---
sidebar_position: 1
---

# Castled Form Language

UI Forms are required to configure almost anything in a Reverse ETL pipeline(eg: warehouse configuration, app configuration and pipeline sync configuration). The major challenge with having such forms is that there is no concrete format to any of these configurations and they change drastically with each destination connector. Some of the different variations include:

* **Conditional Forms** : Forms where some form fields need to be displayed only if a certain condition is met. For instance, in case of Redshift and Postgres, SSH tunnel variables need to be displayed in the form only if the enable tunnel checkbox is set.

* **Dynamic Option Selectors** : The options for form select fields need to be populated based on the values previously selected in the form by making an api call to the backend.

* **Code Snippets** : Forms like BigQuery configuration need to generate dynamic code snippets based on the selected form fields, which users can copy and execute on their console.

* **Help Text** : Forms like Google Sheets configuration need to generate dynamic help texts to guide the user on the next steps.

Because of this, building a new connector required significant effort from the UI as well to build out these forms. Thats why we built CFL or Castled Form Language. With CFL, extremely complex forms can be auto generated by writing a few java annotations on the configuration class. This removed the need of a UI developer to build a new connector.

## How does CFL work

Each configuration in the UI is denoted by a configuration class on the backend. CFL works by adding class level and field level annotations to the configuration class as shown below.

![gads_cfl](/docs/static/img/screens/contributing/gads_cfl.png)


The different form controls supported by CFL include:

* **TEXT_BOX** : A simple input text field, with placeholders, title and descriptions controlled by the backend

* **DROP_DOWN** : A input select field with options either provided statically or dynamically using the optionsRef parameter

* **RADIO_GROUP** : A group of radio buttons with the title and description of each field controlled from the backend

* **CHECK_BOX** : A simple check box

* **JSON_FILE** : File upload control to upload a json file. It will be mapped to the corresponding object in the configuration class by Jackson for eg: ServiceAccountDetails in [BigQueryWarehouseConfig](https://github.com/castledio/castled/blob/main/connectors/src/main/java/io/castled/warehouses/connectors/bigquery/BigQueryWarehouseConfig.java)

* **TEXT_FILE** : File upload control to upload a text file. It will be mapped to the corresponding string variable in the configuration class for eg: privateKey form field in [TunneledWarehouseConfig](https://github.com/castledio/castled/blob/main/connectors/src/main/java/io/castled/warehouses/TunneledWarehouseConfig.java)


### Group Activator

Each form field is part of a field group mentioned by its group tag. Group activator is a class level annotation which decides when a particular group should be activated on the form. A group can consist of one or more form fields. Group activation can be triggered either by a condition or just by selecting a few other dependent fields. Group activation conditions are denoted using jexl expressions. For instance in case of CustomerIO, pageViewGroup will be visible only eventType equals `pageView` and objectName equals `Event`.

```
@GroupActivator(dependencies = {"eventType","object"}, condition = "eventType == 'pageView' && object.objectName == 'Event'", group = "pageViewGroup")
```


### Options Reference

Options References are used to fetch valid options for DROP_DOWN or RADIO_GROUP controls either dynamically or statically. Static options are passed on to the UI before the form is rendered by the backend. for eg: s3 bucket locations supported by aws. Dynamic options depend on other fields in the form and can be fetched only after the dependent fields are populated by the user. Once the relevant fields are populated, UI makes a call dynamically to the backend passing the partially populated form and backend provides the list of options accordingly.

### Help Text

Sometimes, forms need to auto generate help texts to guide the user on the next steps to follow. For instance in case of Google Sheets, once the service account is created, a help text needs to be shown to the user to give editor access to google sheets to the generated email.

![gsheets_config](/docs/static/img/screens/contributing/gsheets_config.png)

This can be achieved by the following CFL:

```
@HelpText(value = "Provide Editor access of your google sheets to this email: ${serviceAccount.client_email}", dependencies = {"spreadSheetId", "serviceAccount"})
```


