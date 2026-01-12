package com.glycemic.core.config;

import io.sentry.CustomSamplingContext;
import io.sentry.SamplingContext;
import io.sentry.SentryOptions.TracesSamplerCallback;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class SentryTracingConfigurer implements TracesSamplerCallback {
    @Override
    public Double sample(SamplingContext context) {
        CustomSamplingContext customSamplingContext = context.getCustomSamplingContext();
        if (customSamplingContext != null) {
            HttpServletRequest request = (HttpServletRequest) customSamplingContext.get("request");
            // return a number between 0 and 1 or null (to fallback to configured value)
        } else {
            // return a number between 0 and 1 or null (to fallback to configured value)
        }
        return 1.0;
    }
}
