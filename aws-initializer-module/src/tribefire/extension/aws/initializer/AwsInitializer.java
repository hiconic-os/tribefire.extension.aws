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
package tribefire.extension.aws.initializer;

import com.braintribe.model.aws.deployment.cloudfront.CloudFrontConfiguration;
import com.braintribe.model.aws.resource.S3Source;
import com.braintribe.model.aws.service.AcquiredCloudFrontKeyPair;
import com.braintribe.model.aws.service.AwsRequest;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.bootstrapping.TribefireRuntime;
import com.braintribe.model.processing.meta.editor.BasicModelMetaDataEditor;
import com.braintribe.model.processing.meta.editor.ModelMetaDataEditor;
import com.braintribe.model.processing.session.api.collaboration.PersistenceInitializationContext;
import com.braintribe.utils.StringTools;
import com.braintribe.wire.api.module.WireTerminalModule;

import tribefire.cortex.initializer.support.api.WiredInitializerContext;
import tribefire.cortex.initializer.support.impl.AbstractInitializer;
import tribefire.extension.aws.initializer.wire.AwsInitializerModuleWireModule;
import tribefire.extension.aws.initializer.wire.contract.AwsInitializerModuleContract;
import tribefire.extension.aws.initializer.wire.contract.AwsInitializerModuleMainContract;
import tribefire.extension.aws.initializer.wire.contract.RuntimePropertiesContract;

/**
 * <p>
 * This {@link AbstractInitializer initializer} initializes targeted accesses with our custom instances available from
 * initializer's contracts.
 * </p>
 */
public class AwsInitializer extends AbstractInitializer<AwsInitializerModuleMainContract> {

	@Override
	public WireTerminalModule<AwsInitializerModuleMainContract> getInitializerWireModule() {
		return AwsInitializerModuleWireModule.INSTANCE;
	}

	@Override
	public void initialize(PersistenceInitializationContext context, WiredInitializerContext<AwsInitializerModuleMainContract> initializerContext,
			AwsInitializerModuleMainContract initializerMainContract) {

		TribefireRuntime.setPropertyPrivate("S3_ACCESS_KEY", "S3_SECRET_ACCESS_KEY");

		GmMetaModel cortexModel = initializerMainContract.coreInstancesContract().cortexModel();
		cortexModel.getDependencies().add(initializerMainContract.initializerModelsContract().configuredDeploymentModel());

		GmMetaModel cortexServiceModel = initializerMainContract.coreInstancesContract().cortexServiceModel();
		cortexServiceModel.getDependencies().add(initializerMainContract.initializerModelsContract().configuredServiceModel());

		RuntimePropertiesContract properties = initializerMainContract.propertiesContract();
		if (properties.S3_CREATE_DEFAULT_STORAGE_BINARY_PROCESSOR()) {
			if (!StringTools.isAnyBlank(properties.S3_ACCESS_KEY(), properties.S3_SECRET_ACCESS_KEY())) {
				initializerMainContract.initializerContract().s3DefaultStorageBinaryProcessor();
				addMetaDataToModelsBinaryProcess(context, initializerMainContract);
			}
		}
		initializerMainContract.initializerContract().serviceRequestProcessor();
		initializerMainContract.initializerContract().functionalCheckBundle();
		addMetaDataToModelsProcess(context, initializerMainContract);
		addMetaDataToDeploymentModel(context, initializerMainContract);
		addMetaDataToServiceModel(context, initializerMainContract);
	}

	private void addMetaDataToModelsBinaryProcess(PersistenceInitializationContext context,
			AwsInitializerModuleMainContract initializerMainContract) {
		ModelMetaDataEditor modelEditor = BasicModelMetaDataEditor.create(initializerMainContract.initializerModelsContract().configuredDataModel())
				.withEtityFactory(context.getSession()::create).done();
		modelEditor.onEntityType(S3Source.T).addMetaData(initializerMainContract.initializerContract().binaryProcessWith());
	}

	private void addMetaDataToModelsProcess(PersistenceInitializationContext context, AwsInitializerModuleMainContract initializerMainContract) {
		ModelMetaDataEditor modelEditor = BasicModelMetaDataEditor
				.create(initializerMainContract.initializerModelsContract().configuredServiceModel()).withEtityFactory(context.getSession()::create)
				.done();
		modelEditor.onEntityType(AwsRequest.T).addMetaData(initializerMainContract.initializerContract().serviceProcessWith());
	}

	private void addMetaDataToDeploymentModel(PersistenceInitializationContext context, AwsInitializerModuleMainContract initializerMainContract) {
		ModelMetaDataEditor modelEditor = BasicModelMetaDataEditor
				.create(initializerMainContract.initializerModelsContract().configuredDeploymentModel())
				.withEtityFactory(context.getSession()::create).done();

		AwsInitializerModuleContract initializerContract = initializerMainContract.initializerContract();
		//@formatter:off
		modelEditor.onEntityType(CloudFrontConfiguration.T)
			.addPropertyMetaData(CloudFrontConfiguration.publicKeyPem, initializerContract.outline());
		//@formatter:on

	}

	private void addMetaDataToServiceModel(PersistenceInitializationContext context, AwsInitializerModuleMainContract initializerMainContract) {
		ModelMetaDataEditor modelEditor = BasicModelMetaDataEditor
				.create(initializerMainContract.initializerModelsContract().configuredServiceModel()).withEtityFactory(context.getSession()::create)
				.done();

		AwsInitializerModuleContract initializerContract = initializerMainContract.initializerContract();
		//@formatter:off
		modelEditor.onEntityType(AcquiredCloudFrontKeyPair.T)
			.addPropertyMetaData(AcquiredCloudFrontKeyPair.publicKeyPem, initializerContract.outline());
		//@formatter:on

	}

}
