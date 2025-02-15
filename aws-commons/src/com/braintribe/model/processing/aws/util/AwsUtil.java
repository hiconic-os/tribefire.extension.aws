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

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Base64;
import java.util.Date;

import com.braintribe.exception.Exceptions;

import software.amazon.awssdk.core.exception.SdkException;

public class AwsUtil {

	private static final SecureRandom srand = new SecureRandom();

	public static Keys generateKeyPair(int keySize) {
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(keySize);
			KeyPair newKeyPair = kpg.genKeyPair();
			byte[] pri = newKeyPair.getPrivate().getEncoded();
			byte[] pub = newKeyPair.getPublic().getEncoded();

			String publicKey = Base64.getEncoder().encodeToString(pub);
			String privateKey = Base64.getEncoder().encodeToString(pri);

			return new Keys(publicKey, privateKey);
		} catch (Exception e) {
			throw Exceptions.unchecked(e, "Error while creating a key pair of size " + keySize);
		}
	}

	// Note: here are some exmaple implementations on how to create a non-canned policy:
	// https://github.com/dashpradeep99/aws-sdk-java-code/blob/master/aws-java-sdk-cloudfront/src/main/java/com/amazonaws/services/cloudfront/util/SignerUtils.java
	// https://github.com/dashpradeep99/aws-sdk-java-code/blob/master/aws-java-sdk-cloudfront/src/main/java/com/amazonaws/services/cloudfront/CloudFrontUrlSigner.java

	/**
	 * Generates a signed url that expires after given date.
	 * 
	 * @param resourceUrlOrPath
	 *            The url.
	 * @param keyPairId
	 *            The keypair id used to sign.
	 * @param privateKey
	 *            The private key.
	 * @param dateLessThan
	 *            The expire date/time.
	 * @return A valid cloudwatch url.
	 * @throws SdkException
	 *             If any errors occur during the signing process.
	 */
	public static String getSignedUrlWithCannedPolicy(String resourceUrlOrPath, String keyPairId, PrivateKey privateKey, Date dateLessThan)
			throws SdkException {
		try {
			String cannedPolicy = buildCannedPolicy(resourceUrlOrPath, dateLessThan);
			byte[] signatureBytes = signWithSha1Rsa(cannedPolicy.getBytes(StandardCharsets.UTF_8), privateKey);
			String urlSafeSignature = makeBytesUrlSafe(signatureBytes);
			return resourceUrlOrPath + (resourceUrlOrPath.indexOf('?') >= 0 ? "&" : "?") + "Expires=" + MILLISECONDS.toSeconds(dateLessThan.getTime())
					+ "&Signature=" + urlSafeSignature + "&Key-Pair-Id=" + keyPairId;
		} catch (InvalidKeyException e) {
			throw SdkException.create("Couldn't sign url", e);
		}
	}

	/**
	 * Returns a "canned" policy for the given parameters. For more information, see
	 * <a href= "http://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/private-content-signed-urls-overview.html" >Overview of Signed
	 * URLs</a>.
	 * 
	 * @param resourceUrlOrPath
	 *            The resource to grant access.
	 * @param dateLessThan
	 *            The expiration time.
	 * @return the aws policy as a string.
	 */
	public static String buildCannedPolicy(String resourceUrlOrPath, Date dateLessThan) {
		return "{\"Statement\":[{\"Resource\":\"" + resourceUrlOrPath + "\",\"Condition\":{\"DateLessThan\":{\"AWS:EpochTime\":"
				+ MILLISECONDS.toSeconds(dateLessThan.getTime()) + "}}}]}";
	}

	/**
	 * Signs the data given with the private key given, using the SHA1withRSA algorithm provided by bouncy castle.
	 * 
	 * @param dataToSign
	 *            The data to sign.
	 * @param privateKey
	 *            The private key.
	 * @return A signature.
	 * @throws InvalidKeyException
	 *             if an invalid key was provided.
	 */
	public static byte[] signWithSha1Rsa(byte[] dataToSign, PrivateKey privateKey) throws InvalidKeyException {
		Signature signature;
		try {
			signature = Signature.getInstance("SHA1withRSA");
			signature.initSign(privateKey, srand);
			signature.update(dataToSign);
			return signature.sign();
		} catch (NoSuchAlgorithmException | SignatureException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Converts the given data to be safe for use in signed URLs for a private distribution by using specialized Base64 encoding.
	 * 
	 * @param bytes
	 *            The bytes
	 */
	public static String makeBytesUrlSafe(byte[] bytes) {
		byte[] encoded = java.util.Base64.getEncoder().encode(bytes);

		for (int i = 0; i < encoded.length; i++) {
			switch (encoded[i]) {
				case '+':
					encoded[i] = '-';
					continue;
				case '=':
					encoded[i] = '_';
					continue;
				case '/':
					encoded[i] = '~';
					continue;
				default:
					continue;
			}
		}
		return new String(encoded, StandardCharsets.UTF_8);
	}
}