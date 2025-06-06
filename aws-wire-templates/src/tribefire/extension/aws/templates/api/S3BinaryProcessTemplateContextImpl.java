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
package tribefire.extension.aws.templates.api;

import java.util.function.Function;

import com.braintribe.model.aws.deployment.S3Region;
import com.braintribe.model.deployment.Module;
import com.braintribe.model.descriptive.HasExternalId;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.utils.StringTools;
import com.braintribe.wire.api.scope.InstanceConfiguration;
import com.braintribe.wire.api.scope.InstanceQualification;

public class S3BinaryProcessTemplateContextImpl implements S3BinaryProcessTemplateContext, S3BinaryProcessTemplateContextBuilder {

	private String idPrefix;
	private String bucketName;
	private String pathPrefix;

	private Function<EntityType<?>, GenericEntity> entityFactory = EntityType::create;
	private String awsAccessKey;
	private String awsSecretAccessKey;
	private S3Region region;
	private String name;
	private Function<String, ? extends GenericEntity> lookupFunction;
	private Module awsModule;
	private Integer httpConnectionPoolSize;
	private Long connectionAcquisitionTimeout;
	private Long connectionTimeout;
	private Long socketTimeout;
	private String urlOverride;
	private String cloudFrontBaseUrl;
	private String cloudFrontPrivateKey;
	private String cloudFrontPublicKey;
	private String cloudFrontKeyGroupId;

	@Override
	public S3BinaryProcessTemplateContext build() {
		return this;
	}

	@Override
	public S3BinaryProcessTemplateContextBuilder setIdPrefix(String idPrefix) {
		this.idPrefix = idPrefix;
		return this;
	}

	@Override
	public S3BinaryProcessTemplateContextBuilder setName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public S3BinaryProcessTemplateContextBuilder setAwsAccessKey(String awsAccessKey) {
		this.awsAccessKey = awsAccessKey;
		return this;
	}

	@Override
	public S3BinaryProcessTemplateContextBuilder setAwsSecretAccessKey(String awsSecretAccessKey) {
		this.awsSecretAccessKey = awsSecretAccessKey;
		return this;
	}

	@Override
	public S3BinaryProcessTemplateContextBuilder setRegion(S3Region region) {
		this.region = region;
		return this;
	}

	@Override
	public S3BinaryProcessTemplateContextBuilder setPathPrefix(String pathPrefix) {
		this.pathPrefix = pathPrefix;
		return this;
	}

	@Override
	public S3BinaryProcessTemplateContextBuilder setBucketName(String bucketName) {
		this.bucketName = bucketName;
		return this;
	}

	@Override
	public String getIdPrefix() {
		return idPrefix;
	}

	@Override
	public String getAwsAccessKey() {
		return awsAccessKey;
	}

	@Override
	public String getAwsSecretAccessKey() {
		return awsSecretAccessKey;
	}

	@Override
	public S3Region getRegion() {
		return region;
	}

	@Override
	public String getBucketName() {
		return bucketName;
	}

	@Override
	public String getPathPrefix() {
		return pathPrefix;
	}

	@Override
	public S3BinaryProcessTemplateContextBuilder setAwsModule(Module awsModule) {
		this.awsModule = awsModule;
		return this;
	}

	@Override
	public Module getAwsModule() {
		return this.awsModule;
	}

