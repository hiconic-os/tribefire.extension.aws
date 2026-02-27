package tribefire.extension.aws.processing.api;

import com.braintribe.model.processing.aws.connect.S3Connector;

import tribefire.extension.aws.processing.CloudFrontConnectionInfo;

public interface AwsConnection {
	S3Connector connector();
	CloudFrontConnectionInfo cloudFronConnectionInfo();
}
