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
package tribefire.extension.aws.templates.wire.contract;

import com.braintribe.model.aws.deployment.S3Connector;
import com.braintribe.model.aws.deployment.processor.S3BinaryProcessor;
import com.braintribe.model.processing.resource.configuration.ExternalResourcesContext;
import com.braintribe.wire.api.space.WireSpace;

import tribefire.extension.aws.templates.api.S3BinaryProcessTemplateContext;

public interface AwsTemplatesContract extends WireSpace {

	S3BinaryProcessor s3StorageBinaryProcessor(S3BinaryProcessTemplateContext context);
	
	S3Connector connector(S3BinaryProcessTemplateContext context);

	ExternalResourcesContext externalResourcesContext(S3BinaryProcessTemplateContext context);
	
}
