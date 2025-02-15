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
package com.braintribe.model.aws.deployment.cloudfront;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.annotation.SelectiveInformation;
import com.braintribe.model.generic.annotation.meta.Name;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

@SelectiveInformation("CloudFront ${baseUrl}")
public interface CloudFrontConfiguration extends GenericEntity {

	final EntityType<CloudFrontConfiguration> T = EntityTypes.T(CloudFrontConfiguration.class);

	String baseUrl = "baseUrl";
	String keyGroupId = "keyGroupId";
	String privateKey = "privateKey";
	String publicKey = "publicKey";
	String publicKeyPem = "publicKeyPem";

	@Name("Base URL")
	String getBaseUrl();
	void setBaseUrl(String baseUrl);

	@Name("Key Group Id")
	String getKeyGroupId();
	void setKeyGroupId(String keyGroupId);

	@Name("Private Key (PKCS8, Base64)")
	String getPrivateKey();
	void setPrivateKey(String privateKey);

	@Name("Public Key (PKCS8, Base64)")
	String getPublicKey();
	void setPublicKey(String publicKey);

	@Name("Public Key (PEM)")
	String getPublicKeyPem();
	void setPublicKeyPem(String publicKeyPem);

}
