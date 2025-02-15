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
package tribefire.extension.aws.wire.space;

import com.braintribe.model.aws.deployment.S3Connector;
import com.braintribe.model.aws.deployment.processor.AwsServiceProcessor;
import com.braintribe.model.aws.deployment.processor.HealthCheckProcessor;
import com.braintribe.model.aws.deployment.processor.S3BinaryProcessor;
import com.braintribe.model.processing.deployment.api.binding.DenotationBindingBuilder;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;

import tribefire.module.wire.contract.TribefireModuleContract;
import tribefire.module.wire.contract.TribefireWebPlatformContract;

/**
 * This module's javadoc is yet to be written.
 */
@Managed
public class AwsModuleSpace implements TribefireModuleContract {

	@Import
	private TribefireWebPlatformContract tfPlatform;

	@Import
	private AwsDeployablesSpace deployables;

	@Override
	public void bindDeployables(DenotationBindingBuilder bindings) {
		//@formatter:off
		bindings.bind(AwsServiceProcessor.T) //
			.component(tfPlatform.binders().serviceProcessor()) //
			.expertSupplier(deployables::awsServiceProcessor);
		
		bindings.bind(S3Connector.T) //
			.component(com.braintribe.model.processing.aws.connect.S3Connector.class) //
			.expertFactory(deployables::connector);
		
		bindings.bind(S3BinaryProcessor.T) //
			.component(tfPlatform.binders().binaryRetrievalProcessor()) //
			.expertFactory(deployables::binaryProcessor) //
			.component(tfPlatform.binders().binaryPersistenceProcessor()) //
			.expertFactory(deployables::binaryProcessor);
		
		bindings.bind(HealthCheckProcessor.T) //
			.component(tfPlatform.binders().checkProcessor()) //
			.expertSupplier(this.deployables::healthCheckProcessor);
		//@formatter:on
	}
}
