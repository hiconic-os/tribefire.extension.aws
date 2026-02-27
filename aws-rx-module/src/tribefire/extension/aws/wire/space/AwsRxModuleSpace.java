package tribefire.extension.aws.wire.space;

import static com.braintribe.gm.model.reason.UnsatisfiedMaybeTunneling.getOrTunnel;

import com.braintribe.gm.model.reason.Maybe;
import com.braintribe.model.aws.resource.S3Source;
import com.braintribe.model.processing.aws.connect.S3Connector;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;

import hiconic.rx.module.api.wire.RxModuleContract;
import hiconic.rx.module.api.wire.RxPlatformConfigurator;
import hiconic.rx.module.api.wire.RxPlatformContract;
import tribefire.extension.aws.model.configuration.AwsConfiguration;
import tribefire.extension.aws.model.configuration.AwsConnection;
import tribefire.extension.aws.model.configuration.S3ResourceStorage;
import tribefire.extension.aws.processing.AwsConnections;
import tribefire.extension.aws.processing.S3ConnectionImpl;

/**
 * This module's javadoc is yet to be written.
 */
@Managed
public class AwsRxModuleSpace implements RxModuleContract {

	@Import
	private RxPlatformContract platform;
	
	@Override
	public void configurePlatform(RxPlatformConfigurator configurator) {
		configurator.registerResourceStorageDeploymentExpert(S3ResourceStorage.T, S3Source.T, config -> Maybe.complete(deployResourceStorage(config)));
	}
	
	@Managed
	private AwsConnections connections() {
		AwsConnections bean = new AwsConnections();
		
		AwsConfiguration awsConfiguration = platform.readConfig(AwsConfiguration.T).get();
		
		for (AwsConnection connectionConfig: awsConfiguration.getConnections()) {
			S3Connector connection = deployConnection(connectionConfig);
			bean.registerConnection(connectionConfig.getConnectionId(), connection);
		}
		
		return bean;
	}

	@Managed
	private tribefire.extension.aws.processing.S3ResourceStorage deployResourceStorage(S3ResourceStorage config) {
		var bean = new tribefire.extension.aws.processing.S3ResourceStorage();
		bean.setS3Connection(getOrTunnel(connections().getConnection(config.getAwsConnectionId())));
		bean.setStorageId(config.getStorageId());
		bean.setBucketName(config.getBucketName());
		bean.setKeyPrefix(config.getKeyPrefix());
		
		var cacheOptions = config.getCacheOptions();
		
		if (cacheOptions != null) {
			bean.setCacheType(cacheOptions.getType());
			bean.setCacheMustRevalidate(cacheOptions.getMustRevalidate());
			bean.setCacheMaxAge(cacheOptions.getMaxAge());
		}
		
		return bean;
	}
	
	@Managed
	private S3ConnectionImpl deployConnection(AwsConnection config) {
		S3ConnectionImpl bean = new S3ConnectionImpl();
		bean.setAwsAccessKey(config.getAwsAccessKeyId());
		bean.setAwsSecretAccessKey(config.getAwsSecretAccessKey());
		bean.setRegion(config.getRegion().replace('_', '-'));
		bean.setConnectionAcquisitionTimeout(config.getConnectionAcquisitionTimeout());
		bean.setConnectionTimeout(config.getConnectionTimeout());
		bean.setHttpConnectionPoolSize(config.getHttpConnectionPoolSize());
		bean.setSocketTimeout(config.getSocketTimeout());
		bean.setStreamingPoolSize(config.getStreamingPoolSize());
		bean.setUrlOverride(config.getUrlOverride());
		return bean;
	}

}