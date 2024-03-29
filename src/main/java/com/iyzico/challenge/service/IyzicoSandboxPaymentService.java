package com.iyzico.challenge.service;

import com.iyzico.challenge.exception.PaymentException;
import com.iyzico.challenge.repository.PaymentRepository;
import com.iyzipay.Options;
import com.iyzipay.model.*;
import com.iyzipay.request.CreatePaymentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Primary
@ConditionalOnProperty(prefix = "sandbox", name = "enabled")
public class IyzicoSandboxPaymentService implements PaymentService {

    @Value("${sandbox.apiKey}")
    public String apiKey;

    @Value("${sandbox.secretKey}")
    public String secretKey;

    @Value("${sandbox.url}")
    public String baseUrl;

    private PaymentRepository paymentRepository;

    private Options options;
    private CreatePaymentRequest request;

    public IyzicoSandboxPaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public void pay(BigDecimal price) {
        request.setPrice(price);
        request.setPaidPrice(price);
        request.getBasketItems().get(0).setPrice(price);
        com.iyzipay.model.Payment response = com.iyzipay.model.Payment.create(request, options);

        com.iyzico.challenge.entity.Payment payment = new com.iyzico.challenge.entity.Payment();
        payment.setBankResponse(response.getStatus());
        payment.setPrice(price);
        paymentRepository.save(payment);

        if ("failure".equals(response.getStatus())) {
            throw new PaymentException(response.getErrorCode() + "-" + response.getErrorMessage());
        }
    }

    @PostConstruct
    public void init() {
        options = new Options();
        options.setApiKey(apiKey);
        options.setSecretKey(secretKey);
        options.setBaseUrl(baseUrl);

        request = new CreatePaymentRequest();
        request.setLocale(Locale.TR.getValue());
        request.setConversationId("123456789");
        request.setCurrency(Currency.TRY.name());
        request.setInstallment(1);
        request.setBasketId("B67832");
        request.setPaymentChannel(PaymentChannel.WEB.name());
        request.setPaymentGroup(PaymentGroup.PRODUCT.name());

        PaymentCard paymentCard = new PaymentCard();
        paymentCard.setCardHolderName("John Doe");
        paymentCard.setCardNumber("5528790000000008");
        paymentCard.setExpireMonth("12");
        paymentCard.setExpireYear("2030");
        paymentCard.setCvc("123");
        paymentCard.setRegisterCard(0);
        request.setPaymentCard(paymentCard);

        Buyer buyer = new Buyer();
        buyer.setId("BY789");
        buyer.setName("John");
        buyer.setSurname("Doe");
        buyer.setGsmNumber("+905350000000");
        buyer.setEmail("email@email.com");
        buyer.setIdentityNumber("74300864791");
        buyer.setLastLoginDate("2015-10-05 12:43:35");
        buyer.setRegistrationDate("2013-04-21 15:12:09");
        buyer.setRegistrationAddress("Nidakule Göztepe, Merdivenköy Mah. Bora Sok. No:1");
        buyer.setIp("85.34.78.112");
        buyer.setCity("Istanbul");
        buyer.setCountry("Turkey");
        buyer.setZipCode("34732");
        request.setBuyer(buyer);

        Address shippingAddress = new Address();
        shippingAddress.setContactName("Jane Doe");
        shippingAddress.setCity("Istanbul");
        shippingAddress.setCountry("Turkey");
        shippingAddress.setAddress("Nidakule Göztepe, Merdivenköy Mah. Bora Sok. No:1");
        shippingAddress.setZipCode("34742");
        request.setShippingAddress(shippingAddress);

        Address billingAddress = new Address();
        billingAddress.setContactName("Jane Doe");
        billingAddress.setCity("Istanbul");
        billingAddress.setCountry("Turkey");
        billingAddress.setAddress("Nidakule Göztepe, Merdivenköy Mah. Bora Sok. No:1");
        billingAddress.setZipCode("34742");
        request.setBillingAddress(billingAddress);

        List<BasketItem> basketItems = new ArrayList<BasketItem>();
        BasketItem firstBasketItem = new BasketItem();
        firstBasketItem.setId("BI101");
        firstBasketItem.setName("Binocular");
        firstBasketItem.setCategory1("Collectibles");
        firstBasketItem.setCategory2("Accessories");
        firstBasketItem.setItemType(BasketItemType.PHYSICAL.name());
        basketItems.add(firstBasketItem);
        request.setBasketItems(basketItems);
    }
}
