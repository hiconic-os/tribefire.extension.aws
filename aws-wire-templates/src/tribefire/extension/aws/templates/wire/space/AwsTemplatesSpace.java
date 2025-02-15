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
package tribefire.extension.aws.templates.wire.space;

import com.braintribe.model.aws.deployment.S3Connector;
import com.braintribe.model.aws.deployment.cloudfront.CloudFrontConfiguration;
import com.braintribe.model.aws.deployment.processor.S3BinaryProcessor;
import com.braintribe.model.aws.resource.S3Source;
import com.braintribe.model.extensiondeployment.meta.BinaryProcessWith;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.aws.util.AwsUtil;
import com.braintribe.model.processing.aws.util.Keys;
import com.braintribe.model.processing.resource.configuration.ExternalResourcesContext;
import com.braintribe.utils.lcd.StringTools;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.scope.InstanceConfiguration;

import tribefire.extension.aws.templates.api.AwsConstants;
import tribefire.extension.aws.templates.api.S3BinaryProcessTemplateContext;
import tribefire.extension.aws.templates.wire.contract.AwsTemplatesContract;

@Managed
public class AwsTemplatesSpace implements AwsTemplatesContract {

	@Override
	@Managed
	public S3BinaryProcessor s3StorageBinaryProcessor(S3BinaryProcessTemplateContext context) {
		S3BinaryProcessor bean = context.create(S3BinaryProcessor.T, InstanceConfiguration.currentInstance());
		bean.setCartridge(context.getAwsCartridge());
		bean.setModule(context.getAwsModule());
		bean.setName("S3 Storage Binary Processor " + context.getBucketName());
		bean.setConnection(connector(context));
		bean.setBucketName(context.getBucketName());
		bean.setAutoDeploy(credentialsAvailable(context));
		bean.setPathPrefix(context.getPathPrefix());
		return bean;
	}

	@Override
	@Managed
	public S3Connector connector(S3BinaryProcessTemplateContext context) {
		S3Connector bean = context.create(S3Connector.T, InstanceConfiguration.currentInstance());
		bean.setCartridge(context.getAwsCartridge());
		bean.setModule(context.getAwsModule());
		bean.setName("S3 Connector " + context.getName());
		bean.setAwsAccessKey(context.getAwsAccessKey());
		bean.setAwsSecretAccessKey(context.getAwsSecretAccessKey());
		bean.setRegion(context.getRegion());
		bean.setAutoDeploy(credentialsAvailable(context));
		bean.setHttpConnectionPoolSize(context.getHttpConnectionPoolSize());
		bean.setConnectionAcquisitionTimeout(context.getConnectionAcquisitionTimeout());
		bean.setConnectionTimeout(context.getConnectionTimeout());
		bean.setSocketTimeout(context.getSocketTimeout());
		bean.setUrlOverride(context.getUrlOverride());

		String cloudFrontBaseUrl = context.getCloudFrontBaseUrl();
		if (!StringTools.isBlank(cloudFrontBaseUrl)) {
			CloudFrontConfiguration config = cloudFrontConfiguration(context);
			bean.setCloudFrontConfiguration(config);
		}

		return bean;
	}

	@Managed
	private CloudFrontConfiguration cloudFrontConfiguration(S3BinaryProcessTemplateContext context) {
		String cloudFrontBaseUrl = context.getCloudFrontBaseUrl();
		String privateKey = context.getCloudFrontPrivateKey();
		String publicKey = context.getCloudFrontPublicKey();
		String keyGroupId = context.getCloudFrontKeyGroupId();

		Keys keys;
		if (StringTools.isAnyBlank(privateKey, publicKey)) {
			keys = AwsUtil.generateKeyPair(2048);
		} else {
			keys = new Keys(publicKey, privateKey);
		}

		privateKey = keys.getPrivateKeyBase64();
		publicKey = keys.getPublicKeyBase64();
		String publicKeyPem = keys.getPublicKeyPem();

		CloudFrontConfiguration bean = context.create(CloudFrontConfiguration.T, InstanceConfiguration.currentInstance());
		bean.setBaseUrl(cloudFrontBaseUrl);
		bean.setPrivateKey(privateKey);
		bean.setPublicKey(publicKey);
		bean.setPublicKeyPem(publicKeyPem);
		bean.setKeyGroupId(keyGroupId);

		return bean;
	}

	private boolean credentialsAvailable(S3BinaryProcessTemplateContext context) {
		return !StringTools.isAnyBlank(context.getAwsAccessKey(), context.getAwsSecretAccessKey());
	}

	@Override
	@Managed
	public ExternalResourcesContext externalResourcesContext(S3BinaryProcessTemplateContext context) {
		ExternalResourcesContext bean = ExternalResourcesContext.builder().setBinaryProcessWith(binaryProcessWithAws(context))
				.setPersistenceDataModel((GmMetaModel) context.lookup("model:" + AwsConstants.DATA_MODEL_QUALIFIEDNAME))
				.setResourceSourceType(S3Source.T).build();
		return bean;
	}

	@Managed
	private BinaryProcessWith binaryProcessWithAws(S3BinaryProcessTemplateContext context) {
		BinaryProcessWith bean = context.create(BinaryProcessWith.T, InstanceConfiguration.currentInstance());

		S3BinaryProcessor processor = s3StorageBinaryProcessor(context);

		bean.setRetrieval(processor);
		bean.setPersistence(processor);
		return bean;
	}

}
