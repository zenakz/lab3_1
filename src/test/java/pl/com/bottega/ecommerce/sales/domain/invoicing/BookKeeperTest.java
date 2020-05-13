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
    private static InvoiceRequest invoiceRequest;

    @Mock
    private static TaxPolicy taxPolicy=mock(TaxPolicy.class);
    @BeforeAll
    public static void start(){
        bookKeeper = new BookKeeper(new InvoiceFactory());
        money = new Money(1, Money.DEFAULT_CURRENCY);
        product = new Product(Id.generate(), money, "product", ProductType.STANDARD);
        Mockito.when(taxPolicy.calculateTax(Mockito.any(), Mockito.any())).thenReturn(new Tax(Money.ZERO, ""));
    }

    @BeforeEach
    public void prep(){
        invoiceRequest = new InvoiceRequest(clientData);
        clientData = new ClientData(Id.generate(), "");

    }

    @Test
    void oneItemRequestShouldReturnOneItem(){
        RequestItem requestItem = new RequestItem(product.generateSnapshot(), 1, money);
        invoiceRequest.add(requestItem);
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        assertEquals(1, invoice.getItems().size());
    }

    @Test
    public void twoItemRequestShouldCallCalculateTwice() {
        RequestItem requestItem = new RequestItem(product.generateSnapshot(), 1, money);
        invoiceRequest.add(requestItem);
        invoiceRequest.add(requestItem);
        bookKeeper.issuance(invoiceRequest, taxPolicy);

        verify(taxPolicy, times(2)).calculateTax(any(ProductType.class), any(Money.class));
    }
}
