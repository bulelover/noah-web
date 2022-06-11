package org.noah.web.base.config.swagger;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.util.Set;

class ParameterTypeDeterminer {
    private ParameterTypeDeterminer() {
        throw new UnsupportedOperationException();
    }

    public static String determineScalarParameterType(Set<? extends MediaType> consumes, HttpMethod method) {
        String parameterType = "query";

        if (consumes.contains(MediaType.APPLICATION_FORM_URLENCODED)
                && method == HttpMethod.POST) {
            parameterType = "form";
        } else if (consumes.contains(MediaType.MULTIPART_FORM_DATA)
                && method == HttpMethod.POST) {
            parameterType = "formData";
        }

        return parameterType;
    }
}
