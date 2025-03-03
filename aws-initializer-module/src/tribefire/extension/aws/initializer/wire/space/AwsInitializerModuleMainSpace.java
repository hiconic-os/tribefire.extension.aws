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
package tribefire.extension.aws.initializer.wire.space;

import tribefire.cortex.initializer.support.integrity.wire.contract.CoreInstancesContract;
import tribefire.cortex.initializer.support.wire.space.AbstractInitializerSpace;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;

import tribefire.extension.aws.initializer.wire.contract.AwsInitializerModuleContract;
import tribefire.extension.aws.initializer.wire.contract.AwsInitializerModuleMainContract;
import tribefire.extension.aws.initializer.wire.contract.AwsInitializerModuleModelsContract;
import tribefire.extension.aws.initializer.wire.contract.RuntimePropertiesContract;
import tribefire.extension.aws.initializer.wire.contract.ExistingInstancesContract;

/**
 * @see AwsInitializerModuleMainContract
 */
@Managed
public class AwsInitializerModuleMainSpace extends AbstractInitializerSpace implements AwsInitializerModuleMainContract {

	@Import
	private AwsInitializerModuleContract initializer;
	
	@Import
	private AwsInitializerModuleModelsContract models;
	
	@Import
	private RuntimePropertiesContract properties;
	
	@Import
	private ExistingInstancesContract existingInstances;
	
	@Import
	private CoreInstancesContract coreInstances;
	
	@Override
	public AwsInitializerModuleContract initializerContract() {
		return initializer;
	}

	@Override
	public AwsInitializerModuleModelsContract initializerModelsContract() {
		return models;
	}

	@Override
	public RuntimePropertiesContract propertiesContract() {
		return properties;
	}
	
	@Override
	public ExistingInstancesContract existingInstancesContract() {
		return existingInstances;
	}
	
	@Override
	public CoreInstancesContract coreInstancesContract() {
		return coreInstances;
	}
}
