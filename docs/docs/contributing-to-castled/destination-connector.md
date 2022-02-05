---
sidebar_position: 2
---

# New destination connector

With Castled Form Langauge(CFL), its really easy to write a new destination connector in Castled. Lets take the example of a Kafka connector to explain the process step by step.

## Configuring the app connection

Creating an app connection is a prerequisite to creating a pipeline. [AppConfig](https://github.com/castledio/castled/blob/main/connectors/src/main/java/io/castled/apps/AppConfig.java) class includes the connection params required to establish connection to our destination app. We have to extend the AppConfig class and mention the connection parameters in the sub class.

```
@Getter
@Setter
public class KafkaAppConfig extends AppConfig {

    @FormField(title = "Bootstrap Servers", placeholder = "eg: host1:9092, host2:9092", 
    schema = FormFieldSchema.STRING, type = FormFieldType.TEXT_BOX)
    private String bootstrapServers;
}

```

In case of kafka, only the bootstrap servers configuration (which includes the kafka hostname and port) is sufficient to establish connection to the kafka server. The CFL will auto generate the Kafka App Config Form as shown below

![kafka_config](/docs/static/img/screens/contributing/kafka_config.png)

## Configuring the app sync config

[AppSyncConfig](https://github.com/castledio/castled/blob/main/connectors/src/main/java/io/castled/apps/syncconfigs/AppSyncConfig.java) class includes additional configurations required to sync the data to our destination app. In most cases, this would mean the destination object, sub objects and any other configuration which can be used to control the behaviour of the data sync. In case of kafka, it just includes the kafka topic, where you want the data synced.

```
@Getter
@Setter
public class KafkaAppSyncConfig extends AppSyncConfig {

    @FormField(title = "Select kafka topic", type = FormFieldType.DROP_DOWN, group = MappingFormGroups.OBJECT, optionsRef = @OptionsRef(value = OptionsReferences.OBJECT, type = OptionsRefType.DYNAMIC))
    private GenericSyncObject topic;

}
```

As you can see here, we are using an *OptionalRef* here to fetch the list of available kafka topics from the backend. The form generated using AppSyncConfig will be shown as part of the pipeline create wizard after selecting the source and destination. 


## ExternalAppConnector

[ExternalAppConnector](https://github.com/castledio/castled/blob/main/connectors/src/main/java/io/castled/apps/ExternalAppConnector.java) is the service interface that exposes all the methods which controls the mapping page as well the list of objects/subobjects in AppSyncConfig page. It also contains a validation method which validates the configs entered in AppConfig.

```

@Singleton
@Slf4j
public class KafkaAppConnector implements ExternalAppConnector<KafkaAppConfig, KafkaDataSink, KafkaAppSyncConfig> {

    @Override
    public List<FormFieldOption> getAllObjects(KafkaAppConfig config, KafkaAppSyncConfig mappingConfig) {
        Properties properties = new Properties();
        try {
            properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getBootstrapServers());
            try (AdminClient adminClient = KafkaAdminClient.create(properties)) {
                return adminClient.listTopics().names().get()
                        .stream().map(topic -> new FormFieldOption(new GenericSyncObject(topic,
                                ExternalAppType.KAFKA), topic)).collect(Collectors.toList());
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("Topics list failed for kafka", e);
            throw new CastledRuntimeException(e);
        }
    }

    public void validateAppConfig(KafkaAppConfig kafkaAppConfig) throws InvalidConfigException {
        Properties properties = new Properties();

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaAppConfig.getBootstrapServers());
        try (AdminClient ignored = KafkaAdminClient.create(properties)) {
        } catch (KafkaException e) {
            String rootCause = ExceptionUtils.getRootCauseMessage(e);
            throw new InvalidConfigException(rootCause);
        }
    }

    @Override
    public KafkaDataSink getDataSink() {
        return ObjectRegistry.getInstance(KafkaDataSink.class);
    }

    @Override
    public ExternalAppSchema getSchema(KafkaAppConfig config, KafkaAppSyncConfig kafkaAppSyncConfig) {
        return new ExternalAppSchema(null, Lists.newArrayList());
    }

    @Override
    public Class<KafkaAppSyncConfig> getMappingConfigType() {
        return KafkaAppSyncConfig.class;
    }

    @Override
    public Class<KafkaAppConfig> getAppConfigType() {
        return KafkaAppConfig.class;
    }

    public List<AppSyncMode> getSyncModes(KafkaAppConfig kafkaAppConfig, KafkaAppSyncConfig kafkaAppSyncConfig) {
        return Lists.newArrayList(AppSyncMode.INSERT);
    }

```

## DataSink

Finally we have to implement the syncRecords method of the [DataSink](https://github.com/castledio/castled/blob/main/connectors/src/main/java/io/castled/apps/DataSink.java) interface, which does the actual data sync to the destination apis. 

```
void syncRecords(DataSinkRequest dataSinkRequest) throws Exception;
```

DataSink takes a DataSinkRequest, which contains all required context about the pipeline required for the data sync and also a MessageInputStream and an ErrorOutputStream.

### MessageInputStream

[MessageInputStream](https://github.com/castledio/castled/blob/main/connectors/src/main/java/io/castled/commons/streams/MessageInputStream.java) contains a stream of records which can be fetched on demand from the source using the readMessage method. Message consists of an offset(which indicates the serial no of the message in the stream) and the actual message tuple. The message tuple contains both the schema and value of each field in the message.


### ErrorOutputStream

The failure message and the failure reason needs to be passed to the [ErrorOutputStream](https://github.com/castledio/castled/blob/main/connectors/src/main/java/io/castled/commons/streams/ErrorOutputStream.java), so that it can be taken care by the framework. Based on the failure feedback from the DataSink, a failure report is created and made available to the user for download.

Lets implement our KafkaDataSink now.

```

public class KafkaDataSink implements DataSink {

    private final AtomicLong recordsProcessed = new AtomicLong(0);
    private final Set<Long> pendingMessageIds = Sets.newConcurrentHashSet();
    private long lastBufferedOffset = 0;
    private volatile Exception throwable;

    public class DataSinkCallback implements CastledProducerCallback {

        private final long messageOffset;

        public DataSinkCallback(long messageOffset) {
            this.messageOffset = messageOffset;
        }

        @Override
        public void onSuccess(RecordMetadata recordMetadata) {
            recordsProcessed.incrementAndGet();
            pendingMessageIds.remove(messageOffset);
        }

        @Override
        public void onFailure(RecordMetadata recordMetadata, Exception e) {
            throwable = e;
        }
    }

    @Override
    public void syncRecords(DataSinkRequest dataSinkRequest) throws Exception {
        KafkaAppConfig kafkaAppConfig = (KafkaAppConfig) dataSinkRequest.getExternalApp().getConfig();
        KafkaAppSyncConfig kafkaAppSyncConfig = (KafkaAppSyncConfig) dataSinkRequest.getAppSyncConfig();
        Message message;
        try (CastledKafkaProducer kafkaProducer = new CastledKafkaProducer
                (KafkaProducerConfiguration.builder().bootstrapServers(kafkaAppConfig.getBootstrapServers()).build())) {
            while ((message = dataSinkRequest.getMessageInputStream().readMessage()) != null) {
                validateAndThrow();
                pendingMessageIds.add(message.getOffset());
                publishMessage(kafkaProducer, message, kafkaAppSyncConfig.getObject().getObjectName(),
                        dataSinkRequest.getErrorOutputStream());
                lastBufferedOffset = message.getOffset();
            }
            kafkaProducer.flush();
            validateAndThrow();
        }
    }

    private void publishMessage(CastledKafkaProducer kafkaProducer, Message message, String topic,
                                ErrorOutputStream errorOutputStream) {
        try {
            kafkaProducer.publish(new ProducerRecord<>(topic, null,
                    MessageUtils.messageToBytes(message)), new DataSinkCallback(message.getOffset()));
        } catch (Exception e) {
            pendingMessageIds.remove(message.getOffset());
            recordsProcessed.incrementAndGet();
            errorOutputStream.writeFailedRecord(message, ObjectRegistry.getInstance(KafkaErrorParser.class)
                    .parseException(e));
        }
    }

    private void validateAndThrow() throws Exception {
        if (throwable != null) {
            throw throwable;
        }
    }

    @Override
    public AppSyncStats getSyncStats() {
        return new AppSyncStats(recordsProcessed.get(), getProcessedOffset(), 0);
    }

    public long getProcessedOffset() {
        try {
            long currentMinPendingId = Collections.min(pendingMessageIds);
            return currentMinPendingId - 1;
        } catch (NoSuchElementException e) {
            return lastBufferedOffset;
        }
    }
}

```

We also need to implement the *getSyncStats* method of the DataSink interface. This gives an indication of the process of the data sync. This method is called from a parallel thread to show the status of the data sink on the UI, while its in progress.

**Thats it!! We have now built a new Kafka connector in Castled**




    






