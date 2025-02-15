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

import java.util.function.Function;

import com.braintribe.model.aws.deployment.S3Region;
import com.braintribe.model.deployment.Cartridge;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;

public interface S3BinaryProcessTemplateContextBuilder {

	S3BinaryProcessTemplateContextBuilder setIdPrefix(String idPrefix);

	S3BinaryProcessTemplateContextBuilder setName(String name);

	S3BinaryProcessTemplateContextBuilder setAwsAccessKey(String awsAccessKey);

	S3BinaryProcessTemplateContextBuilder setAwsSecretAccessKey(String awsSecretAccessKey);

	S3BinaryProcessTemplateContextBuilder setHttpConnectionPoolSize(Integer httpConnectionPoolSize);

	S3BinaryProcessTemplateContextBuilder setConnectionAcquisitionTimeout(Long connectionAcquisitionTimeout);

	S3BinaryProcessTemplateContextBuilder setConnectionTimeout(Long connectionTimeout);

	S3BinaryProcessTemplateContextBuilder setSocketTimeout(Long socketTimeout);

	S3BinaryProcessTemplateContextBuilder setUrlOverride(String urlOverride);

	S3BinaryProcessTemplateContextBuilder setCloudFrontBaseUrl(String cloudFrontBaseUrl);
	S3BinaryProcessTemplateContextBuilder setCloudFrontPrivateKey(String cloudFrontPrivateKey);
	S3BinaryProcessTemplateContextBuilder setCloudFrontPublicKey(String cloudFrontPublicKey);
	S3BinaryProcessTemplateContextBuilder setCloudFrontKeyGroupId(String cloudFrontKeyGroupId);

	S3BinaryProcessTemplateContextBuilder setRegion(S3Region region);

	S3BinaryProcessTemplateContextBuilder setPathPrefix(String pathPrefix);

	S3BinaryProcessTemplateContextBuilder setBucketName(String bucketName);

	S3BinaryProcessTemplateContextBuilder setEntityFactory(Function<EntityType<?>, GenericEntity> entityFactory);

	S3BinaryProcessTemplateContextBuilder setAwsCartridge(Cartridge awsCartridge);

	S3BinaryProcessTemplateContextBuilder setAwsModule(com.braintribe.model.deployment.Module awsModule);

	S3BinaryProcessTemplateContextBuilder setLookupFunction(Function<String, ? extends GenericEntity> lookupFunction);

	S3BinaryProcessTemplateContext build();
}