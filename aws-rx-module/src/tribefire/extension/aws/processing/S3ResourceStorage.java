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
package tribefire.extension.aws.processing;

import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.UUID;

import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.Required;
import com.braintribe.gm.model.reason.Maybe;
import com.braintribe.gm.model.reason.Reasons;
import com.braintribe.gm.model.reason.essential.InvalidArgument;
import com.braintribe.gm.model.reason.essential.IoError;
import com.braintribe.logging.Logger;
import com.braintribe.model.aws.resource.S3Source;
import com.braintribe.model.cache.CacheControl;
import com.braintribe.model.processing.aws.connect.S3Connector;
import com.braintribe.model.processing.aws.connect.S3InputStreamResult;
import com.braintribe.model.resource.Resource;
import com.braintribe.model.resourceapi.stream.condition.StreamCondition;
import com.braintribe.model.resourceapi.stream.range.StreamRange;
import com.braintribe.utils.FileTools;
import com.braintribe.utils.lcd.StopWatch;
import com.braintribe.utils.paths.UniversalPath;

import hiconic.rx.module.api.resource.AbstractResourceStorage;
import hiconic.rx.module.api.resource.ResourceStorage;
import hiconic.rx.resource.model.api.DeleteResourcePayload;
import hiconic.rx.resource.model.api.DeleteResourcePayloadResponse;
import hiconic.rx.resource.model.api.GetResourcePayload;
import hiconic.rx.resource.model.api.GetResourcePayloadResponse;
import hiconic.rx.resource.model.api.StoreResourcePayload;
import hiconic.rx.resource.model.api.StoreResourcePayloadResponse;

public class S3ResourceStorage extends AbstractResourceStorage<S3InputStreamResult> implements ResourceStorage {

	private static Logger logger = Logger.getLogger(S3ResourceStorage.class);

	private S3Connector s3Connection = null;
	private String bucketName;
	private String pathPrefix = null;

	@Required
	public void setS3Connection(S3Connector s3Connection) {
		this.s3Connection = s3Connection;
	}
	@Required
	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}
	
	@Configurable
	public void setKeyPrefix(String pathPrefix) {
		if (pathPrefix != null && (pathPrefix.startsWith("/") || pathPrefix.endsWith("/")))
			throw new IllegalArgumentException("pathPrefix [" + pathPrefix + "] must not start or end with /");
		this.pathPrefix = pathPrefix;
	}
	
	@Override
	public String storageId() {
		return "s3-storage";
	}
	
	@Override
	protected Maybe<S3InputStreamResult> resolvePayload(GetResourcePayload request) {

		final S3Source source = (S3Source)request.getResourceSource();
		final StreamRange streamRange = request.getRange();
		final StreamCondition streamCondition = request.getCondition();

		String key = source.getKey();
		String bucketName = source.getBucketName();
		if (bucketName == null) {
			bucketName = this.bucketName;
		}

		Long start = streamRange != null ? streamRange.getStart() : null;
		Long end = streamRange != null ? streamRange.getEnd() : null;
		
		logger.debug(() -> "Opening " + key);
		return s3Connection.openStreamEx(bucketName, key, start, end, streamCondition);
	}
	
	@Override
	protected boolean matchesCondition(S3InputStreamResult payload, CacheControl cacheControl,
			StreamCondition streamCondition) {
		return !payload.notModified();
	}
	
	@Override
	protected Maybe<GetResourcePayloadResponse> getPayload(S3InputStreamResult payload, GetResourcePayload request,
			GetResourcePayloadResponse response) throws UncheckedIOException {

		S3Source source = (S3Source) request.getResourceSource();
		
		Resource resource = Resource.createTransient(payload::openStream);
		resource.setName(source.getBucketName() +  "/" + source.getKey());
		resource.setMimeType(payload.contentType());
		resource.setFileSize(payload.size());
		resource.setMd5(payload.md5());
		resource.setCreated(payload.createdAt());
		response.setResource(resource);

		return Maybe.complete(response);
	}
	
	@Override
	protected Maybe<StoreResourcePayloadResponse> storePayload(StoreResourcePayload request)
			throws UncheckedIOException {

		Resource resource = request.getData();
		StopWatch stopWatch = new StopWatch();

		String key = generateKey(resource);
		
		stopWatch.intermediate("Preparation");

		logger.debug(() -> "Storing " + key);

		stopWatch.intermediate("UploadInformation Gathered");

		try (InputStream in = resource.openStream()) {
			s3Connection.uploadFile(bucketName, key, in, resource.getFileSize(), resource.getMimeType());
		} catch (Exception e) {
	    	String msg = "Could not upload resource (tracebackId=" + UUID.randomUUID().toString() + ")";
	    	logger.error(msg, e);
	    	return Reasons.build(IoError.T).text(msg).toMaybe();
		}

		stopWatch.intermediate("Transfer");

		final S3Source resourceSource = S3Source.T.create();
		resourceSource.setBucketName(bucketName);
		resourceSource.setKey(key);

		final StoreResourcePayloadResponse response = StoreResourcePayloadResponse.T.create();
		response.setResourceSource(resourceSource);

		stopWatch.intermediate("Result Created");

		logger.debug(() -> "store: " + stopWatch);

		
		return Maybe.complete(response);
	}

	/**
	 * Generate a unique and valid S3 resource key (max 1024 chars, only legal chars) from a resource
	 * optionally using its id and name as key parts if present.
	 *  
	 * Example key:
	 * <p>
	 * prefix/2026/02/19/14/8d87e991-cef9-4f68-b752-223f3f631fe2/8d87e991-cef9-4f68-b752-223f3f631fe2/contract.pdf
	 */
	private String generateKey(Resource resource) {
		String shardingPart = S3Tools.newDateStructuredIndividualShardingPath();
		
		String uniquePart = UUID.randomUUID().toString(); 
		
		UniversalPath path = UniversalPath.empty();

		if (pathPrefix != null)
			path = path.pushSlashPath(pathPrefix);
		
		path = path.push(shardingPart).push(uniquePart);
		
		if (resource.getId() != null)
			path = path.push(resource.getId().toString());
		
		if (resource.getName() != null)
			path = path.push(resource.getName());
		
		String key = FileTools.truncateFilenameByUtf8BytesLength(S3Tools.sanitizeKey(path.toSlashPath()), 1024);
		return key;
	}

	@Override
	protected Maybe<DeleteResourcePayloadResponse> deletePayload(DeleteResourcePayload request)
			throws UncheckedIOException {
		final S3Source source = (S3Source) request.getResourceSource();

		String key = source.getKey();
		String bucketName = source.getBucketName();

		if (bucketName == null) {
			bucketName = this.bucketName;
		}
		
		if (key == null)
			return Reasons.build(InvalidArgument.T).text("S3Source.key must not be null").toMaybe();

		logger.debug(() -> "Deleting " + key);

		s3Connection.deleteFile(bucketName, key);

		final DeleteResourcePayloadResponse response = DeleteResourcePayloadResponse.T.create();
		response.setDeleted(true);
		return Maybe.complete(response);
	}
}
