// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
//     http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ============================================================================
package com.braintribe.model.aws.deployment;

import com.braintribe.model.aws.deployment.cloudfront.CloudFrontConfiguration;
import com.braintribe.model.deployment.connector.Connector;
import com.braintribe.model.descriptive.HasName;
import com.braintribe.model.generic.annotation.Initializer;
import com.braintribe.model.generic.annotation.meta.Mandatory;
import com.braintribe.model.generic.annotation.meta.Name;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

public interface S3Connector extends Connector, HasName {

	final EntityType<S3Connector> T = EntityTypes.T(S3Connector.class);

	@Name("Access Key")
	@Mandatory
	String getAwsAccessKey();
	void setAwsAccessKey(String awsAccessKey);

	@Name("Secret Key")
	@Mandatory
	String getAwsSecretAccessKey();
	void setAwsSecretAccessKey(String awsSecretAccessKey);

	@Name("Region")
	@Mandatory
	S3Region getRegion();
	void setRegion(S3Region region);

	@Name("Streaming Pool Size")
	@Initializer("10")
	Integer getStreamingPoolSize();
	void setStreamingPoolSize(Integer streamingPoolSize);

	@Name("HTTP Pool Size")
	Integer getHttpConnectionPoolSize();
	void setHttpConnectionPoolSize(Integer httpConnectionPoolSize);

	@Name("Connection Acquisition Timeout")
	Long getConnectionAcquisitionTimeout();
	void setConnectionAcquisitionTimeout(Long connectionAcquisitionTimeout);

	@Name("Connection Timeout")
	Long getConnectionTimeout();
	void setConnectionTimeout(Long connectionTimeout);

	@Name("Socket Timeout")
	Long getSocketTimeout();
	void setSocketTimeout(Long socketTimeout);

	@Name("URL Override")
	String getUrlOverride();
	void setUrlOverride(String urlOverride);

	@Name("CloudFront Configuration")
	CloudFrontConfiguration getCloudFrontConfiguration();
	void setCloudFrontConfiguration(CloudFrontConfiguration cloudFrontConfiguration);
}
