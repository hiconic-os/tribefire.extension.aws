package tribefire.extension.aws.processing;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.braintribe.gm.model.reason.Maybe;
import com.braintribe.gm.model.reason.Reasons;
import com.braintribe.gm.model.reason.essential.NotFound;
import com.braintribe.model.processing.aws.connect.S3Connector;

public class AwsConnections {
	private Map<String, S3Connector> connections = new ConcurrentHashMap<>();
	
	public void registerConnection(String name, S3Connector connection) {
		connections.put(name, connection);
	}
	
	public S3Connector findConnection(String name) {
		return connections.get(name);
	}
	
	public Maybe<S3Connector> getConnection(String name) {
		S3Connector connection = findConnection(name);
		
		if (connection == null)
			return Reasons.build(NotFound.T).text("Could not find AwsConnection " + name).toMaybe();
		
		return Maybe.complete(connection);
	}
}
