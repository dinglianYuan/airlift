package io.airlift.discovery.client;

import java.net.URI;

public interface AnnouncementHttpServerInfo
{
    URI getHttpUri();
    URI getHttpExternalUri();

    URI getHttpsUri();
    URI getHttpsExternalUri();
}
