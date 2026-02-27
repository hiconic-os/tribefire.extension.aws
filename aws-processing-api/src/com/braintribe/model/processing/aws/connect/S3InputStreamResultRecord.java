package com.braintribe.model.processing.aws.connect;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import com.braintribe.model.generic.session.InputStreamProvider;

public record S3InputStreamResultRecord(InputStreamProvider inputStreamProvider, boolean notModified, Long size, String md5, String contentType, Date createdAt) 
	implements S3InputStreamResult {
	@Override
	public InputStream openStream() throws IOException {
		return inputStreamProvider.openInputStream();
	}
}
