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
package tribefire.extension.aws.initializer.wire;

import static com.braintribe.wire.api.util.Lists.list;

import java.util.List;

import tribefire.cortex.initializer.support.integrity.wire.CoreInstancesWireModule;
import com.braintribe.wire.api.module.WireModule;
import com.braintribe.wire.api.module.WireTerminalModule;

import tribefire.extension.aws.initializer.wire.contract.AwsInitializerModuleMainContract;
import tribefire.extension.aws.templates.wire.AwsTemplatesWireModule;

/**
 * <p>
 * This is the {@link WireModule Wire module} of the aws-module-initializer.
 * </p>
 */
public enum AwsInitializerModuleWireModule implements WireTerminalModule<AwsInitializerModuleMainContract> {
	
	INSTANCE;
	
	/**
	 * <p>
	 * Declares initializer's Wire module dependencies. All external managed
	 * instances used within initializer, have Wire module providing their
	 * exposing contract declared here.
	 * </p>
	 */
	@Override
	public List<WireModule> dependencies() {
		return list(CoreInstancesWireModule.INSTANCE, AwsTemplatesWireModule.INSTANCE);
	}
	
}