	@Override
	public int hashCode() {
		return idPrefix.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof S3BinaryProcessTemplateContext) {
			return ((S3BinaryProcessTemplateContext) obj).getIdPrefix().equals(this.idPrefix);
		}
		return super.equals(obj);
	}

	@Override
	public S3BinaryProcessTemplateContextBuilder setEntityFactory(Function<EntityType<?>, GenericEntity> entityFactory) {
		this.entityFactory = entityFactory;
		return this;
	}

	@Override
	public <T extends GenericEntity> T create(EntityType<T> entityType, InstanceConfiguration instanceConfiguration) {

		T entity = (T) entityFactory.apply(entityType);

		if (idPrefix == null) {
			throw new IllegalStateException("You have to specify a idPrefix.");
		}

		InstanceQualification qualification = instanceConfiguration.qualification();

		String globalId = "wire://" + idPrefix + "/" + qualification.space().getClass().getSimpleName() + "/" + qualification.name();

		entity.setGlobalId(globalId);

		if (entity instanceof HasExternalId) {
			HasExternalId eid = (HasExternalId) entity;

			String externalId = StringTools.camelCaseToDashSeparated(entityType.getShortName()) + "." + StringTools.camelCaseToDashSeparated(idPrefix)
					+ "." + StringTools.camelCaseToDashSeparated(qualification.space().getClass().getSimpleName()) + "."
					+ StringTools.camelCaseToDashSeparated(qualification.name());

			eid.setExternalId(externalId);
		}

		return entity;
	}

	@Override
	public S3BinaryProcessTemplateContextBuilder setLookupFunction(Function<String, ? extends GenericEntity> lookupFunction) {
		this.lookupFunction = lookupFunction;
		return this;
	}

	@Override
	public <T extends GenericEntity> T lookup(String globalId) {
		return (T) lookupFunction.apply(globalId);
	}

	@Override
	public S3BinaryProcessTemplateContextBuilder setHttpConnectionPoolSize(Integer httpConnectionPoolSize) {
		this.httpConnectionPoolSize = httpConnectionPoolSize;
		return this;
	}

	@Override
	public Integer getHttpConnectionPoolSize() {
		return httpConnectionPoolSize;
	}

	@Override
	public S3BinaryProcessTemplateContextBuilder setConnectionAcquisitionTimeout(Long connectionAcquisitionTimeout) {
		this.connectionAcquisitionTimeout = connectionAcquisitionTimeout;
		return this;
	}

	@Override
	public S3BinaryProcessTemplateContextBuilder setConnectionTimeout(Long connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
		return this;
	}

	@Override
	public S3BinaryProcessTemplateContextBuilder setSocketTimeout(Long socketTimeout) {
		this.socketTimeout = socketTimeout;
		return this;
	}

	@Override
	public Long getConnectionAcquisitionTimeout() {
		return connectionAcquisitionTimeout;
	}

	@Override
	public Long getConnectionTimeout() {
		return connectionTimeout;
	}

	@Override
	public Long getSocketTimeout() {
		return socketTimeout;
	}

	@Override
	public S3BinaryProcessTemplateContextBuilder setUrlOverride(String urlOverride) {
		this.urlOverride = urlOverride;
		return this;
	}

	@Override
	public String getUrlOverride() {
		return this.urlOverride;
	}

	@Override
	public S3BinaryProcessTemplateContextBuilder setCloudFrontBaseUrl(String cloudFrontBaseUrl) {
		this.cloudFrontBaseUrl = cloudFrontBaseUrl;
		return this;
	}

	@Override
	public S3BinaryProcessTemplateContextBuilder setCloudFrontPrivateKey(String cloudFrontPrivateKey) {
		this.cloudFrontPrivateKey = cloudFrontPrivateKey;
		return this;
	}

	@Override
	public S3BinaryProcessTemplateContextBuilder setCloudFrontPublicKey(String cloudFrontPublicKey) {
		this.cloudFrontPublicKey = cloudFrontPublicKey;
		return this;
	}

	@Override
	public S3BinaryProcessTemplateContextBuilder setCloudFrontKeyGroupId(String cloudFrontKeyGroupId) {
		this.cloudFrontKeyGroupId = cloudFrontKeyGroupId;
		return this;
	}

	@Override
	public String getCloudFrontBaseUrl() {
		return cloudFrontBaseUrl;
	}

	@Override
	public String getCloudFrontPrivateKey() {
		return cloudFrontPrivateKey;
	}

	@Override
	public String getCloudFrontPublicKey() {
		return cloudFrontPublicKey;
	}

	@Override
	public String getCloudFrontKeyGroupId() {
		return cloudFrontKeyGroupId;
	}

}
