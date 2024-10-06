package org.example;

import com.github.javafaker.Faker;
import java.util.Date;
import java.util.Random;

public class Transaction {
  private String transactionId;
  private double amount;
  private Date date;
  private String description;
  private String accountNumber;
  private String currency;
  private String merchantName;
  private String merchantCategory;
  private String cardNumber;
  private String cardType;
  private String cardHolderName;
  private String bankName;
  private String bankBranch;
  private String country;
  private String city;
  private String state;
  private String zipCode;
  private String phoneNumber;
  private String email;
  private String ipAddress;
  private String deviceType;
  private String transactionStatus;
  private String failureReason;

  public void init() {

    Faker faker = new Faker();
    Random random = new Random();

    this.transactionId = faker.idNumber().valid();
    this.amount = 100 + (10000 - 100) * random.nextDouble();
    this.date = faker.date().past(30, java.util.concurrent.TimeUnit.DAYS);
    this.description = faker.commerce().productName();
    this.accountNumber = faker.finance().iban();
    this.currency = faker.currency().code();
    this.merchantName = faker.company().name();
    this.merchantCategory = faker.commerce().department();
    this.cardNumber = faker.finance().creditCard();
    this.cardType = faker.business().creditCardType();
    this.cardHolderName = faker.name().fullName();
    this.bankName = faker.company().name();
    this.bankBranch = faker.address().streetName();
    this.country = faker.address().country();
    this.city = faker.address().city();
    this.state = faker.address().state();
    this.zipCode = faker.address().zipCode();
    this.phoneNumber = faker.phoneNumber().phoneNumber();
    this.email = faker.internet().emailAddress();
    this.ipAddress = faker.internet().ipV4Address();
    this.deviceType = faker.internet().domainWord();
    this.transactionStatus = faker.options().option("Success", "Failed", "Pending");
    this.failureReason = faker.lorem().sentence();

  }

  // Empty constructor
  public Transaction() {
  }

  public Transaction(String transactionId, double amount, Date date, String description, String accountNumber,
      String currency, String merchantName, String merchantCategory, String cardNumber,
      String cardType, String cardHolderName, String bankName, String bankBranch, String country, String city,
      String state, String zipCode, String phoneNumber, String email, String ipAddress, String deviceType,
      String transactionStatus, String failureReason) {
    this.transactionId = transactionId;
    this.amount = amount;
    this.date = date;
    this.description = description;
    this.accountNumber = accountNumber;
    this.currency = currency;
    this.merchantName = merchantName;
    this.merchantCategory = merchantCategory;
    this.cardNumber = cardNumber;
    this.cardType = cardType;
    this.cardHolderName = cardHolderName;
    this.bankName = bankName;
    this.bankBranch = bankBranch;
    this.country = country;
    this.city = city;
    this.state = state;
    this.zipCode = zipCode;
    this.phoneNumber = phoneNumber;
    this.email = email;
    this.ipAddress = ipAddress;
    this.deviceType = deviceType;
    this.transactionStatus = transactionStatus;
    this.failureReason = failureReason;
  }

  @Override
  public String toString() {
    return "Transaction ID: " + transactionId + "\n" +
        "Amount: $" + amount + "\n" +
        "Date: " + date + "\n" +
        "Description: " + description + "\n" +
        "Account Number: " + accountNumber + "\n" +
        "Currency: " + currency + "\n" +
        "Merchant Name: " + merchantName + "\n" +
        "Merchant Category: " + merchantCategory + "\n" +
        "Card Number: " + cardNumber + "\n" +
        "Card Type: " + cardType + "\n" +
        "Card Holder Name: " + cardHolderName + "\n" +
        "Bank Name: " + bankName + "\n" +
        "Bank Branch: " + bankBranch + "\n" +
        "Country: " + country + "\n" +
        "City: " + city + "\n" +
        "State: " + state + "\n" +
        "Zip Code: " + zipCode + "\n" +
        "Phone Number: " + phoneNumber + "\n" +
        "Email: " + email + "\n" +
        "IP Address: " + ipAddress + "\n" +
        "Device Type: " + deviceType + "\n" +
        "Transaction Status: " + transactionStatus + "\n" +
        "Failure Reason: " + failureReason;
  }

  public static void main(String[] args) {
    Transaction transaction = new Transaction();
    System.out.println(transaction);
  }

  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getMerchantName() {
    return merchantName;
  }

  public void setMerchantName(String merchantName) {
    this.merchantName = merchantName;
  }

  public String getMerchantCategory() {
    return merchantCategory;
  }

  public void setMerchantCategory(String merchantCategory) {
    this.merchantCategory = merchantCategory;
  }

  public String getCardNumber() {
    return cardNumber;
  }

  public void setCardNumber(String cardNumber) {
    this.cardNumber = cardNumber;
  }

  public String getCardType() {
    return cardType;
  }

  public void setCardType(String cardType) {
    this.cardType = cardType;
  }

  public String getCardHolderName() {
    return cardHolderName;
  }

  public void setCardHolderName(String cardHolderName) {
    this.cardHolderName = cardHolderName;
  }

  public String getBankName() {
    return bankName;
  }

  public void setBankName(String bankName) {
    this.bankName = bankName;
  }

  public String getBankBranch() {
    return bankBranch;
  }

  public void setBankBranch(String bankBranch) {
    this.bankBranch = bankBranch;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getZipCode() {
    return zipCode;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }

  public String getDeviceType() {
    return deviceType;
  }

  public void setDeviceType(String deviceType) {
    this.deviceType = deviceType;
  }

  public String getTransactionStatus() {
    return transactionStatus;
  }

  public void setTransactionStatus(String transactionStatus) {
    this.transactionStatus = transactionStatus;
  }

  public String getFailureReason() {
    return failureReason;
  }

  public void setFailureReason(String failureReason) {
    this.failureReason = failureReason;
  }
}
