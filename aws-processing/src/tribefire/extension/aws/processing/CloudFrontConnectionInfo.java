package tribefire.extension.aws.processing;

public record CloudFrontConnectionInfo(String baseUrl, String keyGroupId, String privateKey, String publicKey, String publicKeyPem) {
}
