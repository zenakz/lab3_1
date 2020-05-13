package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;

public class InvoiceRequestMock {

    private ClientData clientData = null;
    private ArrayList<RequestItem> list;

    InvoiceRequestMock() {
        list = new ArrayList<>();
    }

    public InvoiceRequestMock setClient(ClientData clientData) {
        this.clientData = clientData;
        return this;
    }

    public void addItem(RequestItem item) {
        list.add(item);
    }

    public InvoiceRequest build() {
        if (clientData == null) {
            clientData = mock(ClientData.class);
        }
        InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);

        for (RequestItem item : list) {
            invoiceRequest.add(item);
        }

        return invoiceRequest;
    }
}
