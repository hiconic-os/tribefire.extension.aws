package tribefire.extension.aws.processing;

import java.util.List;

import com.braintribe.model.check.service.CheckRequest;
import com.braintribe.model.processing.service.api.ServiceRequestContext;

import hiconic.rx.check.api.CheckProcessor;
import hiconic.rx.check.model.result.CheckResult;
import hiconic.rx.check.model.result.CheckResultEntry;
import hiconic.rx.check.model.result.CheckStatus;

public class CheckProcessorAdapter implements CheckProcessor {
	private com.braintribe.model.processing.check.api.CheckProcessor checkProcessor;
	private static CheckRequest dummyRequest = CheckRequest.T.create();

	public CheckProcessorAdapter(com.braintribe.model.processing.check.api.CheckProcessor checkProcessor) {
		super();
		this.checkProcessor = checkProcessor;
	}
	
	@Override
	public CheckResult check(ServiceRequestContext context) {
		com.braintribe.model.check.service.CheckResult result = checkProcessor.check(context, dummyRequest);
		
		CheckResult adaptedResult = CheckResult.T.create();
		adaptedResult.setElapsedTimeInMs(result.getElapsedTimeInMs());
		List<CheckResultEntry> adaptedEntries = adaptedResult.getEntries();
		
		for (com.braintribe.model.check.service.CheckResultEntry entry: result.getEntries()) {
			CheckResultEntry adaptedEntry = CheckResultEntry.T.create();
			
			adaptedEntry.setCheckStatus(adaptStatus(entry.getCheckStatus()));
			adaptedEntry.setDetails(entry.getDetails());
			adaptedEntry.setDetailsAsMarkdown(entry.getDetailsAsMarkdown());
			adaptedEntry.setMessage(entry.getMessage());
			adaptedEntry.setName(entry.getName());
			
			adaptedEntries.add(adaptedEntry);
		}
		
		return adaptedResult;
	}
	
	private CheckStatus adaptStatus(com.braintribe.model.check.service.CheckStatus status) {
		return switch (status) {
		case ok -> CheckStatus.ok;
		case warn -> CheckStatus.warn;
		case fail -> CheckStatus.fail;
		case null -> null;
		};
	}

}
