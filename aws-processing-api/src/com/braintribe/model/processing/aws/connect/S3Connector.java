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

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.braintribe.model.aws.api.ConnectionStatistics;

public interface S3Connector {

	Set<String> getBucketsList();

	List<String> getFilesList(String bucketName);

	void uploadFile(String bucketName, String key, InputStream in, Long fileSize, String contentType);

	void downloadFile(String bucketName, String key, OutputStream os, Long start, Long end);

	InputStream openStream(String bucketName, String key, Long start, Long end);

	void deleteFile(String bucketName, String key);

	Map<String, ConnectionStatistics> getStatisticsPerRegion();

	String generatePresignedUrl(String bucketName, String key, long ttlInMs);
}
