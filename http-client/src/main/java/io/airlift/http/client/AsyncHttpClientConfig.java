package io.airlift.http.client;

import com.google.common.annotations.Beta;
import io.airlift.configuration.Config;

import javax.validation.constraints.Min;

@Beta
public class AsyncHttpClientConfig
{
    private int workerThreads = 16;

    @Min(1)
    public int getWorkerThreads()
    {
        return workerThreads;
    }

    @Config("http-client.threads")
    public AsyncHttpClientConfig setWorkerThreads(int workerThreads)
    {
        this.workerThreads = workerThreads;
        return this;
    }
}
