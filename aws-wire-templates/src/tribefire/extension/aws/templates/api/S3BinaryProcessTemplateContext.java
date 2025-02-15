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
package tribefire.extension.aws.templates.api;

import com.braintribe.model.aws.deployment.S3Region;
import com.braintribe.model.deployment.Cartridge;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.wire.api.scope.InstanceConfiguration;

public interface S3BinaryProcessTemplateContext {

	String getIdPrefix();

	String getName();

	String getAwsAccessKey();

	String getAwsSecretAccessKey();

	S3Region getRegion();

	String getBucketName();

	String getPathPrefix();

	Integer getHttpConnectionPoolSize();

	Long getConnectionAcquisitionTimeout();

	Long getConnectionTimeout();

	Long getSocketTimeout();

	String getUrlOverride();

	String getCloudFrontBaseUrl();

	String getCloudFrontPrivateKey();

	String getCloudFrontPublicKey();

	String getCloudFrontKeyGroupId();

	Cartridge getAwsCartridge();

	com.braintribe.model.deployment.Module getAwsModule();

	<T extends GenericEntity> T lookup(String globalId);

	<T extends GenericEntity> T create(EntityType<T> entityType, InstanceConfiguration instanceConfiguration);

	static S3BinaryProcessTemplateContextBuilder builder() {
		return new S3BinaryProcessTemplateContextImpl();
	}

}