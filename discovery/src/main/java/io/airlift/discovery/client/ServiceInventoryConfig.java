package io.airlift.discovery.client;

import io.airlift.configuration.Config;
import io.airlift.configuration.ConfigDescription;
import io.airlift.units.Duration;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.concurrent.TimeUnit;

public class ServiceInventoryConfig
{
    private URI serviceInventoryUri;
    private Duration updateInterval = new Duration(10, TimeUnit.SECONDS);

    public URI getServiceInventoryUri()
    {
        return serviceInventoryUri;
    }

    @Config("service-inventory.uri")
    @ConfigDescription("Service inventory base URI")
    public ServiceInventoryConfig setServiceInventoryUri(URI uri)
    {
        this.serviceInventoryUri = uri;
        return this;
    }

    @NotNull
    public Duration getUpdateInterval()
    {
        return updateInterval;
    }

    @Config("service-inventory.update-interval")
    @ConfigDescription("Service inventory update interval")
    public ServiceInventoryConfig setUpdateInterval(Duration updateInterval)
    {
        this.updateInterval = updateInterval;
        return this;
    }
}
