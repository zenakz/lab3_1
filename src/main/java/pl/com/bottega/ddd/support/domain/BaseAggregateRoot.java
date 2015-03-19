package pl.com.bottega.ddd.support.domain;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sharedkernel.exceptions.DomainOperationException.DomainOperationException;

public class BaseAggregateRoot {
	public static enum AggregateStatus {
		ACTIVE, ARCHIVE
	}

	protected Id aggregateId;
	private AggregateStatus aggregateStatus = AggregateStatus.ACTIVE;

	public void markAsRemoved() {
		aggregateStatus = AggregateStatus.ARCHIVE;
	}

	public Id getId() {
		return aggregateId;
	}

	public boolean isRemoved() {
		return aggregateStatus == AggregateStatus.ARCHIVE;
	}

	protected void domainError(String message) {
		throw new DomainOperationException(aggregateId, message);
	}
}
