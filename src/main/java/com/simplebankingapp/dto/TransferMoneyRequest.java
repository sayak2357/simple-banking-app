package com.simplebankingapp.dto;


public class TransferMoneyRequest {

    Long fromBankAccountId;
    Long toBankAccountId;
    int amount;

    public Long getFromBankAccountId() {
        return fromBankAccountId;
    }

    public void setFromBankAccountId(Long fromBankAccountId) {
        this.fromBankAccountId = fromBankAccountId;
    }

    public Long getToBankAccountId() {
        return toBankAccountId;
    }

    public void setToBankAccountId(Long toBankAccountId) {
        this.toBankAccountId = toBankAccountId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
