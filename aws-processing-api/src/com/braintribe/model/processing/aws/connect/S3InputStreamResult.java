package com.braintribe.model.processing.aws.connect;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public interface S3InputStreamResult {
	InputStream openStream() throws IOException;
	boolean notModified();
	Long size();
	String md5();
	String contentType();
	Date createdAt();
}
