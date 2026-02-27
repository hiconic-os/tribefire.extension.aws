package tribefire.extension.aws.processing.api;

import com.braintribe.gm.model.reason.Maybe;

@FunctionalInterface
public interface AwsConnectionResolver {
	Maybe<AwsConnection> resolveConnection(String accessId);
}
