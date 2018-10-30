package com.amazonaws.lambda.demo;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent.DynamodbStreamRecord;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

public class LambdaFunctionHandler implements RequestHandler<Object, String> {

	private AmazonS3 s3 = AmazonS3ClientBuilder.standard().build();

	public LambdaFunctionHandler() {
	}

	// Test purpose only.
	LambdaFunctionHandler(AmazonS3 s3) {
        this.s3 = s3;
    }

	@Override
    public String handleRequest(Object event, Context context) {
        
    	if(event  instanceof S3Event) {
    	context.getLogger().log("Received event: SHARIQ QQQQQQQ " + event);

        // Get the object from the event and show its content type
        String bucket = ((S3Event)event).getRecords().get(0).getS3().getBucket().getName();
        String key = ((S3Event)event).getRecords().get(0).getS3().getObject().getKey();
        try {
            S3Object response = s3.getObject(new GetObjectRequest(bucket, key));
            String contentType = response.getObjectMetadata().getContentType();
            context.getLogger().log("CONTENT TYPE: " + contentType);
            return contentType;
        } catch (Exception e) {
            e.printStackTrace();
            context.getLogger().log(String.format(
                "Error getting object %s from bucket %s. Make sure they exist and"
                + " your bucket is in the same region as this function.", key, bucket));
            throw e;
        } 
        }     else {// if(event instanceof DynamodbEvent) {
        	 context.getLogger().log("Received event for SHARIQ DYNAMOOOOO : " + event);

             for (DynamodbStreamRecord record : ((DynamodbEvent)event).getRecords()) {
                 context.getLogger().log(record.getEventID());
                 context.getLogger().log(record.getEventName());
                 context.getLogger().log(record.getDynamodb().toString());
             }
             return String.valueOf(((DynamodbEvent)event).getRecords().size());
        }
		//return null;
    	
    	
    
    }

}
