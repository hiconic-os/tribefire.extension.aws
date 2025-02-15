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
package com.braintribe.model.aws.service;

import com.braintribe.model.generic.annotation.meta.Description;
import com.braintribe.model.generic.annotation.meta.Mandatory;
import com.braintribe.model.generic.annotation.meta.Name;
import com.braintribe.model.generic.eval.EvalContext;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.service.api.ServiceRequest;

@Description("Generates a pre-signed URL for a given Access ID and Resource ID that can be used for direct download for a given time.")
public interface CreatePresignedUrlForResource extends AwsRequest {

	EntityType<CreatePresignedUrlForResource> T = EntityTypes.T(CreatePresignedUrlForResource.class);

	@Name("Access ID")
	@Description("The access ID of the Resource")
	@Mandatory
	String getAccessId();
	void setAccessId(String accessId);

	@Name("Resource ID")
	@Description("The ID of the Resource")
	@Mandatory
	String getResourceId();
	void setResourceId(String resourceId);

	@Name("Time to Live (ms)")
	@Description("Specifies how long the link should be valid (in milliseconds)")
	long getTimeToLiveInMs();
	void setTimeToLiveInMs(long timeToLiveInMs);

	@Override
	EvalContext<? extends PresignedUrl> eval(Evaluator<ServiceRequest> evaluator);

}
