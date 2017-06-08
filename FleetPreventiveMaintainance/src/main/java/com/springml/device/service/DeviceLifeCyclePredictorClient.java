package com.springml.device.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.*;
import com.google.api.client.http.json.JsonHttpContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.Collections;

/**
 * Created by kaarthikraaj on 6/6/17.
 */
public class DeviceLifeCyclePredictorClient implements Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(DeviceLifeCyclePredictorClient.class);
    private String CLOUDML_SCOPE = "https://www.googleapis.com/auth/cloud-platform";


    public String getPredictedLifeCycle(JsonHttpContent jsonHttpContent, String predictRestUrl) {
        String predictionResponse = "";
        try {
            GoogleCredential credential = GoogleCredential.getApplicationDefault()
                    .createScoped(Collections.singleton(CLOUDML_SCOPE));
            HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            HttpRequestFactory requestFactory = httpTransport.createRequestFactory(
                    credential);
            GenericUrl url = new GenericUrl(predictRestUrl);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            jsonHttpContent.writeTo(baos);
            LOG.info("The ML URL being tried is " + predictRestUrl);
            LOG.info("Executing CloudML predictions with payload : " + baos.toString());
            HttpRequest request = requestFactory.buildPostRequest(url, jsonHttpContent);

            HttpResponse response = request.execute();
            predictionResponse = response.parseAsString();

            LOG.info("CloudML prediction response \n" + predictionResponse);
        } catch (Exception e) {
            LOG.error("Error while getting predictions using CloudML", e);
            predictionResponse = "Exception while getting prediction";
        }

        return predictionResponse;
    }


}
