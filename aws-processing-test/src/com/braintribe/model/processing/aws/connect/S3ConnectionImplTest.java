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
package com.braintribe.model.processing.aws.connect;

import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.braintribe.model.aws.deployment.S3Region;
import com.braintribe.model.processing.aws.service.AwsTestCredentials;
import com.braintribe.utils.RandomTools;

public class S3ConnectionImplTest {

	private static S3ConnectionImpl connection;
	private final static String bucketName = "playground-rku";

	@BeforeClass
	public static void beforeClass() {
		com.braintribe.model.aws.deployment.S3Connector deployable = com.braintribe.model.aws.deployment.S3Connector.T.create();
		deployable.setRegion(S3Region.eu_central_1);
		deployable.setAwsAccessKey(AwsTestCredentials.getAccessKey());
		deployable.setAwsSecretAccessKey(AwsTestCredentials.getSecretAccessKey());

		connection = new S3ConnectionImpl();
		connection.setS3ConnectorDeployable(deployable);
		// connection.setHttpConnectionPoolSize(10);
	}

	@AfterClass
	public static void shutdown() throws Exception {
		connection.preDestroy();
	}

	@Test
	public void testBinaryProcessorGet() throws Exception {
		String key = "test/" + RandomTools.newStandardUuid() + ".txt";
		byte[] bytes = ("Hello, world! from " + S3ConnectionImpl.class.getName()).getBytes(StandardCharsets.UTF_8);
		InputStream in = new ByteArrayInputStream(bytes);

		connection.uploadFile(bucketName, key, in, (long) bytes.length, "text/plain");
		String url = connection.generatePresignedUrl(bucketName, key, 15000l);
		System.out.println("Generated presigned URL: " + url);

		connection.deleteFile(bucketName, key);
	}

	@Test
	public void testInvalidCredentials() throws Exception {
		com.braintribe.model.aws.deployment.S3Connector deployable = com.braintribe.model.aws.deployment.S3Connector.T.create();
		deployable.setRegion(S3Region.eu_central_1);
		deployable.setAwsAccessKey(AwsTestCredentials.getAccessKey());
		deployable.setAwsSecretAccessKey("<not existing>");

		S3ConnectionImpl failConnection = new S3ConnectionImpl();
		failConnection.setS3ConnectorDeployable(deployable);
		try {
			Set<String> list = failConnection.getBucketsList();
			fail("Received unexpected list of buckets: " + list);
		} catch (Exception expected) {
			System.out.println("Received expected exception: " + expected.getMessage());
		}
	}
}
