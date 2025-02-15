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
package tribefire.extension.aws.initializer.wire.contract;

import com.braintribe.model.aws.deployment.S3Region;
import com.braintribe.wire.api.annotation.Decrypt;
import com.braintribe.wire.api.annotation.Default;

import tribefire.cortex.initializer.support.wire.contract.PropertyLookupContract;

/*
 * For compatibility reasons, this is not using the PropertyDefinitionsContract yet. This will be activated later.
 */
public interface RuntimePropertiesContract extends PropertyLookupContract {

	@Decrypt
	String S3_ACCESS_KEY();
	@Decrypt
	String S3_SECRET_ACCESS_KEY();

	String S3_STORAGE_BUCKETNAME();

	String S3_PATH_PREFIX();

	@Default("eu_central_1")
	S3Region S3_REGION();

	@Default("true")
	boolean S3_CREATE_DEFAULT_STORAGE_BINARY_PROCESSOR();

	Integer S3_HTTP_CONNECTION_POOL_SIZE();

	Long S3_CONNECTION_ACQUISITION_TIMEOUT();

	Long S3_CONNECTION_TIMEOUT();

	Long S3_SOCKET_TIMEOUT();

	String S3_URL_OVERRIDE();

	String S3_CLOUDFRONT_BASE_URL();
	String S3_CLOUDFRONT_KEYGROUP_ID();
	@Decrypt
	String S3_CLOUDFRONT_PUBLIC_KEY();
	@Decrypt
	String S3_CLOUDFRONT_PRIVATE_KEY();
}
