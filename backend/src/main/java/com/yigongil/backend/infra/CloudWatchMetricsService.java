package com.yigongil.backend.infra;

import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.cloudwatch.model.Dimension;
import software.amazon.awssdk.services.cloudwatch.model.MetricDatum;
import software.amazon.awssdk.services.cloudwatch.model.PutMetricDataRequest;
import software.amazon.awssdk.services.cloudwatch.model.StandardUnit;

import java.util.List;

import static org.springframework.boot.actuate.metrics.MetricsEndpoint.MetricResponse;
import static org.springframework.boot.actuate.metrics.MetricsEndpoint.Sample;

@Profile(value = {"prod"})
@Service
public class CloudWatchMetricsService {

    private static final CloudWatchClient CLOUD_WATCH_CLIENT = CloudWatchClient.builder()
            .region(Region.AP_NORTHEAST_2)
            .build();
    private final MetricsEndpoint metricsEndpoint;

    public CloudWatchMetricsService(MetricsEndpoint metricsEndpoint) {
        this.metricsEndpoint = metricsEndpoint;
    }

    @Scheduled(fixedDelay = 60_000)
    public void pushMetricsToCloudWatch() {
        MetricResponse metrics = metricsEndpoint.metric("http.server.requests", null);

        for (Sample sample : metrics.getMeasurements()) {
            List<String> uris = metrics.getAvailableTags().stream()
                    .filter(tag -> "uri".equals(tag.getTag()))
                    .flatMap(tag -> tag.getValues().stream())
                    .toList();

            for (String uri : uris) {
                MetricDatum datum = MetricDatum.builder()
                        .metricName("http.server.requests." + sample.getStatistic())
                        .value(sample.getValue())
                        .unit(StandardUnit.COUNT)
                        .dimensions(Dimension.builder().name("URI").value(uri).build())
                        .build();

                PutMetricDataRequest request = PutMetricDataRequest.builder()
                        .namespace("yigongil-prod")
                        .metricData(datum)
                        .build();

                CLOUD_WATCH_CLIENT.putMetricData(request);
            }
        }
    }
}
