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
package com.braintribe.model.processing.aws.util;

import java.io.StringWriter;
import java.util.Base64;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

import com.braintribe.exception.Exceptions;

public class Keys {

	private String publicKeyBase64;
	private String privateKeyBase64;

	public Keys(String publicKeyBase64, String privateKeyBase64) {
		this.publicKeyBase64 = publicKeyBase64;
		this.privateKeyBase64 = privateKeyBase64;
	}

	public String getPublicKeyBase64() {
		return publicKeyBase64;
	}

	public void setPublicKeyBase64(String publicKeyBase64) {
		this.publicKeyBase64 = publicKeyBase64;
	}

	public String getPrivateKeyBase64() {
		return privateKeyBase64;
	}

	public void setPrivateKeyBase64(String privateKeyBase64) {
		this.privateKeyBase64 = privateKeyBase64;
	}

	public String getPublicKeyPem() {
		try {
			byte[] publicKey = Base64.getDecoder().decode(publicKeyBase64);
			StringWriter writer = new StringWriter();
			PemWriter pemWriter = new PemWriter(writer);
			pemWriter.writeObject(new PemObject("PUBLIC KEY", publicKey));
			pemWriter.flush();
			pemWriter.close();

			return writer.toString();
		} catch (Exception e) {
			throw Exceptions.unchecked(e, "Could not create PEM output of public key");
		}
	}
}
