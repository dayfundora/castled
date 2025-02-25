package io.castled.apps.connectors.restapi;

import com.google.common.collect.Lists;
import com.google.inject.Singleton;
import io.castled.ObjectRegistry;
import io.castled.apps.ExternalAppConnector;
import io.castled.apps.models.ExternalAppSchema;
import io.castled.commons.models.AppSyncMode;
import io.castled.forms.dtos.FormFieldOption;

import java.util.List;

@Singleton
public class RestApiAppConnector implements ExternalAppConnector<RestApiAppConfig,
        RestApiDataSink, RestApiAppSyncConfig> {

    @Override
    public List<FormFieldOption> getAllObjects(RestApiAppConfig restApiAppConfig, RestApiAppSyncConfig restApiAppSyncConfig) {
        throw new UnsupportedOperationException();
    }

    @Override
    public RestApiDataSink getDataSink() {
        return ObjectRegistry.getInstance(RestApiDataSink.class);
    }

    @Override
    public ExternalAppSchema getSchema(RestApiAppConfig restApiAppConfig, RestApiAppSyncConfig restApiAppSyncConfig) {
        return new ExternalAppSchema(null, Lists.newArrayList());
    }

    public List<AppSyncMode> getSyncModes(RestApiAppConfig restApiAppConfig, RestApiAppSyncConfig restApiAppSyncConfig) {
        return Lists.newArrayList(AppSyncMode.INSERT);
    }

    public Class<RestApiAppSyncConfig> getMappingConfigType() {
        return RestApiAppSyncConfig.class;
    }

    @Override
    public Class<RestApiAppConfig> getAppConfigType() {
        return RestApiAppConfig.class;
    }
}
