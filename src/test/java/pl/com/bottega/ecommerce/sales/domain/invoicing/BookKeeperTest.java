package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookKeeperTest {

    private static BookKeeper bookKeeper;
    private InvoiceRequestMock invoiceRequest;
    private static RequestItemMock requestItemMock;

    @Mock private static TaxPolicy taxPolicy;

    @BeforeAll public static void start() {
        requestItemMock = new RequestItemMock();
    }

    @BeforeEach public void prep() {
        invoiceRequest = new InvoiceRequestMock();
        taxPolicy = mock(TaxPolicy.class);
        bookKeeper = new BookKeeper(new InvoiceFactory());
        Mockito.when(taxPolicy.calculateTax(Mockito.any(), Mockito.any())).thenReturn(new Tax(Money.ZERO, ""));
    }

    @Test void oneItemRequestShouldReturnOneItem() {
        invoiceRequest.addItem(requestItemMock.getItem());
        Invoice invoice = bookKeeper.issuance(invoiceRequest.build(), taxPolicy);
        assertEquals(1, invoice.getItems().size());
    }

    @Test public void noItemsRequestShouldReturnNoItems() {
        Invoice invoice = bookKeeper.issuance(invoiceRequest.build(), taxPolicy);
        assertEquals(0, invoice.getItems().size());
    }

    @Test public void fourItemRequestShouldReturnFourItems() {
        invoiceRequest.addItem(requestItemMock.getItem());
        invoiceRequest.addItem(requestItemMock.getItem());
        invoiceRequest.addItem(requestItemMock.getItem());
        invoiceRequest.addItem(requestItemMock.getItem());

        Invoice invoice = bookKeeper.issuance(invoiceRequest.build(), taxPolicy);
        assertEquals(4, invoice.getItems().size());
    }

    @Test public void twoItemRequestShouldCallCalculateTwice() {
        invoiceRequest.addItem(requestItemMock.getItem());
        invoiceRequest.addItem(requestItemMock.getItem());
        bookKeeper.issuance(invoiceRequest.build(), taxPolicy);

        verify(taxPolicy, times(2)).calculateTax(any(ProductType.class), any(Money.class));
    }

    @Test public void issuanceWithNullArgumentsThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> bookKeeper.issuance(null, null));
    }

    @Test void requestWithNoItemsShouldNotCallCalculate() {
        invoiceRequest.build();
        bookKeeper.issuance(invoiceRequest.build(), taxPolicy);
        verify(taxPolicy, times(0)).calculateTax(any(), any());
    }
}
