package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookKeeperTest {
    private static BookKeeper bookKeeper;
    private static Money money;
    private static Product product;
    private static ClientData clientData;
    private static RequestItemMock requestItemMock;
    private static TaxPolicy taxPolicy=mock(TaxPolicy.class);
    @BeforeAll
    public static void start(){
        money = new Money(1, Money.DEFAULT_CURRENCY);
        product = new Product(Id.generate(), money, "product", ProductType.STANDARD);
        Mockito.when(taxPolicy.calculateTax(Mockito.any(), Mockito.any())).thenReturn(new Tax(Money.ZERO, ""));
    }

    @BeforeEach
    public void prep(){
        bookKeeper = new BookKeeper(new InvoiceFactory());
        clientData = new ClientData(Id.generate(), "");
        requestItemMock = new RequestItemMock();
    }

    //state tests
    @Test
    void oneItemRequestShouldReturnOneItem(){
        InvoiceRequestMock invoiceRequest = new InvoiceRequestMock();
        invoiceRequest.addItem(requestItemMock.getItem());
        Invoice invoice = bookKeeper.issuance(invoiceRequest.build(), taxPolicy);
        assertEquals(1, invoice.getItems().size());
    }


    //behavior tests

    @Test
    public void twoItemRequestShouldCallCalculateTwice() {
        InvoiceRequestMock invoiceRequest = new InvoiceRequestMock();
        invoiceRequest.addItem(requestItemMock.getItem());
        invoiceRequest.addItem(requestItemMock.getItem());
        bookKeeper.issuance(invoiceRequest.build(), taxPolicy);

        verify(taxPolicy, times(2)).calculateTax(any(ProductType.class), any(Money.class));
    }

    @Test
    public void issuanceWithNullArgumentsThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                bookKeeper.issuance(null, null));
    }
}
