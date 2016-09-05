package org.ngam.app;

import org.junit.*;

class BillingServiceImplTest extends TestCase {
	private final int orderAmount=100;
	private final PizzaOrder order = new PizzaOrder(orderAmount);
	private final CreditCard creditCard = new CreditCard("1234",11,2010);

	private final InMemoryTransactionLog transLog = new InMemoryTransactionLog();
	private final MockCreditCardProcessor processor = new MockCreditCardProcessor();

	@Override
	public void setUp() {
		TransactionLogFactory.setInstance(transLog);
		CreditCardProcessorFactory.setInstance(processor);
	}
	@Override
	public void tearDown() {
		TransactionLogFactory.setInstance(null);
		CreditCardProcessorFactory.setInstance(null);
	}
	public void testSuccessfulChange() {
		BillingServiceImpl billingService = new BillingServiceImpl();
		Receipt receipt = billingService.chargeOrder(order,creditCard);

		assertTrue(receipt.hasSuccessfulCharge());
		assertEquals(orderAmount,receipt.getChargeAmount());
		assertEquals(creditCard,processor.getCardOfOnlyCharge());
		assertEquals(orderAmount,processor.getAmountOfOnlyCharge());
		assertTrue(transLog.wasSuccessLogged());
	}
}
