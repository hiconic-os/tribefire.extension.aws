package tribefire.extension.aws.model.configuration;

import java.util.Set;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

public interface AwsConfiguration extends GenericEntity {

	EntityType<AwsConfiguration> T = EntityTypes.T(AwsConfiguration.class);

	String connections = "connections";
	
	Set<AwsConnection> getConnections();
	void setConnections(Set<AwsConnection> s3Connections);
}
