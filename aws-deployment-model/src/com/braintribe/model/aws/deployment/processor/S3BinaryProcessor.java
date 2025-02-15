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
package com.braintribe.model.aws.deployment.processor;

import com.braintribe.model.aws.deployment.HasS3Connection;
import com.braintribe.model.cache.HasCacheOptions;
import com.braintribe.model.extensiondeployment.BinaryPersistence;
import com.braintribe.model.extensiondeployment.BinaryRetrieval;
import com.braintribe.model.generic.annotation.meta.Name;
import com.braintribe.model.generic.annotation.meta.Pattern;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

public interface S3BinaryProcessor extends BinaryRetrieval, BinaryPersistence, HasS3Connection, HasCacheOptions {

	EntityType<S3BinaryProcessor> T = EntityTypes.T(S3BinaryProcessor.class);

	@Name("Bucket Name")
	@Pattern("^[a-z][a-zA-Z0-9\\-_\\.]+[a-zA-Z0-9]$")
	String getBucketName();
	void setBucketName(String bucketName);

	@Name("Path Prefix")
	String getPathPrefix();
	void setPathPrefix(String pathPrefix);

}
