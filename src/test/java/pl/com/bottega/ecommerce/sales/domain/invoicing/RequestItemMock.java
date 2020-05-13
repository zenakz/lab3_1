package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class RequestItemMock {
    static Product product = new Product(Id.generate(), Money.ZERO, "bread", ProductType.FOOD);

    public RequestItem getItem() {
        return new RequestItem(product.generateSnapshot(), 1, Money.ZERO);
    }

}
