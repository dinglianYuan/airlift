package io.airlift.discovery.client;

import com.google.common.collect.Iterables;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import io.airlift.discovery.client.testing.TestingDiscoveryModule;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.Set;

import static io.airlift.discovery.client.ServiceAnnouncement.serviceAnnouncement;

public class TestHttpAnnouncementBinder
{
    @Test
    public void testHttpAnnouncement()
    {
        final StaticAnnouncementHttpServerInfoImpl httpServerInfo = new StaticAnnouncementHttpServerInfoImpl(
                URI.create("http://127.0.0.1:4444"),
                URI.create("http://example.com:4444"),
                null,
                null
        );

        Injector injector = Guice.createInjector(
                new TestingDiscoveryModule(),
                new Module()
                {
                    @Override
                    public void configure(Binder binder)
                    {
                        binder.bind(AnnouncementHttpServerInfo.class).toInstance(httpServerInfo);
                        DiscoveryBinder.discoveryBinder(binder).bindHttpAnnouncement("apple");
                    }
                }
        );

        ServiceAnnouncement announcement = serviceAnnouncement("apple")
                .addProperty("http", httpServerInfo.getHttpUri().toASCIIString())
                .addProperty("http-external", httpServerInfo.getHttpExternalUri().toASCIIString())
                .build();

        Set<ServiceAnnouncement> announcements = injector.getInstance(Key.get(new TypeLiteral<Set<ServiceAnnouncement>>()
        {
        }));

        assertAnnouncement(announcements, announcement);
    }

    @Test
    public void testHttpsAnnouncement()
    {
        final StaticAnnouncementHttpServerInfoImpl httpServerInfo = new StaticAnnouncementHttpServerInfoImpl(
                null,
                null,
                URI.create("https://127.0.0.1:4444"),
                URI.create("https://example.com:4444")
        );

        Injector injector = Guice.createInjector(
                new TestingDiscoveryModule(),
                new Module()
                {
                    @Override
                    public void configure(Binder binder)
                    {
                        binder.bind(AnnouncementHttpServerInfo.class).toInstance(httpServerInfo);
                        DiscoveryBinder.discoveryBinder(binder).bindHttpAnnouncement("apple");
                    }
                }
        );

        ServiceAnnouncement announcement = serviceAnnouncement("apple")
                .addProperty("https", httpServerInfo.getHttpsUri().toASCIIString())
                .addProperty("https-external", httpServerInfo.getHttpsExternalUri().toASCIIString())
                .build();

        Set<ServiceAnnouncement> announcements = injector.getInstance(Key.get(new TypeLiteral<Set<ServiceAnnouncement>>()
        {
        }));

        assertAnnouncement(announcements, announcement);
    }

    @Test
    public void testHttpAnnouncementWithPool()
    {
        final StaticAnnouncementHttpServerInfoImpl httpServerInfo = new StaticAnnouncementHttpServerInfoImpl(
                URI.create("http://127.0.0.1:4444"),
                URI.create("http://example.com:4444"),
                URI.create("https://127.0.0.1:4444"),
                URI.create("https://example.com:4444")
        );

        Injector injector = Guice.createInjector(
                new TestingDiscoveryModule(),
                new Module()
                {
                    @Override
                    public void configure(Binder binder)
                    {
                        binder.bind(AnnouncementHttpServerInfo.class).toInstance(httpServerInfo);
                        DiscoveryBinder.discoveryBinder(binder).bindHttpAnnouncement("apple");
                    }
                }
        );

        ServiceAnnouncement announcement = serviceAnnouncement("apple")
                .addProperty("http", httpServerInfo.getHttpUri().toASCIIString())
                .addProperty("http-external", httpServerInfo.getHttpExternalUri().toASCIIString())
                .addProperty("https", httpServerInfo.getHttpsUri().toASCIIString())
                .addProperty("https-external", httpServerInfo.getHttpsExternalUri().toASCIIString())
                .build();

        Set<ServiceAnnouncement> announcements = injector.getInstance(Key.get(new TypeLiteral<Set<ServiceAnnouncement>>()
        {
        }));

        assertAnnouncement(announcements, announcement);
    }

    @Test
    public void testHttpAnnouncementWithCustomProperties()
    {
        final StaticAnnouncementHttpServerInfoImpl httpServerInfo = new StaticAnnouncementHttpServerInfoImpl(
                URI.create("http://127.0.0.1:4444"),
                URI.create("http://example.com:4444"),
                URI.create("https://127.0.0.1:4444"),
                URI.create("https://example.com:4444")
        );

        Injector injector = Guice.createInjector(
                new TestingDiscoveryModule(),
                new Module()
                {
                    @Override
                    public void configure(Binder binder)
                    {
                        binder.bind(AnnouncementHttpServerInfo.class).toInstance(httpServerInfo);
                        DiscoveryBinder.discoveryBinder(binder).bindHttpAnnouncement("apple").addProperty("a", "apple");
                    }
                }
        );

        ServiceAnnouncement announcement = serviceAnnouncement("apple")
                .addProperty("a", "apple")
                .addProperty("http", httpServerInfo.getHttpUri().toASCIIString())
                .addProperty("http-external", httpServerInfo.getHttpExternalUri().toASCIIString())
                .addProperty("https", httpServerInfo.getHttpsUri().toASCIIString())
                .addProperty("https-external", httpServerInfo.getHttpsExternalUri().toASCIIString())
                .build();

        Set<ServiceAnnouncement> announcements = injector.getInstance(Key.get(new TypeLiteral<Set<ServiceAnnouncement>>()
        {
        }));

        assertAnnouncement(announcements, announcement);
    }

    private void assertAnnouncement(Set<ServiceAnnouncement> actualAnnouncements, ServiceAnnouncement expected)
    {
        Assert.assertNotNull(actualAnnouncements);
        Assert.assertEquals(actualAnnouncements.size(), 1);
        ServiceAnnouncement announcement = Iterables.getOnlyElement(actualAnnouncements);
        Assert.assertEquals(announcement.getType(), expected.getType());
        Assert.assertEquals(announcement.getProperties(), expected.getProperties());
    }
}
